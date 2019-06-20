package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.platform.PlatformMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Utils;

public class Platform extends Game<PlatformMap> {

	@Override
	public String getName() {
		return "Platform";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Knock other players off a platform.",
				"Use your knockback swords wisely, you",
				"only get two knockback swords (one at",
				"the start, one at " + KNOCKBACK_SWORDS_TIME + " seconds.",
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public PlatformMap[] getGameMaps() {
		return PlatformMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 40;
	}

	private static final int KNOCKBACK_SWORDS_TIME = 20;

	private List<UUID> dead;
	private List<UUID> all;

	private boolean started;

	@Override
	public void onPreStart() {
		this.dead = new ArrayList<>();
		this.all = new ArrayList<>();

		this.started = false;

		for (final MPlayer player : Minigames.getOnlinePlayers()){
			player.queueTeleport(this.map.getSpawnLocation());
		}
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.setDisableDamage(false);
			player.giveInfiniteEffect(PotionEffectType.DAMAGE_RESISTANCE, 255);
			Platform.this.all.add(player.getUniqueId());
		}

		Platform.this.started = true;
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (secondsLeft == 20) {
			Platform.this.giveSwords();
		}

		if (Utils.getAliveAcountFromDeadAndAllList(Platform.this.dead, Platform.this.all) <= 1 && secondsLeft > 5) {
			return 5;
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		Platform.this.endGame(Utils.getWinnersFromDeadAndAllList(Platform.this.dead, Platform.this.all, false));
		Platform.this.dead.clear();
		Platform.this.all.clear();
	}

	private void giveSwords(){
		final ItemStack sword = new ItemBuilder(Material.WOODEN_SWORD)
				.damage(59)
				.name(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Knockback Sword")
				.lore(ChatColor.AQUA + "You can only use this once!")
				.enchant(Enchantment.KNOCKBACK, 1)
				.create();

		for (final Player player: Bukkit.getOnlinePlayers()){
			if (!this.dead.contains(player.getUniqueId())){ //Don't give sword to spectators
				player.getInventory().setItem(0, sword);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		final MPlayer player = new MPlayer(event);

		if (this.dead.contains(player.getUniqueId())) {
			return;
		}

		if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.RED_TERRACOTTA)){
			// Die

			// Put player back if game hasn't started yet
			if (!this.started) {
				player.teleport(this.map.getSpawnLocation());
				return;
			}

			this.dead.add(player.getUniqueId());
			this.sendMessage(player.getName() + " died");
			player.dieUp(10);
			this.map.getWorld().spigot().strikeLightningEffect(player.getLocation(), false);
		}
	}

}
