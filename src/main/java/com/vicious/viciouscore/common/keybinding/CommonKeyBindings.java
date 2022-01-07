package com.vicious.viciouscore.common.keybinding;

import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to identity key bindings with an integer on both the client and server side.
 */
public class CommonKeyBindings {
    //public static final Method grow = Reflection.getMethod(ArrayList.class,"ensureExplicitCapacity", new Class[]{Integer.TYPE});
    public static Side side;
    public static Map<Integer,CommonKeyBinding> keyBindingList = new HashMap<>();
    public static CommonKeyBinding VCSHOOT = add(new CommonKeyBinding("key.vc.shoot", -99, "key.viciouscore.category"));
    public static CommonKeyBinding VCAIM = add(new CommonKeyBinding("key.vc.aim", -100, "key.viciouscore.category"));
    public static void init(){}
    public static CommonKeyBinding add(CommonKeyBinding in){
        if(side == null) side = detectSide();
        if(side.isClient()){
            System.out.println("Detected Client");
            ClientRegistry.registerKeyBinding(in.toClientKeyBinding(KeyConflictContext.IN_GAME,null));
        }
        else System.out.println("Detected Server");
        //Reflection.invokeMethod(keyBindingList,grow, new Object[]{in.ID + 1});
        keyBindingList.put(in.ID,in);
        return in;
    }
    public static Side detectSide(){
        try{
            Class.forName("net.minecraft.client.Minecraft");
            return Side.CLIENT;
        }
        catch (ClassNotFoundException e){
            return Side.SERVER;
        }
    }

    public static Map<Integer,CommonKeyBinding> copyBindings() {
        Map<Integer,CommonKeyBinding> ret = new HashMap<>();
        keyBindingList.forEach((i,k)->{
            ret.put(k.ID,k.copy());
        });
        return ret;
    }

}
