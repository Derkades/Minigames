package com.robinmc.minigames;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.robinmc.minigames.games.Game;

public class Command implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command arg1, String arg2, String[] args) {
		Player player = (Player) sender;
		
		if (args.length == 2){
			if (args[0].equals("staff")){
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				VIP.setStaff(target, true);
			} else if (args[0].equals("unstaff")){
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				VIP.setStaff(target, false);
			}
		} else if (args.length == 1){
			if (args[0].equalsIgnoreCase("start") &&
					player.isOp()){
				AutoRotate.startNewRandomGame();
				Main.STOP_GAMES = false;
			} else if (args[0].equalsIgnoreCase("stop") &&
					player.isOp()){
				player.sendMessage("! STOPPED GAMES !");
				Main.STOP_GAMES = true;
			} else if (player.isOp()) {
				Game game = Game.fromString(args[0].replace("_", " "));
				if (game == null){
					player.sendMessage("Unknown game");
					return true;
				} else {
					Main.NEXT_GAME = game;
					AutoRotate.startNewRandomGame();
				}
			}
		} else if (args.length == 0){
			Menu.open(player);
		}
		
		return true;
	}

}
