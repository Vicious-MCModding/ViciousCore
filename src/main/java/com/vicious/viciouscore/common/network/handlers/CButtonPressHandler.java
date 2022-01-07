package com.vicious.viciouscore.common.network.handlers;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.VCoreConfig;
import com.vicious.viciouscore.common.network.CMessageButtonPressReceived;
import com.vicious.viciouscore.common.network.SMessageButtonUpdate;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashMap;
import java.util.Map;

public class CButtonPressHandler implements IMessageHandler<CMessageButtonPressReceived, IMessage> {
    private static final CButtonPressHandler instance = new CButtonPressHandler();
    public static CButtonPressHandler getInstance(){
        return instance;
    }
    public Map<Integer, SMessageButtonUpdate> toSend = new HashMap<>();
    private boolean hasGottenResponse = false;
    @Override
    public IMessage onMessage(CMessageButtonPressReceived message, MessageContext ctx) {
        if(message == null) return null;
        if(toSend.get(message.getCode()) == null) return null;
        if(toSend.get(message.getCode()).isPressed() == message.isPressed()){
            toSend.remove(message.getCode());
            hasGottenResponse=true;
        }
        return null;
    }
    public void startSending(SMessageButtonUpdate buttonPacket){
        //Reflection.invokeMethod(toSend,CommonKeyBindings.grow, new Object[]{buttonPacket.getCode() + 1});
        toSend.put(buttonPacket.getCode(),buttonPacket);
    }
    public void tick(){
        for (int i : toSend.keySet()) {
            if(hasGottenResponse){
                hasGottenResponse = false;
                return;
            }
            SMessageButtonUpdate pk = toSend.get(i);
            if(pk == null) continue;
            if(pk.getTicksExisted() >= VCoreConfig.getInstance().buttonPressResponseTimeOut.value()){
                toSend.remove(i);
            }
            pk.tick();
            ViciousCore.NETWORK.sendToServer(pk);
        }
    }
}
