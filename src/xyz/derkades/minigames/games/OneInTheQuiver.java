package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.sniper.SniperMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import xyz.derkades.minigames.utils.Utils;

public class OneInTheQuiver extends Game<SniperMap> {

	private static final ItemStack SWORD = new ItemBuilder(Material.WOODEN_SWORD)
			.unbreakable()
			.create();

	private static final ItemStack BOW = new ItemBuilder(Material.BOW)
			.unbreakable()
			.name("Insta-kill bow")
			.lore("Get arrows by killing other players")
			.create();

	private static final ItemStack ARROW = new ItemBuilder(Material.ARROW)
			.create();

	@Override
	public String getName() {
		return "One in the Quiver";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Kill other players with a weak wooden sword.",
				"For every kill you'll get a single arrow. Arrows",
				"do enough damage to kill any player instantly."
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 3;
	}

	@Override
	public SniperMap[] getGameMaps() {
		return SniperMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 120;
	}

	private List<UUID> dead;
	private List<UUID> all;

	@Override
	public void onPreStart() {
		this.dead = new ArrayList<>();
		this.all = Utils.getOnlinePlayersUuidList();

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(this.map.getSpawnLocation());
			player.giveEffect(PotionEffectType.INVISIBILITY, 5*20, 0);
		}
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.setDisableSneaking(true);
			player.setDisableDamage(false);
			player.giveItem(SWORD, BOW, ARROW);
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (Utils.getAliveAcountFromDeadAndAllList(OneInTheQuiver.this.dead, OneInTheQuiver.this.all) < 2 && secondsLeft > 2) {
			return 2;
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		final List<Player> winners = Utils.getWinnersFromDeadAndAllList(OneInTheQuiver.this.dead, OneInTheQuiver.this.all, false);

		OneInTheQuiver.super.endGame(winners);

		OneInTheQuiver.this.dead.clear();
		OneInTheQuiver.this.all.clear();
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event){
		final Player player = event.getPlayer();

		final Material below = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();

		if (below == Material.HAY_BLOCK) {
			final PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, 30, 7, true, false);
			player.addPotionEffect(jump);
		}

		if (player.getGameMode().equals(GameMode.SPECTATOR) &&
				(player.getLocation().getX() > 500 || player.getLocation().getX() < -500 ||
						player.getLocation().getZ() > 500 || player.getLocation().getZ() < -500)) {
			player.teleport(this.map.getSpawnLocation());
		}
	}

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		final MPlayer player = event.getPlayer();

		if (this.dead.contains(event.getDamagerEntity().getUniqueId()) || this.dead.contains(event.getDamagerEntity().getUniqueId())) {
			event.setCancelled(true);
			return;
		}

		if (event.getDamagerEntity().getType().equals(EntityType.ARROW)){
			event.setDamage(20);
		}

		if (event.getType().equals(DamageType.ENTITY)) {
			final MPlayer attacker = event.getDamagerPlayer();
			final ItemStack weapon = attacker.getInventory().getItemInMainHand();

			if (weapon.isSimilar(SWORD)) {
				event.setDamage(3);
			}
		}

		if (event.willBeDead()) {
			event.setCancelled(true);

			if (event.getType().equals(DamageType.ENTITY)) {
				final MPlayer killer = event.getDamagerPlayer();

				final int playersLeft = Utils.getAliveAcountFromDeadAndAllList(this.dead, this.all);
				this.sendMessage(String.format("%s has been killed by %s. There are %s players left.",
						player.getName(), killer.getName(), playersLeft));

				killer.getInventory().addItem(ARROW);
			} else {
				final int playersLeft = Utils.getAliveAcountFromDeadAndAllList(this.dead, this.all);
				this.sendMessage(String.format("%s has died. There are %s players left.",
						player.getName(), playersLeft));
			}

			this.dead.add(player.getUniqueId());
			player.dieUp(2);
		}

	}

}
