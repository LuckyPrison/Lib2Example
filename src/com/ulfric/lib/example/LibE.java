package com.ulfric.lib.example;

import com.ulfric.lib.bukkit.module.Plugin;
import com.ulfric.lib.coffee.command.Command;
import com.ulfric.lib.coffee.event.HandlerMeta;
import com.ulfric.lib.coffee.event.Listener;
import com.ulfric.lib.craft.entity.player.Player;
import com.ulfric.lib.craft.event.player.PlayerJoinEvent;

public class LibE extends Plugin /* Modular plugin */ {

	@Override
	public void onFirstEnable() // Only calls on the first enable
	{
		// Adds a new listener, with the owner being the LibE plugin
		this.addListener(new Listener(this) // Listener#new(ModuleBase)
		{
			@HandlerMeta // Not required, Eclipse just freaks out
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
			}
		});

		// Registers a command with the module
		this.addCommand(new Command("ping" /* The name of the command */, this /* The owner of the command */)
		{
			@Override
			public void run()
			{
				// Sends "PONG!"
				this.getSender().sendMessage("PONG!");
			}
		});

		// Simple version
		this.addCommand("pong", command -> command.getSender().sendMessage("PING!"));
	}

}