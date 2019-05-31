package xyz.derkades.minigames.menu;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.RED;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.maps.GameMap;

public class MainMenu extends IconMenu {

	public MainMenu(final Player player) {
		super(Minigames.getInstance(), "Menu", 9, player);

		this.addItems(player);
	}

	private void addItems(final Player player) {
		this.items.put(0, new ItemBuilder(Material.GRAY_DYE).name(DARK_AQUA + "Points").lore(AQUA + "You have " + Points.getPoints(player) + " points.").create());
		this.items.put(1, new ItemBuilder(Material.GRAY_DYE).name(DARK_AQUA + "Games").lore(AQUA + "Click to get game information.").create());
		this.items.put(2, new ItemBuilder(Material.GRAY_DYE).name(DARK_AQUA + "Points explanation").lore(AQUA + "Click to get points information.").create());
		this.items.put(3, new ItemBuilder(Material.GRAY_DYE).name(DARK_AQUA + "Shop").lore(AQUA + "Buy perks using GladeCoins").create());

		Material settingMaterial;
		if (Minigames.getInstance().getConfig().getStringList("disabled-description").contains(player.getUniqueId().toString())) {
			settingMaterial = Material.RED_DYE;
		} else {
			settingMaterial = Material.LIME_DYE;
		}

		this.items.put(4, new ItemBuilder(settingMaterial).name(DARK_AQUA + "Game descriptions").lore(AQUA + "Click to enable or disable game", AQUA + "description messages at the start of each game").create());

		this.items.put(8, new ItemBuilder(Material.BARRIER).name(RED + "Close").lore(DARK_RED + "Click to close this menu.").create());
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		if (event.getName().contains("Games")){
			final Player player = event.getPlayer();
			for (final Game game : Game.GAMES){
				player.sendMessage(ChatColor.GRAY + "------------------------------------------");
				player.sendMessage(DARK_AQUA + "Name: " + AQUA + game.getName());
				//player.sendMessage(DARK_AQUA + "Reward (points): " + AQUA + game.getMinimumPoints() + "-" + game.getMaximumPoints());
				player.sendMessage(DARK_AQUA + "Minimum online players: " + AQUA + game.getRequiredPlayers());
				if (game.getGameMaps() != null) player.sendMessage(DARK_AQUA + "Maps: " + AQUA + this.getMapsString(game));
			}
			player.sendMessage("------------------------------------------");
			return true;
		} else if (event.getName().contains("Shop")){
			new ShopMenu(event.getPlayer()).open();
			//event.getPlayer().sendMessage("The shop is temporarily disabled");
			return false;
		} else if (event.getName().contains("Close")){
			return true;
		} else if (event.getName().contains("Points explanation")) {
			final Player player = event.getPlayer();
			player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Winning a game");
			player.sendMessage(ChatColor.GRAY + "  1-2 online players: +3 points, +3 GladeCoins");
			player.sendMessage(ChatColor.GRAY + "  3-4 online players: +4 points, +4 GladeCoins");
			player.sendMessage(ChatColor.GRAY + "  5+ online players: +5 points, +5 GladeCoins");
			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Losing a game");
			player.sendMessage(ChatColor.GRAY + "  +1 point, +0 GladeCoins");
			return true;
		} else if (event.getName().contains("Points")) {
			return false;
		} else if (event.getName().contains("Game descriptions")) {
			final Player player = event.getPlayer();

			final ItemStack item = event.getItemStack();
			final List<String> list = Minigames.getInstance().getConfig().getStringList("disabled-description");

			if (item.getType().equals(Material.LIME_DYE)) {
				player.sendMessage(ChatColor.GOLD + "Minigame descriptions have been disabled.");
				list.add(player.getUniqueId().toString());
			} else {
				player.sendMessage(ChatColor.GOLD + "Minigame descriptions have been enabled.");
				list.remove(player.getUniqueId().toString());
			}

			Minigames.getInstance().getConfig().set("disabled-description", list);
			Minigames.getInstance().saveConfig();

			this.addItems(player);
			this.refreshItems();

			return false;
		} else {
			event.getPlayer().sendMessage("error");
			return false;
		}
	}

	private String getMapsString(final Game game) {
		final List<String> mapNames = new ArrayList<>();
		for (final GameMap map : game.getGameMaps()) {
			mapNames.add(map.getName());
		}
		return String.join(", ", mapNames);
	}

}
