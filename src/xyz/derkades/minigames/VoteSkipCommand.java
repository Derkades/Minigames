package xyz.derkades.minigames;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteSkipCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		final Player player = (Player) sender;
		if (Minigames.CURRENT_GAME == null) {
			player.sendMessage("You can only use this command during a game.");
			return true;
		}
		
		if (Minigames.CURRENT_GAME.voteGameSkip(player.getUniqueId())) {
			Minigames.CURRENT_GAME.sendMessage(player.getName() + " voted to skip this game using /voteskip.");
		} else {
			player.sendMessage("You have already voted to skip this game.");
		}
		
		return true;
	}

}
