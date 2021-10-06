package derkades.minigames.menu;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameMap;
import derkades.minigames.games.Games;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

import java.io.File;
import java.util.Date;

import static net.kyori.adventure.text.Component.text;

public class StatsMenu extends IconMenu {

	public StatsMenu(final Player player) {
		super(Minigames.getInstance(), text("Stats"), 1, player);

		Game<? extends GameMap> worstMapGame = null;
		GameMap worstMap = null;
		Game<? extends GameMap> bestMapGame = null;
		GameMap bestMap = null;
		Game<? extends GameMap> worstGame = null;
		Game<? extends GameMap> bestGame = null;
		for (final Game<? extends GameMap> game : Games.GAMES) {
			if (worstGame == null) {
				worstGame = game;
			} else if (game.getWeight() < worstGame.getWeight()) {
				worstGame = game;
			}

			if (bestGame == null) {
				bestGame = game;
			} else if (game.getWeight() > bestGame.getWeight()) {
				bestGame = game;
			}

			final GameMap[] maps = game.getGameMaps();
			for (final GameMap map : maps) {
				if (worstMap == null) {
					worstMap = map;
					worstMapGame = game;
				} else if (map.getWeight() < worstMap.getWeight()) {
					worstMap = map;
					worstMapGame = game;
				}

				if (bestMap == null) {
					bestMap = map;
					bestMapGame = game;
				} else if (map.getWeight() > bestMap.getWeight()) {
					bestMap = map;
					bestMapGame = game;
				}
			}
		}

		addItem(0, new ItemBuilder(Material.GRAY_DYE)
				.name(text("Best game"))
				.lore(text(bestGame.getName()), text(String.format("Weight: %.2f", bestGame.getWeight())))
				.create());

		addItem(1, new ItemBuilder(Material.GRAY_DYE)
				.name(text("Worst game"))
				.lore(text(worstGame.getName()), text(String.format("Weight: %.2f", worstGame.getWeight())))
				.create());

		addItem(2, new ItemBuilder(Material.GRAY_DYE)
				.name(text("Best map"))
				.lore(text(bestMapGame.getName() + " - " + bestMap.getName()), text(String.format("Weight: %.2f", bestMap.getWeight())))
				.create());

		addItem(3, new ItemBuilder(Material.GRAY_DYE)
				.name(text("Worst map"))
				.lore(text(worstMapGame.getName() + " - " + worstMap.getName()), text(String.format("Weight: %.2f", worstMap.getWeight())))
				.create());

		addItem(4, new ItemBuilder(Material.GRAY_DYE)
				.name(text("Last plugin update"))
				.lore(text(Minigames.PRETTY_TIME.format(new Date(new File(Minigames.getInstance().getDataFolder().getParentFile().getPath(), "Minigames.jar").lastModified()))))
				.create()
				);
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		return false;
	}

}
