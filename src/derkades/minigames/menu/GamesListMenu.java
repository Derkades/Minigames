package derkades.minigames.menu;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.games.GameMap;
import derkades.minigames.games.Games;
import derkades.minigames.utils.PaperItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import xyz.derkades.derkutils.NumberUtils;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class GamesListMenu extends IconMenu {

	public GamesListMenu(final Player player) {
		super(Minigames.getInstance(), "Games list", (int) (Math.ceil(Games.GAMES.length / 9f) + 1), player);

		int slot = 0;
		for (final Game<? extends GameMap> game : Games.GAMES){

			final List<String> lore = new ArrayList<>();
			Arrays.stream(game.getDescription()).forEach(s -> lore.add(RESET + "" + WHITE + s));

			final double gameWeight = NumberUtils.roundApprox(game.getWeight(), 2);

			lore.add(GOLD + "Multiplier: " + YELLOW + gameWeight);
			lore.add(GOLD + "Minimum players: " + YELLOW + game.getRequiredPlayers());

			lore.add(GOLD + "Maps:");
			for (final GameMap map : game.getGameMaps()) {
				String disabled = map.isDisabled() ? RED + " (disabled)" : "";
				lore.add("  " + YELLOW + map.getName() + disabled);
				final double mapWeight = NumberUtils.roundApprox(map.getWeight(), 2);
				lore.add(GRAY + "  Multiplier: " + YELLOW + mapWeight);
				if (map.getCredits() != null) {
					lore.add(GRAY + "  Credits: " + map.getCredits());
				}
				if (player.hasPermission("minigames.list_admin")) {
					lore.add(DARK_GRAY + "  Identifier: " + map.getIdentifier());
					lore.add(DARK_GRAY + "  World: " + map.getGameWorld());
				}
			}

			lore.add(GOLD + "Labels:");
			for (GameLabel label : game.getGameLabels()) {
				lore.add(GRAY + "  " + label);
			}

			if (player.hasPermission("minigames.list_admin")) {
				lore.add("");
				lore.add(DARK_GRAY + "Identifier: " + game.getIdentifier());
				lore.add(DARK_GRAY + "Command name: " + game.getIdentifier());
				lore.add(DARK_GRAY + "" + game.getClass().getName().substring(25));
				if (game.getGameMaps().length == 0) {
					lore.add(DARK_GRAY + "No maps defined");
				} else {
					lore.add(DARK_GRAY + "" + game.getGameMaps().getClass().getName().substring(27));
				}
			}

			this.addItem(slot, new PaperItemBuilder(game.getMaterial())
					.name(GOLD + game.getName())
					.lore(lore)
					.itemFlags(ItemFlag.HIDE_ATTRIBUTES)
					.create());

			slot++;
		}

		addItem(this.getSize() - 1, Menu.BACK_BUTTON);
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		// Back button
		if (event.getPosition() == this.getSize() - 1) {
			new MainMenu(event.getPlayer());
		}

		return false;
	}

}
