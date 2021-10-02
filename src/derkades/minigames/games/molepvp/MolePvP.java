package derkades.minigames.games.molepvp;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameTeam;
import derkades.minigames.games.TeamGame;
import derkades.minigames.games.TeamManager;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import derkades.minigames.utils.PaperItemBuilder;
import derkades.minigames.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MolePvP extends Game<MolePvPMap> implements TeamGame {

	private static final MolePvPMap[] MAPS = {
			new Prototype(),
	};

	@Override
	public @NotNull String getIdentifier() {
		return "mole_pvp";
	}

	@Override
	public @NotNull String getName() {
		return "Mole PvP";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Underground PvP in teams.",
		};
	}

	@Override
	public @NotNull Material getMaterial() {
		return Material.DIRT;
	}

	@Override
	public int getRequiredPlayers() {
		return 4;
	}

	@Override
	public MolePvPMap[] getGameMaps() {
		return MAPS;
	}

	@Override
	public int getDuration() {
		return 100;
	}

	private Set<UUID> dead;
	private TeamManager teams;

	@Override
	public void onPreStart() {
		this.dead = new HashSet<>();
		this.teams = new TeamManager(Set.of(GameTeam.RED, GameTeam.BLUE));

		this.map.setUpMap();

		boolean teamBool = false;

		for (final MPlayer player : Minigames.getOnlinePlayersInRandomOrder()) {
			final GameTeam team = teamBool ? GameTeam.RED : GameTeam.BLUE;
			player.queueTeleport(teamBool ? this.map.getTeamRedSpawnLocation() : this.map.getTeamBlueSpawnLocation());
			this.teams.setTeam(player, team, true);
			teamBool = !teamBool;
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
			super.endGame(this.teams.getMembers(GameTeam.RED));
		} else if (this.teams.getMemberCount(GameTeam.RED) == 0) {
			// red is dead so team blue wins
			MolePvP.super.endGame(this.teams.getMembers(GameTeam.BLUE));
		} else {
			// both teams are still alive
			MolePvP.super.endGame();
		}
		this.dead = null;
		this.teams = null;
	}

	@Override
	public TeamManager getTeams() {
		return this.teams;
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setCancelled(true);
		MPlayer player = new MPlayer(event);
		MPlayer killer = Utils.getKiller(event);
		if (killer != null) {
			this.sendFormattedPlainMessage("%s%s%s %shas been killed by %s%s%s",
					this.teams.getTeam(player).getChatColor(), ChatColor.BOLD, player.getName(), ChatColor.GRAY,
					this.teams.getTeam(killer).getChatColor(), ChatColor.BOLD, killer.getName());
		} else {
			this.sendFormattedPlainMessage("%s%s%s %has died.",
					this.teams.getTeam(player).getChatColor(), ChatColor.BOLD, player.getName(), ChatColor.GRAY);
		}

		this.dead.add(player.getUniqueId());
		player.die();
	}

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		final MPlayer player = event.getPlayer();

		if (event.getType().equals(DamageType.ENTITY)) {
			final MPlayer damager = event.getDamagerPlayer();

			if (this.teams.isTeamMember(damager, GameTeam.BLUE) && this.teams.isTeamMember(player, GameTeam.RED)) {
				// blue attacks red -> allow
			} else if (this.teams.isTeamMember(damager, GameTeam.RED) && this.teams.isTeamMember(player, GameTeam.BLUE)) {
				// red attacks blue -> allow
			} else {
				event.setCancelled(true);
			}
		}

		if (event.willBeDead()) {

		}
	}

	private void giveItems(final MPlayer player) {
		final ItemBuilder helmet = new ItemBuilder(Material.LEATHER_HELMET);
		final ItemBuilder chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE);
		final ItemBuilder leggings = new ItemBuilder(Material.LEATHER_LEGGINGS);
		final ItemBuilder boots = new ItemBuilder(Material.LEATHER_BOOTS);

		if (this.teams.isTeamMember(player, GameTeam.RED)) {
			helmet.leatherArmorColor(Color.RED);
			chestplate.leatherArmorColor(Color.RED);
			leggings.leatherArmorColor(Color.RED);
			boots.leatherArmorColor(Color.RED);
		}

		if (this.teams.isTeamMember(player, GameTeam.BLUE)) {
			helmet.leatherArmorColor(Color.BLUE);
			chestplate.leatherArmorColor(Color.BLUE);
			leggings.leatherArmorColor(Color.BLUE);
			boots.leatherArmorColor(Color.BLUE);
		}

		player.setArmor(helmet.create(), chestplate.create(), leggings.create(), boots.create());

		final ItemStack sword = new ItemBuilder(Material.IRON_SWORD)
				.enchant(Enchantment.KNOCKBACK)
				.enchant(Enchantment.DAMAGE_ALL)
				.unbreakable()
				.create();

		final ItemStack shovel = new PaperItemBuilder(Material.DIAMOND_SHOVEL)
				.enchant(Enchantment.DIG_SPEED, 5)
				.canDestroyMinecraft("dirt")
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
