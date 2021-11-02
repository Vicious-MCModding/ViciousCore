package com.vicious.viciouscore.common;

import com.vicious.viciouscore.common.util.Directories;
import com.vicious.viciouscore.common.util.configuration.Config;
import com.vicious.viciouscore.common.util.configuration.ConfigurationValue;

import java.nio.file.Path;

public class VCoreConfig extends Config {
    public ConfigurationValue<Boolean> firstLoad = add(new ConfigurationValue<>("FirstLoadDone", ()->false, this).modifyOnRuntime(true).description("Whether the mod has loaded one time. Do not change unless you want a lot of things to reset."));
    public static VCoreConfig init(){
        //If the old CFG still exists, this will probably send an error which is totally fine. Instantiation will not fail. Task Failed Successfully.
        VCoreConfig cfg = new VCoreConfig(Directories.viciousCoreConfigPath);
        temp.put(Directories.viciousCoreConfigPath,cfg);
        //INSTANCE.overWriteFile();
        return cfg;
    }
    public VCoreConfig(Path f) {
        super(f);
    }

}
