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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.platform.PlatformMap;
import xyz.derkades.minigames.utils.Utils;

public class Platform extends Game {

	Platform() {
		super("Platform", new String[] {
				"Knock other players off a platform.",
				"Use your knockback swords wisely, you",
				"only get two knockback swords (one at",
				"the start, one at " + KNOCKBACK_SWORDS_TIME + " seconds.",
		}, 2, PlatformMap.MAPS);
	}

	private static final int SPREAD_TIME = 3;
	private static final int GAME_DURATION = 40;
	private static final int KNOCKBACK_SWORDS_TIME = 20;

	private List<UUID> dead;
	private List<UUID> all;

	private PlatformMap map;

	@Override
	public void begin(final GameMap genericMap){
		this.dead = new ArrayList<>();
		this.all = new ArrayList<>();

		this.map = (PlatformMap) genericMap;

		for (final Player player : Bukkit.getOnlinePlayers()){
			player.teleport(this.map.spawnLocation());
			Utils.giveInfiniteEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 255);
		}

		new GameTimer(this, GAME_DURATION, SPREAD_TIME) {

			@Override
			public void onStart() {
				for (final Player player : Bukkit.getOnlinePlayers()) {
					Minigames.setCanTakeDamage(player, true);
					Platform.this.all.add(player.getUniqueId());
				}
			}

			@Override
			public int gameTimer(final int secondsLeft) {
				if (secondsLeft == 20) {
					Platform.this.giveSwords();
				}

				if (Utils.getAliveAcountFromDeadAndAllList(Platform.this.dead, Platform.this.all) <= 1 && secondsLeft > 2) {
					return 2;
				}

				return secondsLeft;
			}

			@Override
			public void onEnd() {
				Platform.this.endGame(Utils.getWinnersFromDeadList(Platform.this.dead));
			}

		};
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

	private void playerDie(final Player player){
		this.sendMessage(player.getName() + " has been eliminated from the game!");

		Var.WORLD.spigot().strikeLightningEffect(player.getLocation(), false);
		player.teleport(this.map.getSpectatorLocation());
		this.dead.add(player.getUniqueId());
		player.getInventory().clear();
		Utils.giveInvisibility(player);
		player.setAllowFlight(true);
		Minigames.setCanTakeDamage(player, false); //Disallow PvP
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		final Player player = event.getPlayer();

		if (this.dead.contains(player.getUniqueId())) {
			return;
		}

		if(event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.RED_TERRACOTTA)){
			this.playerDie(player);
		}
	}

	@EventHandler
	public void onDamage(final EntityDamageByEntityEvent event) {
		if (this.dead.contains(event.getDamager().getUniqueId())) {
			event.setCancelled(true);
		}
	}

}
