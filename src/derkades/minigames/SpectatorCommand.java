package derkades.minigames;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpectatorCommand implements CommandExecutor {

	private static final Component INVALID_TARGET = Component.text("Invalid target", NamedTextColor.RED);
	private static final Component STOP_INSTRUCTIONS = Component.text("Press shift to stop spectating", NamedTextColor.GRAY);
	private static final Component USAGE = Component.text("Usage: /spec <player>", NamedTextColor.GRAY);
	private static final Component SPECTATOR_ONLY = Component.text("This command can only be used as a spectator", NamedTextColor.GRAY);

	@Override
	public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.getGameMode().equals(GameMode.SPECTATOR)) {
			if (args.length == 1) {
				final Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					player.sendMessage(INVALID_TARGET);
				} else {
					player.sendMessage(STOP_INSTRUCTIONS);
					player.setSpectatorTarget(target);
				}
			} else {
				player.sendMessage(USAGE);
			}
		} else {
			player.sendMessage(SPECTATOR_ONLY);
		}
		return true;
	}

}
