package derkades.minigames.worlds;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

public class WorldTeleportCommandCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(final @NotNull CommandSender arg0, final @NotNull Command arg1, final @NotNull String arg2, final String[] args) {
		if (args.length == 1) {
//			final List<String> complete = new ArrayList<>();
//			complete.addAll(Arrays.asList(GameWorld.values()).stream().map((s) -> s.toString().toLowerCase()).filter((s) -> s.contains(args[0]) || s.equals(args[0])).collect(Collectors.toList()));
//			if ("old_lobby".contains(args[0]) || args[0].equals("old_lobby")) {
//				complete.add("old_lobby");
//			}
//			if ("lobby".contains(args[0]) || args[0].equals("lobby")) {
//				complete.add("lobby");
//			}
//			return complete;
			return Arrays.stream(GameWorld.values())
					.map(Object::toString)
					.map(String::toLowerCase)
					.filter(s -> s.contains(args[0]) || s.equals(args[0]))
					.collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

}
