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
import xyz.derkades.minigames.games.GameMap;

public class MainMenu extends IconMenu {

	public MainMenu(Player player) {
		super(Minigames.getInstance(), "Menu", 9, player);
		
		addItems(player);
	}
	
	private void addItems(Player player) {
		items.put(0, new ItemBuilder(Material.GRAY_DYE).name(DARK_AQUA + "Points").lore(AQUA + "You have " + Points.getPoints(player) + " points.").create());
		items.put(1, new ItemBuilder(Material.GRAY_DYE).name(DARK_AQUA + "Games").lore(AQUA + "Click to get game information.").create());
		items.put(2, new ItemBuilder(Material.GRAY_DYE).name(DARK_AQUA + "Shop").lore(AQUA + "Buy items using points.").create());
		
		Material settingMaterial;
		if (Minigames.getInstance().getConfig().getStringList("disabled-description").contains(player.getUniqueId().toString())) {
			settingMaterial = Material.RED_DYE;
		} else {
			settingMaterial = Material.LIME_DYE;
		}
		
		items.put(3, new ItemBuilder(settingMaterial).name(DARK_AQUA + "Game descriptions").lore(AQUA + "Click to enable or disable game", AQUA + "description messages at the start of each game").create());
		
		items.put(8, new ItemBuilder(Material.BARRIER).name(RED + "Close").lore(DARK_RED + "Click to close this menu.").create());
	}
	
	@Override
	public boolean onOptionClick(OptionClickEvent event) {
		if (event.getName().contains("Games")){
			Player player = event.getPlayer();
			for (Game game : Game.GAMES){
				player.sendMessage(ChatColor.GRAY + "------------------------------------------");
				player.sendMessage(DARK_AQUA + "Name: " + AQUA + game.getName());
				//player.sendMessage(DARK_AQUA + "Reward (points): " + AQUA + game.getMinimumPoints() + "-" + game.getMaximumPoints());
				player.sendMessage(DARK_AQUA + "Minimum online players: " + AQUA + game.getRequiredPlayers());
				if (game.getGameMaps() != null) player.sendMessage(DARK_AQUA + "Maps: " + AQUA + getMapsString(game));
			}
			player.sendMessage("------------------------------------------");
			return true;
		} else if (event.getName().contains("Shop")){
			new ShopMenu(event.getPlayer()).open();
			return false;
		} else if (event.getName().contains("Close")){
			return true;
		} else if (event.getName().contains("Points")) {
			return false;
		} else if (event.getName().contains("Game descriptions")) {
			Player player = event.getPlayer();
			
			ItemStack item = event.getItemStack();
			List<String> list = Minigames.getInstance().getConfig().getStringList("disabled-description");
			
			if (item.getType().equals(Material.LIME_DYE)) {
				player.sendMessage(ChatColor.GOLD + "Minigame descriptions have been disabled.");
				list.add(player.getUniqueId().toString());
			} else {
				player.sendMessage(ChatColor.GOLD + "Minigame descriptions have been enabled.");
				list.remove(player.getUniqueId().toString());
			}
			
			Minigames.getInstance().getConfig().set("disabled-description", list);
			Minigames.getInstance().saveConfig();
			
			addItems(player);
			this.refreshItems();
			
			return false;
		} else {
			event.getPlayer().sendMessage("error");
			return false;
		}
	}
	
	private String getMapsString(Game game) {
		List<String> mapNames = new ArrayList<>();
		for (GameMap map : game.getGameMaps()) {
			mapNames.add(map.getName());
		}
		return String.join(", ", mapNames);
	}

}
