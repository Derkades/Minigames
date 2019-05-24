package xyz.derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.menu.MainMenu;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class Command implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command arg1, String arg2, String[] args) {
		Player player = (Player) sender;
		
		if (args.length == 2 && args[0].equalsIgnoreCase("next") && player.hasPermission("minigames.next")) {
			Game game = Game.fromString(args[1].replace("_", " "));
			if (game == null){
				player.sendMessage(ChatColor.RED + "Unknown game. Make sure the game is spelled correctly. For spaces use underscores.");
				return true;
			} else {
				Minigames.NEXT_GAME = game;
				player.sendMessage(game.getName() + " will be chosen as the next game.");
			}
		}
		
		if (args.length == 1){
			if ((args[0].equalsIgnoreCase("start") || args[0].equals("b")) && player.hasPermission("minigames.start")){
				AutoRotate.startNewRandomGame();
				Minigames.STOP_GAMES = false;
			} else if ((args[0].equalsIgnoreCase("stop") || args[0].equals("e")) && player.hasPermission("minigames.stop")){
				player.sendMessage(ChatColor.RED + "! STOPPED GAMES !");
				Minigames.STOP_GAMES = true;
			} else if (args[0].equalsIgnoreCase("!") && player.hasPermission("minigames.emerg")) {
				player.sendMessage("! EMERGENCY STOP !");
				Bukkit.broadcastMessage(ChatColor.RED + "Initiating emergency stop. You may be kicked or experience lag.");
				Bukkit.getOnlinePlayers().forEach(player2 -> {
					player2.teleport(Var.LOBBY_LOCATION);
					Utils.clearInventory(player2);
					Utils.clearPotionEffects(player2);
					player2.setGameMode(GameMode.ADVENTURE);
				});
				Scheduler.delay(20, () -> Bukkit.reload());
			} else if (args[0].equalsIgnoreCase("min") && player.hasPermission("minigames.min")) {
				Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = true;
				player.sendMessage("Bypassing minimum player check");
			} else if (args[0].equals("test") && player.hasPermission("minigames.test")) {
				/*final Location loc = player.getLocation();
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
		        f.setIsIncendiary(false);*/
				BlockUtils.fillArea(244, 67, 161, 212, 74, 131, Material.DIRT);
				BlockUtils.fillArea(244, 69, 161, 242, 67, 159, Material.AIR);
				BlockUtils.fillArea(214, 67, 133, 212, 69, 131, Material.AIR);
		        player.sendMessage("test");
			} else {
				player.sendMessage("no.");
			}
		} else if (args.length == 0){
			new MainMenu(player).open();
		}
		
		return true;
	}

}
