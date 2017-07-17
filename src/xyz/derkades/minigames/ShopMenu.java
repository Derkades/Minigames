package xyz.derkades.minigames;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import xyz.derkades.minigames.utils.IconMenu;
import xyz.derkades.minigames.utils.ItemBuilder;
import xyz.derkades.minigames.utils.IconMenu.OptionClickEvent;

public class ShopMenu {
	
	private static IconMenu menu = new IconMenu("Shop", 1*9, new IconMenu.OptionClickEventHandler(){

		@Override
		public void onOptionClick(OptionClickEvent event){
			
			Player player = event.getPlayer();
			
			String name = event.getName();
			
			if (name.equals("Name colors (250 points for every color).")){
				event.setWillClose(false);
				ShopColorMenu.open(player);
			} else if (name.equals("Back")){
				event.setWillClose(false);
				Menu.open(event.getPlayer());
			}
		}
		
	});
	
	static void open(Player player){
		fillMenu(player);
		menu.open(player);
	}
	
	private static void fillMenu(Player player){
		menu.setOption(0, new ItemBuilder(Material.INK_SACK).setDamage(12).create(), "Name colors (250 points for every color).");
		menu.setOption(8, new ItemStack(Material.BARRIER), "Back", "Click to go back.");
	}

}
