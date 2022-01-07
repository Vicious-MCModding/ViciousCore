package com.vicious.viciouscore.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SMessageButtonUpdate implements IMessage {
    // A default constructor is always required
    public SMessageButtonUpdate(){}
    @SideOnly(Side.CLIENT)
    private int ticksExisted;
    private int toSend;
    private boolean pressed;
    public SMessageButtonUpdate(int toSend, boolean pressed) {
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
    @SideOnly(Side.CLIENT)
    public int getTicksExisted(){return ticksExisted;}
    @SideOnly(Side.CLIENT)
    public void tick(){ticksExisted++;}
}

