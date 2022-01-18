package com.vicious.viciouscore.common.configuration;

import com.vicious.viciouscore.common.util.file.ViciousDirectories;
import com.vicious.viciouscore.common.worldgen.StructureConfiguration;

import java.util.HashMap;
import java.util.Map;

public class OverrideConfigurations {
    private static final Map<String, StructureConfiguration> structureConfigurations = new HashMap<>();

    public static void createStructureConfiguration(String structureName) {
        structureConfigurations.putIfAbsent(structureName, new StructureConfiguration(ViciousDirectories.directorize(ViciousDirectories.viciousStructuresDirectory.toAbsolutePath().toString(), structureName + ".json")));
    }

    public static StructureConfiguration getStructureConfiguration(String structureName) {
        return structureConfigurations.get(structureName);
    }
}
