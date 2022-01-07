package com.vicious.viciouscore.common.keybinding;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonKeyBinding {
    @SideOnly(Side.CLIENT)
    private static int nextId = -1;
    public KeyBinding clientKey;
    public final String name;
    public final int defaultKeyCode;
    public String category;
    public boolean isDown = false;
    public final int ID;
    public CommonKeyBinding(String name, int defaultKeyCode, String category){
        this.name=name;
        this.defaultKeyCode=defaultKeyCode;
        this.category=category;
        this.ID = nextId();
    }

    public CommonKeyBinding(String name, int defaultKeyCode, String category, int identifier){
        this.name=name;
        this.defaultKeyCode=defaultKeyCode;
        this.category=category;
        this.ID=identifier;
    }
    @SideOnly(Side.CLIENT)
    public KeyBinding toClientKeyBinding(IKeyConflictContext context, KeyModifier modifier) {
        if(context == null) clientKey = new KeyBinding(name, defaultKeyCode, category);
        else {
            clientKey = modifier != null ? new KeyBinding(name, context, modifier, defaultKeyCode, category) : new KeyBinding(name, context, defaultKeyCode, category);
        }
        return clientKey;
    }
    public static int nextId(){
        nextId++;
        return nextId;
    }
    public CommonKeyBinding copy(){
        return new CommonKeyBinding(name,defaultKeyCode,category,ID);
    }
    public String toString(){
        return "CKB: " + name + " : " + " : " + category + " : " + ID;
    }
}
