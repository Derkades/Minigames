package xyz.derkades.minigames.menu;

import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.maps.GameMap;

public class GamesListMenu extends IconMenu {

	public GamesListMenu(final Player player) {
		super(Minigames.getInstance(), "Games list", 6*9, player);

		int slot = 0;
		for (final Game<? extends GameMap> game : Game.GAMES){
			final List<String> lore = new ArrayList<>();

			lore.addAll(Arrays.asList(game.getDescription()));

			double gameWeight = Minigames.getInstance().getConfig().contains("game-voting." + game.getName())
					? Minigames.getInstance().getConfig().getDouble("game-voting." + game.getName())
					: 1;

			gameWeight = Math.round(gameWeight * 100.0) / 100.0;

			lore.add(GOLD + "Game multiplier: " + YELLOW + gameWeight);
			lore.add(GOLD + "Minimum online players: " + YELLOW + game.getRequiredPlayers());
			if (game.getGameMaps() == null) {
				lore.add(GOLD + "Maps: " + YELLOW + "none");
			} else {
				lore.add(GOLD + "Maps:");
				for (final GameMap map : game.getGameMaps()) {
					lore.add("  " + YELLOW + map.getName());
					final String configPath = "game-voting.map." + game.getName() + "." + map.getName();
					double mapWeight = Minigames.getInstance().getConfig().getDouble(configPath, 1);
					mapWeight = Math.round(mapWeight * 100.0) / 100.0;
					lore.add(GRAY + "  Multiplier: " + YELLOW + mapWeight);
					if (map.getCredits() != null) {
						lore.add(GRAY + "  Credits: " + map.getCredits());
					}
					if (player.hasPermission("minigames.list_admin")) {
						lore.add(GRAY + "  Name for commands: " + map.getName().replace(" ", "_").toLowerCase());
					}
				}
			}

			if (player.hasPermission("minigames.list_admin")) {
				lore.add("");
				lore.add(GRAY + "Name for commands: " + game.getName().toLowerCase().replace(" ", "_"));
				lore.add(DARK_GRAY + "" + game.getClass());
				if (game.getGameMaps() == null) {
					lore.add(DARK_GRAY + "No map support");
				} else if (game.getGameMaps().length == 0) {
					lore.add(DARK_GRAY + "No maps defined");
				} else {
					lore.add(DARK_GRAY + "" + game.getGameMaps().getClass());
				}
			}

			this.items.put(slot, new ItemBuilder(Material.GRAY_DYE)
				.name(GOLD + game.getName())
				.lore(lore)
				.create());

			slot++;
		}

		this.items.put(53, Menu.BACK_BUTTON);
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		// Back button
		if (event.getPosition() == 53) {
			new MainMenu(event.getPlayer()).open();
		}

		return false;
	}

}
