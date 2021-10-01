package derkades.minigames;

import derkades.minigames.games.Game;
import derkades.minigames.games.GameMap;
import derkades.minigames.games.Games;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(final @NotNull CommandSender arg0, final @NotNull Command arg1, final @NotNull String label, final String[] args) {
		if (args.length == 2 && (args[0].equalsIgnoreCase("next") || args[0].equalsIgnoreCase("n"))) {
			if (args[1] == null) {
				return Collections.singletonList("error");
			}

			final String arg = args[1];

			final List<String> list = new ArrayList<>();

			for (final Game<? extends GameMap> game : Games.GAMES) {
				final String formattedName = game.getName().replace(" ", "_").toLowerCase();
				if (formattedName.contains(arg)) {
					list.add(formattedName);
				}

				final String alias = game.getAlias();
				if (alias != null && alias.contains(arg)) {
					list.add(game.getAlias());
				}
			}

			return list;
		} else if (args.length == 2 && args[0].equalsIgnoreCase("map")) {
			final List<String> mapIdentifiers = new ArrayList<>();
			if (args[1].equals("")) {
				mapIdentifiers.add("<map>");
			}

			Arrays.stream(Games.GAMES)
					.flatMap(g -> Arrays.stream(g.getGameMaps()))
					.map(GameMap::getIdentifier)
					.filter(i -> i.contains(args[1]))
					.forEach(mapIdentifiers::add);
			return mapIdentifiers;
		} else {
			return new ArrayList<>();
		}
	}

}
