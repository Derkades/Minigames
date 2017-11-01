package xyz.derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.menu.MainMenu;

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
				Minigames.STOP_GAMES = false;
			} else if (args[0].equalsIgnoreCase("stop") &&
					player.isOp()){
				player.sendMessage("! STOPPED GAMES !");
				Minigames.STOP_GAMES = true;
			} else if (args[0].equals("test")) {
				final Location loc = player.getLocation();
		        final Location fbLocation = loc.add(
		        		loc
		                .getDirection()
		                .normalize()
		                .multiply(2)
		                .toLocation(player.getWorld(), loc.getYaw(),
		                		loc.getPitch())).add(0, 1D, 0);
		        final Fireball f = player.getWorld().spawn(fbLocation, Fireball.class);
		        f.setYield(100);
		        f.setShooter(player);
		        f.setIsIncendiary(false);
		        player.sendMessage("test");
			} else if (player.isOp()) {
				Game game = Game.fromString(args[0].replace("_", " "));
				if (game == null){
					player.sendMessage("Unknown game");
					return true;
				} else {
					Minigames.NEXT_GAME = game;
					AutoRotate.startNewRandomGame();
				}
			}
		} else if (args.length == 0){
			new MainMenu(player).open();
		}
		
		return true;
	}

}
