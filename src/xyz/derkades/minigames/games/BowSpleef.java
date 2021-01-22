package xyz.derkades.minigames.games;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.bowspleef.BowSpleefMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Utils;

public class BowSpleef extends Game<BowSpleefMap> {

	private static final ItemStack BOW = new ItemBuilder(Material.BOW)
			.enchant(Enchantment.ARROW_INFINITE, 1)
			.enchant(Enchantment.ARROW_FIRE, 1)
			.unbreakable()
			.create();

	@Override
	public String getIdentifier() {
		return "bow_spleef";
	}
	
	@Override
	public String getName() {
		return "Bow Spleef";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Use your bow to ignite tnt under players",
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public BowSpleefMap[] getGameMaps() {
		return BowSpleefMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 100;
	}

	private List<UUID> alive;

	@Override
	public void onPreStart() {
		this.alive = Utils.getOnlinePlayersUuidList();

		this.map.restoreLayers();

		Minigames.getOnlinePlayers().forEach((p) -> {
			p.queueTeleport(this.map.getSpawnLocation());
			p.giveItem(BOW);
		});
	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach((p) -> {
			p.giveItem(new ItemStack(Material.ARROW, 1));

			if (p.getLocation().getY() < this.map.getLayerCenter().getY()) {
				p.teleport(this.map.getSpawnLocation());
			}
		});
	}

	@Override
	public int gameTimer(int secondsLeft) {
		Minigames.getOnlinePlayers().stream()
			.filter((p) -> this.alive.contains(p.getUniqueId()))
			.filter((p) -> p.getLocation().getY() < this.map.getLayerCenter().getY() - 20)
			.forEach((p) -> {
				p.clearInventory();
				p.dieTo(this.map.getSpawnLocation());
				this.alive.remove(p.getUniqueId());
				sendMessage(p.getName() + " died");
			});

		if (this.alive.size() < 2 && secondsLeft > 2) {
			secondsLeft = 2;
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		endGame(this.alive, true);
		this.alive = null;
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		if (this.started) {
			player.spectator();
		} else {
			this.alive.add(player.getUniqueId());
		}

		player.teleport(this.map.getSpawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

	@EventHandler
	public void onHit(final ProjectileHitEvent event) {
		if (event.getEntityType() == EntityType.ARROW &&
				event.getHitEntity() == null &&
				event.getHitBlock().getType() == Material.TNT) {
			final Block block = event.getHitBlock();

			Arrays.asList(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH).forEach((f) -> {
				final Block relative = block.getRelative(f);
				if (relative.getType() == Material.TNT) {
					relative.setType(Material.AIR);
					relative.getWorld().spawnEntity(relative.getLocation().add(0.5, 0, 0.5), EntityType.PRIMED_TNT);
				}
			});
		}
	}

}
