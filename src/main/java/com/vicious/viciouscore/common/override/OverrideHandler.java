package com.vicious.viciouscore.common.override;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.util.reflect.FieldRetrievalRoute;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistry;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * The overrider can be used to modify the structure of other mods and apply fixes by injecting custom data into fields.
 * There are a few events on which an override will be executed.
 * Calls to Overrider.registerPreInitInjector should be made after VCore and whatever overridden mod has initialized.
 */
@Mod.EventBusSubscriber(modid = ViciousCore.MODID)
public class OverrideHandler {
    private static final Map<String, Runnable> modInit = new HashMap<>();
    private static final Map<ResourceLocation, Block> blockRegReplacement = new HashMap<>();
    private static Map<FieldRetrievalRoute, OverrideConverter<?>> preInit = new HashMap<>();
    private static Map<FieldRetrievalRoute, OverrideConverter<?>> init = new HashMap<>();
    private static Map<FieldRetrievalRoute, OverrideConverter<?>> postInit = new HashMap<>();
    //Overrider object
    //TODO: separate from this class.
    public final String applier;
    public final String modid;
    public final boolean required;
    public final int priority;

    public OverrideHandler(String applier, String modidTarget) {
        this.applier = applier;
        this.modid = modidTarget;
        required = false;
        priority = 0;
    }

    public OverrideHandler(String applier, String modidTarget, boolean required, int priority) {
        this.applier = applier;
        this.modid = modidTarget;
        this.required = required;
        this.priority = priority;
    }

    public OverrideHandler(String applier, String modidTarget, int priority) {
        this.applier = applier;
        this.modid = modidTarget;
        this.priority = priority;
        required = false;
    }

    public OverrideHandler(String applier, String modidTarget, boolean required) {
        this.applier = applier;
        this.modid = modidTarget;
        this.required = required;
        priority = 0;
    }

    /**
     * Called when VCore starts preinitialization.
     *
     * @param route                    route of classes and fields to go through to access the target field via an object.
     * @param overriddenVariantFactory converts the original variant to the overridden variant.
     */
    public static void registerPreInitInjector(FieldRetrievalRoute route, OverrideConverter<?> overriddenVariantFactory) {
        preInit.putIfAbsent(route, overriddenVariantFactory);
    }
    /*

    Did not complete. This method is literally too dangerous to give anyone access to. Even myself. Overwriting tiles in the TE registry can delete their data on world load.
       Use TileEntityOverrider instead.
    @SubscribeEvent
    @SuppressWarnings({"unchecked","rawtypes"})
    public static void onTileEntityRegistration(){
        RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> reg = (RegistryNamespaced) Reflection.accessField(TileEntity.class,"REGISTRY");
        blockReg.forEach((rr, bo)->{
            if(reg.containsKey(rr.resource)){
                Block b = reg.remove(rr.resource);
                Block overridden = bo.override(b);
                reg.register(b);
                processRegistrationOverride(overridden,rr.route);
            }
        });
    }*/

    public static void registerInitInjector(FieldRetrievalRoute route, OverrideConverter<?> overriddenVariantFactory) {
        init.putIfAbsent(route, overriddenVariantFactory);
    }

    public static void registerPostInitInjector(FieldRetrievalRoute route, OverrideConverter<?> overriddenVariantFactory) {
        postInit.putIfAbsent(route, overriddenVariantFactory);
    }

    /**
     * Warning! Replacing registry entries is something that has never been done before outside of VCore as far as I know.
     * Be sure to inject the replacement block into any fields where the original block was.
     * There are likely unknown risks. Be careful.
     */
    public static void registerBlockRegistryReplacer(ResourceLocation rl, Block block) {
        blockRegReplacement.putIfAbsent(rl, block);
    }

    @SubscribeEvent
    public static void onBlockRegistration(RegistryEvent.Register<Block> forgereg) {
        ForgeRegistry<Block> frg = (ForgeRegistry<Block>) forgereg.getRegistry();
        blockRegReplacement.forEach((rl, blk) -> {
            if (frg.containsKey(rl)) {
                Block b1 = frg.remove(rl);
                blk.setRegistryName(b1.getRegistryName());
                frg.register(blk);
            }
        });
    }

