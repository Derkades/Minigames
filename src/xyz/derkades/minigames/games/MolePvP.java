package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.molepvp.MolePvPMap;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class MolePvP extends Game {

	MolePvP() {
		super("MolePvP", new String[] {
				"Underground PvP",
				"Hiding won't work, go kill others because",
				"if both teams are alive at the end no one wins."
		}, 4, MolePvPMap.MAPS);
	}

	private MolePvPMap map;

	private List<UUID> dead;
	private List<UUID> teamRed;
	private List<UUID> teamBlue;

	@Override
	void begin(final GameMap genericMap) {
		this.map = (MolePvPMap) genericMap;

		this.dead = new ArrayList<>();
		this.teamRed = new ArrayList<>();
		this.teamBlue = new ArrayList<>();

		this.map.setupMap();

		boolean team = false;

		final List<Player> players = new ArrayList<>();

		Bukkit.getOnlinePlayers().forEach((player) -> players.add(player));

		Collections.shuffle(players);

		for (final Player player : players) {
			if (team) {
				Utils.sendTitle(player, "", String.format("%sYou are in the %s%sRED%s team", ChatColor.GRAY, ChatColor.RED, ChatColor.BOLD, ChatColor.GRAY));
				this.teamRed.add(player.getUniqueId());
				player.teleport(this.map.getTeamRedSpawnLocation());
			} else {
				Utils.sendTitle(player, "", String.format("%sYou are in the %s%sBLUE%s team", ChatColor.GRAY, ChatColor.BLUE, ChatColor.BOLD, ChatColor.GRAY));
				this.teamBlue.add(player.getUniqueId());
				player.teleport(this.map.getTeamBlueSpawnLocation());
			}

			team = !team;
		}

		new GameTimer(this, 100, 5) {

			@Override
			public void onStart() {
				Utils.setGameRule("doTileDrops", false);

				Bukkit.getOnlinePlayers().forEach((player) -> {
					MolePvP.this.giveItems(player);
					Minigames.setCanTakeDamage(player, true);
				});
			}

			@Override
			public int gameTimer(final int secondsLeft) {
				if ((MolePvP.this.getNumPlayersLeftInBlueTeam() == 0 || MolePvP.this.getNumPlayersLeftInRedTeam() == 0) && secondsLeft > 1) {
					return 1;
				}

				return secondsLeft;
			}

			@Override
			public void onEnd() {
				if (MolePvP.this.getNumPlayersLeftInBlueTeam() == 0) {
					// blue is dead so team red wins
					MolePvP.super.endGame(Utils.getPlayerListFromUUIDList(MolePvP.this.teamRed));
				} else if (MolePvP.this.getNumPlayersLeftInRedTeam() == 0) {
					// red is dead so team blue wins
					MolePvP.super.endGame(Utils.getPlayerListFromUUIDList(MolePvP.this.teamBlue));
				} else {
					// both teams are still alive
					MolePvP.super.endGame(new ArrayList<>());
				}
			}

		};
	}

	@EventHandler
	public void onDamage(final EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() != EntityType.PLAYER ||
				event.getDamager().getType() != EntityType.PLAYER) {
			return;
		}

		final Player damager = (Player) event.getDamager();
		final Player damaged = (Player) event.getEntity();

		if (this.teamBlue.contains(damager.getUniqueId()) && this.teamRed.contains(damaged.getUniqueId())) {
			// blue attacks red -> allow
		} else if (this.teamRed.contains(damager.getUniqueId()) && this.teamBlue.contains(damaged.getUniqueId())) {
			// red attacks blue -> allow
		} else {
			/*
			 * block in other situations, such as
			 * red attacks red
			 * blue attacks blue
			 * spectator attacks red/blue
			 * red/blue attacks spectator
			 */
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		final Player player = event.getEntity();

		final Player killer = player.getKiller();
		if (killer == null) {
			event.setDeathMessage(String.format("%s%s%s %has taken their own life.",
					this.getTeamColor(player), ChatColor.BOLD, player.getName(), ChatColor.GRAY));
			return;
		}

		this.dead.add(player.getUniqueId());

		event.setDeathMessage(String.format("%s%s%s %shas been killed by %s%s%s",
				this.getTeamColor(player), ChatColor.BOLD, player.getName(), ChatColor.GRAY,
				this.getTeamColor(killer), ChatColor.BOLD, killer.getName()));

		Scheduler.delay(1, () -> {
			player.spigot().respawn();
			Utils.hideForEveryoneElse(player);
			Minigames.setCanTakeDamage(player, false);
			Utils.clearInventory(player);
			player.setAllowFlight(true);
			player.teleport(Var.NO_SPECTATOR_LOCATION);
		});
	}

	private int getNumPlayersLeftInRedTeam() {
		int players = 0;
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (this.teamRed.contains(player.getUniqueId()) && !this.dead.contains(player.getUniqueId())) {
				players++;
			}
		};
		return players;
	}

	private int getNumPlayersLeftInBlueTeam() {
		int players = 0;
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (this.teamBlue.contains(player.getUniqueId()) && !this.dead.contains(player.getUniqueId())) {
				players++;
			}
		};
		return players;
	}

	private ChatColor getTeamColor(final Player player) {
		if (this.teamRed.contains(player.getUniqueId())) {
			return ChatColor.RED;
		} else if (this.teamBlue.contains(player.getUniqueId())) {
			return ChatColor.BLUE;
		} else {
			return ChatColor.GREEN;
		}
	}

	private void giveItems(final Player player) {
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

		Utils.setArmor(player, helmet.create(), chestplate.create(), leggings.create(), boots.create());

		final ItemStack sword = new ItemBuilder(Material.IRON_SWORD)
				.enchant(Enchantment.KNOCKBACK, 1)
				.enchant(Enchantment.DAMAGE_ALL, 1)
				.unbreakable()
				.create();

		final ItemStack shovel = new ItemBuilder(Material.DIAMOND_SHOVEL)
				.enchant(Enchantment.DIG_SPEED, 5)
				.canDestroy("dirt")
				.unbreakable()
				.create();

		player.getInventory().addItem(sword, shovel);
	}

}
