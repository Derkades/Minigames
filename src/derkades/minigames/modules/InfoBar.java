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

import derkades.minigames.GameState;
import derkades.minigames.games.Game;
import derkades.minigames.utils.PluginLoadEvent;
import derkades.minigames.utils.PluginUnloadEvent;
import derkades.minigames.utils.Scheduler;

public class InfoBar extends Module {

	private BossBar bar;

	public void tick() {
		switch(GameState.getCurrentState()) {
			case IDLE -> {
				this.bar.setTitle("Waiting");
				this.bar.setProgress(1.0f);
				this.bar.setColor(BarColor.WHITE);
				this.bar.setStyle(BarStyle.SOLID);
			}
			case IDLE_MAINTENANCE -> {
				this.bar.setTitle("Maintenance mode");
				this.bar.setProgress(1.0f);
				this.bar.setColor(BarColor.RED);
				this.bar.setStyle(BarStyle.SOLID);
			}
			case COUNTDOWN -> {
				final Game<?> game = GameState.getCurrentGame();
				this.bar.setTitle("Next up: " + game.getName());
				this.bar.setProgress(1.0f);
				this.bar.setColor(BarColor.GREEN);
				this.bar.setStyle(BarStyle.SOLID);
			}
			case RUNNING_COUNTDOWN -> {
				final Game<?> game = GameState.getCurrentGame();
				this.bar.setTitle("Starting soon: " + game.getName());
				if (game.getPreDuration() > 0) {
					this.bar.setProgress(getInterpolatedSmoothProgress(game.getSecondsLeft() - game.getDuration(), game.getPreDuration()));
				} else {
					this.bar.setProgress(0f);
				}
				this.bar.setColor(BarColor.GREEN);
				this.bar.setStyle(BarStyle.SEGMENTED_10);
			}
			case RUNNING_STARTED -> {
				final Game<?> game = GameState.getCurrentGame();
				this.bar.setTitle("Playing " + game.getName());
				this.bar.setProgress(getInterpolatedSmoothProgress(game.getSecondsLeft(), game.getDuration()));
				this.bar.setColor(BarColor.BLUE);
				this.bar.setStyle(BarStyle.SOLID);
			}
			case RUNNING_SKIPPED, RUNNING_ENDED_EARLY -> {
				final Game<?> game = GameState.getCurrentGame();
				this.bar.setTitle("Playing " + game.getName());
				this.bar.setProgress(getInterpolatedSmoothProgress(game.getSecondsLeft(), game.getDuration()));
				this.bar.setColor(BarColor.RED);
				this.bar.setStyle(BarStyle.SOLID);
			}
		}
	}

	private int lastTimeLeft = -1;
	private int numberOfTimesTheSameTime = 0;

	private float getInterpolatedSmoothProgress(final int timeLeftSeconds, final int totalSeconds) {
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
