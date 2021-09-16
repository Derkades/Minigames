package derkades.minigames.games.teamsbowbattle;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

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
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public class TeamsBowBattle extends Game<TeamsBowBattleMap> {

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

	private Set<UUID> dead;
	private Map<GameTeam, Set<UUID>> teamMembers;

	@Override
	public void onPreStart() {
		this.dead = new HashSet<>();
		this.teamMembers = new EnumMap<>(GameTeam.class);
		this.teamMembers.put(GameTeam.RED, new HashSet<>());
		this.teamMembers.put(GameTeam.BLUE, new HashSet<>());

		boolean teamBool = false;

		for (final MPlayer player : Minigames.getOnlinePlayersInRandomOrder()) {
			final GameTeam team = teamBool ? GameTeam.RED : GameTeam.BLUE;
			this.teamMembers.get(team).add(player.getUniqueId());
			player.sendTitle(Component.empty(),
					Component.text("You are in the ", NamedTextColor.GRAY)
					.append(Component.text(team.getDisplayName(), team.getTextColor()).decorate(TextDecoration.BOLD))
					.append(Component.text(".", NamedTextColor.GRAY))
					);

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
		return TeamsBowBattle.this.getNumPlayersLeftInTeam(GameTeam.RED) == 0 || TeamsBowBattle.this.getNumPlayersLeftInTeam(GameTeam.BLUE) == 0;
	}

	@Override
	public void onEnd() {
		if (getNumPlayersLeftInTeam(GameTeam.BLUE) == 0) {
			// blue is dead so team red wins
			TeamsBowBattle.super.endGame(this.teamMembers.get(GameTeam.RED));
		} else if (getNumPlayersLeftInTeam(GameTeam.RED) == 0) {
			// red is dead so team blue wins
			TeamsBowBattle.super.endGame(this.teamMembers.get(GameTeam.BLUE));
		} else {
			// both teams are still alive
			TeamsBowBattle.super.endGame();
		}

		this.dead = null;
		this.teamMembers = null;
	}

	@EventHandler
	public void damage(final MinigamesPlayerDamageEvent event){
		final MPlayer player = event.getPlayer();

		final GameTeam playerTeam = getTeam(player);

		if (event.willBeDead()) {
			event.setCancelled(true);
			if (event.getType().equals(DamageType.ENTITY)) {
				final MPlayer killer = event.getDamagerPlayer();
				final GameTeam killerTeam = getTeam(killer);
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

		if (this.teamMembers.get(GameTeam.BLUE).contains(shooter.getUniqueId()) && this.teamMembers.get(GameTeam.RED).contains(player.getUniqueId())) {
			// blue attacks red -> allow
		} else if (this.teamMembers.get(GameTeam.RED).contains(shooter.getUniqueId()) && this.teamMembers.get(GameTeam.BLUE).contains(player.getUniqueId())) {
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

	private int getNumPlayersLeftInTeam(final GameTeam team) {
		return (int) Minigames.getOnlinePlayers().stream()
				.map(MPlayer::getUniqueId)
				.filter(Predicate.not(this.dead::contains))
				.filter(this.teamMembers.get(team)::contains)
				.count();
	}

	private GameTeam getTeam(final MPlayer player) {
		if (this.teamMembers.get(GameTeam.RED).contains(player.getUniqueId())) {
			return GameTeam.RED;
		} else if (this.teamMembers.get(GameTeam.BLUE).contains(player.getUniqueId())) {
			return GameTeam.BLUE;
		}
		return null;
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

		final GameTeam team = getTeam(player);

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
		final GameTeam team = getTeam(player);
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
