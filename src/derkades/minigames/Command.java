package derkades.minigames;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
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

import derkades.minigames.Minigames.ShutdownReason;
import derkades.minigames.games.Game;
import derkades.minigames.games.maps.GameMap;
import derkades.minigames.games.missile_wars.MissileWarsMap;
import derkades.minigames.games.missiles.Missile;
import derkades.minigames.games.missiles.Shield;
import derkades.minigames.menu.GamesListMenu;
import derkades.minigames.menu.MainMenu;
import derkades.minigames.menu.StatsMenu;
import derkades.minigames.random.RandomPicking;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import derkades.minigames.utils.queue.TaskQueue;
import derkades.minigames.worlds.GameWorld;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.Hastebin;

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
			final GameMap map = GameMap.fromIdentifier(args[1]);
			if (map == null) {
				sender.sendMessage("The specified map '" + args[1] + "' does not exist.");
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
			} else if (args[0].equals("refreshpack") && sender.hasPermission("minigames.refreshpack")) {
				ResourcePack.refreshAsync();
			} else if (args[0].equals("clearpack") && sender.hasPermission("minigames.clearpack")) {
				ResourcePack.sendEmptyPack((Player) sender);
			} else if (args[0].equals("applypack") && sender.hasPermission("minigames.applypack")) {
				ResourcePack.sendPack((Player) sender);
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

						final Shield shield = ThreadLocalRandom.current().nextBoolean() ? Shield.BLUE : Shield.RED;

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

			final Map<String, Integer> lines = new HashMap<>();
			try {
				generateMissileCode(block, 0, 0, 0, lines, 0);
			} catch (final StackOverflowError e) {
				player.sendMessage("te veel blokken, zorg de missile niet tegen een muur aan zit");
				return true;
			}

			Scheduler.async(() -> {
				final StringBuilder content = new StringBuilder();
				Utils.sortByValue(lines).forEach((l, weight) -> {
					if (l.contains("SIGN") || l.contains("OBSIDIAN") || l.contains("PISTON_HEAD")) {
						return;
					}
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

		if (args.length >= 1 && args[0].equals("test3") && sender.hasPermission("minigames.test")) {
			final Set<Material> DONT_REPLACE = Set.of(
					Material.BARRIER,
					Material.BEDROCK
			);
			final MissileWarsMap map = MissileWarsMap.MAPS[0];
			final Location min = map.getArenaBorderMin();
			final Location max = map.getArenaBorderMax();
			for (int y = max.getBlockY() - 1; y >= 0; y--) {
				final int finalY = y;
				TaskQueue.add(() -> {
					for (int x = min.getBlockX() + 1; x < max.getBlockX(); x++) {
						for (int z = min.getBlockZ() + 1; z < max.getBlockZ(); z++) {
							final Block block = map.getWorld().getBlockAt(x, finalY, z);
							if (!DONT_REPLACE.contains(block.getType())) {
								block.setType(Material.AIR);
							}
						}
					}
				});
			}
			map.buildArena();
		}

		return true;
	}

	public void generateMissileCode(final Block start, final int fb, final int ud, final int lr, final Map<String, Integer> lines, int airCounter) {
		if (airCounter > 3) {
			return;
		}

		final Block block = start.getRelative(lr, ud, -fb);

		if (block.getType() != Material.AIR) {
			airCounter = 0;

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
					line = String.format("new MissileBlock(%s, %s, %s, Material.%s, %s),", lr, ud, fb, block.getType().name(), facing);
					break;
				default:
					line = String.format("new MissileBlock(%s, %s, %s, Material.%s),", lr, ud, fb, block.getType().name());
			}
			if (lines.containsKey(line)) {
				return;
			} else {
				final int weight = -(fb * 20 + ud + lr);
				lines.put(line, weight);
			}
		} else {
			airCounter++;
		}

		generateMissileCode(start, fb+1, ud, lr, lines, airCounter);
		generateMissileCode(start, fb, ud+1, lr, lines, airCounter);
		generateMissileCode(start, fb, ud-1, lr, lines, airCounter);
		if (lr == 0) {
			generateMissileCode(start, fb, ud, 1, lines, airCounter);
			generateMissileCode(start, fb, ud, -1, lines, airCounter);
		} else {
			generateMissileCode(start, fb, ud, lr > 0 ? lr+1 : lr-1, lines, airCounter);
		}
	}

}
