package derkades.minigames.games.creeperattack;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.MaterialLists;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CreeperAttack extends Game<CreeperAttackMap> {

	private static final CreeperAttackMap[] MAPS = {
			new DeckedOutForest(),
			new Mineshaft(),
			new Hedges(),
	};

	@Override
	public @NotNull String getIdentifier() {
		return "creeper_attack";
	}

	@Override
	public @NotNull String getName() {
		return "Creeper Attack";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Avoid dying from creeper explosions. Use your",
				"knockback stick to defend yourself against creepers."
		};
	}

	@Override
	public @NotNull Material getMaterial() {
		return Material.CREEPER_HEAD;
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public CreeperAttackMap[] getGameMaps() {
		return MAPS;
	}

	@Override
	public int getDuration() {
		return 60;
	}

	private Set<UUID> alive;

	private int numberOfCreepers = 2;

	@Override
	public void onPreStart() {
		this.alive = new HashSet<>();
		this.numberOfCreepers = 1;

		final ItemStack knockbackStick = new ItemBuilder(Material.STICK)
				.name(ChatColor.GOLD + "" + ChatColor.BOLD + "Creeper Smasher")
				.create();

		knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);

		Minigames.getOnlinePlayers().forEach((player) -> {
			this.alive.add(player.getUniqueId());
			player.giveItem(knockbackStick);
			player.setDisableDamage(false);
			player.queueTeleport(this.map.getSpawnLocation());
		});
	}

	@Override
	public void onStart() {}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (secondsLeft % 10 == 0){
			this.numberOfCreepers++;
		}

		for (int i = 0; i < this.numberOfCreepers; i++) {
			if (this.alive.size() > 1) {
				spawnCreeper();
			}
		}

		return secondsLeft;
	}

	private boolean checkSpawnable(final Block block) {
		return block.isSolid() &&
				!MaterialLists.LEAVES.contains(block.getType()) &&
				block.getType() != Material.BARRIER &&
				block.getRelative(BlockFace.UP).getType() == Material.AIR &&
				block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == Material.AIR;
	}

	private void spawnCreeper() {
		attemptLoop:
		for (int attempts = 0; attempts < 10; attempts++) {
			final int x = ThreadLocalRandom.current().nextInt(this.map.getSpawnBoundsMin().getBlockX(), this.map.getSpawnBoundsMax().getBlockX() + 1);
			final int z = ThreadLocalRandom.current().nextInt(this.map.getSpawnBoundsMin().getBlockZ(), this.map.getSpawnBoundsMax().getBlockZ() + 1);

			for (int y = this.map.getSpawnBoundsMax().getBlockY(); y >= this.map.getSpawnBoundsMin().getBlockY(); y--) {
				final Block block = this.map.getWorld().getBlockAt(x, y, z);
				if (checkSpawnable(block)) {
					final Location loc = block.getLocation();
					loc.add(.5, 1, .5);
					final Creeper creeper = CreeperAttack.this.map.getWorld().spawn(loc, Creeper.class);
					creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.5);
					creeper.setTarget(Bukkit.getPlayer(ListUtils.choice(CreeperAttack.this.alive)));
					break attemptLoop;
				}
			}
		}
	}

	@Override
	public boolean endEarly() {
		return this.alive.size() < 2;
	}

	@Override
	public void onEnd() {
		for (final Creeper creeper : CreeperAttack.this.map.getWorld().getEntitiesByClass(Creeper.class)) {
			creeper.remove();
		}

		this.endGame(this.alive, true);

		this.alive = null;
	}

	@EventHandler
	public void onAttack(final EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() != EntityType.CREEPER) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		event.setCancelled(true);
		final MPlayer player = new MPlayer(event);
		this.alive.remove(player.getUniqueId());
		player.die();
		sendPlainMessage(player.getName() + " has been blown up by a creeper");
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.die();
		player.teleport(this.map.getSpawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

}
