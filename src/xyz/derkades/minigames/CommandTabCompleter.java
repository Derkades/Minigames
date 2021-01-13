package xyz.derkades.minigames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.maps.GameMap;

public class CommandTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(final CommandSender arg0, final Command arg1, final String label, final String[] args) {
		if (args.length == 2 && args[0].equalsIgnoreCase("next")) {
			if (args[1] == null) {
				return Arrays.asList("error");
			}

			final String arg = args[1];

			final List<String> list = new ArrayList<>();

			for (final Game<? extends GameMap> game : Game.GAMES) {
				final String formattedName = game.getName().replace(" ", "_").toLowerCase();
				if (formattedName.contains(arg)) {
					list.add(formattedName);
				}

				if (game.getAlias() != null && game.getAlias().contains(arg)) {
					list.add(game.getAlias());
				}
			}

			return list;
		} else if (args.length == 2 && args[0].equalsIgnoreCase("map")) {
			final List<String> mapIdentifiers = new ArrayList<>();
			if (args[1].equals("")) {
				mapIdentifiers.add("<map>");
			}

			for (final Game<? extends GameMap> game : Game.GAMES) {
				for (final GameMap map : game.getGameMaps()) {
					if (map.getIdentifier().startsWith(args[1])){
						mapIdentifiers.add(map.getIdentifier());
					}
				}
			}
			return mapIdentifiers;
		} else {
			return new ArrayList<>();
		}
	}

}
