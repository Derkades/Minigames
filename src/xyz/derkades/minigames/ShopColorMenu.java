package xyz.derkades.minigames;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import xyz.derkades.minigames.utils.IconMenu;
import xyz.derkades.minigames.utils.ItemBuilder;
import xyz.derkades.minigames.utils.IconMenu.OptionClickEvent;
	
public class ShopColorMenu {
	
	private static IconMenu menu = new IconMenu("Name colors", 1*9, new IconMenu.OptionClickEventHandler(){

		@Override
		public void onOptionClick(OptionClickEvent event) {
			event.setWillClose(false);
			
			Player player = event.getPlayer();
			
			String name = event.getName();
			
			if (name.equals("Back")){
				ShopMenu.open(event.getPlayer());
				return;
			}
			
			ChatColor color = ChatColor.RESET;
			
			if (name.equals("Cyan")){
				color = ChatColor.DARK_AQUA;
			} else if (name.equals("Blue")){
				color = ChatColor.BLUE;
			} else if (name.equals("Red")){
				color = ChatColor.RED;
			} else if (name.equals("Orange")){
				color = ChatColor.GOLD;
			} else if (name.equals("Yellow")){
				color = ChatColor.YELLOW;
			} else if (name.equals("Green")){
				color = ChatColor.DARK_GREEN;
			} else if (name.equals("Light green")){
				color = ChatColor.GREEN;
			} else if (name.equals("Magenta")){
				color = ChatColor.LIGHT_PURPLE;
			}
			
			if (Points.getPoints(player) >= 250){
				Points.removePoints(player, 250);
				player.sendMessage(ChatColor.AQUA + "Changed color to " + name.toLowerCase());
				NameColor.setNameColor(player, color + "");
			} else {
				player.sendMessage(ChatColor.RED + "Not enough points.");
			}
		}
		
	});
	
	static void open(Player player){
		fillMenu(player);
		menu.open(player);
	}
	
	private static void fillMenu(Player player){
		menu.setOption(0, new ItemBuilder(Material.INK_SACK).setDamage(6).create(), "Cyan", "Change name color to cyan.");
		menu.setOption(1, new ItemBuilder(Material.INK_SACK).setDamage(4).create(), "Blue", "Change name color to blue.");
		menu.setOption(2, new ItemBuilder(Material.INK_SACK).setDamage(1).create(), "Red", "Change name color to red.");
		menu.setOption(3, new ItemBuilder(Material.INK_SACK).setDamage(14).create(), "Orange", "Change name color to orange.");
		menu.setOption(4, new ItemBuilder(Material.INK_SACK).setDamage(11).create(), "Yellow", "Change name color to yellow.");
		menu.setOption(5, new ItemBuilder(Material.INK_SACK).setDamage(2).create(), "Green", "Change name color to green.");
		menu.setOption(6, new ItemBuilder(Material.INK_SACK).setDamage(10).create(), "Light green", "Change name color to light green.");
		menu.setOption(7, new ItemBuilder(Material.INK_SACK).setDamage(13).create(), "Magenta", "Change name color to magenta.");
		menu.setOption(8, new ItemStack(Material.BARRIER), "Back", "Click to go back.");
	}

}
