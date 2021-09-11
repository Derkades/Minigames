package derkades.minigames.worlds;

import java.util.Arrays;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import derkades.minigames.Var;
import derkades.minigames.utils.MPlayer;

public class WorldTeleportCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!sender.hasPermission("minigames.world.tp")){
			return true;
		}

		if (args.length != 1) {
			sender.sendMessage("");
			sender.sendMessage("lobby");
			sender.sendMessage("");
			Arrays.asList(GameWorld.values()).stream().map(GameWorld::toString).map(String::toLowerCase).forEach(sender::sendMessage);
			return true;
		}

		final MPlayer player = new MPlayer((Player) sender);

		if (args[0].equals("lobby")) {
			player.teleport(Var.LOBBY_LOCATION);
		} else {
			final String enumName = args[0].toUpperCase();
			try {
				player.queueTeleport(new Location(GameWorld.valueOf(args[0].toUpperCase()).getWorld(), 0.5, 65, 0.5));
				player.setGameMode(GameMode.CREATIVE);
				player.bukkit().setFlying(true);
			} catch (final IllegalArgumentException e) {
				player.sendChat("No world '" + enumName + "'");
			}
		}

		return true;
	}

}
