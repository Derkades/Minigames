package derkades.minigames.modules;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.derkutils.bukkit.VoidGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
			final String worldName = world.getName();
			final UUID worldId = world.getUID();

			Scheduler.delay(5*20, () -> {
				World world2 = Bukkit.getWorld(worldId);
				if (world2 == null) {
					return;
				}

				if (world2.getPlayerCount() > 0) {
					Logger.debug("Not unloading %s", worldName);
					return;
				}

				Logger.debug("Requesting chunk unload for all chunks in world %s", worldName);
				for (Chunk chunk : world2.getLoadedChunks()) {
					world2.unloadChunkRequest(chunk.getX(), chunk.getZ());
				}

				Scheduler.delay(30*20, () -> {
					World world3 = Bukkit.getWorld(worldId);

					if (world3 == null) {
						Logger.debug("World %s already unloaded", worldName);
						return;
					}

					if (world3.getPlayerCount() > 0) {
						Logger.debug("Not unloading %s", worldName);
						return;
					}

					if (Bukkit.unloadWorld(world3, true)) {
						Logger.debug("Unloaded world %s", worldName);
					} else {
						Logger.warning("Failed to unload world %s", worldName);
					}
				});
			});
		}
	}

	private static class TestWorldCommand implements CommandExecutor {

		@Override
		public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label,
				@NotNull final String[] args) {

			String worldName = "testworlds/" + args[0];
			boolean exists = new File(Bukkit.getWorldContainer(), worldName).exists();
			final WorldCreator creator = new WorldCreator(worldName);
			creator.generateStructures(false);
			creator.type(WorldType.FLAT);
			creator.generator(new VoidGenerator());
			creator.environment(Environment.NORMAL);

			final World world = Bukkit.getServer().createWorld(creator);
			Objects.requireNonNull(world);

			if (exists) {
				Logger.debug("World already exists");
			} else {
				Logger.debug("New world, setting spawn");
				world.setSpawnLocation(0, 65, 0);
				BlockUtils.fillArea(world, -5, 64, -5, 5, 64, 5, Material.GLASS);
			}

			world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);

			final MPlayer player = new MPlayer((Player) sender);
			player.queueTeleport(world.getSpawnLocation(), p -> {
				p.setGameMode(GameMode.CREATIVE);
				p.bukkit().setFlying(true);
			});
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
