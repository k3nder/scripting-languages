package net.k3nder.scripting;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.k3nder.js.Init;
import net.k3nder.scripting.objects.ScriptBlock;
import net.k3nder.scripting.objects.ScriptItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.k3nder.scripting.ScriptSource.ScriptCompiled.REGISTERS;

public class Scriptinglanguages implements ModInitializer {
	public static final String MOD_ID = "scripting-languages";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Using ScLa!");


		REGISTERS.put("ITEM", (source) -> {
			var tags = source.getTag();

			if (!tags.contains("ID")) throw new RuntimeException("Tag 'ID' in ITEM not found");
			var id = Identifier.of(tags.tag("ID"));
			if (tags.contains("LOG"))
				source.setVar("log", LoggerFactory.getLogger(tags.tag("LOG")));
			source.runSource();

			var settings = (Item.Settings) source.getVar("settings");

			if (settings == null) { settings = new Item.Settings(); }

			settings.registryKey(RegistryKey.of(RegistryKey.ofRegistry(Identifier.of("item")), id));

			var item = new ScriptItem(settings, source);

			Registry.register(Registries.ITEM, id, item);

			if (tags.contains("ITEM_GROUP")) {
				var group_parts = tags.tag("ITEM_GROUP").split(":");
				if (group_parts.length != 2) throw new RuntimeException("Tag 'ITEM_GROUP' in ITEM is not valid");
				var group_id = Identifier.of(group_parts[0], group_parts[1]);

				RegistryKey<ItemGroup> group = RegistryKey.of(RegistryKey.ofRegistry(Identifier.of("minecraft", "creative_mode_tab")), group_id);
				ItemGroupEvents.modifyEntriesEvent(group)
						.register((items) -> items.add(item));
			}
		});

		REGISTERS.put("BLOCK", (source) -> {
			var tags = source.getTag();

			if (!tags.contains("ID")) throw new RuntimeException("Tag 'ID' in ITEM not found");
			var id = Identifier.of(tags.tag("ID"));
			if (tags.contains("LOG"))
				source.setVar("log", LoggerFactory.getLogger(tags.tag("LOG")));
			source.runSource();

			var settings = (AbstractBlock.Settings) source.getVar("settings");

			settings.registryKey(RegistryKey.of(RegistryKey.ofRegistry(Identifier.of("block")), id));

			if (settings == null) settings = AbstractBlock.Settings.create();

			var block = new ScriptBlock(settings, source);

			Registry.register(Registries.BLOCK, id, block);

			if (tags.contains("ITEM")) {

				var item_settings = new Item.Settings().registryKey(RegistryKey.of(RegistryKey.ofRegistry(Identifier.of("item")), id));

				var block_item = new BlockItem(block,item_settings);

				Registry.register(Registries.ITEM, id,block_item);

				if (tags.contains("ITEM_GROUP")) {
					var group_parts = tags.tag("ITEM_GROUP").split(":");
					if (group_parts.length != 2) throw new RuntimeException("Tag 'ITEM_GROUP' in ITEM is not valid");
					var group_id = Identifier.of(group_parts[0], group_parts[1]);

					RegistryKey<ItemGroup> group = RegistryKey.of(RegistryKey.ofRegistry(Identifier.of("minecraft", "creative_mode_tab")), group_id);
					ItemGroupEvents.modifyEntriesEvent(group)
							.register((items) -> items.add(block.asItem()));
				}
			}
		});

		Init.init();
	}
}