    public static void onPreInit() {
        processOverrideMap(preInit);
        preInit = null;
    }

    public static void onInit() {
        processOverrideMap(init);
        init = null;
    }

    public static void onPostInit() {
        processOverrideMap(postInit);
        postInit = null;
    }

    public static void onModInit(String modid) {
        if (modInit.get(modid) != null) {
            modInit.remove(modid).run();
        }
    }

    private static void inject(Object overridden, FieldRetrievalRoute route) {
        if (route != null) {
            Object objectToAccess = route.getEndObjectSupplier();
            if (objectToAccess != null) Reflection.setField(objectToAccess, overridden, route.endField());
        }
    }

    public static <V extends OverrideHandler> void handleOverrideMap(Map<String, V> queuedMap, Map<Class<?>, V> regMap) {
        queuedMap.forEach((clazz, overrider) -> {
            try {
                Class<?> cls = Class.forName(clazz);
                OverrideHandler.handleOverride(regMap, cls, overrider);
            } catch (ClassNotFoundException e) {
                ViciousCore.logger.fatal("Failed to override target class: " + clazz);
                OverrideHandler.handleFailedOverride(overrider, e);
            }
        });
    }

    public static <K, V extends OverrideHandler> void handleOverride(Map<K, V> map, K key, V value) {
        if (Loader.isModLoaded(value.modid)) {
            if (map.containsKey(key)) {
                V overrider = map.get(key);
                if (!overrider.required) {
                    if (overrider.priority <= value.priority) {
                        map.replace(key, value);
                    }
                } else {
                    ViciousCore.logger.fatal("Two required Overriders tried to override the same target. The offenders are: " + overrider.modid + " and " + value.modid + ". The overrider: " + overrider + " will be used. Please contact the mod authors to get this conflict resolved.");
                    return;
                }
            } else map.put(key, value);
            ViciousCore.logger.info("Successfully registered overrider: " + value);
        } else {
            ViciousCore.logger.info("Target modid was not loaded so this override was not registered: " + value);
        }
    }

    public static void handleFailedOverride(OverrideHandler overrider, Exception e) {
        if (Loader.isModLoaded(overrider.modid))
            ViciousCore.logger.fatal("Failed to register overrider even though the target mod was loaded. Overrider: " + overrider);
        e.printStackTrace();
    }

    private static void processOverrideMap(Map<FieldRetrievalRoute, OverrideConverter<?>> map) {
        map.forEach((route, converter) -> {
            try {
                Class<?> clazz = Class.forName(route.endClass());
                Field f = Reflection.getField(clazz, route.endField());
                if (f != null) {
                    Object toOverride = Reflection.accessField(route.getEndValue(), f);
                    toOverride = converter.override(toOverride);
                    Object objectToAccess = route.getEndObjectSupplier();
                    if (toOverride != null && objectToAccess != null)
                        Reflection.setField(objectToAccess, toOverride, f.getName());
                    else {
                        ViciousCore.logger.error("Not overriding " + route + " because the final object was null or the override object was null. \nFIELD: " + f.getName() + "\nTARGET: " + objectToAccess + "\nOVERRIDE" + toOverride);
                    }
                } else {
                    ViciousCore.logger.info("Not overriding " + route + " because one of the route fields was missing.");
                }
            } catch (ClassNotFoundException ignored) {
                ViciousCore.logger.info("Not overriding " + route + " because one of the route classes was missing.");
                //Mod isn't installed, don't finish execution.
            }
        });
    }

    private static void executeOnModInit(String modid, Runnable exec) {

    }

    public String toString() {
        return "(Overrider by: " + applier + " on target " + modid + ")";
    }

    @FunctionalInterface
    public interface OverrideConverter<T> {
        T override(Object in);
    }
}
