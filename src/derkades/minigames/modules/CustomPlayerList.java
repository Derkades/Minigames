package derkades.minigames.modules;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.Scheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import nl.rkslot.pluginreloader.PluginReloader;

public class CustomPlayerList extends Module {

	private final AtomicReference<String> lastPluginUpdate;
	final PluginReloader reloader = (PluginReloader) Bukkit.getPluginManager().getPlugin("PluginReloader");

	public CustomPlayerList() {
		this.lastPluginUpdate = new AtomicReference<>();
		this.lastPluginUpdate.set("?");
		final Component header = Component.text(" ");
		Scheduler.repeat(20, () -> {
			final Component footer = Component.text("\n")
					.append(Component.text("Online players: ", NamedTextColor.GRAY))
					.append(Component.text(Minigames.getOnlinePlayerCount(), NamedTextColor.WHITE))
					.append(Component.text("\nPlayable games: ", NamedTextColor.GRAY))
					.append(Component.text(getPlayableGames(), NamedTextColor.WHITE))
					.append(Component.text(" / ", NamedTextColor.GRAY))
					.append(Component.text(Game.GAMES.length, NamedTextColor.WHITE))
					.append(Component.text("\nLast plugin update: " + this.lastPluginUpdate.get(), NamedTextColor.GRAY))
					.append(Component.text("\nBukkit reload count: " + this.getReloadCount(), NamedTextColor.GRAY))
					.append(Component.text("\nPlugin reload count: " + this.reloader.getReloadCount(Minigames.getInstance()), NamedTextColor.GRAY));

			Bukkit.getOnlinePlayers().forEach(p -> p.sendPlayerListHeaderAndFooter(header, footer));
		});

		Scheduler.repeat(30*20, () -> Scheduler.async(() -> {
			final File jar = new File(Minigames.getInstance().getDataFolder().getParentFile().getPath(), "Minigames.jar");
			final long lastModified = jar.lastModified();
			final String pretty = Minigames.PRETTY_TIME.format(new Date(lastModified));
			this.lastPluginUpdate.set(pretty);
		}));
	}

	private int getPlayableGames() {
		final int players = Minigames.getOnlinePlayerCount();
		return (int) Arrays.stream(Game.GAMES).filter(g -> players >= g.getRequiredPlayers()).count();
	}

	private int getReloadCount() {
		try {
			final Class<?> c = Class.forName("org.bukkit.craftbukkit.v1_17_R1.CraftServer");
			return (int) c.getField("reloadCount").get(Bukkit.getServer());
		} catch (final Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
