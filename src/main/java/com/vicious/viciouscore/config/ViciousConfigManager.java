package com.vicious.viciouscore.config;

import com.vicious.viciouslib.configuration.JSONConfig;

import java.util.ArrayList;

public class ViciousConfigManager {
    private static final ArrayList<JSONConfig> cfgs = new ArrayList<>();

    public static void register(JSONConfig cfg) {
        cfgs.add(cfg);
    }

    public static void reload() {
        for (JSONConfig c : cfgs) {
            c.readFromJSON();
        }
    }
}
