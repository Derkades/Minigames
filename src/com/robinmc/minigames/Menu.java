package com.robinmc.minigames;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.robinmc.minigames.games.Game;
import com.robinmc.minigames.utils.IconMenu;
import com.robinmc.minigames.utils.IconMenu.OptionClickEvent;
import com.robinmc.minigames.utils.ItemBuilder;

public class Menu {
	
	private static IconMenu menu = new IconMenu("Menu", 1*9, new IconMenu.OptionClickEventHandler(){

		@Override
		public void onOptionClick(OptionClickEvent event) {
			if (event.getName() == "Game info"){
				Player player = event.getPlayer();
				for (Game game : Game.getAllGames()){
					player.sendMessage("------------------------------------------");
					player.sendMessage(DARK_AQUA + "Name: " + AQUA + game.getName());
					player.sendMessage(DARK_AQUA + "Reward (points): " + AQUA + game.getPoints().getMinimum() + "-" + game.getPoints().getMaximum());
					player.sendMessage(DARK_AQUA + "Minimum online players: " + AQUA + game.getRequiredPlayers());
				}
				player.sendMessage("------------------------------------------");
			} else if (event.getName().equals("Shop")){
				event.setWillClose(false);
				ShopMenu.open(event.getPlayer());
			} else if (event.getName().equals("Close")){
				event.setWillClose(true);
			} else {
				event.setWillClose(false);
			}
		}
		
	});
	
	static void open(Player player){
		fillMenu(player);
		menu.open(player);
	}
	
	private static void fillMenu(Player player){
		menu.setOption(0, new ItemBuilder(Material.INK_SACK).setDamage(12).create(), "Points", "You have " + Points.getPoints(player) + " points.");
		menu.setOption(1, new ItemBuilder(Material.INK_SACK).setDamage(12).create(), "Game info", "Click to get game info.");
		menu.setOption(2, new ItemBuilder(Material.INK_SACK).setDamage(12).create(), "Shop", "Buy items using points.");
		menu.setOption(8, new ItemStack(Material.BARRIER), "Close", "Click to close this menu.");
	}

}
