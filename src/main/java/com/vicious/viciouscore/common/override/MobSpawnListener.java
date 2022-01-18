package com.vicious.viciouscore.common.override;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Used to modify mobs on spawn. To do so you can add to the entityModificator map a new handler.
 */
public class MobSpawnListener {
    private static final List<EntityMob> queuedSpawns = new ArrayList<>();
    public static Map<Class<? extends EntityMob>, List<MobSpawnHandler>> listeners = new HashMap<>();

    public static void registerMobSpawnHandler(Class<? extends EntityMob> entity, String modid, boolean required, Consumer<EntityMob> consumer) {
        List<MobSpawnHandler> cons = listeners.get(entity);
        if (cons == null) cons = new ArrayList<>();
        else if (required) {
            ViciousCore.logger.warn("Duplicate required Mob spawn handlers registered for the same class!");
            ViciousCore.logger.warn("Conflict between: " + cons.get(0).MODID + " and " + modid + " USING: " + modid);
        }
        cons.add(new MobSpawnHandler(consumer, modid, required));
        ViciousCore.logger.info("Vicious Core registered an entity spawn handler for " + modid + " on entity class: " + entity);
    }

    @SubscribeEvent
    public static void onMobSpawn(EntityJoinWorldEvent ev) {
        //Queue only if supported.
        Entity entity = ev.getEntity();
        if (listeners.containsKey(entity.getClass())) {
            NBTTagCompound tag = entity.getEntityData();
            if (!tag.hasKey("vfirstjoin")) {
                tag.setTag("vfirstjoin", new NBTTagInt(1));
                queuedSpawns.add((EntityMob) entity);
            }
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent event) {
        if (event.type != TickEvent.Type.SERVER) return;
        int i = 0;
        while (queuedSpawns.size() > 0 && i < queuedSpawns.size()) {
            EntityMob entity = queuedSpawns.get(i);
            if (entity.ticksExisted <= 0) {
                i++;
                continue;
            } else {
                queuedSpawns.remove(i);
            }
            //lol funny name
            List<MobSpawnHandler> mobEater = listeners.get(entity.getClass());
            if (mobEater != null) {
                for (MobSpawnHandler entityMobConsumer : mobEater) {
                    entityMobConsumer.CONSUMER.accept(entity);
                }
            }
        }
    }

    public static class MobSpawnHandler {
        public final Consumer<EntityMob> CONSUMER;
        public final String MODID;
        public final boolean SHOULDOVERRIDEOTHERS;

        public MobSpawnHandler(Consumer<EntityMob> consumer, String modid, boolean shouldOverrideOthers) {
            CONSUMER = consumer;
            MODID = modid;
            SHOULDOVERRIDEOTHERS = shouldOverrideOthers;
        }
    }
}
