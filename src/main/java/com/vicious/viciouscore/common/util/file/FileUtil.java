package com.vicious.viciouscore.common.util.file;

import com.vicious.viciouscore.ViciousCore;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;

public class FileUtil {
    public static Path createDirectoryIfDNE(Path p) {
        if (!Files.isDirectory(p)) {
            try {
                Files.createDirectory(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    public static void createOrWipe(Path p) {
        try {
            Files.write(p, "".getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            try {
                Files.createFile(p);
            } catch (IOException ex) {
                ViciousCore.logger.error("I'm not sure how we got here, but somehow the file you have created both exists and doesn't exist at the same time. Is this God?");
                ex.printStackTrace();
            }
        }
    }

    public static JSONObject loadJSON(Path p) throws JSONException, IOException {
        try {
            InputStream is = new FileInputStream(p.toFile());
            String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            is.close();
            return new JSONObject(jsonTxt);

        } catch (Exception ex) {
            ViciousCore.logger.error("Could not load, probably doesn't actually exist. " + p.toString() + " caused by: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Copied this from the internet somewhere, Idk where. Its meant to copy resources from a mod's resource cache.
     * NOTE: This will copy the folder contents! The folder itself will NOT be copied. Make sure to set the targetDestination as the intended folder of storage.
     * You know what they say, if it works don't question it.
     *
     * @param modMainClass
     * @param resourcePath
     * @param targetDestination
     */
    public static void copyResources(Class<?> modMainClass, String resourcePath, String targetDestination) {
        URL url = modMainClass.getResource(resourcePath + "/");
        try {
            if (url != null) {
                URI uri = url.toURI();
                Path path = null;
                if ("file".equals(uri.getScheme())) {
                    path = Paths.get(modMainClass.getResource(resourcePath).toURI());
                } else {
                    FileSystem filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    path = filesystem.getPath(resourcePath);
                }
                Iterator<Path> it = Files.walk(path).iterator();
                it.next();
                while (it.hasNext()) {
                    Path p = it.next();
                    String postTarget = p.toAbsolutePath().toString().replaceAll(resourcePath + "/", "");
                    Path destination = ViciousDirectories.directorize(targetDestination, postTarget);
                    new File(destination.toString());
                    Files.copy(p, destination);
                }
            }
        } catch (Exception e) {
            ViciousCore.logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createDNE(String path) {
        Path p = Paths.get(path);
        if (Files.exists(p)) return;
        try {
            Files.createFile(p);
        } catch (Exception e) {
            ViciousCore.logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
