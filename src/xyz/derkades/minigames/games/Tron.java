package xyz.derkades.minigames.games;

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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.tron.TronMap;
import xyz.derkades.minigames.utils.MPlayer;

public class Tron extends Game<TronMap> {

	private static final double MOVEMENT_SPEED = 0.3;

	@Override
	public String getName() {
		return "Tron";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Snake in Minecraft",
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 4;
	}

	@Override
	public TronMap[] getGameMaps() {
		return TronMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 100;
	}

	private List<BukkitTask> tasks;
	private List<UUID> spectators;

	Map<UUID, TronTeam> teams;

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
			this.teams.put(player.getUniqueId(), playerTeam);
			player.queueTeleport(this.map.getSpawnLocations().get(playerTeam));
			player.giveInfiniteEffect(PotionEffectType.SLOW, 100);
			player.giveInfiniteEffect(PotionEffectType.JUMP, 200);
		}

		this.sendMessage("Make sure that you are not facing a wall");
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.clearPotionEffects();
			new BlockPlacerTask(player).runTaskTimer(Minigames.getInstance(), 1, 1);
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (Tron.this.teams.size() < 2 && secondsLeft > 2)
			return 2;

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		Tron.this.tasks.forEach((task) -> task.cancel());
		Tron.this.tasks.clear();
		Tron.this.spectators.clear();

		if (Tron.this.teams.size() == 1) {
			Tron.this.endGame(Tron.this.teams.keySet().toArray(new UUID[] {})[0]);
		} else {
			Tron.this.endGame();
		}

		Tron.this.teams.clear();
	}

	public static enum TronTeam {

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

		TronTeam(final ChatColor chatColor, final Material bottomBlock, final Material glassBlock){
			this.chatColor = chatColor;
			this.bottomBlock = bottomBlock;
			this.glassBlock = glassBlock;
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
			if (!this.offlinePlayer.isOnline()) {
				System.out.println("Player " + this.offlinePlayer.getName() + " is no longer online");
				this.cancel();
				return;
			}

			final MPlayer player = new MPlayer((Player) this.offlinePlayer);

			if (!Tron.this.teams.containsKey(player.getUniqueId())) {
				System.out.println("Player " + this.offlinePlayer.getName() + " is not in the teams hashmap");
				this.cancel();
				return;
			}

			if (!player.isIn2dBounds(Tron.this.map.getOuterCornerOne(), Tron.this.map.getOuterCornerTwo())) {
				System.out.println("Player " + this.offlinePlayer.getName() + " is out of bounds!! Canceling task.");
				this.cancel();
				return;
			}

			if (!player.getGameMode().equals(GameMode.ADVENTURE)) {
				player.bukkit().sendMessage("Player is no(t) (longer) in gamemode ADVENTURE");
				this.cancel();
				return;
			}

			this.i++;

			final TronTeam team = Tron.this.teams.get(player.getUniqueId());

			final Block block = player.getLocation().getBlock();
			block.setType(team.glassBlock);
			block.getRelative(BlockFace.UP).setType(team.glassBlock);
			block.getRelative(BlockFace.DOWN).setType(team.bottomBlock);

			final String direction = this.getDirection(player);

			final Location walkingTo = player.getLocation();

			final Location location = player.getLocation();

			if (direction.equals("north")) {
				walkingTo.setZ(walkingTo.getZ() - 1);
				player.bukkit().setVelocity(new Vector(0, 0, -MOVEMENT_SPEED));
				location.setX(location.getBlockX() + 0.5);
			} else if (direction.equals("east")) {
				walkingTo.setX(walkingTo.getX() + 1);
				player.bukkit().setVelocity(new Vector(MOVEMENT_SPEED, 0, 0));
				location.setZ(location.getBlockZ() + 0.5);
			} else if (direction.equals("south")) {
				walkingTo.setZ(walkingTo.getZ() + 1);
				player.bukkit().setVelocity(new Vector(0, 0, MOVEMENT_SPEED));
				location.setX(location.getBlockX() + 0.5);
			} else if (direction.equals("west")) {
				walkingTo.setX(walkingTo.getX() - 1);
				player.bukkit().setVelocity(new Vector(-MOVEMENT_SPEED, 0, 0));
				location.setZ(location.getBlockZ() + 0.5);
			}

			if (this.i % 30 == 0) {
				player.teleport(location);
			}

			final Material toType = walkingTo.getBlock().getType();
			if (toType != Material.AIR) {
				//dead
				Tron.this.spectators.add(player.getUniqueId());
				Tron.this.teams.remove(player.getUniqueId());
				this.cancel();


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
						if (Tron.this.teams.get(player2.getUniqueId()).equals(killerTeam)) {
							killer = player2;
						}
					}

					if (killer != null) {
						Tron.this.sendMessage(player.getName() + " was killed by " + killer.getName());
					} else {
						// can occur for example if the killer has logged out
						final String killerTeamName = killerTeam.name().toLowerCase().replace("_", " ");
						Tron.this.sendMessage(player.getName() + " was killed by the " + killerTeamName + " team");
					}
				} else {
					Tron.this.sendMessage(player.getName() + " has died.");
				}

				player.dieUp(20);
			}
		}

	    public String getDirection(final MPlayer player) {
		    float yaw = player.getLocation().getYaw();
		    if (yaw < 0) {
		        yaw += 360;
		    }
		    if (yaw >= 315 || yaw < 45)
				return "south";
			else if (yaw < 135)
				return "west";
			else if (yaw < 225)
				return "north";
			else if (yaw < 315)
				return "east";
		    return "north";
		}

	}

}
