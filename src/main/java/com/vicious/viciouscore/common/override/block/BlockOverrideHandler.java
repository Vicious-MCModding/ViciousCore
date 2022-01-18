package com.vicious.viciouscore.common.override.block;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.override.OverrideHandler;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockOverrideHandler {
    private static final Map<Class<?>, BlockOverrider> overriders = new HashMap<>();
    private static final Map<String, BlockOverrider> queued = new HashMap<>();
    private static final Map<Block, Supplier<TileEntity>> tileInjectors = new HashMap<>();

    public static void fixPos(World w, BlockPos p) {
        IBlockState state = w.getBlockState(p);
        if (tileInjectors.containsKey(state.getBlock())) {
            TileEntity te = tileInjectors.get(state.getBlock()).get();
            if (w.getTileEntity(p) == null) {
                w.setTileEntity(p, te);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void onBlockEvent(BlockEvent ev) {
        //Don't care
        if (ev instanceof BlockEvent.BreakEvent || ev.isCanceled()) return;
        IBlockState state = ev.getState();

        if (ev instanceof BlockEvent.PlaceEvent) {
            fixPos(ev.getWorld(), ev.getPos());
        }
        if (overriders.containsKey(state.getBlock().getClass())) {
            Block overridden = overriders.get(state.getBlock().getClass()).block;
            BlockStateContainer.Builder builder = new BlockStateContainer.Builder(overridden);
            IBlockState result = builder.build().getBaseState();
            Collection<IProperty<?>> keys = state.getPropertyKeys();
            for (IProperty key : keys) {
                result = result.withProperty(key, state.getValue(key));
            }
            ev.getWorld().setBlockState(ev.getPos(), result);
        }
    }

    public static void init() {
        OverrideHandler.handleOverrideMap(queued, overriders);
    }

    /**
     * Registers a tile entity for a block. This will create a tileEntity when the target block is placed.
     * WARNING: THIS SYSTEM IS VERY UNSTABLE! Tiles will not be saved to the world data on stop. Do not use this to create tiles for blocks without tile entities by default.
     * Use TileEntityOverrideHandler for THAT purpose.
     */
    public static void registerTileEntity(Block b, Supplier<TileEntity> te) {
        tileInjectors.put(b, te);
        ViciousCore.logger.info("Registered a Tile Entity Injector for Block " + b.getRegistryName());
    }

    public static void registerOverrider(String targetBlockClassCanonicalName, BlockOverrider bo) {
        queued.putIfAbsent(targetBlockClassCanonicalName, bo);
    }

    public static boolean hasTileInjector(Block b) {
        return tileInjectors.containsKey(b);
    }
}
