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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
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

	private static final String SECONDS_LEFT = "%s seconds left.";

	private List<UUID> dead;

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

		Scheduler.delay(3*20, () -> {
			this.sendMessage("The game has started!");

			final ItemStack shovel = new ItemBuilder(Material.DIAMOND_SHOVEL)
					.name("Spleefanator 8000")
					.enchant(Enchantment.DIG_SPEED, 5)
					.unbreakable()
					.canDestroy("snow_block")
					.create();

			Bukkit.getOnlinePlayers().forEach((player) -> player.getInventory().setItem(0, shovel));
		});

		new BukkitRunnable() {

			int secondsLeft = DURATION;

			@Override
			public void run() {
				if (Utils.getAliveCountFromDeadList(RegeneratingSpleef.this.dead) <= 1 && this.secondsLeft > 2) {
					this.secondsLeft = 2;
				}

				if (this.secondsLeft <= 0) {
					this.cancel();

					//End game
					Utils.setGameRule("doTileDrops", true);
					RegeneratingSpleef.this.endGame(Utils.getWinnersFromDeadList(RegeneratingSpleef.this.dead));

					return;
				}

				if (this.secondsLeft == 30 || this.secondsLeft <= 5) {
					RegeneratingSpleef.this.sendMessage(String.format(SECONDS_LEFT, this.secondsLeft));
				}

				this.secondsLeft--;
			}

		}.runTaskTimer(Minigames.getInstance(), 0, 1*20);
	}

	@EventHandler
	public void spleefBlock(final BlockBreakEvent event) {
		final Player player = event.getPlayer();
		final PlayerInventory inv = player.getInventory();
		if (inv.getItemInMainHand().getType() == Material.DIAMOND_SHOVEL){
			final Block block = event.getBlock();
			if (block.getType() == Material.SNOW_BLOCK){
				block.setType(Material.AIR);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Minigames.getInstance(), () -> block.setType(Material.SNOW_BLOCK), 3*20);
			}
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
