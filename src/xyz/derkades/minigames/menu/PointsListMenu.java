package xyz.derkades.minigames.menu;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.utils.MPlayer;

public class PointsListMenu extends IconMenu {

	public PointsListMenu(final Player player) {
		super(Minigames.getInstance(), "Points", 3*9, player);

		int slot = 0;
		for (final MPlayer target : Minigames.getOnlinePlayers()) {
			this.items.put(slot, new ItemBuilder(target.bukkit()).name(target.getName()).lore(ChatColor.GOLD + "Points: " + ChatColor.YELLOW + target.getPoints()).create());
			slot++;
		}

		this.items.put(26, Menu.BACK_BUTTON);
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		if (event.getPosition() == 26) {
			new MainMenu(event.getPlayer()).open();
		}
		return false;
	}

}
