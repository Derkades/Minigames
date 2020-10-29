package xyz.derkades.minigames;

import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.Minigames.ShutdownReason;
import xyz.derkades.minigames.games.Missiles.Missile;
import xyz.derkades.minigames.menu.GamesListMenu;
import xyz.derkades.minigames.menu.MainMenu;
import xyz.derkades.minigames.menu.StatsMenu;
import xyz.derkades.minigames.random.RandomPicking;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.worlds.GameWorld;

public class Command implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command arg1, final String arg2, final String[] args) {
		if (args.length == 0) {
			final Player player = (Player) sender;
			new MainMenu(player);
			return true;
		}
		
		if (args.length == 2 && (args[0].equalsIgnoreCase("next") || args[0].equalsIgnoreCase("n")) && sender.hasPermission("minigames.next")) {
			RandomPicking.FORCE_GAME = args[1].replace("_", " ");
			Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = true;
			sender.sendMessage("If exists, " + args[1] + " will be chosen as the next game");
			return true;
		}

		if (args.length == 2 && (args[0].equalsIgnoreCase("map") || args[0].equalsIgnoreCase("m")) && sender.hasPermission("minigames.nextmap")) {
			RandomPicking.FORCE_MAP = args[1].replace("_", " ");
			sender.sendMessage("If exists, " + args[1] + " will be chosen as the next map");
			return true;
		}

		if (args.length == 1){
			if ((args[0].equalsIgnoreCase("start") || args[0].equals("b")) && sender.hasPermission("minigames.start")){
				AutoRotate.startNewRandomGame();
				Minigames.STOP_GAMES = false;
			} else if ((args[0].equalsIgnoreCase("stop") || args[0].equals("e")) && sender.hasPermission("minigames.stop")){
				sender.sendMessage(ChatColor.RED + "! STOPPED GAMES !");
				Minigames.STOP_GAMES = true;
			} else if (args[0].equalsIgnoreCase("!") && sender.hasPermission("minigames.emerg")) {
				Minigames.shutdown(ShutdownReason.EMERGENCY_MANUAL, "The /games ! command was executed");
			} else if (args[0].equalsIgnoreCase("min") && sender.hasPermission("minigames.min")) {
				Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = true;
				sender.sendMessage("Bypassing minimum player check");
			} else if (args[0].equals("reloadworlds") && sender.hasPermission("minigames.world.reload")) {
				Logger.info("Reloading worlds, this may take a long time and cause lag..");

				for (final GameWorld gWorld : GameWorld.values()) {
					gWorld.getWorld();
					gWorld.unload();
				}

				Logger.info("Reloading worlds done.");
			} else if (args[0].equals("unloadworlds") && sender.hasPermission("minigames.world.unload")) {
				Logger.info("Unloading worlds, this may take a long time and cause lag..");

				for (final GameWorld gWorld : GameWorld.values()) {
					gWorld.unload();
				}

				Logger.info("Reloading worlds done.");
			} else if (args[0].equalsIgnoreCase("list")) {
				new GamesListMenu((Player) sender);
			} else if (args[0].equals("damage") && sender.hasPermission("minigames.damage")) {
				final MPlayer player = new MPlayer((Player) sender);
				player.setDisableDamage(false);
				sender.sendMessage("Set damageDisabled to false");
			} else if (args[0].equals("debug") && sender.hasPermission("minigames.debug")) {
				Logger.debugMode = !Logger.debugMode;
				Minigames.getInstance().getConfig().set("debug_mode", Logger.debugMode);
				Minigames.getInstance().saveConfig();
				sender.sendMessage("Set debug mode to " + Logger.debugMode);
			} else if (args[0].equals("currentgame") && sender.hasPermission("minigames.currentgame")) {
				sender.sendMessage("Current game: " + Minigames.CURRENT_GAME);
			} else if (args[0].equals("stats")) {
				new StatsMenu((Player) sender);
			}
		}
		
		if (args.length >= 1 && args[0].equals("test") && sender.hasPermission("minigames.test")) {
//				new DieAnimationMenu(new BoardPlayer(new MPlayer((Player) sender)), 1, 10, Random.getRandomInteger(1, 10));
//		        final Location fbLocation = loc.add(
//		        		loc
//		                .getDirection()
//		                .normalize()
//		                .multiply(2)
//		                .toLocation(player.getWorld(), loc.getYaw(),
//		                		loc.getPitch())).add(0, 1D, 0);
//		        final Fireball f = player.getWorld().spawn(fbLocation, Fireball.class);
//		        f.setYield(100);
//		        f.setShooter(player);
//		        f.setIsIncendiary(false);

//				BlockUtils.fillArea(244, 67, 161, 212, 74, 131, Material.DIRT);
//				BlockUtils.fillArea(244, 69, 161, 242, 67, 159, Material.AIR);
//				BlockUtils.fillArea(214, 67, 133, 212, 69, 131, Material.AIR);
				sender.sendMessage("test");

//		        final Poll poll = new Poll("Test poll?", new PollAnswer(1, "Yes", ChatColor.GREEN), new PollAnswer(2, "No", ChatColor.RED));
//		        ChatPoll.sendPoll(player, poll);

//				player.spigot().sendMessage(new ComponentBuilder("")
//						.append("Game")
//						.bold(true)
//						.color(ChatColor.GOLD)
//						.append(" (" + 3.0 + ")")
//						.color(ChatColor.GRAY)
//						.bold(false)
//						.append(" [hover for help]")
//						.color(ChatColor.YELLOW)
//						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
//								"The number shown after the game name in parentheses\n"
//								+ "is the game weight. A higher weight means that the\n"
//								+ "minigame has a higher chance of being picked. The\n"
//								+ "game weight can be increased or decreased by voting\n"
//								+ "on the poll at the end of the game.")
//								.color(ChatColor.GRAY).create()))
//						.create());
//
//				final Block block = player.getTargetBlockExact(3);
//				final FallingBlock fall = block.getWorld().spawnFallingBlock(
//						new Location(Var.WORLD, block.getX() + 0.5, block.getY(), block.getZ() + 0.5),
//						block.getBlockData());
//				final Vector velocity = fall.getVelocity();
//				velocity.setY(1.5);
//				fall.setVelocity(velocity);
//				block.setType(Material.AIR);

//				final List<String> mapIdentifiers = new ArrayList<>();
//				mapIdentifiers.add("<map>");
//				for (final Game<? extends GameMap> game : Game.GAMES) {
//					for (final GameMap map : game.getGameMaps()) {
//						mapIdentifiers.add(map.getIdentifier());
//					}
//				}
//				sender.sendMessage(String.join(", ", mapIdentifiers));
//				final NBTItem nbt = new NBTItem(new ItemStack(Material.DIAMOND_SHOVEL));
//				nbt.getStringList("CanDestroy").add("minecraft:dirt");
//				((Player) sender).getInventory().addItem(nbt.getItem());
			final Player player = (Player) sender;
			
			if (args.length == 2) {
				final Missile missile;
				if (args[1].equals("jug")) {
					missile = Missile.JUGGERNAUT;
				} else if (args[1].equals("test")) {
					missile = Missile.TEST;
				} else if (args[1].equals("comp")) {
					missile = Missile.COMPRESSOR;
				} else {
					player.sendMessage("deze missile bestaat niet");
					return true;
				}
				
				missile.build(player.getLocation().add(0, -3, -2), BlockFace.NORTH);
				player.sendMessage("Building missile");
				return true;
			} else {
				player.sendMessage("geef naam (jug, test, comp)");
				return true;
			}
		}

		return true;
	}

}
