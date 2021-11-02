package com.vicious.viciouscore.client.render.item.configuration;

import com.vicious.viciouscore.common.util.Directories;
import com.vicious.viciouscore.common.util.file.FileUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLContainer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Powerful class designed to allow us to save and load model and render override settings while in game. This means we can edit the rendering of entities and such
 * while in game without needing to restart. It also grants other modders control over our renders.
 */
@SuppressWarnings("unchecked")
public class OverrideConfigurations {
    private final static Map<String, OverrideConfigurations> overrideConfigurationsMap = new HashMap<>();
    private final Path PATH;
    private final RenderConfiguration ITEM;
    private final Map<Class<? extends ModelBase>, EntityModelOverride<?>> MOBMAP = new HashMap<>();
    public OverrideConfigurations(Item in) {
        String itemName = in.getRegistryName().toString().replaceAll(":","-");
        PATH = Directories.directorize(Directories.itemRenderOverridesDirectory.toAbsolutePath().toString(), itemName);
        FileUtil.createDirectoryIfDNE(PATH);
        ITEM = new RenderConfiguration(Directories.directorize(PATH.toAbsolutePath().toString(), itemName + ".json"));
    }

    public static void saveAll() {
        overrideConfigurationsMap.forEach((name, cfg)->{
            cfg.ITEM.save();
            cfg.MOBMAP.forEach((mob,override)->{
                override.saveAll();
            });
        });
    }

    public static void readAll() {
        overrideConfigurationsMap.forEach((name, cfg) -> {
            cfg.ITEM.readFromJSON();
            cfg.MOBMAP.forEach((mob, override) -> {
                override.readAll();
            });
        });
    }

    public static void read(String itemname) {
        OverrideConfigurations cfg = overrideConfigurationsMap.get(itemname);
        if(cfg == null) return;
        cfg.ITEM.readFromJSON();
        cfg.MOBMAP.forEach((mob, override) -> {
            override.readAll();
        });
    }

    public <T extends ModelBase> OverrideConfigurations addEntityModelOverrider(Class<T> in){
        EntityModelOverride<T> modelconfigurator = new EntityModelOverride<T>(Directories.directorize(PATH.toAbsolutePath().toString(),in.getCanonicalName().replaceAll("\\.","-")), in);
        MOBMAP.put(in,modelconfigurator);
        return this;
    }
    public <T extends ModelBase> EntityModelOverride<T> getEntityModelConfig(T in){
        return (EntityModelOverride<T>) MOBMAP.get(in.getClass());
    }
    public <T extends ModelBase> EntityModelOverride<T> getEntityModelConfig(Class<T> in){
        return (EntityModelOverride<T>) MOBMAP.get(in);
    }
    public RenderConfiguration getItemConfig(){
        return ITEM;
    }
    public static OverrideConfigurations create(Item in){
        OverrideConfigurations cfg = new OverrideConfigurations(in);
        overrideConfigurationsMap.put(in.getRegistryName().toString(), cfg);
        return cfg;
    }
    public static OverrideConfigurations getConfiguration(Item in){
        return overrideConfigurationsMap.get(in.getRegistryName().toString());
    }
    public static void copyFromResources(String modid, Class<?> mainClass){
        FileUtil.copyResources(mainClass, "/assets/" + modid + "/itemrenderoverrides", Directories.viciousResourcesDirectory.toAbsolutePath().toString());
    }
}
