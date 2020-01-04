package xyz.derkades.minigames.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;

@Deprecated
public class VoteMenu extends IconMenu {

	private final String gameName;

	public VoteMenu(final Player player, final String gameName) {
		super(Minigames.getInstance(), "Did you enjoy this game?", 5, player);

		this.gameName = gameName;

		this.addItem(13, new ItemBuilder(Material.CYAN_TERRACOTTA)
				.name(ChatColor.DARK_AQUA + "Did you like this game?")
				.lore(ChatColor.GRAY + "Please click either \"Yes\" or \"No\".")
				.create());
		this.addItem(20, new ItemBuilder(Material.RED_TERRACOTTA)
				.name(ChatColor.RED + "No")
				.lore(ChatColor.GRAY + "The game will be picked less often.")
				.create());
		this.addItem(22, new ItemBuilder(Material.YELLOW_TERRACOTTA)
				.name(ChatColor.RED + "Neutral")
				.lore(ChatColor.GRAY + "The game will be picked just as often.")
				.create());
		this.addItem(24, new ItemBuilder(Material.GREEN_TERRACOTTA)
				.name(ChatColor.GREEN + "Yes")
				.lore(ChatColor.GRAY + "The game will be picked more often.")
				.create());
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final String displayName = event.getItemStack().getItemMeta().getDisplayName();
		if (displayName.equals(ChatColor.RED + "No")) {
			this.vote(false);
		} else if (displayName.equals(ChatColor.GREEN + "Yes")){
			this.vote(true);
		}

		return true;
	}

	private void vote(final boolean yes) {
		double multiplier = Minigames.getInstance().getConfig().contains("game-voting." + this.gameName)
				? Minigames.getInstance().getConfig().getDouble("game-voting." + this.gameName)
				: 1;

		if (yes) {
			multiplier *= 1.1; //Increase chance factor a bit (e.g. from to 1.5 to 1.65)
		} else {
			multiplier *= 0.9; //Decrease chance factor a bit (e.g. from 1.5 to 1.35)
		}

		if (multiplier > 5) {
			multiplier = 5;
		}

		Minigames.getInstance().getConfig().set("game-voting." + this.gameName, multiplier);
		Minigames.getInstance().saveConfig();
	}

}
