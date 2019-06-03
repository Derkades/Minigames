package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.spleef.SpleefMap;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class RegeneratingSpleef extends Game {

	RegeneratingSpleef() {
		super("Regenerating Spleef",
				new String[] {
						"Regenerating Spleef is very similar to the",
						"classic spleef game. One twist: the blocks",
						"you break regenerate after 2 seconds, and",
						"the arena is pretty small (usually)."
		}, 2, SpleefMap.MAPS);
	}

	private static final int DURATION = 30;

	private List<UUID> dead;
	private List<UUID> all;

	private SpleefMap map;

	@Override
	void begin(final GameMap genericMap) {
		this.dead = new ArrayList<>();

		this.map = (SpleefMap) genericMap;

		Utils.setGameRule("doTileDrops", false);

		this.map.fill();

		for (final Player player: Bukkit.getOnlinePlayers()){
			player.teleport(this.map.getStartLocation());
		}

		new GameTimer(this, DURATION, 4) {

			@Override
			public void onStart() {
				final ItemStack shovel = new ItemBuilder(Material.DIAMOND_SHOVEL)
						.name("Spleefanator 8000")
						.enchant(Enchantment.DIG_SPEED, 5)
						.unbreakable()
						.canDestroy("snow_block")
						.create();

				Bukkit.getOnlinePlayers().forEach((player) -> player.getInventory().setItem(0, shovel));

				all = Utils.getOnlinePlayersUuidList();
			}

			@Override
			public int gameTimer(final int secondsLeft) {
				if (Utils.getAliveAcountFromDeadAndAllList(RegeneratingSpleef.this.dead, RegeneratingSpleef.this.all) < 2 && secondsLeft > 2) {
					return 2;
				}

				return secondsLeft;
			}

			@Override
			public void onEnd() {
				RegeneratingSpleef.this.endGame(Utils.getWinnersFromDeadAndAllList(RegeneratingSpleef.this.dead, RegeneratingSpleef.this.all, false));
				dead.clear();
				all.clear();
			}

		};
	}

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event) {
		final Block block = event.getBlock();
		if (block.getType() == Material.SNOW_BLOCK){
			block.setType(Material.AIR); // XXX check if this is needed
			Scheduler.delay(3*20, () -> block.setType(Material.SNOW_BLOCK));
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		final Player player = event.getPlayer();
		if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK){
			player.teleport(this.map.getSpectatorLocation());
			player.getInventory().clear();
			this.dead.add(player.getUniqueId());
		}
	}

}
