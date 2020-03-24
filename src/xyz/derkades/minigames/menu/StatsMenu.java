package xyz.derkades.minigames.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.derkades.derkutils.NumberUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.maps.GameMap;

public class StatsMenu extends IconMenu {

	public StatsMenu(final Player player) {
		super(Minigames.getInstance(), "Stats", 1, player);
		
		Game<? extends GameMap> worstMapGame = null;
		GameMap worstMap = null;
		Game<? extends GameMap> bestMapGame = null;
		GameMap bestMap = null;
		Game<? extends GameMap> worstGame = null;
		Game<? extends GameMap> bestGame = null;
		for (final Game<? extends GameMap> game : Game.GAMES) {
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
				.name("Best game")
				.lore(bestGame.getName(), "Weight: " + NumberUtils.roundApprox(bestGame.getWeight(), 2))
				.create());
		
		addItem(1, new ItemBuilder(Material.GRAY_DYE)
				.name("Worst game")
				.lore(worstGame.getName(), "Weight: " + NumberUtils.roundApprox(worstGame.getWeight(), 2))
				.create());
		
		addItem(2, new ItemBuilder(Material.GRAY_DYE)
				.name("Best map")
				.lore(bestMapGame.getName() + " - " + bestMap.getName(), "Weight: " + NumberUtils.roundApprox(bestMap.getWeight(), 2))
				.create());
		
		addItem(3, new ItemBuilder(Material.GRAY_DYE)
				.name("Worst map")
				.lore(worstMapGame.getName() + " - " + worstMap.getName(), "Weight: " + NumberUtils.roundApprox(worstMap.getWeight(), 2))
				.create());
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		return false;
	}

}
