package com.vicious.viciouscore.common.override.tile;

import com.vicious.viciouscore.common.override.OverrideHandler;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityOverrideHandler {
    private static final Map<Class<?>, TileEntityOverrider> overriders = new HashMap<>();
    private static final Map<String, TileEntityOverrider> queued = new HashMap<>();
    private static final Map<World, Integer> prevTileCounts = new HashMap<>();

    /**
     * I tried doing this in an easier way by accessing addedTileEntityList which by ALL MEANS should have worked.
     * It didn't. For some reason, this code works correctly.
     */
    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END || tick.side == Side.CLIENT) return;
        World world = tick.world;
        List<TileEntity> loadedTileEntityList = (List<TileEntity>) Reflection.accessField(world, "loadedTileEntityList");
        prevTileCounts.putIfAbsent(world, loadedTileEntityList.size());
        if (prevTileCounts.get(world) != loadedTileEntityList.size()) {
            prevTileCounts.replace(world, loadedTileEntityList.size());
            try {
                for (TileEntity ent : loadedTileEntityList) {
                    if (overriders.containsKey(ent.getClass())) {
                        TileEntity overridden = overriders.get(ent.getClass()).func.apply(ent);
                        overridden.setWorld(world);
                        world.setTileEntity(ent.getPos(), overridden);
                    }
                }
            } catch (ConcurrentModificationException ignored) {
                //Rare, usually happens on world start when an old unoverwritten tile is present. In this case I've just chosen to skip the tick.
            }
        }
    }

    public static void init() {
        OverrideHandler.handleOverrideMap(queued, overriders);
    }

    public static void registerOverrider(String targetTileClassCanonicalName, TileEntityOverrider teo) {
        queued.putIfAbsent(targetTileClassCanonicalName, teo);
    }
}
