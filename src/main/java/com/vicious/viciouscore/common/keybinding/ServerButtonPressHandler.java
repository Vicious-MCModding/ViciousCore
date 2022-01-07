package com.vicious.viciouscore.common.keybinding;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public class ServerButtonPressHandler {
    public static Map<EntityPlayer, Map<Integer,CommonKeyBinding>> bindings = new HashMap<>();
    public static void addPlayer(EntityPlayer entity){
        bindings.put(entity,CommonKeyBindings.copyBindings());
    }
    public static void removePlayer(EntityPlayer entity){
        bindings.remove(entity);
    }

    public static boolean isDown(EntityPlayer plr, CommonKeyBinding commonkey) {
        return bindings.get(plr).get(commonkey.ID).isDown;
    }
    public static boolean isDown(EntityPlayer plr, int commonKeyId) {
        return bindings.get(plr).get(commonKeyId).isDown;
    }
}
