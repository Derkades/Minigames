package derkades.minigames.modules;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import derkades.minigames.Minigames;

public abstract class Module implements Listener {

	public Module() {
		Bukkit.getPluginManager().registerEvents(this, Minigames.getInstance());
	}

}
