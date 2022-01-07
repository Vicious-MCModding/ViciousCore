package com.vicious.viciouscore.common.network.handlers;

import com.vicious.viciouscore.common.keybinding.CommonKeyBinding;
import com.vicious.viciouscore.common.keybinding.ServerButtonPressHandler;
import com.vicious.viciouscore.common.network.CMessageButtonPressReceived;
import com.vicious.viciouscore.common.network.SMessageButtonUpdate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Map;

public class SButtonPressHandler implements IMessageHandler<SMessageButtonUpdate, CMessageButtonPressReceived> {
    @Override
    public CMessageButtonPressReceived onMessage(SMessageButtonUpdate message, MessageContext ctx) {
        if(message == null) return null;
        EntityPlayerMP plr = ctx.getServerHandler().player;
        Map<Integer, CommonKeyBinding> bindings = ServerButtonPressHandler.bindings.get(plr);
        CommonKeyBinding binding = bindings.get(message.getCode());
        binding.isDown = message.isPressed();
        return new CMessageButtonPressReceived(message.getCode(),binding.isDown);
    }
}

