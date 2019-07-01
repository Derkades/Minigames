package xyz.derkades.minigames.games;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.Powerable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.mysterymurder.MurderyMisterMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import xyz.derkades.minigames.utils.Utils;

public class MurderyMister extends Game<MurderyMisterMap> {

	@Override
	public String getName() {
		return "Murdery Mister";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Description",
		}; // TODO Description
	}

	@Override
	public int getRequiredPlayers() {
		return 0;
	}

	@Override
	public MurderyMisterMap[] getGameMaps() {
		return null;
	}

	@Override
	public int getDuration() {
		return 200;
	}

	private UUID murderer;
	private List<UUID> alive; // All alive players, except murderer

	@Override
	public void onPreStart() {
		this.murderer = null;
		this.alive = Utils.getOnlinePlayersUuidList();

		final Location[] spawnLocations = this.map.getSpawnLocations();
		int index = 0;

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (index < 1) {
				index = spawnLocations.length - 1;
			}

			player.teleport(spawnLocations[index]);
			index--;
		}
	}

	@Override
	public void onStart() {
		if (Bukkit.getOnlinePlayers().size() < 1) {
			return; // Just in case everyone leaves, so the code below doesn't crash
		}

		final MPlayer murderer = Minigames.getOnlinePlayersInRandomOrder().get(0);
		murderer.sendTitle("", ChatColor.RED + "You are the murderer!");
		murderer.giveItem(new ItemBuilder(Material.TRIDENT)
				.name(ChatColor.GOLD + "Knife")
				.lore(ChatColor.YELLOW + "Stab people to kill them, ",
						ChatColor.YELLOW + "right click to throw")
				.enchant(Enchantment.LOYALTY, 1)
				.create());
		murderer.getInventory().setHeldItemSlot(6);
		this.murderer = murderer.getUniqueId();

		this.alive.remove(this.murderer);

		final List<MPlayer> all = Minigames.getOnlinePlayersInRandomOrder();
		all.removeIf((p) -> p.getUniqueId().equals(this.murderer));

		final MPlayer sheriff = all.remove(0);
		sheriff.giveItem(new ItemBuilder(Material.BOW).unbreakable().create());
	}

	@Override
	public int gameTimer(int secondsLeft) {
		if (Utils.getWinnersFromAliveList(this.alive, true).size() < 1 && secondsLeft > 5) {
			secondsLeft = 5;
		}

		for (final Location location : this.map.getCandles()) {
			location.setY(location.getY() + 1.15);
			location.getWorld().spawnParticle(Particle.FLAME, location, 0, 0, 0, 0.001, 2);
		}

		if (secondsLeft % 2 == 0) {
			final Powerable powerable = (Powerable) ListUtils.getRandomValueFromArray(this.map.getFlickeringRedstomeLamps()).getBlock().getBlockData();
			powerable.setPowered(!powerable.isPowered());
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		super.endGame(Utils.getWinnersFromAliveList(this.alive, true));
	}

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		if (event.getType().equals(DamageType.SELF)) {
			event.setCancelled(true);
		}

		if (event.willBeDead()) {
			event.setCancelled(true);

			final MPlayer player = event.getPlayer();

			this.sendMessage(player.getName() + " has been killed");
			Minigames.getOnlinePlayers().forEach((p) -> p.playSound(Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.0f));
			this.alive.remove(player.getUniqueId());
			player.clearInventory();

			if (player.getUniqueId().equals(this.murderer)) {
				// Murder is dead, all alive players win (except murderer)
				super.endGame(Utils.getWinnersFromAliveList(this.alive, true));
			} else if (player.getInventory().contains(Material.BOW)) {
				// Sheriff is dead, drop bow
				player.getLocation().getWorld().dropItemNaturally(player.getLocation(), new ItemBuilder(Material.BOW).unbreakable().create());
				player.die();
			} else {
				// Innocent is dead
				player.die();
			}
		}
	}

}
