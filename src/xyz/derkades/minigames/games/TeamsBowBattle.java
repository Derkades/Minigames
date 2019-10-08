package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.teamsbowbattle.TeamsBowBattleMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;

public class TeamsBowBattle extends Game<TeamsBowBattleMap> {

	@Override
	public String getName() {
		return "Teams Bow Battle";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"A bow battle. In teams."
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
			TeamsBowBattle.this.giveItems(player);
			player.setDisableDamage(false);
		});
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if ((TeamsBowBattle.this.getNumPlayersLeftInBlueTeam() == 0 || TeamsBowBattle.this.getNumPlayersLeftInRedTeam() == 0) && secondsLeft > 1)
			return 1;

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		if (TeamsBowBattle.this.getNumPlayersLeftInBlueTeam() == 0) {
			// blue is dead so team red wins
			TeamsBowBattle.super.endGame(TeamsBowBattle.this.teamRed);
		} else if (TeamsBowBattle.this.getNumPlayersLeftInRedTeam() == 0) {
			// red is dead so team blue wins
			TeamsBowBattle.super.endGame(TeamsBowBattle.this.teamBlue);
		} else {
			// both teams are still alive
			TeamsBowBattle.super.endGame(new ArrayList<>());
		}

		this.dead = null;
		this.teamRed = null;
		this.teamBlue = null;

	}

	@EventHandler
	public void damage(final MinigamesPlayerDamageEvent event){
		final MPlayer player = event.getPlayer();

		if (event.willBeDead()) {
			event.setCancelled(true);
			if (event.getType().equals(DamageType.ENTITY)) {
				final MPlayer killer = event.getDamagerPlayer();
				sendMessage(String.format("%s%s%s %shas been killed by %s%s%s",
						getTeamColor(player), ChatColor.BOLD, player.getName(), ChatColor.GRAY,
						getTeamColor(killer), ChatColor.BOLD, killer.getName()));
			} else {
				sendMessage(String.format("%s%s%s %has died.",
						getTeamColor(player), ChatColor.BOLD, player.getName(), ChatColor.GRAY));

			}

			this.dead.add(player.getUniqueId());
			player.clearInventory();
			player.dieUp(2);
			return;
		}

		if (event.getType().equals(DamageType.SELF))
			return;

		// Cancel damage if a player directly hits another player
		if (event.getDamagerEntity() instanceof Player) {
			event.setCancelled(true);
			return;
		}

		final MPlayer shooter = event.getDamagerPlayer();

		if (this.teamBlue.contains(shooter.getUniqueId()) && this.teamRed.contains(player.getUniqueId())) {
			// blue attacks red -> allow
		} else if (this.teamRed.contains(shooter.getUniqueId()) && this.teamBlue.contains(player.getUniqueId())) {
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

//	@EventHandler
//	public void join(final MinigamesJoinEvent event) {
//		event.setTeleportPlayerToLobby(false);
//		final MPlayer player = event.getPlayer();
//		if (!this.dead.contains(player.getUniqueId())) {
//			this.dead.add(player.getUniqueId());
//			player.die();
//		}
//		if (this.teamBlue.contains(player.getUniqueId())) {
//			player.teleport(this.map.getTeamBlueSpawnLocation());
//		} else if (this.teamRed.contains(player.getUniqueId())) {
//			player.teleport(this.map.getTeamRedSpawnLocation());
//		} else {
//			player.teleport(this.map.getTeamRedSpawnLocation());
//		}
//	}

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
		if (this.teamRed.contains(player.getUniqueId()))
			return ChatColor.RED;
		else if (this.teamBlue.contains(player.getUniqueId()))
			return ChatColor.BLUE;
		else
			return ChatColor.GREEN;
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

		final ItemStack bow = new ItemBuilder(Material.BOW)
				.enchant(Enchantment.ARROW_DAMAGE, 3)
				.enchant(Enchantment.ARROW_INFINITE, 1)
				.unbreakable()
				.create();

		final ItemStack arrow = new ItemBuilder(Material.ARROW)
				.create();

		player.getInventory().addItem(bow, arrow);
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		if (!this.dead.contains(player.getUniqueId())) {
			this.dead.add(player.getUniqueId());
			player.die();
		}
		if (this.teamBlue.contains(player.getUniqueId())) {
			player.teleport(this.map.getTeamBlueSpawnLocation());
		} else if (this.teamRed.contains(player.getUniqueId())) {
			player.teleport(this.map.getTeamRedSpawnLocation());
		} else {
			player.teleport(this.map.getTeamRedSpawnLocation());
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
	}


}
