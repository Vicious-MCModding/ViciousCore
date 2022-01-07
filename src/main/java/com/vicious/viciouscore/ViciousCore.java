package com.vicious.viciouscore;

import com.vicious.viciouscore.client.ViciousCoreInputEventHandler;
import com.vicious.viciouscore.common.VCoreConfig;
import com.vicious.viciouscore.common.ViciousCTab;
import com.vicious.viciouscore.common.commands.CommandConfig;
import com.vicious.viciouscore.common.commands.CommandStructure;
import com.vicious.viciouscore.common.event.ViciousCoreCommonEventHandler;
import com.vicious.viciouscore.common.item.ViciousItem;
import com.vicious.viciouscore.common.keybinding.CommonKeyBindings;
import com.vicious.viciouscore.common.network.CMessageButtonPressReceived;
import com.vicious.viciouscore.common.network.SMessageButtonUpdate;
import com.vicious.viciouscore.common.network.handlers.CButtonPressHandler;
import com.vicious.viciouscore.common.network.handlers.SButtonPressHandler;
import com.vicious.viciouscore.common.override.MobSpawnListener;
import com.vicious.viciouscore.common.override.OverrideHandler;
import com.vicious.viciouscore.common.override.block.BlockOverrideHandler;
import com.vicious.viciouscore.common.override.chunk.ChunkOverrideHandler;
import com.vicious.viciouscore.common.override.tile.TileEntityOverrideHandler;
import com.vicious.viciouscore.common.player.ViciousCorePlayerManager;
import com.vicious.viciouscore.common.registries.VEntityRegistry;
import com.vicious.viciouscore.common.registries.VTileEntityRegistry;
import com.vicious.viciouscore.common.util.file.ViciousDirectories;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod(modid = ViciousCore.MODID, name = ViciousCore.NAME, version = ViciousCore.VERSION, acceptableRemoteVersions = "*", dependencies = "after:reborncore;after:techreborn;after:nuclearcraft;after:sponge")
public class ViciousCore
{
    private static int nextPacketId = -1;
    public static ViciousCTab TABVICIOUS = new ViciousCTab("viciouscreativetab", new ViciousItem("creativeicon", false));
    public boolean isFirstLoad(){
        return !CFG.firstLoad.getBoolean();
    }
    public static final String MODID = "viciouscore";
    public static final String NAME = "Vicious Core";
    public static final String VERSION = "1.1.0";
    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    public static VCoreConfig CFG;
    public static ViciousCore instance;
    static {
        ViciousDirectories.initializeConfigDependents();
        CFG = VCoreConfig.getInstance();
    }

    public static Logger logger;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        logger = event.getModLog();
        if(event.getSide() == Side.CLIENT) clientPreInit();
        if(isFirstLoad()) {
            logger.info("ViciousCore detected first load setup. Time to do some cool stuff and things!");
        }
        CommonKeyBindings.init();
        networkInit();
        VEntityRegistry.register();
        MinecraftForge.EVENT_BUS.register(MobSpawnListener.class);
        MinecraftForge.EVENT_BUS.register(ViciousCorePlayerManager.class);
        MinecraftForge.EVENT_BUS.register(TileEntityOverrideHandler.class);
        MinecraftForge.EVENT_BUS.register(BlockOverrideHandler.class);
        MinecraftForge.EVENT_BUS.register(ChunkOverrideHandler.class);
        MinecraftForge.EVENT_BUS.register(ViciousCoreCommonEventHandler.class);
        OverrideHandler.onPreInit();
        TileEntityOverrideHandler.init();
    }
    public void networkInit(){
        NETWORK.registerMessage(new SButtonPressHandler(), SMessageButtonUpdate.class,nextPacketId(), Side.SERVER);
        NETWORK.registerMessage(CButtonPressHandler.getInstance(), CMessageButtonPressReceived.class,nextPacketId(),Side.CLIENT);
    }
    public void clientPreInit(){
        MinecraftForge.EVENT_BUS.register(ViciousCoreInputEventHandler.class);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if(isFirstLoad()) {
            CFG.firstLoad.set(true);
        }
        VTileEntityRegistry.register();
        OverrideHandler.onInit();
    }

    @EventHandler
    public static void serverInit(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandConfig());
        event.registerServerCommand(new CommandStructure());
    }
    @EventHandler
    public void onStop(FMLModDisabledEvent event){
        CFG.save();
    }
    private int nextPacketId(){
        nextPacketId++;
        return nextPacketId;
    }
}
