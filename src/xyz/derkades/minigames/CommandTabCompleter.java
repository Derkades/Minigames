package xyz.derkades.minigames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import xyz.derkades.minigames.games.Game;

public class CommandTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(final CommandSender arg0, final Command arg1, final String label, final String[] args) {
		if (args.length == 2 && args[0].equalsIgnoreCase("next")) {
			return Arrays.asList(Game.GAMES).stream().map((s) -> s.getName().replace(" ", "_").toLowerCase()).filter((s) -> s.startsWith(args[1])).collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}

}
