package derkades.minigames.games.teamsbowbattle;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.games.GameLabel;
import derkades.minigames.games.GameTeam;
import derkades.minigames.games.RedBlueTeamGame;
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
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.EnumSet;

public class TeamsBowBattle extends RedBlueTeamGame<TeamsBowBattleMap> {

	public TeamsBowBattle() {
		super(
				"teams_bow_battle",
				"Teams Bow Battle",
				new String[] {
						"A bow battle. In teams."
				},
				Material.BOW,
				new TeamsBowBattleMap[] {
						new Forest(),
				},
				4,
				120,
				EnumSet.of(GameLabel.MULTIPLAYER, GameLabel.PLAYER_COMBAT, GameLabel.TEAMS)
		);
	}

//	private Set<UUID> dead;

	@Override
	public void onPreStart() {
//		this.dead = new HashSet<>();

		super.splitPlayers((player, team) -> player.teleport(getSpawnLocation(team)));
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
	public void onEnd() {
		if (this.getTeams().getMemberCount(GameTeam.BLUE, true) == 0) {
			// blue is dead so team red wins
			TeamsBowBattle.super.endGame(this.getTeams().getMembers(GameTeam.RED, false));
		} else if (this.getTeams().getMemberCount(GameTeam.RED, true) == 0) {
			// red is dead so team blue wins
			TeamsBowBattle.super.endGame(this.getTeams().getMembers(GameTeam.BLUE, false));
		} else {
			// both teams are still alive
			TeamsBowBattle.super.endGame();
		}

//		this.dead = null;
	}

	@EventHandler(ignoreCancelled = true)
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

		if (this.getTeams().isTeamMember(damager, GameTeam.BLUE) && this.getTeams().isTeamMember(player, GameTeam.RED)) {
			// blue attacks red -> allow
		} else if (this.getTeams().isTeamMember(damager, GameTeam.RED) && this.getTeams().isTeamMember(player, GameTeam.BLUE)) {
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
		this.sendMessage(Utils.getDeathMessage(event));

		MPlayer player = new MPlayer(event);
//		this.dead.add(player.getUniqueId());
		this.getTeams().setDead(player);
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

		final GameTeam team = this.getTeams().getTeam(player);

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
		final GameTeam team = this.getTeams().getTeam(player);
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
