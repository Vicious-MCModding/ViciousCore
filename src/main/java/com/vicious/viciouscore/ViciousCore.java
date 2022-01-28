package com.vicious.viciouscore;

import com.vicious.viciouscore.util.file.ViciousDirectories;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViciousCore implements ModInitializer {
	public static final String MODID = "viciouscore";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static ViciousCore INSTANCE;
	public static VCoreConfig CFG;

	public static ItemGroup ITEMGROUP = FabricItemGroupBuilder.build(
			new Identifier(MODID, "item_group"),
			() -> new ItemStack(Items.ANVIL));

	static {
		ViciousDirectories.initializeConfigDependents();
		CFG = VCoreConfig.getInstance();
	}

	public boolean isFirstLoad() {
		return !CFG.firstLoad.getBoolean();
	}

	@Override
	public void onInitialize() {
		INSTANCE = this;
		LOGGER.info("Hello Fabric world!");

		if (isFirstLoad()) {
			LOGGER.info("ViciousCore detected first load setup");
		}
	}
}
