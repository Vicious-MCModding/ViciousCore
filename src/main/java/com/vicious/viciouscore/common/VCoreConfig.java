package com.vicious.viciouscore.common;

import com.vicious.viciouscore.common.configuration.ViciousConfigManager;
import com.vicious.viciouscore.common.util.file.ViciousDirectories;
import com.vicious.viciouslib.configuration.ConfigurationValue;
import com.vicious.viciouslib.configuration.JSONConfig;

import java.nio.file.Path;

public class VCoreConfig extends JSONConfig {
    private static VCoreConfig instance;
    public ConfigurationValue<Boolean> firstLoad = add(new ConfigurationValue<>("FirstLoadDone", ()->false, this).modifyOnRuntime(true).description("Whether the mod has loaded one time. Do not change unless you want a lot of things to reset."));
    public ConfigurationValue<Boolean> debug = add(new ConfigurationValue<>("DebugSettingsOn", ()->false, this).modifyOnRuntime(true).description("Enable Debug?"));
    public ConfigurationValue<Integer> buttonPressResponseTimeOut = add(new ConfigurationValue<>("ButtonPressResponseTimeOut", ()->100, this).modifyOnRuntime(true).description("The maximum amount of ticks the client can continue to send button press packets without a response."));

    public static VCoreConfig getInstance() {
        if (instance == null) {
            instance = new VCoreConfig(ViciousDirectories.viciousCoreConfigPath);
            ViciousConfigManager.register(instance);
        }
        return instance;
    }
    public VCoreConfig(Path f) {
        super(f);
    }
}
