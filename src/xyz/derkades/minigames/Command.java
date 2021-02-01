package xyz.derkades.minigames;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.Hastebin;
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.Minigames.ShutdownReason;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.missiles.Missile;
import xyz.derkades.minigames.games.missiles.Shield;
import xyz.derkades.minigames.menu.GamesListMenu;
import xyz.derkades.minigames.menu.MainMenu;
import xyz.derkades.minigames.menu.StatsMenu;
import xyz.derkades.minigames.random.RandomPicking;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.queue.TaskQueue;
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
			final String name = args[1].replace("_", " ");
			final Game<? extends GameMap> game = Game.fromString(name);
			if (game == null) {
				sender.sendMessage("The specified game '" + name + "' does not exist.");
			} else {
				sender.sendMessage(game.getName() + " will be chosen as the next game");
				RandomPicking.FORCE_GAME = game;
				Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = true;
			}
			return true;
		}

		if (args.length == 2 && (args[0].equalsIgnoreCase("map") || args[0].equalsIgnoreCase("m")) && sender.hasPermission("minigames.nextmap")) {
			final String name = args[1].replace("_", " ");
			final GameMap map = GameMap.fromIdentifier(name);
			if (map == null) {
				sender.sendMessage("The specified map '" + name + "' does not exist.");
			} else {
				sender.sendMessage(map.getIdentifier() + " will be chosen as the next map");
				RandomPicking.FORCE_MAP = map;
				Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = true;
			}
			return true;
		}

		if (args.length == 1){
			if ((args[0].equalsIgnoreCase("start") || args[0].equals("b")) && sender.hasPermission("minigames.start")) {
				AutoRotate.startNewRandomGame();
				Minigames.STOP_GAMES = false;
			} else if ((args[0].equalsIgnoreCase("stop") || args[0].equals("e")) && sender.hasPermission("minigames.stop")) {
				sender.sendMessage(ChatColor.RED + "! STOPPED GAMES !");
				Minigames.STOP_GAMES = true;
				Logger.info("Games stopped. After this game, no new game will be started.");
			} else if (args[0].equalsIgnoreCase("!") && sender.hasPermission("minigames.emerg")) {
				Minigames.shutdown(ShutdownReason.EMERGENCY_MANUAL, "The /games ! command was executed");
			} else if (args[0].equalsIgnoreCase("min") && sender.hasPermission("minigames.min")) {
				Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = true;
				sender.sendMessage("Bypassing minimum player check");
			} else if (args[0].equals("reloadworlds") && sender.hasPermission("minigames.world.reload")) {
				Logger.info("Reloading worlds, this may take a long time and cause lag..");

				for (final GameWorld world : GameWorld.values()) {
					TaskQueue.add(() -> {
						world.load();
						world.unload();
					});
				}

				Logger.info("Reloading worlds done.");
			} else if (args[0].equals("unloadworlds") && sender.hasPermission("minigames.world.unload")) {
				Logger.info("Unloading worlds");

				for (final GameWorld world : GameWorld.values()) {
					world.unload();
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
			} else if (args[0].equals("jazz") && sender.hasPermission("minigames.music")) {
				Bukkit.broadcastMessage("Initiating jazz mode...");
				Scheduler.delay(15, () -> {
					for (final Player player : Bukkit.getOnlinePlayers()) {
						player.stopSound(Sound.MUSIC_DISC_13);
						player.playSound(player.getLocation(), Sound.MUSIC_DISC_13, 0.7f, 1.0f);
					}
				});
			} else if (args[0].equals("stopmusic") && sender.hasPermission("minigames.music")) {
				Logger.info("Music stopped");
				for (final Player player : Bukkit.getOnlinePlayers()) {
					player.stopSound(Sound.MUSIC_DISC_13);
				}
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
				Missile missile;
				try {
					missile = Missile.valueOf(args[1].toUpperCase());
				} catch (final IllegalArgumentException e) {
					player.sendMessage("Deze missile bestaat niet, kies uit: ");
					player.sendMessage("geef naam, kies uit:");
					player.sendMessage(Arrays.stream(Missile.values()).map(Missile::name).map(String::toLowerCase).sorted().collect(Collectors.joining(", ")));
					return true;
				}
				
				final BlockFace face = new MPlayer(player).getFacingAsBlockFace();
				Logger.debug("%s: building missile \"%s\" in direction %s", player.getName(), missile, face);
				missile.build(player.getLocation().add(0, -3, 0), face);
				return true;
			} else {
				player.sendMessage("geef naam, kies uit:");
				player.sendMessage(Arrays.stream(Missile.values()).map(Missile::name).map(String::toLowerCase).sorted().collect(Collectors.joining(", ")));
				return true;
			}
		}
		
		if (args.length >= 1 && args[0].equals("test2") && sender.hasPermission("minigames.test")) {
			final Player player = (Player) sender;
			final BlockFace facing = player.getFacing();
			
			new BukkitRunnable() {

				double t = 0;
				final Location loc = player.getLocation();

				@Override
				public void run() {
					this.t = this.t + 0.9;
					
					final Vector direction = this.loc.getDirection().normalize();
					final double x = direction.getX() * this.t;
					final double y = direction.getY() * this.t + 1.5;
					final double z = direction.getZ() * this.t;
					this.loc.add(x, y, z);
					this.loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, this.loc, 1, 0, 0, 0, 0.0f);
					this.loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, this.loc, 1, 0, 0, 0, 0.0f);

					final Block block = this.loc.getBlock();

					if (this.t > 40 || block.getType() != Material.AIR) {
						this.cancel();
						this.loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, this.loc, 1);
						this.loc.getWorld().playSound(this.loc, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 1.0f);
						
						final Shield shield = Random.getRandomBoolean() ? Shield.BLUE : Shield.RED;
						
						shield.build(this.loc, facing);
					}

					this.loc.subtract(x, y, z);
				}
			}.runTaskTimer(Minigames.getInstance(), 0, 1);
			return true;
		}
		
		if (args.length >= 1 && args[0].equals("generatemissilecode") && sender.hasPermission("minigames.test")) {
			final Player player = (Player) sender;
			player.sendMessage("missile code genereren... LET OP: werkt alleen voor missiles richting NORTH");
			final Block block = player.getTargetBlockExact(5);
			if (block == null) {
				player.sendMessage("kijk naar het beginblok");
				return true;
			}
			
			final Set<String> lines = new HashSet<>();
			try {
				generateMissileCode(block, 0, 0, 0, lines, 0);
			} catch (final StackOverflowError e) {
				player.sendMessage("te veel blokken, zorg de missile niet tegen een muur aan zit");
				return true;
			}
			
			Scheduler.async(() -> {
				final StringBuilder content = new StringBuilder();
				lines.stream().sorted().forEach((l) -> {
					content.append(l);
					content.append("\n");
				});
				try {
					final String key = Hastebin.createPaste(content.toString(), "paste.derkad.es");
					final String url = "https://paste.derkad.es/raw/" + key;
					Scheduler.run(() -> {
						player.sendMessage(url);
					});
				} catch (final Exception e) {
					Logger.warning(e.getClass() + " " + e.getMessage());
					Scheduler.run(() -> player.sendMessage("het ging niet goed. Als je HTTP error 400 of 413 krijgt zijn er waarschijnlijk te veel blokken, controleer dat de missile niet tegen een muur aan zit."));
					e.printStackTrace();
				}
			});
		}

		return true;
	}
	
	public void generateMissileCode(final Block start, final int fb, final int ud, final int lr, final Set<String> lines, int airCounter) {
		if (airCounter > 3) {
			return;
		}
		
		final Block block = start.getRelative(lr, ud, -fb);
		
		if (block.getType() != Material.AIR) {
			final String line;
			switch(block.getType()) {
				case STICKY_PISTON:
				case PISTON:
				case OBSERVER:
					final BlockFace face = ((Directional) block.getBlockData()).getFacing();
					String facing;
					switch(face) {
					case NORTH:
						facing = "FRONT"; break;
					case SOUTH:
						facing = "BACK"; break;
					case WEST:
						facing = "LEFT"; break;
					case EAST:
						facing = "RIGHT"; break;
					case DOWN:
						facing = "DOWN"; break;
					case UP:
						facing = "UP"; break;
					default:
						facing = null;
					}
					line = String.format("new MissileBlock(%s, %s, %s, Material.%s, %s),", fb, ud, lr, block.getType().name(), facing);
					break;
				default:
					line = String.format("new MissileBlock(%s, %s, %s, Material.%s),", fb, ud, lr, block.getType().name());
			}
			if (lines.contains(line)) {
				return;
			} else {
				lines.add(line);
			}
		} else {
			airCounter++;
		}
		
		generateMissileCode(start, fb+1, ud, lr, lines, airCounter);
		generateMissileCode(start, fb, ud+1, lr, lines, airCounter);
		generateMissileCode(start, fb, ud-1, lr, lines, airCounter);
		generateMissileCode(start, fb, ud, lr+1, lines, airCounter);
		generateMissileCode(start, fb, ud, lr-1, lines, airCounter);
	}

}
