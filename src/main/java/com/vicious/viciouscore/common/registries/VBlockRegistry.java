package com.vicious.viciouscore.common.registries;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.block.ViciousBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ViciousCore.MODID)
public class VBlockRegistry extends Registrator {
    private static final List<Block> BLOCK_LIST = new ArrayList<>();
    public static ViciousBlock testBlock = new ViciousBlock("sampleblock", Material.ROCK);
    public static ViciousBlock testBlock2 = new ViciousBlock("sampleblock2", Material.ROCK);

    // when creating blocks last .method() goes first and first .method() goes last
    // so add properties before assigning them otherwise runtime exception will be thrown
    public static void init() {
        testBlock = register(testBlock.setDisplayTickAction(() -> {
                    for (int i = 0; i < 3; ++i) {
                        int j = testBlock.attributes.rand.nextInt(2) * 2 - 1;
                        int k = testBlock.attributes.rand.nextInt(2) * 2 - 1;
                        double d0 = testBlock.attributes.pos.getX() + 0.5D + 0.25D * (double) j;
                        double d1 = (float) testBlock.attributes.pos.getY() + testBlock.attributes.rand.nextFloat();
                        double d2 = testBlock.attributes.pos.getZ() + 0.5D + 0.25D * (double) k;
                        double d3 = testBlock.attributes.rand.nextFloat() * (float) j;
                        double d4 = ((double) testBlock.attributes.rand.nextFloat() - 0.5D) * 0.125D;
                        double d5 = testBlock.attributes.rand.nextFloat() * (float) k;
                        testBlock.attributes.world.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, d3, d4, d5);
                    }
                })
                .setPassableCondition(true)
                .setReplaceableCondition(true))
                .addProperties(PropertyBool.create("passable"), PropertyBool.create("replaceable"));
        testBlock2 = register(testBlock2.setDisplayTickAction(() -> {
            for (int i = 0; i < 3; ++i) {
                int j = testBlock2.attributes.rand.nextInt(2) * 2 - 1;
                int k = testBlock2.attributes.rand.nextInt(2) * 2 - 1;
                double d0 = testBlock2.attributes.pos.getX() + 0.5D + 0.25D * (double) j;
                double d1 = (float) testBlock2.attributes.pos.getY() + testBlock2.attributes.rand.nextFloat();
                double d2 = testBlock2.attributes.pos.getZ() + 0.5D + 0.25D * (double) k;
                double d3 = testBlock2.attributes.rand.nextFloat() * (float) j;
                double d4 = ((double) testBlock2.attributes.rand.nextFloat() - 0.5D) * 0.125D;
                double d5 = testBlock2.attributes.rand.nextFloat() * (float) k;
                testBlock2.attributes.world.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
            }
        }));
    }

    public static <T extends Block> T register(T in) {
        BLOCK_LIST.add(in);
        return in;
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> ev) {
        init();
        IForgeRegistry<Block> reg = ev.getRegistry();
        for (Block b : BLOCK_LIST) {
            reg.register(b);
            ResourceLocation registryLocation = b.getRegistryName();
            assert registryLocation != null; // just assigned, stop complaining
            ItemBlock ib = new ItemBlock(b); // DONT FORGET ABOUT THE FUCKING ITEM ISTG
            ib.setRegistryName(registryLocation);
            GameData.register_impl(ib);
        }
    }
}
