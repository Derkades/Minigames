package derkades.minigames.games.bowspleef;

import derkades.minigames.GameState;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BowSpleef extends Game<BowSpleefMap> {

	private static final BowSpleefMap[] MAPS = {
			new BowSpleefMapOriginal(),
	};

	private static final ItemStack BOW = new ItemBuilder(Material.BOW)
			.enchant(Enchantment.ARROW_INFINITE, 1)
			.enchant(Enchantment.ARROW_FIRE, 1)
			.unbreakable()
			.create();

	@Override
	public @NotNull String getIdentifier() {
		return "bow_spleef";
	}

	@Override
	public @NotNull String getName() {
		return "Bow Spleef";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Use your bow to ignite tnt under players",
		};
	}

	@Override
	public @NotNull Material getMaterial() {
		return Material.BOW;
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public BowSpleefMap[] getGameMaps() {
		return MAPS;
	}

	@Override
	public int getDuration() {
		return 100;
	}

	private Set<UUID> alive;

	@Override
	public void onPreStart() {
		this.alive = Utils.getOnlinePlayersUuidSet();

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
	public int gameTimer(final int secondsLeft) {
		Minigames.getOnlinePlayers().stream()
			.filter((p) -> this.alive.contains(p.getUniqueId()))
			.filter((p) -> p.getLocation().getY() < this.map.getLayerCenter().getY() - 20)
			.forEach((p) -> {
				p.clearInventory();
				p.dieTo(this.map.getSpawnLocation());
				this.alive.remove(p.getUniqueId());
				sendPlainMessage(p.getName() + " died");
			});

		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return this.alive.size() < 2;
	}

	@Override
	public void onEnd() {
		endGame(this.alive, true);
		this.alive = null;
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		if (GameState.getCurrentState().gameIsRunning()) {
			player.spectator();
		} else {
			// If the game hasn't started yet, the player can still participate
			this.alive.add(player.getUniqueId());
		}

		player.queueTeleportNoFadeIn(this.map.getSpawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

	private static final List<BlockFace> FACES = List.of(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);

	@EventHandler
	public void onHit(final ProjectileHitEvent event) {
		final Block block = event.getHitBlock();

		if (event.getEntityType() == EntityType.ARROW &&
				event.getHitEntity() == null &&
				block != null &&
				block.getType() == Material.TNT) {

			FACES.forEach((f) -> {
				final Block relative = block.getRelative(f);
				if (relative.getType() == Material.TNT) {
					relative.setType(Material.AIR);
					relative.getWorld().spawnEntity(relative.getLocation().add(0.5, 0, 0.5), EntityType.PRIMED_TNT);
				}
			});
		}
	}

}
