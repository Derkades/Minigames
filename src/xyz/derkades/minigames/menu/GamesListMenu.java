package xyz.derkades.minigames.menu;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.YELLOW;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
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
			lore.add(GOLD + "Minimum online players: " + YELLOW + game.getRequiredPlayers());
			if (game.getGameMaps() != null) lore.add(GOLD + "Maps: " + YELLOW + this.getMapsString(game));
			if (player.hasPermission("minigames.list_admin")) {
				lore.add("");
				lore.add(ChatColor.GRAY + "Name: " + game.getName());
				lore.add(ChatColor.GRAY + "Name for commands: " + game.getName().toLowerCase().replace(" ", "_"));
				lore.add(ChatColor.GRAY + "Game class: " + game.getClass());
				if (game.getGameMaps() == null) {
					lore.add("No map support");
				} else if (game.getGameMaps().length == 0) {
					lore.add("No maps defined");
				} else {
					lore.add(ChatColor.GRAY + "Map class: " + game.getGameMaps().getClass());
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

	private String getMapsString(final Game<? extends GameMap> game) {
		final List<String> mapNames = new ArrayList<>();
		for (final GameMap map : game.getGameMaps()) {
			mapNames.add(map.getName());
		}
		return String.join(", ", mapNames);
	}

}
