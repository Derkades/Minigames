package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.decay.DecayMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class Decay extends Game<DecayMap> {

	private static final int BLOCKS_PER_CYCLE = 40;
	private static final Material[] BLOCK_TYPES = {
			Material.WHITE_CONCRETE,
			Material.YELLOW_CONCRETE,
			Material.ORANGE_CONCRETE,
			Material.RED_CONCRETE,
	};
	
	@Override
	public String getName() {
		return "Decay";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"something something don't die",
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public DecayMap[] getGameMaps() {
		return DecayMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 100;
	}
	
	private List<Location> blocks;
	private List<UUID> winners;
	
	@Override
	public void onPreStart() {
		this.blocks = new ArrayList<>();
		this.winners = Utils.getOnlinePlayersUuidList();
		
		for (final Location loc : this.map.getBlocks()) {
			loc.getBlock().setType(BLOCK_TYPES[0]);
			this.blocks.add(loc);
		}
		
//		Collections.shuffle(this.blocks);
		
		Minigames.getOnlinePlayers().forEach(p-> {
			p.queueTeleport(this.map.getSpawnLocation());
		});
	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach(p-> {
			p.setDisableDamage(false);
			p.giveInfiniteEffect(PotionEffectType.DAMAGE_RESISTANCE, 30);
		});
	}

	public void decayBlock() {
		final Location location = this.blocks.get(Random.getRandomInteger(0, this.blocks.size() - 1));
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
		
		if (secondsLeft > 5 && this.winners.size() <= 1) {
			return 5;
		}
		
		return secondsLeft;
	}

	@Override
	public void onEnd() {
		endGame(this.winners, false);
		this.blocks = null;
		this.winners = null;
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.spectator();
		player.teleport(this.map.getSpawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.winners.remove(player.getUniqueId());
	}
	
	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final MPlayer player = new MPlayer(event);
		if (this.map.isDead(player)) {
			if (this.started) {
				player.dieTo(this.map.getSpawnLocation());
				this.winners.remove(player.getUniqueId());
			} else {
				player.teleport(this.map.getSpawnLocation());
			}
		}
	}

}
