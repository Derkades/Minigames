package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.teamsbowbattle.TeamsBowBattleMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class TeamsBowBattle extends Game<TeamsBowBattleMap> {

	@Override
	public String getName() {
		return "Teams Bow Battle";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"pvp. with bows. in teams."
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 4;
	}

	@Override
	public TeamsBowBattleMap[] getGameMaps() {
		return TeamsBowBattleMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 120;
	}

	private List<UUID> dead;
	private List<UUID> teamRed;
	private List<UUID> teamBlue;

	@Override
	public void onPreStart() {
		this.dead = new ArrayList<>();
		this.teamRed = new ArrayList<>();
		this.teamBlue = new ArrayList<>();

		boolean team = false;

		final List<Player> players = new ArrayList<>();

		Bukkit.getOnlinePlayers().forEach((player) -> players.add(player));

		Collections.shuffle(players);


		for (final Player player : players) {
			if (team) {
				Utils.sendTitle(player, "", String.format("%sYou are in the %s%sRED%s team", ChatColor.GRAY, ChatColor.RED, ChatColor.BOLD, ChatColor.GRAY));
				this.teamRed.add(player.getUniqueId());
				//player.teleport(map.getTeamRedSpawnLocation());
			} else {
				Utils.sendTitle(player, "", String.format("%sYou are in the %s%sBLUE%s team", ChatColor.GRAY, ChatColor.BLUE, ChatColor.BOLD, ChatColor.GRAY));
				this.teamBlue.add(player.getUniqueId());
				//player.teleport(map.getTeamBlueSpawnLocation());
			}

			team = !team;
		}

		Utils.delayedTeleport(this.map.getTeamRedSpawnLocation(), Utils.getPlayerListFromUUIDList(this.teamRed));

		Scheduler.delay(10, () -> Utils.delayedTeleport(this.map.getTeamBlueSpawnLocation(), Utils.getPlayerListFromUUIDList(this.teamBlue)));

	}

	@Override
	public void onStart() {
		Bukkit.getOnlinePlayers().forEach((player) -> {
			TeamsBowBattle.this.giveItems(player);
			new MPlayer(player).setDisableDamage(false);
		});
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if ((TeamsBowBattle.this.getNumPlayersLeftInBlueTeam() == 0 || TeamsBowBattle.this.getNumPlayersLeftInRedTeam() == 0) && secondsLeft > 1) {
			return 1;
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		if (TeamsBowBattle.this.getNumPlayersLeftInBlueTeam() == 0) {
			// blue is dead so team red wins
			TeamsBowBattle.super.endGame(Utils.getPlayerListFromUUIDList(TeamsBowBattle.this.teamRed));
		} else if (TeamsBowBattle.this.getNumPlayersLeftInRedTeam() == 0) {
			// red is dead so team blue wins
			TeamsBowBattle.super.endGame(Utils.getPlayerListFromUUIDList(TeamsBowBattle.this.teamBlue));
		} else {
			// both teams are still alive
			TeamsBowBattle.super.endGame(new ArrayList<>());
		}

	}

	@EventHandler
	public void onDamage(final EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() != EntityType.PLAYER) {
			return;
		}

		final Entity damager = event.getDamager();

		final Player damaged = (Player) event.getEntity();

		if (damaged.getType() != EntityType.PLAYER) {
			return;
		}

		if (damager.getType() == EntityType.PLAYER) {
			event.setCancelled(true);
			return;
		}

		if (damager.getType() != EntityType.ARROW) {
			return;
		}

		final Arrow arrow = (Arrow) damager;

		if (!(arrow.getShooter() instanceof Player)) {
			return;
		}

		final Player shooter = (Player) arrow.getShooter();

		if (this.teamBlue.contains(shooter.getUniqueId()) && this.teamRed.contains(damaged.getUniqueId())) {
			// blue attacks red -> allow
		} else if (this.teamRed.contains(shooter.getUniqueId()) && this.teamBlue.contains(damaged.getUniqueId())) {
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
			event.setDeathMessage(String.format("%s%s%s %has died.",
					this.getTeamColor(player), ChatColor.BOLD, player.getName(), ChatColor.GRAY));
		}

		/*if (killer.getUniqueId().equals(player.getUniqueId())) {
			event.setDeathMessage(String.format("%s%s%s %has taken their own life.",
					getTeamColor(player), ChatColor.BOLD, player.getName(), ChatColor.GRAY));
			return;
		}*/

		this.dead.add(player.getUniqueId());

		event.setDeathMessage(String.format("%s%s%s %shas been killed by %s%s%s",
				this.getTeamColor(player), ChatColor.BOLD, player.getName(), ChatColor.GRAY,
				this.getTeamColor(killer), ChatColor.BOLD, killer.getName()));

		Scheduler.delay(1, () -> {
			player.spigot().respawn();
			Utils.hideForEveryoneElse(player);
			new MPlayer(player).setDisableDamage(true);
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

		final ItemStack bow = new ItemBuilder(Material.BOW)
				.enchant(Enchantment.ARROW_DAMAGE, 3)
				.enchant(Enchantment.ARROW_INFINITE, 1)
				.unbreakable()
				.create();

		final ItemStack arrow = new ItemBuilder(Material.ARROW)
				.create();

		player.getInventory().addItem(bow, arrow);
	}


}
