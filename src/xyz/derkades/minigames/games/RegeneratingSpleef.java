package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
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

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.games.spleef.SpleefMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class RegeneratingSpleef extends Game<SpleefMap> {

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

	private List<UUID> dead;
	private List<UUID> all;

	@Override
	public void onPreStart() {
		this.dead = new ArrayList<>();

		this.map.fill();

		for (final Player player: Bukkit.getOnlinePlayers()){
			player.teleport(this.map.getStartLocation());
		}

	}

	@Override
	public void onStart() {
		final ItemStack shovel = new ItemBuilder(Material.DIAMOND_SHOVEL)
				.name("Spleefanator 8000")
				.enchant(Enchantment.DIG_SPEED, 5)
				.unbreakable()
				.canDestroy("snow_block")
				.create();

		Bukkit.getOnlinePlayers().forEach((player) -> player.getInventory().setItem(0, shovel));

		RegeneratingSpleef.this.all = Utils.getOnlinePlayersUuidList();
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (Utils.getAliveAcountFromDeadAndAllList(RegeneratingSpleef.this.dead, RegeneratingSpleef.this.all) < 2 && secondsLeft > 5)
			return 5;

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		RegeneratingSpleef.this.endGame(Utils.getWinnersFromDeadAndAllList(RegeneratingSpleef.this.dead, RegeneratingSpleef.this.all, false));
		RegeneratingSpleef.this.dead.clear();
		RegeneratingSpleef.this.all.clear();
	}

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			return;

		if (this.map.enableFlyingBlocks()) {
			final Block block = event.getBlock();
				if (!block.getType().equals(Material.SNOW_BLOCK))
					return;

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
		final MPlayer player = new MPlayer(event);

		if (this.dead.contains(player.getUniqueId()))
			return;

		if (player.getBlockOn().getType() == Material.BEDROCK){
			this.dead.add(player.getUniqueId());
			this.sendMessage(player.getName() + " died");
			player.dieUp(3);
		}
	}

}
