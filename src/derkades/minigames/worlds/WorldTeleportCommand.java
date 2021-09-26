package derkades.minigames.worlds;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import derkades.minigames.utils.MPlayer;
import org.jetbrains.annotations.NotNull;

public class WorldTeleportCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final @NotNull Command command, final @NotNull String label, final String[] args) {
		if (!sender.hasPermission("minigames.world.tp")){
			return true;
		}

		final MPlayer player = new MPlayer((Player) sender);

		if (args.length != 1) {
//			sender.sendMessage("");
//			sender.sendMessage("old_lobby");
//			sender.sendMessage("lobby");
//			sender.sendMessage("");

//			Arrays.stream(GameWorld.values()).map(GameWorld::toString).map(String::toLowerCase).forEach(sender::sendMessage);
			player.queueLobbyTeleport();
			return true;
		}

//		if (args[0].equals("old_lobby")) {
//			player.teleport(Var.LOBBY_LOCATION);
//		} else if (args[0].equals("lobby")) {
//			player.teleportSteampunkLobbyAsync();
//		} else {
			try {
				player.queueTeleport(new Location(GameWorld.valueOf(args[0].toUpperCase()).getWorld(), 0.5, 65, 0.5), () -> {
					player.setGameMode(GameMode.CREATIVE);
					player.bukkit().setFlying(true);
				});
			} catch (final IllegalArgumentException e) {
				player.sendFormattedPlainChat("No world '%s'", args[0]);
			}
//		}

		return true;
	}

}
