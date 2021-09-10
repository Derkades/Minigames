package derkades.minigames.games.teamsbowbattle;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.StandardTextColor;

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
	private Set<UUID> teamRed;
	private Set<UUID> teamBlue;

	@Override
	public void onPreStart() {
		this.dead = new HashSet<>();
		this.teamRed = new HashSet<>();
		this.teamBlue = new HashSet<>();

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
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return TeamsBowBattle.this.getNumPlayersLeftInBlueTeam() == 0 || TeamsBowBattle.this.getNumPlayersLeftInRedTeam() == 0;
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
			TeamsBowBattle.super.endGame();
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
				sendMessage(Component.empty()
						.append(Component.text(player.getName(), TextColor.color(getTeamColor(player).getColor().getRGB())).decorate(TextDecoration.BOLD))
						.append(Component.text(" has been killed by ", StandardTextColor.GRAY))
						.append(Component.text(killer.getName(), TextColor.color(getTeamColor(player).getColor().getRGB())).decorate(TextDecoration.BOLD))
						.append(Component.text(".", StandardTextColor.GRAY))
						);
			} else {
				sendMessage(Component.empty()
						.append(Component.text(player.getName(), TextColor.color(getTeamColor(player).getColor().getRGB())).decorate(TextDecoration.BOLD))
						.append(Component.text(" has died.", StandardTextColor.GRAY))
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

	private int getNumPlayersLeftInRedTeam() {
		return (int) Minigames.getOnlinePlayers().stream()
				.map(MPlayer::getUniqueId)
				.filter(Predicate.not(this.dead::contains))
				.filter(this.teamRed::contains)
				.count();
	}

	private int getNumPlayersLeftInBlueTeam() {
		return (int) Minigames.getOnlinePlayers().stream()
				.map(MPlayer::getUniqueId)
				.filter(Predicate.not(this.dead::contains))
				.filter(this.teamBlue::contains)
				.count();
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
		player.clearInventory();

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
		if (this.teamBlue.contains(player.getUniqueId())) {
			player.teleport(this.map.getTeamBlueSpawnLocation());
			giveItems(player);
		} else if (this.teamRed.contains(player.getUniqueId())) {
			player.teleport(this.map.getTeamRedSpawnLocation());
			giveItems(player);
		} else {
			player.dieTo(this.map.getTeamBlueSpawnLocation());
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {}

}
