package derkades.minigames.games.molepvp;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.games.GameLabel;
import derkades.minigames.games.GameTeam;
import derkades.minigames.games.RedBlueTeamGame;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MPlayerDamageEvent;
import derkades.minigames.utils.PaperItemBuilder;
import derkades.minigames.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MolePvP extends RedBlueTeamGame<MolePvPMap> {

	public MolePvP() {
		super(
				"mole_pvp",
				"Mole PvP",
				new String[]{
						"Underground PvP in teams.",
				},
				Material.DIRT,
				new MolePvPMap[]{
						new Prototype(),
				},
				4,
				100,
				EnumSet.of(GameLabel.MULTIPLAYER, GameLabel.BLOCKS, GameLabel.PLAYER_COMBAT, GameLabel.TEAM)
		);
	}

	private Set<UUID> dead;

	@Override
	public void onPreStart() {
		this.dead = new HashSet<>();

		this.map.setUpMap();

		super.splitPlayers((player, team) ->
				player.queueTeleport(team == GameTeam.RED ? this.map.getTeamRedSpawnLocation() : this.map.getTeamBlueSpawnLocation()));
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
	public void onEnd() {
		if (this.getTeams().getMemberCount(GameTeam.BLUE, true) == 0) {
			// blue is dead so team red wins
			super.endGame(this.getTeams().getMembers(GameTeam.RED, false));
		} else if (this.getTeams().getMemberCount(GameTeam.RED, true) == 0) {
			// red is dead so team blue wins
			MolePvP.super.endGame(this.getTeams().getMembers(GameTeam.BLUE, false));
		} else {
			// both teams are still alive
			MolePvP.super.endGame();
		}
		this.dead = null;
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setCancelled(true);
		MPlayer player = new MPlayer(event);
		this.sendMessage(Utils.getDeathMessage(event));

		this.dead.add(player.getUniqueId());
		player.die();
	}

	@EventHandler
	public void onDamage(final MPlayerDamageEvent event) {
		final MPlayer player = event.getPlayer();
		MPlayer damager = event.getDamagerPlayer();

		if (damager != null) {
			if (this.getTeams().isTeamMember(damager, GameTeam.BLUE) && this.getTeams().isTeamMember(player, GameTeam.RED)) {
				// blue attacks red -> allow
			} else if (this.getTeams().isTeamMember(damager, GameTeam.RED) && this.getTeams().isTeamMember(player, GameTeam.BLUE)) {
				// red attacks blue -> allow
			} else {
				event.setCancelled(true);
			}
		}
	}

	private void giveItems(final MPlayer player) {
		GameTeam team = this.getTeams().getTeam(player);

		if (team == null){
			Logger.warning("Skipped giving items to player %s, their team is null", player.getOriginalName());
			return;
		}

		team.equipArmor(player);

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
