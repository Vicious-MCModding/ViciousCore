package com.vicious.viciouscore.common.event;

import com.vicious.viciouscore.common.keybinding.ServerButtonPressHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class ViciousCoreCommonEventHandler {
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent ev){
        ServerButtonPressHandler.addPlayer(ev.player);
    }
    @SubscribeEvent
    public static void onLogoff(PlayerEvent.PlayerLoggedOutEvent ev){
        ServerButtonPressHandler.removePlayer(ev.player);
    }
}
