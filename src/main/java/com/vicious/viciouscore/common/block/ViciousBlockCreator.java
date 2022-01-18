package com.vicious.viciouscore.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.util.EnumParticleTypes;

import static com.vicious.viciouscore.ViciousCore.logger;
import static com.vicious.viciouscore.common.registries.VBlockRegistry.register;

public class ViciousBlockCreator {
    public static ViciousBlock testBlock = new ViciousBlock("sampleblock", Material.ROCK, PropertyBool.create("passable"), PropertyBool.create("replaceable"));
    public static ViciousBlock testBlock2 = new ViciousBlock("sampleblock2", Material.ROCK);


    static int i = 0; //counting placement

    public static void init() {
        testBlock = register(testBlock.setDisplayTickAction(() -> ViciousBlockCreator.blockParticles(testBlock, EnumParticleTypes.HEART))
                .setPassableCondition(true, () -> logger.info("passing"))
                .setReplaceableCondition(true)
                .setBlockPlaceOnSideCondition(() -> testBlock.attributes.set("canBlockPlaceOnSide", true))
                .setSilkTouchDrop(() -> {
                })
                .setHarvestBlockAction(() -> {
                })
                .setOnBlockActivatedAction(() -> logger.info("testBlock activated"))
                .setOnBlockAddedAction(() -> {
                    i++;
                    System.out.printf("Placed %s", i);
                })
        );
        testBlock2 = register(testBlock2.setDisplayTickAction(() -> ViciousBlockCreator.blockParticles(testBlock, EnumParticleTypes.FLAME))
                .setOnBlockDestroyByPlayerAction(() -> logger.info("testBlock2 destroyed"))
                .setOnBlockActivatedAction(() -> logger.info("testBlock2 activated"))
        );
    }

    private static void blockParticles(ViciousBlock block, EnumParticleTypes type) {
        for (int i = 0; i < 3; ++i) {
            int j = block.attributes.rand.nextInt(2) * 2 - 1;
            int k = block.attributes.rand.nextInt(2) * 2 - 1;
            double d0 = block.attributes.pos.getX() + 0.5D + 0.25D * (double) j;
            double d1 = (float) block.attributes.pos.getY() + block.attributes.rand.nextFloat();
            double d2 = block.attributes.pos.getZ() + 0.5D + 0.25D * (double) k;
            double d3 = block.attributes.rand.nextFloat() * (float) j;
            double d4 = ((double) block.attributes.rand.nextFloat() - 0.5D) * 0.125D;
            double d5 = block.attributes.rand.nextFloat() * (float) k;
            block.attributes.world.spawnParticle(type, d0, d1, d2, d3, d4, d5);
        }
    }
}
