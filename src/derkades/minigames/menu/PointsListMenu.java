package derkades.minigames.menu;

import derkades.minigames.Minigames;
import derkades.minigames.utils.MPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class PointsListMenu extends IconMenu {

	private static final String MHF_QUESTION = "ewogICJ0aW1lc3RhbXAiIDogMTYzMDY2NjgyMzMxOCwKICAicHJvZmlsZUlkIiA6ICI2MDZlMmZmMGVkNzc0ODQyOWQ2Y2UxZDMzMjFjNzgzOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNSEZfUXVlc3Rpb24iLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM0ZTA2M2NhZmI0NjdhNWM4ZGU0M2VjNzg2MTkzOTlmMzY5ZjRhNTI0MzRkYTgwMTdhOTgzY2RkOTI1MTZhMCIKICAgIH0KICB9Cn0";
	private static final String MONITOR = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTFmYWQ1ZDJiODIyZmNlOGQ1OWJjZTA4NDE0YjlmODdmMjdhYTlkNzdhNzM4MjNhNThkZDUxN2VhODBiMmE1In19fQ==";

	public PointsListMenu(final Player player) {
		super(Minigames.getInstance(), text("Points"), 3, player);

		int slot = 0;
		for (final MPlayer target : Minigames.getOnlinePlayers()) {
			this.addItem(slot, new ItemBuilder(Material.PLAYER_HEAD)
							.skullProfile(target.bukkit().getPlayerProfile())
							.name(target.getDisplayName())
							.lore(text("Points: ", GRAY).append(text(target.getPoints(), YELLOW)))
							.create());
			slot++;
		}

		this.addItem(24, new ItemBuilder(Material.PLAYER_HEAD).skullTexture(MONITOR).name(text("View all players", GOLD)).create());
		this.addItem(25, new ItemBuilder(Material.PLAYER_HEAD).skullTexture(MHF_QUESTION).name(text( "Points explanation", GOLD))
				.lore(text("Winning a game", GREEN).decorate(BOLD),
						text("  1-2 online players: +3 points", GRAY),
						text("  3-4 online players: +4 points", GRAY),
						text("  5+ online players: +5 points", GRAY),
						text("Losing a game", RED).decorate(BOLD),
						text("  +1 point", GRAY)).create());
		this.addItem(26, Menu.BACK_BUTTON);
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final Player player = event.getPlayer();
		if (event.getPosition() == 26) {
			new MainMenu(player);
		} else if (event.getPosition() == 24) {
			new PointsLeaderboardMenu(player);
		}
		return false;
	}

}
