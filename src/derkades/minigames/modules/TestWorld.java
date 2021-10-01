package derkades.minigames.modules;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.derkades.derkutils.bukkit.VoidGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TestWorld extends Module {

	public TestWorld() {
		Minigames.getInstance().getCommand("testworld").setExecutor(new TestWorldCommand());
		Minigames.getInstance().getCommand("testworld").setTabCompleter(new TestWorldCommandCompleter());
	}

	@EventHandler
	public void worldChange(final PlayerChangedWorldEvent event) {
		final World world = event.getFrom();
		if (world.getName().startsWith("testworlds/")) {
			if (Bukkit.unloadWorld(world, true)) {
				Logger.info("Unloaded world %s", world.getName());
			}
		}
	}

	private static class TestWorldCommand implements CommandExecutor {

		@Override
		public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label,
				@NotNull final String[] args) {

			final WorldCreator creator = new WorldCreator("testworlds/" + args[0]);
			creator.generateStructures(false);
			creator.type(WorldType.FLAT);
			creator.generator(new VoidGenerator());
			creator.environment(Environment.NORMAL);

			final World world = Bukkit.getServer().createWorld(creator);
//			world.setSpawnLocation(0, 65, 0);

			final Player player = (Player) sender;
			player.teleport(world.getSpawnLocation());
			player.setGameMode(GameMode.CREATIVE);
			player.setFlying(true);
			return true;
		}

	}

	private static class TestWorldCommandCompleter implements TabCompleter {

		@Override
		public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command,
				@NotNull final String alias, @NotNull final String[] args) {

			try {
				return Files.list(Paths.get("testworlds"))
						.filter(s -> args.length < 1 || s.getFileName().toString().contains(args[0]))
						.map(s -> s.getFileName().toString())
						.collect(Collectors.toList());
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
