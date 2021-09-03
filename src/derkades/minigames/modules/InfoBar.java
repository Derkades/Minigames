package derkades.minigames.modules;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.PluginLoadEvent;
import derkades.minigames.utils.PluginUnloadEvent;
import derkades.minigames.utils.Scheduler;

public class InfoBar extends Module {

	private BossBar bar;

	private int lastTimeLeft = -1;
	private int numberOfTimesTheSameTime = 0;

	public void tick() {
		final Game<?> game = Minigames.CURRENT_GAME;
		if (game != null) {
			if (game.hasStarted()) {
				this.bar.setTitle("Playing " + game.getName());
				this.bar.setProgress(getSmoothProgress(game.getSecondsLeft(), game.getDuration()));
				this.bar.setColor(BarColor.BLUE);
				this.bar.setStyle(BarStyle.SOLID);
			} else {
				this.bar.setTitle("Starting soon: " + game.getName());
				if (game.getPreDuration() > 0) {
					this.bar.setProgress(getSmoothProgress(game.getSecondsLeft() - game.getDuration(), game.getPreDuration()));
				} else {
					this.bar.setProgress(0f);
				}
				this.bar.setColor(BarColor.GREEN);
				this.bar.setStyle(BarStyle.SEGMENTED_10);
			}
		} else {
			this.bar.setTitle("Waiting");
			this.bar.setProgress(1.0f);
			this.bar.setColor(BarColor.WHITE);
			this.bar.setStyle(BarStyle.SOLID);
		}
	}

	private float getSmoothProgress(final int timeLeftSeconds, final int totalSeconds) {
		int timeLeftTicks;
		if (timeLeftSeconds == this.lastTimeLeft) {
			timeLeftTicks = timeLeftSeconds*20 - this.numberOfTimesTheSameTime;
			this.numberOfTimesTheSameTime++;
		} else {
			timeLeftTicks = timeLeftSeconds*20;
			this.numberOfTimesTheSameTime = 0;
			this.lastTimeLeft = timeLeftSeconds;
		}
		final float progress = timeLeftTicks / (totalSeconds*20f);
		return progress >= 0 ? progress : 0;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(final PlayerJoinEvent event) {
		this.bar.addPlayer(event.getPlayer());
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		this.bar.removePlayer(event.getPlayer());
	}

	@EventHandler
	public void onLoad(final PluginLoadEvent event) {
		this.bar = Bukkit.getServer().createBossBar("...loading...", BarColor.WHITE, BarStyle.SOLID);
		System.out.println(this.bar.isVisible());
		for (final Player player : Bukkit.getOnlinePlayers()) {
			this.bar.addPlayer(player);
		}
		Scheduler.repeat(1, this::tick);
	}

	@EventHandler
	public void onUnload(final PluginUnloadEvent event) {
		this.bar.removeAll();
	}

}
