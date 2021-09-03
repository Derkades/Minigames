package derkades.minigames.games;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import derkades.minigames.Minigames;
import derkades.minigames.games.molepvp.MolePvPMap;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public class MolePvP extends Game<MolePvPMap> {

	@Override
	public String getIdentifier() {
		return "mole_pvp";
	}

	@Override
	public String getName() {
		return "Mole PvP";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Underground PvP in teams.",
		};
	}

	@Override
	public Material getMaterial() {
		return Material.DIRT;
	}

	@Override
	public int getRequiredPlayers() {
		return 4;
	}

	@Override
	public MolePvPMap[] getGameMaps() {
		return MolePvPMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 100;
	}

	private Set<UUID> dead;
	private Set<UUID> teamRed;
	private Set<UUID> teamBlue;

	@Override
	public void onPreStart() {
		this.dead = new HashSet<>();
		this.teamRed = new HashSet<>();
		this.teamBlue = new HashSet<>();

		this.map.setupMap();

		boolean team = false;

		for (final MPlayer player : Minigames.getOnlinePlayersInRandomOrder()) {
			if (team) {
				player.sendTitle("", String.format("%sYou are in the %s%sRED%s team", ChatColor.GRAY, ChatColor.RED, ChatColor.BOLD, ChatColor.GRAY));
				this.teamRed.add(player.getUniqueId());
				player.queueTeleport(this.map.getTeamRedSpawnLocation());
			} else {
				player.sendTitle("", String.format("%sYou are in the %s%sBLUE%s team", ChatColor.GRAY, ChatColor.BLUE, ChatColor.BOLD, ChatColor.GRAY));
				this.teamBlue.add(player.getUniqueId());
				player.queueTeleport(this.map.getTeamBlueSpawnLocation());
			}

			team = !team;
		}

	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach((player) -> {
			MolePvP.this.giveItems(player);
			player.setDisableDamage(false);
		});
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if ((MolePvP.this.getNumPlayersLeftInBlueTeam() == 0 || MolePvP.this.getNumPlayersLeftInRedTeam() == 0) && secondsLeft > 5) {
			return 5;
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		if (MolePvP.this.getNumPlayersLeftInBlueTeam() == 0) {
			// blue is dead so team red wins
			MolePvP.super.endGame(MolePvP.this.teamRed);
		} else if (MolePvP.this.getNumPlayersLeftInRedTeam() == 0) {
			// red is dead so team blue wins
			MolePvP.super.endGame(MolePvP.this.teamBlue);
		} else {
			// both teams are still alive
			MolePvP.super.endGame();
		}
		this.dead = null;
		this.teamRed = null;
		this.teamBlue = null;
	}

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		final MPlayer player = event.getPlayer();

		if (event.getType().equals(DamageType.ENTITY)) {
			final MPlayer damager = player;
			final MPlayer damaged = event.getDamagerPlayer();

			if (this.teamBlue.contains(damager.getUniqueId()) && this.teamRed.contains(damaged.getUniqueId())) {
				// blue attacks red -> allow
			} else if (this.teamRed.contains(damager.getUniqueId()) && this.teamBlue.contains(damaged.getUniqueId())) {
				// red attacks blue -> allow
			} else {
				event.setCancelled(true);
			}
		}

		if (event.willBeDead()) {
			event.setCancelled(true);
			if (event.getType().equals(DamageType.ENTITY)) {
				final MPlayer killer = event.getDamagerPlayer();
				this.sendMessage(String.format("%s%s%s %shas been killed by %s%s%s",
						this.getTeamColor(player), ChatColor.BOLD, player.getName(), ChatColor.GRAY,
						this.getTeamColor(killer), ChatColor.BOLD, killer.getName()));
			} else {
				this.sendMessage(String.format("%s%s%s %has died.",
						this.getTeamColor(player), ChatColor.BOLD, player.getName(), ChatColor.GRAY));
			}

			this.dead.add(player.getUniqueId());
			player.die();
		}
	}

	private int getNumPlayersLeftInRedTeam() {
		int players = 0;
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (this.teamRed.contains(player.getUniqueId()) && !this.dead.contains(player.getUniqueId())) {
				players++;
			}
		}
		return players;
	}

	private int getNumPlayersLeftInBlueTeam() {
		int players = 0;
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (this.teamBlue.contains(player.getUniqueId()) && !this.dead.contains(player.getUniqueId())) {
				players++;
			}
		}
		return players;
	}

	private ChatColor getTeamColor(final MPlayer player) {
		if (this.teamRed.contains(player.getUniqueId())) {
			return ChatColor.RED;
		} else if (this.teamBlue.contains(player.getUniqueId())) {
			return ChatColor.BLUE;
		} else {
			return ChatColor.GREEN;
		}
	}

	private void giveItems(final MPlayer player) {
		final ItemBuilder helmet = new ItemBuilder(Material.LEATHER_HELMET);
		final ItemBuilder chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE);
		final ItemBuilder leggings = new ItemBuilder(Material.LEATHER_LEGGINGS);
		final ItemBuilder boots = new ItemBuilder(Material.LEATHER_BOOTS);

		if (this.teamRed.contains(player.getUniqueId())) {
			helmet.leatherArmorColor(Color.RED);
			chestplate.leatherArmorColor(Color.RED);
			leggings.leatherArmorColor(Color.RED);
			boots.leatherArmorColor(Color.RED);
		}

		if (this.teamBlue.contains(player.getUniqueId())) {
			helmet.leatherArmorColor(Color.BLUE);
			chestplate.leatherArmorColor(Color.BLUE);
			leggings.leatherArmorColor(Color.BLUE);
			boots.leatherArmorColor(Color.BLUE);
		}

		player.setArmor(helmet.create(), chestplate.create(), leggings.create(), boots.create());

		final ItemStack sword = new ItemBuilder(Material.IRON_SWORD)
				.enchant(Enchantment.KNOCKBACK, 1)
				.enchant(Enchantment.DAMAGE_ALL, 1)
				.unbreakable()
				.create();

		final ItemStack shovel = new ItemBuilder(Material.DIAMOND_SHOVEL)
				.enchant(Enchantment.DIG_SPEED, 5)
				.canDestroy("minecraft:dirt")
//				.canDestroy(Material.DIRT)
				.unbreakable()
				.create();

		player.giveItem(sword, shovel);
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.spectator();
		player.teleport(this.map.getWorld().getSpawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {

	}

}
