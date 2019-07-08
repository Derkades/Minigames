package xyz.derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.utils.Utils;

public class SpectatorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.getGameMode().equals(GameMode.SPECTATOR)) {
			if (args.length == 1) {
				final Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					player.sendMessage(Utils.getChatPrefix(ChatColor.RED, 'S') + "Invalid target");
				} else {
					player.sendMessage(Utils.getChatPrefix(ChatColor.RED, 'S') + "Press shift to stop spectating");
					player.setSpectatorTarget(target);
				}
			} else {
				player.sendMessage(Utils.getChatPrefix(ChatColor.RED, 'S') + "Usage: /spec <player>");
			}
		} else {
			player.sendMessage(Utils.getChatPrefix(ChatColor.RED, 'S') + "This command can only be used as a spectator.");
		}
		return true;
	}

}
