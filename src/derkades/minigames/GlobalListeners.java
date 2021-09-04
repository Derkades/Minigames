package derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
			GameState.getCurrentGame().onPlayerQuit(player); // TODO move to game class?
		}
	}

	@EventHandler
	public void onPlayerDropItem(final PlayerDropItemEvent event){
		if (event.getPlayer().getGameMode() == GameMode.ADVENTURE && new MPlayer(event).getDisableItemMoving()) {
			event.setCancelled(true); // Cancel players dropping items
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

}
