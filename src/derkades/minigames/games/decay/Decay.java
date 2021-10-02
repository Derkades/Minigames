package derkades.minigames.games.decay;

import derkades.minigames.GameState;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Decay extends Game<DecayMap> {

	private static final DecayMap[] MAPS = {
			new SpruceBrick(),
			new SquareDonut(),
	};

	private static final int BLOCKS_PER_CYCLE = 70;
	private static final Material[] BLOCK_TYPES = {
			Material.WHITE_CONCRETE,
			Material.YELLOW_CONCRETE,
			Material.ORANGE_CONCRETE,
			Material.RED_CONCRETE,
	};

	@Override
	public @NotNull String getIdentifier() {
		return "decay";
	}

	@Override
	public @NotNull String getName() {
		return "Decay";
	}

	@Override
	public String[] getDescription() {
		// TODO Description
		return new String[] {
				"something something don't die",
		};
	}

	@Override
	public @NotNull Material getMaterial() {
		return Material.RED_CONCRETE;
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public DecayMap[] getGameMaps() {
		return MAPS;
	}

	@Override
	public int getDuration() {
		return 100;
	}

	private List<Location> blocks;
	private Set<UUID> alive;

	@Override
	public void onPreStart() {
		this.blocks = new ArrayList<>();
		this.alive = Utils.getOnlinePlayersUuidSet();

		for (final Location loc : this.map.getBlocks()) {
			loc.getBlock().setType(BLOCK_TYPES[0]);
			this.blocks.add(loc);
		}

		Minigames.getOnlinePlayers().forEach(p -> p.queueTeleport(this.map.getSpawnLocation()));
	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach(p-> p.setDisableDamage(false));
	}

	public void decayBlock() {
		final Location location = ListUtils.choice(this.blocks);
		final Block block = location.getBlock();

		if (block.getType() == Material.AIR) {
			return;
		}

		for (int i = 0; i < BLOCK_TYPES.length; i++) {
			if (block.getType() == BLOCK_TYPES[i]) {
				i++;

				if (i == BLOCK_TYPES.length) {
					block.setType(Material.AIR);
				} else {
					block.setType(BLOCK_TYPES[i]);
				}

				break;
			}
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (secondsLeft > 5) {
			final int split = 4;

			for (int i = 0; i < split; i++) {
				Scheduler.delay((20 / split) * i, () -> {
					for (int j = 0; j < BLOCKS_PER_CYCLE / split; j++) {
						decayBlock();
					}
				});
			}
		}

		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return this.alive.size() < 2;
	}

	@Override
	public void onEnd() {
		endGame(this.alive, false);
		this.blocks = null;
		this.alive = null;
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.spectator();
		player.teleport(this.map.getSpawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final MPlayer player = new MPlayer(event);

		if (player.isSpectator()) {
			return;
		}

		if (this.map.isDead(player)) {
			if (GameState.getCurrentState().gameIsRunning()) {
				player.dieTo(this.map.getSpawnLocation());
				this.sendFormattedPlainMessage("%s died", player.getName());
				this.alive.remove(player.getUniqueId());
			} else {
				player.teleport(this.map.getSpawnLocation());
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER){
			event.setDamage(0);
		}
	}

}
