package derkades.minigames.games.teamsbowbattle;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameTeam;
import derkades.minigames.games.TeamGame;
import derkades.minigames.games.TeamManager;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MPlayerDamageEvent;
import derkades.minigames.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeamsBowBattle extends Game<TeamsBowBattleMap> implements TeamGame {

	private static final TeamsBowBattleMap[] MAPS = {
			new Forest(),
	};

	@Override
	public @NotNull String getIdentifier() {
		return "teams_bow_battle";
	}

	@Override
	public @NotNull String getName() {
		return "Teams Bow Battle";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"A bow battle. In teams."
		};
	}

	@Override
	public @NotNull Material getMaterial() {
		return Material.BOW;
	}

	@Override
	public int getRequiredPlayers() {
		return 4;
	}

	@Override
	public TeamsBowBattleMap[] getGameMaps() {
		return MAPS;
	}

	@Override
	public int getDuration() {
		return 120;
	}

	@Override
	public TeamManager getTeams() {
		return this.teams;
	}

	private Set<UUID> dead;
	private TeamManager teams;

	@Override
	public void onPreStart() {
		this.dead = new HashSet<>();
		this.teams = new TeamManager(Set.of(GameTeam.RED, GameTeam.BLUE));

		boolean teamBool = false;

		for (final MPlayer player : Minigames.getOnlinePlayersInRandomOrder()) {
			final GameTeam team = teamBool ? GameTeam.RED : GameTeam.BLUE;
			this.teams.setTeam(player, team, true);
			player.queueTeleport(getSpawnLocation(team));

			teamBool = !teamBool;
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
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return this.teams.anyEmptyTeam() != null;
	}

	@Override
	public void onEnd() {
		if (this.teams.getMemberCount(GameTeam.BLUE) == 0) {
			// blue is dead so team red wins
			TeamsBowBattle.super.endGame(this.teams.getMembers(GameTeam.RED));
		} else if (this.teams.getMemberCount(GameTeam.RED) == 0) {
			// red is dead so team blue wins
			TeamsBowBattle.super.endGame(this.teams.getMembers(GameTeam.BLUE));
		} else {
			// both teams are still alive
			TeamsBowBattle.super.endGame();
		}

		this.dead = null;
		this.teams = null;
	}

	@EventHandler
	public void onDamage(MPlayerDamageEvent event) {
		MPlayer damager = event.getDamagerPlayer();

		if (damager == null) {
			// don't modify non-player damage sources like fall damage
			return;
		}

		// Cancel damage if a player directly hits another player
		if (event.getDirectDamagerEntity() instanceof Player) {
			event.setCancelled(true);
			return;
		}

		MPlayer player = event.getPlayer();

		if (this.teams.isTeamMember(damager, GameTeam.BLUE) && this.teams.isTeamMember(player, GameTeam.RED)) {
			// blue attacks red -> allow
		} else if (this.teams.isTeamMember(damager, GameTeam.RED) && this.teams.isTeamMember(player, GameTeam.BLUE)) {
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
		event.setCancelled(true);
		this.sendMessage(Utils.getTeamsDeathMessage(event, teams));

		MPlayer player = new MPlayer(event);
		this.dead.add(player.getUniqueId());
		player.clearInventory();
		player.dieUp(2);
	}

	private Location getSpawnLocation(final GameTeam team) {
		switch(team) {
			case RED -> {
				return this.map.getTeamRedSpawnLocation();
			}
			case BLUE -> {
				return this.map.getTeamBlueSpawnLocation();
			}
			default -> throw new IllegalArgumentException("Unexpected team " + team + ", only supports red and blue");
		}
	}

	private void giveItems(final MPlayer player) {
		player.clearInventory();

		final GameTeam team = this.teams.getTeam(player);

		if (team == null) {
			Logger.warning("Not giving items to %s, unknown team", player.getOriginalName());
			return;
		}

		team.equipArmor(player);

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
		final GameTeam team = this.teams.getTeam(player);
		if (team == null) {
			player.dieTo(this.map.getTeamBlueSpawnLocation());
			return;
		}

		player.teleport(getSpawnLocation(team));
		giveItems(player);
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {}

}
