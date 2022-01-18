package com.vicious.viciouscore.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ViciousCTab extends CreativeTabs {
    private final Item icon;

    public ViciousCTab(String label, Item icon) {
        super(label);
        this.icon = icon;

    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(icon);
    }
}