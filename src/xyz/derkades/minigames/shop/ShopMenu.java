package xyz.derkades.minigames.shop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.derkades.derkutils.bukkit.IconMenu;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;

public class ShopMenu extends IconMenu {

	public ShopMenu(Player player) {
		super(Minigames.getInstance(), "Shop", 9, player);
		
		items.put(0, new ItemBuilder(Material.INK_SACK).data(12).name(ChatColor.AQUA + "Name colors (250 points for every color)").create());
		items.put(8, new ItemBuilder(Material.BARRIER).coloredName("&cBack").create());
	}

	@Override
	public boolean onOptionClick(OptionClickEvent event) {
		Player player = event.getPlayer();
		String name = event.getName();

		if (name.contains("color")){
			new ShopColorMenu(player).open();
		} else {
			new MainMenu(player).open();
		}
		
		return false;
	}
	
	/*private static IconMenu menu = new IconMenu("Shop", 1*9, new IconMenu.OptionClickEventHandler(){

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
	}*/

}
