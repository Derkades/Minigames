package derkades.minigames.utils;

import com.google.gson.stream.JsonWriter;
import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.utils.event.GameResultSaveEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.derkades.derkutils.bukkit.sidebar2.ComponentSidebar;

import java.io.IOException;
import java.lang.ref.Cleaner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

public class Leaderboard implements Listener {

	@NotNull
	private final ComponentSidebar sidebar;
	@NotNull
	private final Map<UUID, Integer> points;
	@Nullable
	private Map<UUID, Integer> sortedCache = null;
	private final Scoreboard scoreboard;
	@Nullable
	private String previousTimeLeftScoreName;
	@NotNull
	private final Objective[] objectives = new Objective[3];

	private boolean unregistered = false;

	private Leaderboard() {
		this.points = new HashMap<>();
		this.sidebar = new ComponentSidebar(text("Scores", NamedTextColor.GRAY));
		this.sidebar.addEntry(text("Game starting soon", NamedTextColor.GRAY));
		this.sidebar.addEntry(empty());

		for (final Player player : Bukkit.getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			this.points.put(uuid, 0);
			this.sidebar.addEntry(leaderboardEntry(uuid, 0));
		}

		Bukkit.getPluginManager().registerEvents(this, Minigames.getInstance());
	}

	private Objective createObjective(DisplaySlot slot) {
		Objective objective = this.scoreboard.registerNewObjective(
				"leaderboard",
				"dummy",
				Component.text("Points", NamedTextColor.GRAY)
		);
		objective.setDisplaySlot(slot);
		return objective;
	}

	@SuppressWarnings("null")
	@NotNull
	private Map<@NotNull UUID, @NotNull Integer> getSorted() {
		var cache = this.sortedCache;
		if (cache == null) {
			cache = Utils.sortByValue(this.points);
			this.sortedCache = cache;
		}
		return cache;
	}

	@NotNull
	private Component leaderboardEntry(final UUID uuid, final int points) {
		Component displayName;
		Player player = Bukkit.getPlayer(uuid);
		if (player == null) {
			OfflinePlayer offline = Bukkit.getOfflinePlayer(uuid);
			String name = offline.getName();
			displayName = name == null ? text("<unknown name?>") : text(name, NamedTextColor.GRAY);
		} else {
			displayName = player.displayName();
		}
		return displayName.append(text(": ", NamedTextColor.GRAY)).append(text(points, NamedTextColor.WHITE));
	}

	public void update(final int secondsLeft) {
		this.sidebar.setEntry(0, text("Time left: " + secondsLeft + " seconds.", NamedTextColor.GRAY));

		int i = 2;
		for (Map.Entry<UUID, Integer> entry : this.getSorted().entrySet()) {
			this.sidebar.setEntry(i++, leaderboardEntry(entry.getKey(), entry.getValue()));
		}
	}

//	public void showTo(final MPlayer player) {
//		this.sidebar.showTo(player.bukkit());
//	}

	public int getScore(final MPlayer player) {
		return this.points.getOrDefault(player.getUniqueId(), 0);
	}

//	public boolean hasScore(final MPlayer player) {
//		return this.points.containsKey(player.getUniqueId());
//	}

	public void setScore(final MPlayer player, final int newScore) {
		this.points.put(player.getUniqueId(), newScore);
		this.sortedCache = null;
	}

//	public void incrementScore(final MPlayer player) {
//		setScore(player, getScore(player) + 1);
//	}

	public int getAndIncrementScore(final MPlayer player) {
		final int previousScore = getScore(player);
		setScore(player, previousScore + 1);
		return previousScore;
	}

	public int incrementAndGetScore(final MPlayer player) {
		return getAndIncrementScore(player) + 1;
	}

	public Set<UUID> getWinnersAndUnregister() {
//		this.hide();
		Set<UUID> winners = new HashSet<>(1); // probably only one winner
		int highestScore = 0;
		int i = 0;
		for (Map.Entry<UUID, Integer> entry : getSorted().entrySet()) {
			UUID uuid = entry.getKey();
			int points = entry.getValue();
			if (i == 0) {
				highestScore = points;
				winners.add(uuid);
			} else {
				if (points == highestScore) {
					winners.add(uuid);
				}
			}

			// Print top 5 to chat
			Bukkit.broadcast(leaderboardEntry(uuid, points));

			if (++i > 4) {
				break;
			}
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			this.sidebar.hideFrom(player);
		}

		HandlerList.unregisterAll(this);
		// TODO testing
//		this.unregistered = true;
		return winners;
	}

	@EventHandler
	public void onGameResultSave(GameResultSaveEvent event) {
		try {
			JsonWriter writer = event.getResultWriter();
			writer.name("leaderboard");
			writer.beginObject();
			for (Map.Entry<UUID, Integer> entry : points.entrySet()) {
				writer.name(entry.getKey().toString());
				writer.value(entry.getValue());
			};
			writer.endObject();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

//	@Override
//	protected void finalize() {
//		if (!this.unregistered) {
//			Logger.warning("Leaderboard was not unregistered before garbage collection!");
//		}
//	}

	public void checkUnregister() {
		if (!this.unregistered) {
			Logger.warning("Leaderboard was not unregistered before garbage collection!");
		}
	}

	public static Leaderboard createLeaderboard() {
		Leaderboard leaderboard = new Leaderboard();
		Cleaner cleaner = Cleaner.create();
		cleaner.register(leaderboard, leaderboard::checkUnregister).clean();
		return leaderboard;
	}

}
