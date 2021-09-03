package derkades.minigames.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.Minigames.ShutdownReason;
import derkades.minigames.games.tron.TronMap;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.queue.TaskQueue;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class Tron extends Game<TronMap> {

	private static final double MOVEMENT_SPEED = 0.3;
	private static final int PLAYER_Y_DISTANCE = 30;
	private static final float PLAYER_PITCH = 90f;
	private static final Material CAGE_MATERIAL = Material.BLACK_CONCRETE;

	@Override
	public String getIdentifier() {
		return "tron";
	}

	@Override
	public String getName() {
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
	public Material getMaterial() {
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

	private List<BukkitTask> tasks;
	private List<UUID> spectators;

	private Map<UUID, TronTeam> teams;

	@Override
	public void onPreStart() {
		this.tasks = new ArrayList<>();
		this.spectators = new ArrayList<>();
		this.teams = new HashMap<>();

		BlockUtils.fillArea(this.map.getWorld(),
				this.map.getInnerCornerOne().getBlockX(), this.map.getInnerCornerOne().getBlockY(), this.map.getInnerCornerOne().getBlockZ(),
				this.map.getInnerCornerTwo().getBlockX(), this.map.getInnerCornerTwo().getBlockY(), this.map.getInnerCornerTwo().getBlockZ(),
				Material.BLACK_CONCRETE);

		BlockUtils.fillArea(this.map.getWorld(),
				this.map.getInnerCornerOne().getBlockX(), this.map.getInnerCornerOne().getBlockY() + 1, this.map.getInnerCornerOne().getBlockZ(),
				this.map.getInnerCornerTwo().getBlockX(), this.map.getInnerCornerTwo().getBlockY() + 2, this.map.getInnerCornerTwo().getBlockZ(),
				Material.AIR);

		final List<TronTeam> availableTeams = new LinkedList<>(Arrays.asList(TronTeam.values()));

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			final TronTeam playerTeam = availableTeams.remove(0);
			playerTeam.direction = this.map.getSpawnDirection(playerTeam);
			this.teams.put(player.getUniqueId(), playerTeam);
			final Location loc = this.map.getSpawnLocations().get(playerTeam).clone().add(0, PLAYER_Y_DISTANCE, 0);
			loc.setYaw(90f);
			TaskQueue.add(() -> {
				player.teleport(loc);
				player.placeCage(true, CAGE_MATERIAL);
			});
			player.giveInfiniteEffect(PotionEffectType.SLOW, 100);
			player.giveInfiniteEffect(PotionEffectType.JUMP, 200);
		}

		sendMessage("Steer using your 4 (left) and 6 (right) keys or by scrolling up and down using your mouse weel");
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.clearPotionEffects();
			player.giveInfiniteEffect(PotionEffectType.SPEED, 3);
			player.placeCage(false, CAGE_MATERIAL);
			player.getInventory().setHeldItemSlot(4);
			this.tasks.add(new BlockPlacerTask(player).runTaskTimer(Minigames.getInstance(), 1, 1));
			player.sendTitle(ChatColor.GRAY + "Use keyboard", ChatColor.GRAY + "[4] LEFT [6] RIGHT");
			player.hideForEveryoneElse();
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (Tron.this.teams.size() < 2 && secondsLeft > 2) {
			return 2;
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		if (Tron.this.teams.size() == 1) {
			Tron.this.endGame(Tron.this.teams.keySet().toArray(new UUID[] {})[0]);
		} else {
			Tron.this.endGame();
		}

		this.tasks.forEach((task) -> task.cancel());
		this.tasks = null;
		this.spectators = null;
		this.teams = null;
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		this.spectators.add(player.getUniqueId());
		this.teams.remove(player.getUniqueId());
		player.dieTo(this.map.getSpawnLocations().get(TronTeam.WHITE));
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {}

	public enum Direction {

		NORTH(180),
		EAST(270),
		SOUTH(0),
		WEST(90),

		;

		float yaw;

		Direction(final float yaw){
			this.yaw = yaw;
		}

	}

	public enum TronTeam {

		ORANGE(ChatColor.GOLD, Material.ORANGE_CONCRETE, Material.ORANGE_STAINED_GLASS),
		PURPLE(ChatColor.DARK_PURPLE, Material.PURPLE_CONCRETE, Material.PURPLE_STAINED_GLASS),
		LIGHT_BLUE(ChatColor.BLUE, Material.LIGHT_BLUE_CONCRETE, Material.LIGHT_BLUE_STAINED_GLASS),
		YELLOW(ChatColor.YELLOW, Material.YELLOW_CONCRETE, Material.YELLOW_STAINED_GLASS),
		GREEN(ChatColor.DARK_GREEN, Material.GREEN_CONCRETE, Material.GREEN_STAINED_GLASS),
		PINK(ChatColor.LIGHT_PURPLE, Material.PINK_CONCRETE, Material.PINK_STAINED_GLASS),
		LIME(ChatColor.GREEN, Material.LIME_CONCRETE, Material.LIME_STAINED_GLASS),
		RED(ChatColor.RED, Material.RED_CONCRETE, Material.RED_STAINED_GLASS),
		WHITE(ChatColor.WHITE, Material.WHITE_CONCRETE, Material.WHITE_STAINED_GLASS),
		BLUE(ChatColor.DARK_BLUE, Material.BLUE_CONCRETE, Material.BLUE_STAINED_GLASS),

		;

		@SuppressWarnings("unused")
		private final ChatColor chatColor;
		private final Material bottomBlock;
		private final Material glassBlock;

		private Direction direction;

		TronTeam(final ChatColor chatColor, final Material bottomBlock, final Material glassBlock){
			this.chatColor = chatColor;
			this.bottomBlock = bottomBlock;
			this.glassBlock = glassBlock;
		}

		public Direction getDirection() {
			return this.direction;
		}

		public void rotateLeft() {
			switch(this.direction) {
			case NORTH:
				this.direction = Direction.WEST;
				break;
			case WEST:
				this.direction = Direction.SOUTH;
				break;
			case SOUTH:
				this.direction = Direction.EAST;
				break;
			case EAST:
				this.direction = Direction.NORTH;
				break;
			default:
				Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Illegal direction '" + this.direction + "'");
			}
		}

		public void rotateRight() {
			switch(this.direction) {
			case NORTH:
				this.direction = Direction.EAST;
				break;
			case EAST:
				this.direction = Direction.SOUTH;
				break;
			case SOUTH:
				this.direction = Direction.WEST;
				break;
			case WEST:
				this.direction = Direction.NORTH;
				break;
			default:
				Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Illegal direction '" + this.direction + "'");
			}
		}

	}

	private class BlockPlacerTask extends BukkitRunnable {

		private final OfflinePlayer offlinePlayer;
		int i = 0;

		BlockPlacerTask(final MPlayer player){
			this.offlinePlayer = player.bukkit();
		}

		@Override
		public void run() {
			if (Tron.this.teams == null) {
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

			if (!Tron.this.teams.containsKey(player.getUniqueId())) {
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

			final TronTeam team = Tron.this.teams.get(player.getUniqueId());

			boolean changedDirection = false;

			final PlayerInventory inv = player.getInventory();
			if (inv.getHeldItemSlot() == 3) {
				team.rotateLeft();
				changedDirection = true;
			} else if (inv.getHeldItemSlot() == 5) {
				team.rotateRight();
				changedDirection = true;
			}

			if (inv.getHeldItemSlot() != 4) {
				inv.setHeldItemSlot(4);
			}

			final Direction direction = team.getDirection();

			final Location walkingTo = player.getLocation();
			walkingTo.add(0, -PLAYER_Y_DISTANCE, 0);
			final Location location = player.getLocation();
			location.add(0, -PLAYER_Y_DISTANCE, 0);

			final Block block = location.getBlock();
			block.setType(team.glassBlock);
			block.getRelative(BlockFace.UP).setType(team.glassBlock);
			block.getRelative(BlockFace.DOWN).setType(team.bottomBlock);

			Vector newVelo;
			switch(direction) {
			case NORTH:
				walkingTo.setZ(walkingTo.getZ() - 1);
				newVelo = new Vector(0, 0, -MOVEMENT_SPEED);
				location.setX(location.getBlockX() + 0.5);
				break;
			case EAST:
				walkingTo.setX(walkingTo.getX() + 1);
				newVelo = new Vector(MOVEMENT_SPEED, 0, 0);
				location.setZ(location.getBlockZ() + 0.5);
				break;
			case SOUTH:
				walkingTo.setZ(walkingTo.getZ() + 1);
				newVelo = new Vector(0, 0, MOVEMENT_SPEED);
				location.setX(location.getBlockX() + 0.5);
				break;
			case WEST:
				walkingTo.setX(walkingTo.getX() - 1);
				newVelo = new Vector(-MOVEMENT_SPEED, 0, 0);
				location.setZ(location.getBlockZ() + 0.5);
				break;
			default:
				Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Illegal direction '" + direction + "'");
				newVelo = null;
			}

			player.bukkit().setVelocity(newVelo);

			if (changedDirection ||
					this.i % 30 == 0 ||
					this.i < 30 // Ensure correct rotation at the start and give some grace time
					) {
				final Location loc2 = location.clone().add(0, PLAYER_Y_DISTANCE, 0);
				loc2.setYaw(direction.yaw);
//				loc2.setYaw(0);
				loc2.setPitch(PLAYER_PITCH);
				player.teleport(loc2);
			}

			final Material toType = walkingTo.getBlock().getType();
			if (toType != Material.AIR) {
				cancel();

				// Try to get killer
				TronTeam killerTeam = null;
				for (final TronTeam team2 : TronTeam.values()) {
					if (toType.equals(team2.glassBlock)) {
						killerTeam = team2;
						break;
					}
				}

				if (killerTeam != null) {
					// Try to get player
					MPlayer killer = null;
					for (final MPlayer player2 : Minigames.getOnlinePlayers()) {
						if (Tron.this.teams.containsKey(player2.getUniqueId()) &&
								Tron.this.teams.get(player2.getUniqueId()).equals(killerTeam)) {
							killer = player2;
						}
					}

					if (killer != null) {
						sendMessage(player.getName() + " was killed by " + killer.getName());
					} else {
						// can occur for example if the killer has logged out
						final String killerTeamName = killerTeam.name().toLowerCase().replace("_", " ");
						sendMessage(player.getName() + " was killed by the " + killerTeamName + " team");
					}
				} else {
					sendMessage(player.getName() + " ran into a wall.");
				}

				// dead
				Tron.this.spectators.add(player.getUniqueId());
				Tron.this.teams.remove(player.getUniqueId());

				player.die();
			}
		}

//	    public String getDirection(final MPlayer player) {
//		    float yaw = player.getLocation().getYaw();
//		    if (yaw < 0) {
//		        yaw += 360;
//		    }
//		    if (yaw >= 315 || yaw < 45) {
//				return "south";
//			} else if (yaw < 135) {
//				return "west";
//			} else if (yaw < 225) {
//				return "north";
//			} else if (yaw < 315) {
//				return "east";
//			}
//		    return "north";
//		}

	}

}
