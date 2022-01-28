package com.vicious.viciouscore.util.file;

import com.vicious.viciouslib.util.FileUtil;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ViciousDirectories {
    public static Path configDirectory;
    public static Path viciousConfigDirectory;
    public static Path viciousCoreConfigPath;

    public static void initializeConfigDependents() {
        ViciousDirectories.configDirectory = FileUtil.createDirectoryIfDNE(directorize(rootDir(),"config").toString());
        ViciousDirectories.viciousConfigDirectory = FileUtil.createDirectoryIfDNE(directorize(configDirectory.toAbsolutePath().toString(),"vicious").toString());
        ViciousDirectories.viciousCoreConfigPath = directorize(viciousConfigDirectory.toAbsolutePath().toString(),"core.json");
    }

    public static Path directorize(String dir, String path) {
        return FileUtil.toPath(dir + "/" + path);
    }

    public static String rootDir() {
        return FabricLoader.getInstance().getConfigDir().getParent().toString();
    }
}
