package com.vicious.viciouscore.common.util.resources;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.util.ResourceLocation;

public class ViciousLoader {
    public static ResourceLocation getViciousResource(String resourcePath){
        return new ResourceLocation(ViciousCore.MODID + ":" + resourcePath);
    }
    public static ResourceLocation getResource(String resourcePath){
        return new ResourceLocation(resourcePath);
    }
    public static ResourceLocation getItemRenderOverrideLocation(String modid){
        return getResource(modid + "/" + "itemrenderoverrides");
    }
}
