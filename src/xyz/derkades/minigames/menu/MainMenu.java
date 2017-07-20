package xyz.derkades.minigames.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.derkades.derkutils.bukkit.IconMenu;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.games.Game;

import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.RED;

public class MainMenu extends IconMenu {

	public MainMenu(Player player) {
		super(Minigames.getInstance(), "Menu", 9, player);
		
		items.put(0, new ItemBuilder(Material.INK_SACK).data(12).name(DARK_AQUA + "Points").lore(AQUA + "You have " + Points.getPoints(player) + " points.").create());
		items.put(1, new ItemBuilder(Material.INK_SACK).data(12).name(DARK_AQUA + "Games").lore(AQUA + "Click to get game information.").create());
		items.put(2, new ItemBuilder(Material.INK_SACK).data(12).name(DARK_AQUA + "Shop").lore(AQUA + "Buy items using points.").create());
		items.put(8, new ItemBuilder(Material.BARRIER).name(RED + "Close").lore(DARK_RED + "Click to close this menu.").create());
	}
	
	@Override
	public boolean onOptionClick(OptionClickEvent event) {
		if (event.getName().contains("Games")){
			Player player = event.getPlayer();
			for (Game game : Game.getAllGames()){
				player.sendMessage("------------------------------------------");
				player.sendMessage(DARK_AQUA + "Name: " + AQUA + game.getName());
				player.sendMessage(DARK_AQUA + "Reward (points): " + AQUA + game.getPoints().getMinimum() + "-" + game.getPoints().getMaximum());
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
		} else {
			event.getPlayer().sendMessage("error");
			return false;
		}
	}

}
