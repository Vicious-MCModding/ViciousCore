package com.vicious.viciouscore.common.registries;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.block.ViciousBlockCreator;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
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

    public static <T extends Block> T register(T in) {
        BLOCK_LIST.add(in);
        return in;
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> ev) {
        ViciousBlockCreator.init();
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
