package com.vicious.viciouscore.common.util.resources;

import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.CoreModManager;

import java.io.*;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class GameResourceHelper {
    private static final HashMap<String, File> ACTIVEMODS = new HashMap<>();
    static{
        scan(new File("mods"));
    }
    private static void scan(File folder){
        if(folder.exists() && folder.isDirectory() && folder.listFiles() != null) {
            for (File f : folder.listFiles()) {
                if (f.getName().endsWith(".jar")) {
                    try{
                        String modid = getModId(f);
                        if(modid != null){
                            ACTIVEMODS.put(modid, f);
                        }
                    } catch (IOException e){}
                }
            }
        }
    }
    private static String getModId(File mod) throws IOException{
        ZipFile modzip = new ZipFile(mod);
        ZipEntry mminfo = modzip.getEntry("mcmod.info");
        if(mminfo != null){
            InputStream istream = modzip.getInputStream(mminfo);
            BufferedReader reader = new BufferedReader( new InputStreamReader(istream));
            String line;
            while((line = reader.readLine()) != null){
                if(line.contains("modid")){
                    return line.replaceAll("[^a-z]", "").replace("modid", "");
                }
            }
        }
        return null;
    }
    public static boolean hasMod(String modid){
        return ACTIVEMODS.containsKey(modid);
    }
    public static File getModJarFromId(String modid){
        if(hasMod(modid)){
            return ACTIVEMODS.get(modid);
        }
        return null;
    }
    public static boolean load(String modid) {
        if (hasMod(modid)) {
            try {
                File modjar = getModJarFromId(modid);
                ((LaunchClassLoader) GameResourceHelper.class.getClassLoader()).addURL(modjar.toURI().toURL());
                CoreModManager.getReparseableCoremods().add(modjar.getName());
                return true;
            }
            catch(MalformedURLException e) {
                return false;
            }
        }
        return false;
    }
    public static void clear(){
        ACTIVEMODS.clear();
    }
}
