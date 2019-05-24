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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.teamsbowbattle.TeamsBowBattleMap;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class TeamsBowBattle extends Game {

	TeamsBowBattle() {
		super("TeamsBowBattle", new String[] {
				"pvp. with bows. in teams."
		}, 4, 3, 4, TeamsBowBattleMap.MAPS);
	}
	
	private TeamsBowBattleMap map;
	
	private List<UUID> dead;
	private List<UUID> teamRed;
	private List<UUID> teamBlue;
	
	@Override
	void begin(GameMap genericMap) {
		map = (TeamsBowBattleMap) genericMap;
		
		dead = new ArrayList<>();
		teamRed = new ArrayList<>();
		teamBlue = new ArrayList<>();
		
		boolean team = false;
		
		List<Player> players = new ArrayList<>();
		
		Bukkit.getOnlinePlayers().forEach((player) -> players.add(player));
		
		Collections.shuffle(players);

		for (Player player : players) {
			if (team) {
				Utils.sendTitle(player, "", String.format("%sYou are in the %s%sRED%s team", ChatColor.GRAY, ChatColor.RED, ChatColor.BOLD, ChatColor.GRAY));
				teamRed.add(player.getUniqueId());
				//player.teleport(map.getTeamRedSpawnLocation());
			} else {
				Utils.sendTitle(player, "", String.format("%sYou are in the %s%sBLUE%s team", ChatColor.GRAY, ChatColor.BLUE, ChatColor.BOLD, ChatColor.GRAY));
				teamBlue.add(player.getUniqueId());
				//player.teleport(map.getTeamBlueSpawnLocation());
			}
			
			team = !team;
		}
		
		Utils.delayedTeleport(map.getTeamRedSpawnLocation(), Utils.getPlayerListFromUUIDList(teamRed));
		
		Scheduler.delay(10, () -> Utils.delayedTeleport(map.getTeamBlueSpawnLocation(), Utils.getPlayerListFromUUIDList(teamBlue)));
		
		new GameTimer(this, 60, 5) {

			@Override
			public void onStart() {				
				Bukkit.getOnlinePlayers().forEach((player) -> {
					giveItems(player);
					Minigames.setCanTakeDamage(player, true);
				});
			}
			
			@Override
			public int gameTimer(final int secondsLeft) {
				if ((getNumPlayersLeftInBlueTeam() == 0 || getNumPlayersLeftInRedTeam() == 0) && secondsLeft > 1) {
					return 1;
				}
				
				return secondsLeft;
			}

			@Override
			public void onEnd() {
				if (getNumPlayersLeftInBlueTeam() == 0) {
					// blue is dead so team red wins
					TeamsBowBattle.super.startNextGame(Utils.getPlayerListFromUUIDList(teamRed));
				} else if (getNumPlayersLeftInRedTeam() == 0) {
					// red is dead so team blue wins
					TeamsBowBattle.super.startNextGame(Utils.getPlayerListFromUUIDList(teamBlue));
				} else {
					// both teams are still alive
					TeamsBowBattle.super.startNextGame(new ArrayList<>());
				}
			}

		};
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() != EntityType.PLAYER ||
				event.getDamager().getType() != EntityType.PLAYER) {
			return;
		}
		
		Player damager = (Player) event.getDamager();
		Player damaged = (Player) event.getEntity();
		
		if (damaged.getLastDamageCause().getCause() != DamageCause.PROJECTILE) {
			System.out.println("not an arrow");
			event.setCancelled(true);
			return;
		}
		
		if (teamBlue.contains(damager.getUniqueId()) && teamRed.contains(damaged.getUniqueId())) {
			// blue attacks red -> allow
		} else if (teamRed.contains(damager.getUniqueId()) && teamBlue.contains(damaged.getUniqueId())) {
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
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		
		Player killer = player.getKiller();
		if (killer == null) {
			event.setDeathMessage(String.format("%s%s%s %has taken their own life.", 
					getTeamColor(player), ChatColor.BOLD, player.getName(), ChatColor.GRAY));
			return;
		}
		
		dead.add(player.getUniqueId());
		
		event.setDeathMessage(String.format("%s%s%s %shas been killed by %s%s%s", 
				getTeamColor(player), ChatColor.BOLD, player.getName(), ChatColor.GRAY,
				getTeamColor(killer), ChatColor.BOLD, killer.getName()));
		
		Scheduler.delay(1, () -> {
			player.spigot().respawn();
			Utils.hideForEveryoneElse(player);
			Minigames.setCanTakeDamage(player, false);
			Utils.clearInventory(player);
			player.setAllowFlight(true);
			player.teleport(Var.NO_SPECATOR_LOCATION);
		});
	}
	
	private int getNumPlayersLeftInRedTeam() {
		int players = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (teamRed.contains(player.getUniqueId()) && !dead.contains(player.getUniqueId())) {
				players++;
			}
		};
		return players;
	}
	
	private int getNumPlayersLeftInBlueTeam() {
		int players = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (teamBlue.contains(player.getUniqueId()) && !dead.contains(player.getUniqueId())) {
				players++;
			}
		};
		return players;
	}
	
	private ChatColor getTeamColor(Player player) {
		if (teamRed.contains(player.getUniqueId())) {
			return ChatColor.RED;
		} else if (teamBlue.contains(player.getUniqueId())) {
			return ChatColor.BLUE;
		} else {
			return ChatColor.GREEN;
		}
	}
	
	private void giveItems(Player player) {
		ItemBuilder helmet = new ItemBuilder(Material.LEATHER_HELMET);
		ItemBuilder chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE);
		ItemBuilder leggings = new ItemBuilder(Material.LEATHER_LEGGINGS);
		ItemBuilder boots = new ItemBuilder(Material.LEATHER_BOOTS);
		
		if (teamRed.contains(player.getUniqueId())) {
			helmet.leatherArmorColor(Color.RED);
			chestplate.leatherArmorColor(Color.RED);
			leggings.leatherArmorColor(Color.RED);
			boots.leatherArmorColor(Color.RED);
		}
		
		if (teamBlue.contains(player.getUniqueId())) {
			helmet.leatherArmorColor(Color.BLUE);
			chestplate.leatherArmorColor(Color.BLUE);
			leggings.leatherArmorColor(Color.BLUE);
			boots.leatherArmorColor(Color.BLUE);
		}
		
		Utils.setArmor(player, helmet.create(), chestplate.create(), leggings.create(), boots.create());
		
		ItemStack bow = new ItemBuilder(Material.BOW)
				.enchant(Enchantment.ARROW_DAMAGE, 3)
				.enchant(Enchantment.ARROW_INFINITE, 1)
				.unbreakable()
				.create();
		
		ItemStack arrow = new ItemBuilder(Material.ARROW)
				.create();
		
		player.getInventory().addItem(bow, arrow);				
	}

}
