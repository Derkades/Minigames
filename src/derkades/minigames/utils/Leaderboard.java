package derkades.minigames.utils;

import derkades.minigames.Minigames;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.derkades.derkutils.bukkit.sidebar.Sidebar;
import xyz.derkades.derkutils.bukkit.sidebar.SidebarString;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Leaderboard {

	@NotNull
	private final Sidebar sidebar;
	@NotNull
	private final Map<UUID, Integer> points;
	@Nullable
	private Map<UUID, Integer> sortedCache = null;

	private static final ComponentSerializer<Component, TextComponent, String> COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
			.character(LegacyComponentSerializer.SECTION_CHAR)
			.useUnusualXRepeatedCharacterHexFormat()
			.build();

	public Leaderboard() {
		this.points = new HashMap<>();
		this.sidebar = new Sidebar(ChatColor.DARK_AQUA + "" + ChatColor.DARK_AQUA + "Score",
				Minigames.getInstance(), Integer.MAX_VALUE, new SidebarString("Loading..."));

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			setScore(player, 0);
		}
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
	private Component leaderboardEntry(final Player player, final int points) {
		return player.displayName().append(Component.text(": ", NamedTextColor.GRAY)).append(Component.text(points, NamedTextColor.WHITE));
	}

	public void update(final int secondsLeft) {
		final List<SidebarString> sidebarStrings = new ArrayList<>();

		getSorted().forEach((uuid, points) -> {
			final Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				return;
			}

			final Component c = leaderboardEntry(player, points);
			sidebarStrings.add(new SidebarString(COMPONENT_SERIALIZER.serialize(c)));
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
		return this.points.getOrDefault(player.getUniqueId(), 0);
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

	public Set<UUID> getWinners() {
		return Winners.fromPointsMap(this.points);
	}

	public Set<UUID> getWinnersPrintHide() {
		this.hide();
		final AtomicInteger i = new AtomicInteger();
		getSorted().forEach((uuid, points) -> {
			// Only list top 3 players
			if (i.getAndIncrement() > 2) {
				return;
			}

			final Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				return;
			}

			Bukkit.broadcast(leaderboardEntry(player, points));
		});

		return getWinners();
	}

}
