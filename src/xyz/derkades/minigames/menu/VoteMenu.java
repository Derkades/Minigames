package xyz.derkades.minigames.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;

public class VoteMenu extends IconMenu {

	private String gameName;
	
	public VoteMenu(Player player, String gameName) {		
		super(Minigames.getInstance(), "Did you enjoy this game?", 5*9, player);
		
		this.gameName = gameName;
		
		items.put(13, new ItemBuilder(Material.STAINED_CLAY)
				.data(9)
				.name(ChatColor.DARK_AQUA + "Did you like this game?")
				.lore(ChatColor.GRAY + "Please click either \"Yes\" or \"No\".")
				.create());
		items.put(20, new ItemBuilder(Material.STAINED_CLAY)
				.data(14)
				.name(ChatColor.RED + "No")
				.lore(ChatColor.GRAY + "The game will be picked less often.")
				.create());
		items.put(22, new ItemBuilder(Material.STAINED_CLAY)
				.data(4)
				.name(ChatColor.RED + "Neutral")
				.lore(ChatColor.GRAY + "The game will be picked just as often.")
				.create());
		items.put(24, new ItemBuilder(Material.STAINED_CLAY)
				.data(5)
				.name(ChatColor.GREEN + "Yes")
				.lore(ChatColor.GRAY + "The game will be picked more often.")
				.create());
	}

	@Override
	public boolean onOptionClick(OptionClickEvent event) {
		String displayName = event.getItemStack().getItemMeta().getDisplayName();
		if (displayName.equals(ChatColor.RED + "No")) {
			vote(false);
		} else if (displayName.equals(ChatColor.GREEN + "Yes")){
			vote(true);
		}
		
		return true;
	}
	
	private void vote(boolean yes) {
		double multiplier;
		if (Minigames.getInstance().getConfig().contains("game-voting." + gameName)) {
			multiplier = Minigames.getInstance().getConfig().getDouble("game-voting." + gameName);
		} else {
			multiplier = 1;
		}
		
		if (yes) {
			multiplier *= 1.1; //Increase chance factor a bit (e.g. from to 1.5 to 1.65)
		} else {
			multiplier *= 0.9; //Decrease chance factor a bit (e.g. from 1.5 to 1.35)
		}
		
		Minigames.getInstance().getConfig().set("game-voting." + gameName, multiplier);
		Minigames.getInstance().saveConfig();
	}

}
