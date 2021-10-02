package derkades.minigames.games.bowspleef;

import derkades.minigames.GameState;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BowSpleef extends Game<BowSpleefMap> {

	private static final ItemStack BOW = new ItemBuilder(Material.BOW)
			.enchant(Enchantment.ARROW_INFINITE, 1)
			.enchant(Enchantment.ARROW_FIRE, 1)
			.unbreakable()
			.create();

	public BowSpleef() {
		super(
				"bow_spleef",
				"Bow Spleef",
				new String[] {
						"Use your bow to ignite tnt under players",
				},
				Material.BOW,
				new BowSpleefMap[] {
						new BowSpleefMapOriginal(),
				},
				2,
				100,
				EnumSet.of(GameLabel.PARKOUR, GameLabel.MULTIPLAYER, GameLabel.PLAYER_COMBAT)
		);
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
				this.sendMessage(p.getDisplayName().append(Component.text(" has died.", NamedTextColor.GRAY)));
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
