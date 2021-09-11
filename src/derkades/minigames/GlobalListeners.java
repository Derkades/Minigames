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
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.chat.ChatRenderer.ViewerUnaware;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.StandardTextColor;

public class GlobalListeners implements Listener {

	private Component getJoinLeaveMessage(final String name, final ChatColor color) {
		final TextColor textColor = TextColor.color(color.getColor().getRGB());
		return Component.text("[")
				.append(Component.text("-").color(textColor))
				.append(Component.text("] "))
				.append(Component.text(name).color(textColor));
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent event){
		final MPlayer player = new MPlayer(event.getPlayer());

//		event.setJoinMessage(String.format("[%s+%s] %s| %s%s", ChatColor.GREEN, ChatColor.RESET, ChatColor.DARK_GRAY, ChatColor.GREEN, player.getName()));
		event.joinMessage(getJoinLeaveMessage(player.getName(), ChatColor.GREEN));

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
	public void onQuit(final PlayerQuitEvent event){
		final MPlayer player = new MPlayer(event);
//		event.setQuitMessage(String.format("[%s-%s] %s| %s%s", ChatColor.RED, ChatColor.RESET, ChatColor.DARK_GRAY, ChatColor.RED, player.getName()));
		event.quitMessage(getJoinLeaveMessage(player.getName(), ChatColor.RED));

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
						.append(Component.text(" \u00BB ").color(StandardTextColor.DARK_GRAY))
						.append(message.color(StandardTextColor.GRAY));
			}

		}));
//		event.setFormat(Utils.getChatPrefix(ChatColor.AQUA, 'C') + ChatColor.WHITE + "%s: " + ChatColor.GRAY + "%s");
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
//		event.setDeathMessage("");
		event.deathMessage(Component.empty());
		Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "A player died: " + event.getEntity().getName());
	}

}
