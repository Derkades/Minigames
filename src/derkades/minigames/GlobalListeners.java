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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import derkades.minigames.Minigames.ShutdownReason;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MPlayerDamageEvent;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.chat.ChatRenderer.ViewerUnaware;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class GlobalListeners implements Listener {

	private Component getJoinLeaveMessage(@NotNull final Component name, @NotNull final TextColor color, final char c) {
		return Component.text("[")
				.append(Component.text(c).color(color))
				.append(Component.text("] "))
				.append(name);
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent event){
		final MPlayer player = new MPlayer(event.getPlayer());

		event.joinMessage(getJoinLeaveMessage(player.bukkit().displayName(), NamedTextColor.GREEN, '+'));

		// Anti collision
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join global " + player.getName());

		player.clearInventory();

		player.setDisableDamage(true);
		player.setDisableHunger(true);
		player.setDisableItemMoving(true);
		player.disableSneakPrevention();

		player.teleport(Var.JAIL_LOCATION);

		if (GameState.getCurrentState().isInGame()) {
			// Game is running, game will handle teleporting
			final Game<?> game = GameState.getCurrentGame();
			game.onPlayerJoin(player);
		} else {
			// No game is running, teleport to lobby
			player.queueTeleportNoFadeOut(Var.LOBBY_LOCATION, () -> {
				player.afterLobbyTeleport();
			});

//			Scheduler.delay(1, () -> player.spigot().sendMessage(
//						Utils.getComponentBuilderWithPrefix(ChatColor.GREEN, 'P')
//						.append("For feature requests and bug reports, ")
//						.color(ChatColor.GRAY)
//						.append("click here")
//						.underlined(true)
//						.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Derkades/Minigames/issues"))
//						.append(".")
//						.underlined(false)
//						.create()));
		}
	}

	@EventHandler
	public void onQuit(@NotNull final PlayerQuitEvent event){
		final MPlayer player = new MPlayer(event);

		event.quitMessage(getJoinLeaveMessage(player.bukkit().displayName(), NamedTextColor.RED, '-'));

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
	public void onChat(final AsyncChatEvent event) {
		event.renderer(ChatRenderer.viewerUnaware(new ViewerUnaware() {

			@Override
			public @NotNull Component render(@NotNull final Player source, @NotNull final Component sourceDisplayName,
					@NotNull final Component message) {
				return sourceDisplayName
						.append(Component.text(" \u00BB ").color(NamedTextColor.DARK_GRAY))
						.append(message.color(NamedTextColor.GRAY));
			}

		}));
//		event.setFormat(Utils.getChatPrefix(ChatColor.AQUA, 'C') + ChatColor.WHITE + "%s: " + ChatColor.GRAY + "%s");
	}

	@SuppressWarnings("deprecation")
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

		final MPlayerDamageEvent event2 = new MPlayerDamageEvent(event);
		Bukkit.getPluginManager().callEvent(event2);
		if (event2.isCancelled()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
//		event.setDeathMessage("");
		event.deathMessage(Component.empty());
		Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "A player died: " + event.getEntity().getName());
	}

}
