package derkades.minigames.games.tron;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.Minigames.ShutdownReason;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameTeam;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.queue.TaskQueue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.BlockUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class Tron extends Game<TronMap> {

	private static final double MOVEMENT_SPEED = 0.3;
	private static final int PLAYER_Y_DISTANCE = 30;
	private static final float PLAYER_PITCH = 90f;
	private static final Material CAGE_MATERIAL = Material.BLACK_CONCRETE;

	private static final PotionEffect PRESTART_SLOW = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100, true);
	private static final PotionEffect PRESTART_JUMP = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200, true);
	private static final PotionEffect LARGER_FOV = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3, true);

	@Override
	public @NotNull String getIdentifier() {
		return "tron";
	}

	@Override
	public @NotNull String getName() {
		return "Tron";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Snake in Minecraft.",
				"Steer using your 4 (left) and 6 (right) keys",
		};
	}

	@Override
	public @NotNull Material getMaterial() {
		return Material.YELLOW_STAINED_GLASS_PANE;
	}

	@Override
	public int getRequiredPlayers() {
		return 3;
	}

	@Override
	public TronMap[] getGameMaps() {
		return TronMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 150;
	}

//	private List<BukkitTask> tasks;
	private List<UUID> spectators;
	private Map<UUID, TronPlayer> players;

	@Override
	public void onPreStart() {
//		this.tasks = new ArrayList<>();
		this.spectators = new ArrayList<>();
		this.players = new HashMap<>();

		BlockUtils.fillArea(this.map.getWorld(),
				this.map.getInnerCornerOne().getBlockX(), this.map.getInnerCornerOne().getBlockY(), this.map.getInnerCornerOne().getBlockZ(),
				this.map.getInnerCornerTwo().getBlockX(), this.map.getInnerCornerTwo().getBlockY(), this.map.getInnerCornerTwo().getBlockZ(),
				Material.BLACK_CONCRETE);

		BlockUtils.fillArea(this.map.getWorld(),
				this.map.getInnerCornerOne().getBlockX(), this.map.getInnerCornerOne().getBlockY() + 1, this.map.getInnerCornerOne().getBlockZ(),
				this.map.getInnerCornerTwo().getBlockX(), this.map.getInnerCornerTwo().getBlockY() + 2, this.map.getInnerCornerTwo().getBlockZ(),
				Material.AIR);

		final List<MPlayer> mplayers = Minigames.getOnlinePlayersInRandomOrder();
		final List<GameTeam> teams = GameTeam.getTeams(mplayers.size());
		final List<TronSpawnLocation> spawnLocations = ListUtils.chooseMultiple(this.map.getSpawnLocations(), mplayers.size());

		for (int i = 0; i < mplayers.size(); i++) {
			final MPlayer player = mplayers.get(i);
			final GameTeam team = teams.get(i);
			final TronSpawnLocation spawnLocation = spawnLocations.get(i);

			final TronPlayer tronPlayer = new TronPlayer(team, spawnLocation);
			this.players.put(player.getUniqueId(), tronPlayer);

			final Location loc = spawnLocation.location().clone().add(0, PLAYER_Y_DISTANCE, 0);
			loc.setYaw(90f);
			TaskQueue.add(() -> {
				player.teleport(loc);
				player.placeCage(true, CAGE_MATERIAL);
			});
			player.giveEffect(PRESTART_SLOW);
			player.giveEffect(PRESTART_JUMP);
		}

		sendPlainMessage("Steer using your 4 (left) and 6 (right) keys or by scrolling up and down using your mouse weel");
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.clearPotionEffects();
			player.placeCage(false, CAGE_MATERIAL);
			player.hideForEveryoneElse();

			final TronPlayer tronPlayer = this.players.get(player.getUniqueId());
			if (tronPlayer == null) {
				// Logged in after pre-start
				player.spectator();
				this.spectators.add(player.getUniqueId());
				continue;
			}

			player.giveEffect(LARGER_FOV);
			player.getInventory().setHeldItemSlot(4);
			player.sendPlainTitle("Use keyboard", "[4] LEFT [6] RIGHT");

			tronPlayer.blockPlacerTask = new BlockPlacerTask(player).runTaskTimer(Minigames.getInstance(), 1, 1);
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return this.players.size() < 2;
	}

	@Override
	public void onEnd() {
		if (this.players.size() == 1) {
			Tron.this.endGame(this.players.keySet().toArray(UUID[]::new)[0]);
		} else {
			Tron.this.endGame();
		}

		for (final TronPlayer tronPlayer : this.players.values()) {
			tronPlayer.blockPlacerTask.cancel();
		}

		this.spectators = null;
		this.players = null;
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		this.spectators.add(player.getUniqueId());
		this.players.remove(player.getUniqueId());
		player.dieTo(this.map.getSpectatorLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		final TronPlayer tronPlayer = this.players.get(player.getUniqueId());
		if (tronPlayer != null) {
			tronPlayer.blockPlacerTask.cancel();
		}
		this.players.remove(player.getUniqueId());
	}

	private class BlockPlacerTask extends BukkitRunnable {

		private final OfflinePlayer offlinePlayer;
		int i = 0;

		BlockPlacerTask(final MPlayer player){
			this.offlinePlayer = player.bukkit();
		}

		@Override
		public void run() {
			if (Tron.this.players == null) {
				Logger.warning("Stopping tron block placer tasks because teams is null. This should never happen");
				this.cancel();
				return;
			}

			if (!this.offlinePlayer.isOnline()) {
				Logger.debug("Player " + this.offlinePlayer.getName() + " is no longer online");
				cancel();
				return;
			}

			final MPlayer player = new MPlayer((Player) this.offlinePlayer);

			if (!Tron.this.players.containsKey(player.getUniqueId())) {
				Logger.warning("Player " + this.offlinePlayer.getName() + " is not in the teams hashmap");
				cancel();
				return;
			}

			if (!player.isIn2dBounds(Tron.this.map.getOuterCornerOne(), Tron.this.map.getOuterCornerTwo())) {
				Logger.warning("Player " + this.offlinePlayer.getName() + " is out of bounds!! Canceling task.");
				cancel();
				return;
			}

			if (!player.getGameMode().equals(GameMode.ADVENTURE)) {
				Logger.warning("Player %s is no(t) (longer) in gamemode ADVENTURE", player.getName());
				cancel();
				return;
			}

			this.i++;

			final TronPlayer tronPlayer = Tron.this.players.get(player.getUniqueId());

			boolean changedDirection = false;

			final PlayerInventory inv = player.getInventory();

			if (inv.getHeldItemSlot() != 4) {
				if (inv.getHeldItemSlot() < 4) {
					tronPlayer.rotateLeft();
				} else if (inv.getHeldItemSlot() > 4) {
					tronPlayer.rotateRight();
				}
				changedDirection = true;
				inv.setHeldItemSlot(4);
			}


			final Location walkingTo = player.getLocation();
			walkingTo.add(0, -PLAYER_Y_DISTANCE, 0);
			final Location location = player.getLocation();
			location.add(0, -PLAYER_Y_DISTANCE, 0);

			final Block block = location.getBlock();
			block.setType(tronPlayer.getTeam().getGlassBlock());
			block.getRelative(BlockFace.UP).setType(tronPlayer.getTeam().getGlassBlock());
			block.getRelative(BlockFace.DOWN).setType(tronPlayer.getTeam().getConcrete());

			final Direction direction = tronPlayer.getDirection();
			Vector newVelo;
			switch (direction) {
				case NORTH -> {
					walkingTo.setZ(walkingTo.getZ() - 1);
					newVelo = new Vector(0, 0, -MOVEMENT_SPEED);
					location.setX(location.getBlockX() + 0.5);
				}
				case EAST -> {
					walkingTo.setX(walkingTo.getX() + 1);
					newVelo = new Vector(MOVEMENT_SPEED, 0, 0);
					location.setZ(location.getBlockZ() + 0.5);
				}
				case SOUTH -> {
					walkingTo.setZ(walkingTo.getZ() + 1);
					newVelo = new Vector(0, 0, MOVEMENT_SPEED);
					location.setX(location.getBlockX() + 0.5);
				}
				case WEST -> {
					walkingTo.setX(walkingTo.getX() - 1);
					newVelo = new Vector(-MOVEMENT_SPEED, 0, 0);
					location.setZ(location.getBlockZ() + 0.5);
				}
				default -> {
					Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Illegal direction '" + direction + "'");
					newVelo = null;
				}
			}

			player.bukkit().setVelocity(newVelo);

			if (changedDirection ||
					this.i % 30 == 0 ||
					this.i < 30 // Ensure correct rotation at the start and give some grace time
					) {
				final Location loc2 = location.clone().add(0, PLAYER_Y_DISTANCE, 0);
				loc2.setYaw(direction.yaw);
				loc2.setPitch(PLAYER_PITCH);
				player.teleport(loc2);
			}

			final Material toType = walkingTo.getBlock().getType();
			if (toType != Material.AIR) {
				cancel();

				// Try to get killer
				TronPlayer killer = null;
				MPlayer killerPlayer = null;
				for (final Entry<UUID, TronPlayer> e : Tron.this.players.entrySet()) {
					final TronPlayer tronPlayer2 = e.getValue();
					if (toType.equals(tronPlayer2.getTeam().getGlassBlock())) {
						killer = tronPlayer2;
						killerPlayer = Minigames.getPlayer(e.getKey());
						break;
					}
				}

				if (killer != null) {
					if (killerPlayer == null) {
						// Player logged out, in theory the team shouldn't exist anymore but just in case
						sendMessage(
								Component.text(player.getName(), tronPlayer.getTeam().getTextColor())
								.append(Component.text(" was killed by the ", NamedTextColor.GRAY))
								.append(Component.text(killer.getTeam().getDisplayName(), killer.getTeam().getTextColor()))
								.append(Component.text(" team.", NamedTextColor.GRAY))
								);
					} else {
						sendMessage(
								Component.text(player.getName(), tronPlayer.getTeam().getTextColor())
								.append(Component.text(" was killed by ", NamedTextColor.GRAY))
								.append(Component.text(killerPlayer.getName(), killer.getTeam().getTextColor()))
								.append(Component.text(".", NamedTextColor.GRAY))
								);
					}
				} else {
					// Ran into a wall or into a player who already died
					sendMessage(
							Component.text(player.getName(), tronPlayer.getTeam().getTextColor())
							.append(Component.text(" died.", NamedTextColor.GRAY))
							);
				}

				// dead
				Tron.this.spectators.add(player.getUniqueId());
				Tron.this.players.remove(player.getUniqueId());

				player.die();
			}
		}
	}

}
