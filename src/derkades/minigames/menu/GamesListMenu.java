package derkades.minigames.menu;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.games.GameMap;
import derkades.minigames.games.Games;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import xyz.derkades.derkutils.NumberUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class GamesListMenu extends IconMenu {

	public GamesListMenu(final Player player) {
		super(Minigames.getInstance(), Component.text("Games list"), (int) (Math.ceil(Games.GAMES.length / 9f) + 1), player);

		int slot = 0;
		for (final Game<? extends GameMap> game : Games.GAMES){

			final List<Component> lore = new ArrayList<>();
			Arrays.stream(game.getDescription()).forEach(s -> lore.add(Component.text(s, WHITE)));

			final double gameWeight = NumberUtils.roundApprox(game.getWeight(), 2);

			lore.add(text("Multiplier: ", GOLD).append(text(gameWeight, YELLOW)));
			lore.add(text("Minimum players: ", GOLD).append(text(game.getRequiredPlayers(), YELLOW)));

			lore.add(text("Maps:", GOLD));
			for (final GameMap map : game.getGameMaps()) {
				lore.add(text("  " + map.getName(), YELLOW)
						.append(map.isDisabled() ? Component.text(" (disabled)", RED) : Component.empty()));
				lore.add(text("  Multiplier: ", GRAY).append(text(String.format("%.2f", map.getWeight()), YELLOW)));
				if (map.getCredits() != null) {
					lore.add(text("  Credits: ", GRAY).append(text(map.getCredits(), YELLOW)));
				}
				if (player.hasPermission("minigames.list_admin")) {
					lore.add(text("  Identifier: " + map.getIdentifier(), DARK_GRAY));
					lore.add(text("  World: " + map.getGameWorld(), DARK_GRAY));
				}
			}

			lore.add(text("Labels:", GOLD));
			for (GameLabel label : game.getGameLabels()) {
				lore.add(text("  " + label, GRAY));
			}

			if (player.hasPermission("minigames.list_admin")) {
				lore.add(Component.empty());
				lore.add(text("Identifier: " + game.getIdentifier(), DARK_GRAY));
				lore.add(text("Command name: " + game.getIdentifier(), DARK_GRAY));
				lore.add(text(game.getClass().getName().substring(25), DARK_GRAY));
				if (game.getGameMaps().length == 0) {
					lore.add(text("No maps defined", DARK_GRAY));
				} else {
					lore.add(text(game.getGameMaps().getClass().getName().substring(27), DARK_GRAY));
				}
			}

			this.addItem(slot, new ItemBuilder(game.getMaterial())
					.name(text(game.getName(), GOLD))
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
