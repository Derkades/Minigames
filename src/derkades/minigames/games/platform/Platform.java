package derkades.minigames.games.platform;

import derkades.minigames.GameState;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MPlayerDamageEvent;
import derkades.minigames.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class Platform extends Game<PlatformMap> {

	private static final int KNOCKBACK_SWORDS_TIME = 20;

	public Platform() {
		super(
				"platform",
				"Platform",
				new String[] {
						"Knock other players off a platform.",
						"Use your knockback swords wisely, you",
						"only get two knockback swords (one at",
						"the start, one at " + KNOCKBACK_SWORDS_TIME + " seconds.",
				},
				Material.SMOOTH_STONE_SLAB,
				new PlatformMap[] {
						new Desert(),
						new Ice(),
				},
				2,
				40,
				EnumSet.of(GameLabel.MULTIPLAYER, GameLabel.PLAYER_COMBAT, GameLabel.NO_TEAMS)
		);
	}

	private Set<UUID> alive;

	@Override
	public void onPreStart() {
		this.alive = Utils.getOnlinePlayersUuidSet();

		for (final MPlayer player : Minigames.getOnlinePlayers()){
			player.queueTeleport(this.map.getSpawnLocation());
		}
	}

	@Override
	public void onStart() {}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (secondsLeft == 20) {
			Platform.this.giveSwords();
		}

		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return this.alive.size() < 2;
	}

	@Override
	public void onEnd() {
		Platform.this.endGame(this.alive, true);
		this.alive = null;
	}

	private void giveSwords(){
		final ItemStack sword = new ItemBuilder(Material.WOODEN_SWORD)
				.damage(59)
				.name(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Knockback Sword")
				.lore(ChatColor.AQUA + "You can only use this once!")
				.enchant(Enchantment.KNOCKBACK, 1)
				.create();

		for (final Player player: Bukkit.getOnlinePlayers()){
			if (this.alive.contains(player.getUniqueId())){ //Don't give sword to spectators
				player.getInventory().setItem(0, sword);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		final MPlayer player = new MPlayer(event);

		if (!this.alive.contains(player.getUniqueId())) {
			return;
		}

		if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.RED_TERRACOTTA)){
			// Die

			// Put player back if game hasn't started yet
			if (!GameState.currentGameIsRunning()) {
				player.teleport(this.map.getSpawnLocation());
				return;
			}

			if (this.alive.size() <= 1) {
				return;
			}

			this.alive.remove(player.getUniqueId());
			this.sendMessage(player.getDisplayName().append(Component.text(" has died.", NamedTextColor.GRAY)));
			player.dieUp(10);
			this.map.getWorld().spigot().strikeLightningEffect(player.getLocation(), false);
		}
	}

	@EventHandler
	public void onDamage(MPlayerDamageEvent event) {
		event.setCancelled(false);
		event.setDamage(0);
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.dieTo(this.map.getSpawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

}
