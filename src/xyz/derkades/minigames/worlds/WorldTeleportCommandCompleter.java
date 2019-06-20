package xyz.derkades.minigames.worlds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class WorldTeleportCommandCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(final CommandSender arg0, final Command arg1, final String arg2, final String[] args) {
		if (args.length == 1) {
			return Arrays.asList(GameWorld.values()).stream().map((s) -> s.toString().toLowerCase()).filter((s) -> s.startsWith(args[0])).collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}

}
