package com.ulfric.lib.example;

import java.util.List;

import com.google.common.collect.Lists;
import com.ulfric.lib.bukkit.Lib;
import com.ulfric.lib.bukkit.module.Plugin;
import com.ulfric.lib.coffee.ObjectUtils;
import com.ulfric.lib.coffee.command.Argument;
import com.ulfric.lib.coffee.command.Command;
import com.ulfric.lib.coffee.event.Listener;
import com.ulfric.lib.coffee.event.ListenerMeta;
import com.ulfric.lib.coffee.event.Priority;
import com.ulfric.lib.coffee.module.ModuleUtils;
import com.ulfric.lib.coffee.string.Strings;
import com.ulfric.lib.craft.entity.player.Player;
import com.ulfric.lib.craft.entity.player.PlayerUtils;
import com.ulfric.lib.craft.event.block.BlockBreakEvent;
import com.ulfric.lib.craft.event.player.AsyncPlayerChatEvent;
import com.ulfric.lib.craft.event.player.PlayerJoinEvent;
import com.ulfric.lib.craft.event.server.ServerPingEvent;
import com.ulfric.lib.craft.inventory.item.ItemStack;
import com.ulfric.lib.craft.inventory.item.ItemUtils;
import com.ulfric.lib.craft.inventory.item.Material;
import com.ulfric.lib.craft.inventory.item.enchant.Enchant;
import com.ulfric.lib.craft.inventory.item.enchant.Enchantment;
import com.ulfric.lib.craft.string.ChatUtils;

public class Example extends Plugin /* Modular plugin */ {

	@Override
	public void onFirstEnable() // Only calls on the first enable
	{
		// Adds a new listener, with the owner being the LibE plugin
		this.addListener(new Listener(this) // Listener#new(ModuleBase)
		{
			@ListenerMeta // Not required, Eclipse just freaks out without any annotation
			public void onJoin(PlayerJoinEvent /* Not a CraftBukkit PlayerJoinEvent */ event)
			{
				// Not a CraftBukkit player
				Player player = event.getPlayer();

				// Logs the player's name
				//this.log(player.getName());

				// Gets or creates the player's speed controller (and sets it to walking)
				Player.Speed speed = player.speed().walking();

				// Logs the player's walking speed before modifying it
				//this.log(speed.toString());

				// Maxes out the player's walking speed
				speed.max();

				// Logs the new speed
				//this.log(speed.toString());

				// Gives the player a speed potion effect
				speed.boost(1000 /* Duration (ticks) */, 1 /* Amplifier (+1 shift) */, true /* Particles */);

				// Sets the player's hearts to 40
				player.health().setMax(40);

				// Scoreboard test
				player.scoreboard().addElement("test.test", "Example");

				// Inventories test
				ItemStack item = ItemUtils.getItem(Material.of("DIAMOND_SWORD"));

				List<Enchant> enchants = Lists.newArrayList(Enchant.of(ObjectUtils.validateNotNull(Enchantment.byName("sharpness")), 1), Enchant.of(ObjectUtils.validateNotNull(Enchantment.byName("fire aspect")), 3));

				item.enchants().addAll(enchants);

				player.inv().add(item);
			}

			@ListenerMeta(ignoreCancelled = true) // Ignore cancelled events
			public void onBreak(BlockBreakEvent event)
			{
				event.setCancelled(true);
				event.getPlayer().sendMessage("No breaking!"); // Send the player a message
			}

			@ListenerMeta(ignoreCancelled = true)
			public void onChat(AsyncPlayerChatEvent event)
			{
				this.log("Chat!"); // Log a message

				event.getRecipients().clear(); // Cancel the chat message from going anywhere
			}

			// Start - tests to make sure priority works properly
			@ListenerMeta(priority = Priority.LOW)
			public void onJoinLow(PlayerJoinEvent event)
			{
				this.log("PRIORITY:LOW");
			}

			@ListenerMeta(priority = Priority.NORMAL)
			public void onJoinNormal(PlayerJoinEvent event)
			{
				this.log("PRIORITY:NORMAL");
			}

			@ListenerMeta(priority = Priority.HIGH)
			public void onJoinHigh(PlayerJoinEvent event)
			{
				this.log("PRIORITY:HIGH");
			}
			// End - tests to make sure priority works properly

			@ListenerMeta(priority = Priority.LOWEST)
			public void onPing(ServerPingEvent event)
			{
				event.setMotd(Strings.format(ChatUtils.color("&aCurrent development version: &7{0}\n&aModules loaded: &7{1}"), Lib.get().getModuleVersion(), ModuleUtils.stream().count()));
			}
		});

		// Registers a command with the module
		this.addCommand(new Command("ping" /* The name of the command */, this /* The owner of the command */)
		{
			@Override
			public void run()
			{
				// Sends "PONG! <name>"
				this.getSender().sendMessage("PONG! " + this.getObject("player"));
			}
		}.addArgument(Argument.builder().addResolver((sender, str) -> PlayerUtils.getPlayer(str)).setPath("player").setDefaultValue(PlayerUtils::getPlayer).build()));

		// Simple version
		this.addCommand("pong", command -> command.getSender().sendMessage("PING!")); // The simple version also returns the Command object, for argument chaining
	}

}