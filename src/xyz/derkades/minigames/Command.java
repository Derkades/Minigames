package xyz.derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.menu.MainMenu;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class Command implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command arg1, final String arg2, final String[] args) {
		if (args.length == 2 && args[0].equalsIgnoreCase("next") && sender.hasPermission("minigames.next")) {
			final Game game = Game.fromString(args[1].replace("_", " "));
			if (game == null){
				sender.sendMessage(ChatColor.RED + "Unknown game. Make sure the game is spelled correctly. For spaces use underscores.");
				return true;
			} else {
				Minigames.NEXT_GAME = game;
				Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = true;
				sender.sendMessage("Bypassing player minimum and forcing " + game.getName() + " to be chosen as the next game.");
			}
		}

		if (args.length == 1){
			if ((args[0].equalsIgnoreCase("start") || args[0].equals("b")) && sender.hasPermission("minigames.start")){
				AutoRotate.startNewRandomGame();
				Minigames.STOP_GAMES = false;
			} else if ((args[0].equalsIgnoreCase("stop") || args[0].equals("e")) && sender.hasPermission("minigames.stop")){
				sender.sendMessage(ChatColor.RED + "! STOPPED GAMES !");
				Minigames.STOP_GAMES = true;
			} else if (args[0].equalsIgnoreCase("!") && sender.hasPermission("minigames.emerg")) {
				sender.sendMessage("! EMERGENCY STOP !");
				Bukkit.broadcastMessage(ChatColor.RED + "Initiating emergency stop. You may be kicked or experience lag.");
				Bukkit.getOnlinePlayers().forEach(player2 -> {
					player2.teleport(Var.LOBBY_LOCATION);
					Utils.clearInventory(player2);
					Utils.clearPotionEffects(player2);
					player2.setGameMode(GameMode.ADVENTURE);
				});
				Scheduler.delay(20, () -> Bukkit.reload());
			} else if (args[0].equalsIgnoreCase("min") && sender.hasPermission("minigames.min")) {
				Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = true;
				sender.sendMessage("Bypassing minimum player check");
			} else if (args[0].equals("test") && sender.hasPermission("minigames.test")) {
				final Player player = (Player) sender;

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
				//BlockUtils.fillArea(244, 67, 161, 212, 74, 131, Material.DIRT);
				//BlockUtils.fillArea(244, 69, 161, 242, 67, 159, Material.AIR);
				//BlockUtils.fillArea(214, 67, 133, 212, 69, 131, Material.AIR);
				sender.sendMessage("test");

		        //final Poll poll = new Poll("Test poll?", new PollAnswer(1, "Yes", ChatColor.GREEN), new PollAnswer(2, "No", ChatColor.RED));
		        //ChatPoll.sendPoll(player, poll);

				player.spigot().sendMessage(new ComponentBuilder("")
						.append("Game")
						.bold(true)
						.color(ChatColor.GOLD)
						.append(" (" + 3.0 + ")")
						.color(ChatColor.GRAY)
						.bold(false)
						.append(" [hover for help]")
						.color(ChatColor.YELLOW)
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
								"The number shown after the game name in parentheses\n"
								+ "is the game weight. A higher weight means that the\n"
								+ "minigame has a higher chance of being picked. The\n"
								+ "game weight can be increased or decreased by voting\n"
								+ "on the poll at the end of the game.")
								.color(ChatColor.GRAY).create()))
						.create());

				final Block block = player.getTargetBlockExact(3);
				final FallingBlock fall = block.getWorld().spawnFallingBlock(
						new Location(Var.WORLD, block.getX() + 0.5, block.getY(), block.getZ() + 0.5),
						block.getBlockData());
				final Vector velocity = fall.getVelocity();
				velocity.setY(1.5);
				fall.setVelocity(velocity);
				block.setType(Material.AIR);

			} else {
				sender.sendMessage("no.");
			}
		} else if (args.length == 0){
			final Player player = (Player) sender;
			new MainMenu(player).open();
		}

		return true;
	}

}
