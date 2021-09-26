package derkades.minigames;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VoteSkipCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String[] args) {
		final Player player = (Player) sender;
		// TODO allow during countdown
		if (!GameState.isCurrentlyInGame()) {
			player.sendMessage("You can only use this command during a game.");
			return true;
		}

		if (!GameState.getCurrentGame().voteGameSkip(player)) {
			player.sendMessage("You have already voted to skip this game.");
		}

		return true;
	}

}
