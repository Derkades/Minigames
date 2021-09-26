package derkades.minigames;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import derkades.minigames.Minigames.ShutdownReason;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameMap;
import derkades.minigames.games.missile.Missile;
import derkades.minigames.games.missile.Shield;
import derkades.minigames.games.missile.wars.MissileWarsMap;
import derkades.minigames.menu.GamesListMenu;
import derkades.minigames.menu.MainMenu;
import derkades.minigames.menu.StatsMenu;
import derkades.minigames.modules.ResourcePack;
import derkades.minigames.random.RandomPicking;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import derkades.minigames.utils.queue.TaskQueue;
import derkades.minigames.worlds.GameWorld;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import nl.rkslot.pluginreloader.PluginReloader;
import xyz.derkades.derkutils.Hastebin;

public class Command implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command arg1, final String arg2, final String[] args) {
		if (args.length == 0) {
			final Player player = (Player) sender;
			new MainMenu(player);
		} else if (args.length == 2) {
			switch(args[0]) {
				case "map", "m" -> {
					if (!sender.hasPermission("minigames.gamecontrol")) {
						return true;
					}
					final GameMap map = GameMap.fromIdentifier(args[1]);
					if (map == null) {
						sender.sendMessage("The specified map '" + args[1] + "' does not exist.");
					} else {
						RandomPicking.FORCE_GAME = GameMap.getGame(map);
						if (RandomPicking.FORCE_GAME != null) {
							RandomPicking.FORCE_MAP = map;
							Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = true;
							sender.sendMessage(map.getIdentifier() + " will be chosen as the next map");
						} else {
							sender.sendMessage("error, no game exists for this map???");
						}
					}
				}
				case "next", "n" -> {
					if (!sender.hasPermission("minigames.gamecontrol")) {
						return true;
					}
					final String name = args[1].replace("_", " ");
					final Game<? extends GameMap> game = Game.fromString(name);
					if (game == null) {
						sender.sendMessage("The specified game '" + name + "' does not exist.");
					} else {
						sender.sendMessage(game.getName() + " will be chosen as the next game");
						RandomPicking.FORCE_GAME = game;
						Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = true;
					}
				}
				case "unloadworld" -> {
					if (!sender.hasPermission("minigames.debug")) {
						return true;
					}
					sender.sendMessage("Unloading world " + args[1]);
					GameWorld.valueOf(args[1]).unload();
				}
				case "buildmissile" -> {
					if (!sender.hasPermission("minigames.debug")) {
						return true;
					}
					final Player player = (Player) sender;
					Missile missile;
					try {
						missile = Missile.valueOf(args[1].toUpperCase());
					} catch (final IllegalArgumentException e) {
						player.sendMessage("Deze missile bestaat niet, kies uit: ");
						player.sendMessage(Arrays.stream(Missile.values()).map(Missile::name).map(String::toLowerCase).sorted().collect(Collectors.joining(", ")));
						return true;
					}

					final BlockFace face = new MPlayer(player).getFacingAsBlockFace();
					Logger.debug("%s: building missile \"%s\" in direction %s", player.getName(), missile, face);
					missile.build(player.getLocation().add(0, -3, 0), face);
				}
				case "sign" -> {
					if (sender.hasPermission("minigames.debug")) {
						final Player player = (Player) sender;
						final char c;
						try {
							c = (char) Integer.parseInt(args[1], 16);
						} catch (final NumberFormatException e) {
							player.sendMessage("invalid base16");
							return true;
						}
						player.sendMessage(String.format("Char: %04x", (int) c));
						final Block block = player.getTargetBlock(10);
						if (block.getState() instanceof Sign) {
							final Sign sign = (Sign) block.getState();
							sign.line(0, Component.text(c));
							sign.setColor(DyeColor.WHITE);
							sign.setGlowingText(true);
							sign.update();
						} else {
							player.sendMessage("not a sign");
						}
					}
				}
			}
		} else if (args.length == 1) {
			switch(args[0]) {
				case "start", "b", "begin" -> {
					if (sender.hasPermission("minigames.gamecontrol")) {
						if (GameState.getCurrentState() == GameState.IDLE_MAINTENANCE) {
							AutoRotate.startNewRandomGame();
							Minigames.STOP_GAMES = false;
						} else {
							sender.sendMessage("this command will only start a new game when the server is in maintenance mode");
						}
					}
				}
				case "stop", "e", "end" -> {
					if (sender.hasPermission("minigames.gamecontrol")) {
						sender.sendMessage(ChatColor.RED + "! STOPPED GAMES !");
						Minigames.STOP_GAMES = true;
						Logger.info("Games stopped. After this game, no new game will be started.");
					}
				}
				case "!", "emerg", "emergency", "shutdown" -> {
					if (sender.hasPermission("minigames.debug")) {
						Minigames.shutdown(ShutdownReason.EMERGENCY_MANUAL, "The /games ! command was executed");
					}
				}
				case "min" -> {
					if (sender.hasPermission("minigames.gamecontrol")) {
						Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = true;
						sender.sendMessage("Bypassing minimum player check");
					}

				}
				case "list" -> new GamesListMenu((Player) sender);
				case "damage" -> {
					if (sender.hasPermission("minigames.debug")) {
						new MPlayer((Player) sender).setDisableDamage(false);
						sender.sendMessage("Set damageDisabled to false");
					}
				}
				case "debug" -> {
					if (sender.hasPermission("minigames.debug")) {
					Logger.debugMode = !Logger.debugMode;
					Minigames.getInstance().getConfig().set("debug_mode", Logger.debugMode);
					Minigames.getInstance().saveConfig();
					sender.sendMessage("Set debug mode to " + Logger.debugMode);
					}
				}
				case "currentgame" -> {
					if (GameState.currentlyHasGame()) {
						sender.sendMessage("Current game: " + GameState.getCurrentGame().getName());
					} else {
						sender.sendMessage("No game in progress.");
					}
				}
				case "stats" -> {
					new StatsMenu((Player) sender);
				}
				case "jazz" -> {
					if (sender.hasPermission("minigames.music")) {
						Bukkit.broadcast(Component.text("Initiating jazz mode..."));
						Scheduler.delay(15, () -> {
							for (final Player player : Bukkit.getOnlinePlayers()) {
								player.stopSound(Sound.MUSIC_DISC_13);
								player.playSound(player.getLocation(), Sound.MUSIC_DISC_13, 0.7f, 1.0f);
							}
						});
					}
				}
				case "stopmusic" -> {
					if (sender.hasPermission("minigames.music")) {
						Logger.info("Music stopped");
						for (final Player player : Bukkit.getOnlinePlayers()) {
							player.stopSound(Sound.MUSIC_DISC_13);
						}
					}
				}
				case "refreshpack" -> {
					if (sender.hasPermission("minigames.debug")) {
						ResourcePack.refresh();
					}
				}
				case "clearpack", "emptypack" -> ResourcePack.sendEmptyPack((Player) sender);
				case "applypack" -> ResourcePack.sendResourcePack((Player) sender);
				case "shield" -> {
					if (sender.hasPermission("minigames.debug")) {
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
					}
				}
				case "reload" -> {
					if (sender.hasPermission("minigames.debug")) {
						final PluginReloader reloader = (PluginReloader) Bukkit.getPluginManager().getPlugin("PluginReloader");
						reloader.forceReload(Minigames.getInstance());
					}
				}
				case "generatemissilecode" -> {
					if (sender.hasPermission("minigames.debug")) {
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
				}
				case "resetmissileworld" -> {
					if (sender.hasPermission("minigames.debug")) {
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
				}
				case "listmissiles" -> {
					if (sender.hasPermission("minigames.debug")) {
						sender.sendMessage(Arrays.stream(Missile.values()).map(Missile::name).map(String::toLowerCase).sorted().collect(Collectors.joining(", ")));
					}
				}
				case "test" -> {
					if (!sender.hasPermission("minigames.debug")) {
						return true;
					}
//					new DieAnimationMenu(new BoardPlayer(new MPlayer((Player) sender)), 1, 10, Random.getRandomInteger(1, 10));
//			        final Location fbLocation = loc.add(
//			        		loc
//			                .getDirection()
//			                .normalize()
//			                .multiply(2)
//			                .toLocation(player.getWorld(), loc.getYaw(),
//			                		loc.getPitch())).add(0, 1D, 0);
//			        final Fireball f = player.getWorld().spawn(fbLocation, Fireball.class);
//			        f.setYield(100);
//			        f.setShooter(player);
//			        f.setIsIncendiary(false);
					sender.sendMessage("test");
//					final Block block = player.getTargetBlockExact(3);
//					final FallingBlock fall = block.getWorld().spawnFallingBlock(
//							new Location(Var.WORLD, block.getX() + 0.5, block.getY(), block.getZ() + 0.5),
//							block.getBlockData());
//					final Vector velocity = fall.getVelocity();
//					velocity.setY(1.5);
//					fall.setVelocity(velocity);
//					block.setType(Material.AIR);
				}
			}
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
