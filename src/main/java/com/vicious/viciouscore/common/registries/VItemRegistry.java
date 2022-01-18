package com.vicious.viciouscore.common.registries;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ViciousCore.MODID)
public class VItemRegistry extends Registrator {
    private static final List<Item> itemList = new ArrayList<>();

    //Structure tools
    /*public static ItemStructureAreaSelectionWand S_AREA_SELECTOR = register(new ItemStructureAreaSelectionWand("sareaselector"));
    public static ItemStructurePasteWand S_PASTER = register(new ItemStructurePasteWand("spaster"));
*/
    public static <T extends Item> T register(T in) {
        itemList.add(in);
        return in;
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> ev) {
        /*IForgeRegistry<Item> reg = ev.getRegistry();
        for(Item i : itemList){
            reg.register(i);
            //Use CCL rendering
            if(i instanceof IRenderOverride){
                ((IRenderOverride)i).registerRenderers();
            }
            //Use vanilla rendering
            else ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(),null));
        }
        Item tabIcon = ViciousCore.TABVICIOUS.getTabIconItem().getItem();
        ModelLoader.setCustomModelResourceLocation(tabIcon,0,new ModelResourceLocation(tabIcon.getRegistryName(),null));
        */
    }
}
