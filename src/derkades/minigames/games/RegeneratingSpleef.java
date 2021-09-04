package derkades.minigames.games;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import derkades.minigames.GameState;
import derkades.minigames.games.spleef.SpleefMap;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public class RegeneratingSpleef extends Game<SpleefMap> {

	@Override
	public String getIdentifier() {
		return "regenerating_spleef";
	}

	@Override
	public String getName() {
		return "Regenerating Spleef";
	}

	@Override
	public String getAlias() {
		return "spleef";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Regenerating Spleef is very similar to the",
				"classic spleef game. One twist: the blocks",
				"you break regenerate after 2 seconds, and",
				"the arena is pretty small (usually)."
		};
	}

	@Override
	public Material getMaterial() {
		return Material.SNOW_BLOCK;
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public SpleefMap[] getGameMaps() {
		return SpleefMap.MAPS;
	}

	@Override
	public int getPreDuration() {
		return 7;
	}

	@Override
	public int getDuration() {
		return 60;
	}

	private Set<UUID> alive;

	@Override
	public void onPreStart() {
		this.map.fill();

		for (final Player player: Bukkit.getOnlinePlayers()){
			player.teleport(this.map.getStartLocation());
		}
	}

	@Override
	public void onStart() {
		this.alive = Utils.getOnlinePlayersUuidSet();

		final ItemStack shovel = new ItemBuilder(Material.DIAMOND_SHOVEL)
				.name("Spleefanator 8000")
				.enchant(Enchantment.DIG_SPEED, 5)
				.unbreakable()
//				.canDestroy(Material.SNOW_BLOCK)
				.canDestroy("minecraft:snow_block")
				.create();

		Bukkit.getOnlinePlayers().forEach((player) -> player.getInventory().setItem(0, shovel));
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return this.alive.size() < 2;
	}

	@Override
	public void onEnd() {
		RegeneratingSpleef.this.endGame(this.alive, true);
		this.alive = null;
	}

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}

		if (this.map.enableFlyingBlocks()) {
			final Block block = event.getBlock();
				if (!block.getType().equals(Material.SNOW_BLOCK)) {
					return;
				}

				final FallingBlock fall = block.getWorld().spawnFallingBlock(
						new Location(this.map.getWorld(), block.getX() + 0.5, block.getY(), block.getZ() + 0.5),
						block.getBlockData());
				final Vector velocity = fall.getVelocity();
				velocity.setY(1.5);
				fall.setVelocity(velocity);
		} else {
			final Block block = event.getBlock();
			if (block.getType() == Material.SNOW_BLOCK){
				Scheduler.delay(3*20, () -> block.setType(Material.SNOW_BLOCK));
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		if (!GameState.getCurrentState().gameIsRunning() ||
				!this.alive.contains(event.getPlayer().getUniqueId())) {
			return;
		}

		final MPlayer player = new MPlayer(event);

		if (player.getBlockOn().getType() == Material.BEDROCK){
			this.alive.remove(player.getUniqueId());
			sendMessage(player.getName() + " died");
			player.dieUp(3);
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.dieTo(this.map.getWorld().getSpawnLocation().add(0, 3, 0));
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

}
