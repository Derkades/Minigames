package xyz.derkades.minigames.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.Game;

public class Leaderboard {
	
	private final Sidebar sidebar;
	private final Map<UUID, Integer> points;
	private Map<UUID, Integer> sortedCache = null;
	
	public Leaderboard() {
		this.points = new HashMap<>();
		this.sidebar = new Sidebar(ChatColor.DARK_AQUA + "" + ChatColor.DARK_AQUA + "Score",
				Minigames.getInstance(), Integer.MAX_VALUE, new SidebarString("Loading..."));
		
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			setScore(player, 0);
		}
	}
	
	private Map<UUID, Integer> getSorted(){
		if (this.sortedCache == null) {
			this.sortedCache = Utils.sortByValue(this.points);
		}

		return this.sortedCache;
	}
	
	public void update(final int secondsLeft) {
		final List<SidebarString> sidebarStrings = new ArrayList<>();
		
		getSorted().forEach((uuid, points) -> {
			final Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				return;
			}
			sidebarStrings.add(new SidebarString(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.GREEN + points));
		});

		this.sidebar.setEntries(sidebarStrings);
		this.sidebar.addEmpty().addEntry(new SidebarString(ChatColor.GRAY + "Time left: " + secondsLeft + " seconds."));
		this.sidebar.update();
	}
	
	public void show() {
		Bukkit.getOnlinePlayers().forEach(this.sidebar::showTo);
	}
	
	public void showTo(final MPlayer player) {
		this.sidebar.showTo(player.bukkit());
	}
	
	public void hide() {
		Bukkit.getOnlinePlayers().forEach(this.sidebar::hideFrom);
	}
	
	public int getScore(final MPlayer player) {
		if (this.points.containsKey(player.getUniqueId())) {
			return this.points.get(player.getUniqueId());
		} else {
			return 0;
		}
	}
	
	public boolean hasScore(final MPlayer player) {
		return this.points.containsKey(player.getUniqueId());
	}
	
	public void setScore(final MPlayer player, final int newScore) {
		this.points.put(player.getUniqueId(), newScore);
		this.sortedCache = null;
	}
	
	public void incrementScore(final MPlayer player) {
		setScore(player, getScore(player) + 1);
	}
	
	public int getAndIncrementScore(final MPlayer player) {
		final int previousScore = getScore(player);
		setScore(player, previousScore + 1);
		return previousScore;
	}
	
	public int incrementAndGetScore(final MPlayer player) {
		return getAndIncrementScore(player) + 1;
	}
	
	public List<UUID> getWinners() {
		return Winners.fromPointsMap(this.points);
	}
	
	public List<UUID> getWinnersPrintHide(final Game<?> game) {
		this.hide();
		this.printToChat(game);
		return Winners.fromPointsMap(this.points);
	}
	
	public void printToChat(final Game<?> game) {
		printToChat(game::sendMessage);
	}
	
	public void printToChat(final Consumer<String> printFunction) {
		final AtomicInteger i = new AtomicInteger();
		getSorted().forEach((uuid, points) -> {
			if (i.getAndIncrement() > 2) {
				return;
			}
			
			final Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				return;
			}
			
			printFunction.accept(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.GREEN + points);
		});
	}
	
}
