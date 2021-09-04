package derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import derkades.minigames.Minigames.ShutdownReason;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;

public class GlobalListeners implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event){
		final MPlayer player = new MPlayer(event.getPlayer());

		event.setJoinMessage(String.format("[%s+%s] %s| %s%s", ChatColor.GREEN, ChatColor.RESET, ChatColor.DARK_GRAY, ChatColor.GREEN, player.getName()));

		// Anti collision
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join global " + player.getName());

		player.clearInventory();

		player.setDisableDamage(true);
		player.setDisableHunger(true);
		player.setDisableItemMoving(true);
		player.disableSneakPrevention();

		if (GameState.getCurrentState().isInGame()) {
			// Game is running, game will handle teleporting
			final Game<?> game = GameState.getCurrentGame();
			game.onPlayerJoin(player);

//			Scheduler.delay(1, () -> player.spigot().sendMessage(
//					Utils.getComponentBuilderWithPrefix(ChatColor.GREEN, 'P')
//					.append("Current game: ")
//					.append(game.getName()).color(ChatColor.WHITE)
//					.create()));
		} else {
			// No game is running, teleport to lobby
			player.teleport(Var.LOBBY_LOCATION);
			player.applyLobbySettings();

			Scheduler.delay(1, () -> player.spigot().sendMessage(
						Utils.getComponentBuilderWithPrefix(ChatColor.GREEN, 'P')
						.append("For feature requests and bug reports, ")
						.color(ChatColor.GRAY)
						.append("click here")
						.underlined(true)
						.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Derkades/Minigames/issues"))
						.append(".")
						.underlined(false)
						.create()));
		}
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event){
		final MPlayer player = new MPlayer(event);
		event.setQuitMessage(String.format("[%s-%s] %s| %s%s", ChatColor.RED, ChatColor.RESET, ChatColor.DARK_GRAY, ChatColor.RED, player.getName()));

		if (GameState.getCurrentState().isInGame()) {
			GameState.getCurrentGame().onPlayerQuit(player);
		}
	}

	@EventHandler
	public void onPlayerDropItem(final PlayerDropItemEvent event){
		if (event.getPlayer().getGameMode() == GameMode.ADVENTURE && new MPlayer(event).getDisableItemMoving()) {
			event.setCancelled(true); // Cancel players dropping items
		}
	}

	@EventHandler
	public void damage(final EntityDamageEvent event){
		if (event.getEntity() instanceof Villager){
			final Villager villager = (Villager) event.getEntity();
			if (villager.getCustomName().equals("Bait") ||
					villager.getCustomName().contentEquals("Click Me!")) {
				event.setCancelled(true);
			}
		}

		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		final MPlayer player = new MPlayer(event);
		if (player.getDisableDamage()){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onChat(final AsyncPlayerChatEvent event) {
		event.setFormat(Utils.getChatPrefix(ChatColor.AQUA, 'C') + ChatColor.WHITE + "%s: " + ChatColor.GRAY + "%s");
	}

	@EventHandler
	public void onDamageTriggerCustomEvent(final EntityDamageEvent event) {
		if (!event.getEntity().getType().equals(EntityType.PLAYER)) {
			return;
		}

		if (event instanceof EntityDamageByEntityEvent) {
			final EntityDamageByEntityEvent event2 = (EntityDamageByEntityEvent) event;

			if (event2.getEntity().getType() != EntityType.PLAYER) {
				return;
			}

			final MinigamesPlayerDamageEvent event3 = new MinigamesPlayerDamageEvent((Player) event2.getEntity(),
					event2.getDamager(), event.getCause(), event.getDamage());

			Bukkit.getPluginManager().callEvent(event3);

			event.setDamage(event3.getDamage());

			if (event3.isCancelled()) {
				event.setCancelled(true);
			}
		} else {
			final MinigamesPlayerDamageEvent event3 = new MinigamesPlayerDamageEvent((Player) event.getEntity(),
					event.getCause(), event.getDamage());

			Bukkit.getPluginManager().callEvent(event3);

			event.setDamage(event3.getDamage());

			if (event3.isCancelled()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		event.setDeathMessage("");
		Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "A player died: " + event.getEntity().getName());
	}

	private static final PotionEffect SLIME_JUMP_EFFECT = new PotionEffect(PotionEffectType.JUMP, 30, 7, true, false);
	private static final Location parkourWater1 = new Location(Var.LOBBY_WORLD, 213, 78, 239);
	private static final Location parkourWater2 = new Location(Var.LOBBY_WORLD, 195, 78, 264);

	@EventHandler
	public void lobbyEffects(final PlayerMoveEvent event){
		if (GameState.isCurrentlyInGame()) {
			return;
		}

		final MPlayer player = new MPlayer(event);
		player.removeFire();

		final Block to = event.getTo().getBlock();
		final Material below = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
		if (
				(
						to.getType() == Material.WATER ||
						(
								to.getBlockData() != null &&
								to.getBlockData() instanceof Waterlogged &&
								((Waterlogged) to.getBlockData()).isWaterlogged()
						)
				) &&
				player.getGameMode() == GameMode.ADVENTURE &&
				!player.getMetadataBool("lobby parkour teleporting", false) &&
				player.isIn2dBounds(parkourWater1, parkourWater2)) {
			player.setMetadata("lobby parkour teleporting", true);
			Scheduler.delay(5, () -> {
				player.teleport(new Location(Var.LOBBY_LOCATION.getWorld(), 213.5, 68, 255.9, 70, 0));
				player.removeMetadata("lobby parkour teleporting");
			});
		} else if (below == Material.SLIME_BLOCK) {
			player.giveEffect(SLIME_JUMP_EFFECT);
		}
	}

}
