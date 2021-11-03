package com.vicious.viciouscore.client.render.item.configuration;

import com.vicious.viciouscore.common.util.Directories;
import com.vicious.viciouscore.common.util.file.FileUtil;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles entity part config management.
 * @param <T>
 */
public class EntityModelOverride<T extends ModelBase> {
    public final Map<String, ModelRendererConfiguration> modelPartConfigs = new HashMap<>();

    public EntityModelOverride(Path f, Class<T> modelclass) {
        FileUtil.createDirectoryIfDNE(f);
        List<Field> rendererFields = Reflection.getFieldsOfType(modelclass, ModelRenderer.class);
        for (Field rendererField : rendererFields) {
            String name = rendererField.getName();
            modelPartConfigs.put(name, new ModelRendererConfiguration(Directories.directorize(f.toAbsolutePath().toString(), name + ".json")));
        }
    }

    /**
     * For when the model has arrays,
     */
    public EntityModelOverride(Path f, Class<T> modelclass, int[] arraySizes) {
        FileUtil.createDirectoryIfDNE(f);
        List<Field> rendererFields = Reflection.getFieldsOfType(modelclass, ModelRenderer.class);
        int i = 0;
        for (Field rendererField : rendererFields) {
            String name = rendererField.getName();
            if(rendererField.getType().isArray()){
                for (int j = 0; j < arraySizes[i]; j++) {
                    name = rendererField.getName() + j;
                    modelPartConfigs.put(name, new ModelRendererConfiguration(Directories.directorize(f.toAbsolutePath().toString(), name + ".json")));
                }
                i++;
            }
            else modelPartConfigs.put(name, new ModelRendererConfiguration(Directories.directorize(f.toAbsolutePath().toString(), name + ".json")));
        }
    }
    public ModelRendererConfiguration getPartConfiguration(String fieldName){
        return modelPartConfigs.get(fieldName);
    }

    public void saveAll() {
        modelPartConfigs.forEach((name,cfg)->{
            cfg.save();
        });
    }

    public void readAll() {
        modelPartConfigs.forEach((name,cfg)->{
            cfg.readFromJSON();
        });
    }
}