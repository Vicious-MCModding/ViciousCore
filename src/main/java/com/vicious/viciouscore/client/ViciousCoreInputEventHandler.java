package com.vicious.viciouscore.client;


import com.vicious.viciouscore.common.keybinding.CommonKeyBindings;
import com.vicious.viciouscore.common.network.SMessageButtonUpdate;
import com.vicious.viciouscore.common.network.handlers.CButtonPressHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ViciousCoreInputEventHandler {
    @SubscribeEvent
    public static void onButtonPressed(TickEvent.ClientTickEvent ev){
        if(ev.phase == TickEvent.Phase.START) return;
        CommonKeyBindings.keyBindingList.forEach((i, k)->{
            KeyBinding binding = k.clientKey;
            if(binding.isKeyDown()){
                if(!k.isDown){
                    CButtonPressHandler.getInstance().startSending(new SMessageButtonUpdate(k.ID,true));
                }
                k.isDown = true;
            }
            else{
                if(k.isDown){
                    CButtonPressHandler.getInstance().startSending(new SMessageButtonUpdate(k.ID,false));
                }
                k.isDown = false;
            }
        });
        CButtonPressHandler.getInstance().tick();
    }
    @SubscribeEvent
    public static void onMouseEvent(MouseEvent ev){
        /*if(getClientPlayer()) {
            ev.setCanceled(true);
        }*/
    }
}
