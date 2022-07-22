package com.vicious.viciouscore.common.network;

import com.vicious.viciouscore.common.network.packets.CPacketButtonPressReceived;
import com.vicious.viciouscore.common.network.packets.SPacketButtonUpdate;
import com.vicious.viciouscore.common.resource.VCResources;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class VCNetwork {
    public static VCNetwork instance;
    public static VCNetwork getInstance(){
        if(instance == null) instance = new VCNetwork();
        return instance;
    }
    private static final String VERSION = "1";
    protected final SimpleChannel channel = NetworkRegistry.newSimpleChannel(VCResources.NETWORK,VCNetwork::getProtocolVersion,VERSION::equals,VERSION::equals);

    public VCNetwork(){
        VCPacket.register(CPacketButtonPressReceived.class, CPacketButtonPressReceived::new);
        VCPacket.register(SPacketButtonUpdate.class, SPacketButtonUpdate::new);
    }

    public static String getProtocolVersion(){
        return VERSION;
    }
    public void sendToServer(VCPacket pk) {
        channel.sendToServer(pk);
    }

    public void reply(NetworkEvent.Context ctx, VCPacket packet) {
        channel.reply(packet,ctx);
    }
}