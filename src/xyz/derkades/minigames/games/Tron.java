package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
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

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.tron.TronMap;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.utils.Utils;

public class Tron extends Game {

	Tron() {
		super("Tron", new String[] {
				"Snake in Minecraft",
		}, 4, TronMap.MAPS);
	}

	private static final double MOVEMENT_SPEED = 0.3;

	private TronMap map;
	private List<BukkitTask> tasks;
	private List<UUID> spectators;

	Map<UUID, TronTeam> teams;

	@Override
	void begin(final GameMap genericMap) {
		this.map = (TronMap) genericMap;
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

		final List<TronTeam> availableTeams = new LinkedList<TronTeam>(Arrays.asList(TronTeam.values()));

		for (final Player player : Bukkit.getOnlinePlayers()) {
			final TronTeam playerTeam = availableTeams.remove(0);
			this.teams.put(player.getUniqueId(), playerTeam);
			player.teleport(this.map.getSpawnLocations().get(playerTeam));
			Utils.giveInfiniteEffect(player, PotionEffectType.SLOW, 100);
		}

		this.sendMessage("Make sure that you are not facing a wall");

		new GameTimer(this, 100, 8) {

			@Override
			public void onStart() {
				Bukkit.getOnlinePlayers().forEach(Utils::clearPotionEffects);

				for (final Player player : Bukkit.getOnlinePlayers()) {
					new BlockPlacerTask(player).runTaskTimer(Minigames.getInstance(), 1, 1);
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

		};
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

		BlockPlacerTask(final OfflinePlayer player){
			this.offlinePlayer = player;
		}

		@Override
		public void run() {
			if (!this.offlinePlayer.isOnline()) {
				System.out.println("Player " + this.offlinePlayer.getName() + " is no longer online");
				this.cancel();
				return;
			}

			final Player player = (Player) this.offlinePlayer;

			if (!Tron.this.teams.containsKey(player.getUniqueId())) {
				System.out.println("Player " + this.offlinePlayer.getName() + " is not in the teams hashmap");
				this.cancel();
				return;
			}

			if (!Utils.isIn2dBounds(player, Tron.this.map.getOuterCornerOne(), Tron.this.map.getOuterCornerTwo())) {
				System.out.println("Player " + this.offlinePlayer.getName() + " is out of bounds!! Canceling task.");
				this.cancel();
				return;
			}

			if (!player.getGameMode().equals(GameMode.ADVENTURE)) {
				player.sendMessage("Player is no(t) (longer) in gamemode ADVENTURE");
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
				player.setVelocity(new Vector(0, 0, -MOVEMENT_SPEED));
				location.setX(location.getBlockX() + 0.5);
			} else if (direction.equals("east")) {
				walkingTo.setX(walkingTo.getX() + 1);
				player.setVelocity(new Vector(MOVEMENT_SPEED, 0, 0));
				location.setZ(location.getBlockZ() + 0.5);
			} else if (direction.equals("south")) {
				walkingTo.setZ(walkingTo.getZ() + 1);
				player.setVelocity(new Vector(0, 0, MOVEMENT_SPEED));
				location.setX(location.getBlockX() + 0.5);
			} else if (direction.equals("west")) {
				walkingTo.setX(walkingTo.getX() - 1);
				player.setVelocity(new Vector(-MOVEMENT_SPEED, 0, 0));
				location.setZ(location.getBlockZ() + 0.5);
			}

			if (this.i % 40 == 0) {
				player.teleport(location);
			}

			if (walkingTo.getBlock().getType() != Material.AIR) {
				//dead
				Tron.this.spectators.add(player.getUniqueId());
				Tron.this.teams.remove(player.getUniqueId());
				this.cancel();

				Tron.this.sendMessage(player.getName() + " has died.");

				Utils.teleportUp(player, 20);
				player.setGameMode(GameMode.SPECTATOR);
			}
		}

	    public String getDirection(final Player player) {
		    float yaw = player.getLocation().getYaw();
		    if (yaw < 0) {
		        yaw += 360;
		    }
		    if (yaw >= 315 || yaw < 45) {
		        return "south";
		    } else if (yaw < 135) {
		        return "west";
		    } else if (yaw < 225) {
		        return "north";
		    } else if (yaw < 315) {
		        return "east";
		    }
		    return "north";
		}

	}

}
