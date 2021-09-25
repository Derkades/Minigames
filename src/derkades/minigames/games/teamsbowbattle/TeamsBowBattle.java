package derkades.minigames.games.teamsbowbattle;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameTeam;
import derkades.minigames.games.TeamGame;
import derkades.minigames.games.TeamManager;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public class TeamsBowBattle extends Game<TeamsBowBattleMap> implements TeamGame {

	@Override
	public String getIdentifier() {
		return "teams_bow_battle";
	}

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
	public Material getMaterial() {
		return Material.BOW;
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
	public void damage(final MinigamesPlayerDamageEvent event){
		final MPlayer player = event.getPlayer();

		final GameTeam playerTeam = this.teams.getTeam(player);

		if (event.willBeDead()) {
			event.setCancelled(true);
			if (event.getType().equals(DamageType.ENTITY)) {
				final MPlayer killer = event.getDamagerPlayer();
				final GameTeam killerTeam = this.teams.getTeam(killer);
				sendMessage(Component.empty()
						.append(Component.text(player.getName(), playerTeam.getTextColor()).decorate(TextDecoration.BOLD))
						.append(Component.text(" has been killed by ", NamedTextColor.GRAY))
						.append(Component.text(killer.getName(), killerTeam.getTextColor()).decorate(TextDecoration.BOLD))
						.append(Component.text(".", NamedTextColor.GRAY))
						);
			} else {
				sendMessage(Component.empty()
						.append(Component.text(player.getName(), playerTeam.getTextColor()).decorate(TextDecoration.BOLD))
						.append(Component.text(" has died.", NamedTextColor.GRAY))
						);
			}

			this.dead.add(player.getUniqueId());
			player.clearInventory();
			player.dieUp(2);
			return;
		}

		if (event.getType().equals(DamageType.SELF)) {
			return;
		}

		// Cancel damage if a player directly hits another player
		if (event.getDamagerEntity() instanceof Player) {
			event.setCancelled(true);
			return;
		}

		final MPlayer shooter = event.getDamagerPlayer();

		if (this.teams.isTeamMember(shooter, GameTeam.BLUE) && this.teams.isTeamMember(player, GameTeam.RED)) {
			// blue attacks red -> allow
		} else if (this.teams.isTeamMember(shooter, GameTeam.RED) && this.teams.isTeamMember(player, GameTeam.BLUE)) {
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

	private Location getSpawnLocation(final GameTeam team) {
		switch(team) {
			case RED -> {
				return this.map.getTeamRedSpawnLocation();
			}
			case BLUE -> {
				return this.map.getTeamBlueSpawnLocation();
			}
			default -> {
				throw new IllegalArgumentException("Unexpected team " + team + ", only supports red and blue");
			}
		}
	}

	private void giveItems(final MPlayer player) {
		player.clearInventory();

		final GameTeam team = this.teams.getTeam(player);

		if (team == null) {
			Logger.warning("Not giving items to %s, unknown team", player.getName());
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
