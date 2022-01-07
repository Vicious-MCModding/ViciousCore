package com.vicious.viciouscore.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CMessageButtonPressReceived implements IMessage {
    private int toSend;
    private boolean pressed;

    public CMessageButtonPressReceived() {
    }

    public CMessageButtonPressReceived(int toSend, boolean pressed) {
        this.toSend = toSend;
        this.pressed=pressed;
    }

    @Override public void toBytes(ByteBuf buf) {
        buf.writeInt(toSend);
        buf.writeBoolean(pressed);
    }

    @Override public void fromBytes(ByteBuf buf) {
        toSend = buf.readInt();
        pressed = buf.readBoolean();
    }
    public int getCode(){
        return toSend;
    }
    public boolean isPressed(){
        return pressed;
    }
}
