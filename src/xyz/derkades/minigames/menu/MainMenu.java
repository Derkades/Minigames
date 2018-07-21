package xyz.derkades.minigames.menu;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.RED;

import java.util.List;

import org.bukkit.Bukkit;
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

public class MainMenu extends IconMenu {

	public MainMenu(Player player) {
		super(Minigames.getInstance(), "Menu", 9, player);
		
		items.put(0, new ItemBuilder(Material.INK_SACK).data(12).name(DARK_AQUA + "Points").lore(AQUA + "You have " + Points.getPoints(player) + " points.").create());
		items.put(1, new ItemBuilder(Material.INK_SACK).data(12).name(DARK_AQUA + "Games").lore(AQUA + "Click to get game information.").create());
		items.put(2, new ItemBuilder(Material.INK_SACK).data(12).name(DARK_AQUA + "Shop").lore(AQUA + "Buy items using points.").create());
		
		int settingData;
		if (Minigames.getInstance().getConfig().getStringList("disabled-description").contains(player.getUniqueId().toString())) {
			settingData = 8;
		} else {
			settingData = 10;
		}
		
		items.put(3, new ItemBuilder(Material.INK_SACK).data(settingData).name(DARK_AQUA + "Description").lore(AQUA + "Click to enable or disable game descriptions").create());
		items.put(8, new ItemBuilder(Material.BARRIER).name(RED + "Close").lore(DARK_RED + "Click to close this menu.").create());
	}
	
	@Override
	public boolean onOptionClick(OptionClickEvent event) {
		if (event.getName().contains("Games")){
			Player player = event.getPlayer();
			for (Game game : Game.GAMES){
				player.sendMessage("------------------------------------------");
				player.sendMessage(DARK_AQUA + "Name: " + AQUA + game.getName());
				player.sendMessage(DARK_AQUA + "Reward (points): " + AQUA + game.getMinimumPoints() + "-" + game.getMaximumPoints());
				player.sendMessage(DARK_AQUA + "Minimum online players: " + AQUA + game.getRequiredPlayers());
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
		} else if (event.getName().contains("Description")) {
			Player player = event.getPlayer();
			
			ItemStack item = event.getItemStack();
			List<String> list = Minigames.getInstance().getConfig().getStringList("disabled-description");
			
			if (item.getDurability() == 10) {
				player.sendMessage(ChatColor.GOLD + "Minigame descriptions have been disabled.");
				list.add(player.getUniqueId().toString());
			} else {
				player.sendMessage(ChatColor.GOLD + "Minigame descriptions have been enabled.");
				list.remove(player.getUniqueId().toString());
			}
			
			Minigames.getInstance().getConfig().set("disabled-description", list);
			Minigames.getInstance().saveConfig();
			
			this.close();
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Minigames.getInstance(), () -> {
				new MainMenu(player).open();
			}, 1);
			
			return false;
		} else {
			event.getPlayer().sendMessage("error");
			return false;
		}
	}

}
