package xyz.derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import xyz.derkades.derkutils.bukkit.MaterialLists;
import xyz.derkades.minigames.menu.MainMenu;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

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

//		final BoardPlayer board = new BoardPlayer(player);
//		board.createNpc();

		if (Minigames.CURRENT_GAME == null) {
			//No game is running, teleport to board
//			board.teleportToBoard(false);
			player.queueTeleport(Var.LOBBY_LOCATION);
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
		} else {
			//Game is running, game will handle teleporting
			Minigames.CURRENT_GAME.onPlayerJoin(player);

			Scheduler.delay(1, () -> player.spigot().sendMessage(
					Utils.getComponentBuilderWithPrefix(ChatColor.GREEN, 'P')
					.append("Current game: ")
					.append(Minigames.CURRENT_GAME.getName()).color(ChatColor.WHITE)
					.create()));
		}
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event){
		final MPlayer player = new MPlayer(event);
		event.setQuitMessage(String.format("[%s-%s] %s| %s%s", ChatColor.RED, ChatColor.RESET, ChatColor.DARK_GRAY, ChatColor.RED, player.getName()));

		if (Minigames.CURRENT_GAME != null) {
			Minigames.CURRENT_GAME.onPlayerQuit(player);
		}

//		final BoardPlayer board = new BoardPlayer(player);
//		board.removeNpc();
	}

	@EventHandler
	public void onPlayerDropItem(final PlayerDropItemEvent event){
		if (event.getPlayer().getGameMode() == GameMode.ADVENTURE && new MPlayer(event).getDisableItemMoving()) {
			event.setCancelled(true); //Cancel players dropping items
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
	public void gamesMenuOpen(final PlayerInteractEntityEvent event){
		if (Minigames.CURRENT_GAME != null) {
			return;
		}

		if (!event.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}

		final Player player = event.getPlayer();
		final Entity entity = event.getRightClicked();
		if (entity instanceof Villager){
			event.setCancelled(true);
			new MainMenu(player);
		}
	}

	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) {
			return;
		}

		if (event.getHand() != null && !event.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}

		final Action action = event.getAction();
		final Block block = event.getClickedBlock();
		if (action == Action.RIGHT_CLICK_BLOCK && MaterialLists.isInList(block.getType(), MaterialLists.TRAPDOORS, MaterialLists.DOORS, MaterialLists.FENCE_GATES)) {
			event.setCancelled(true);
		}

		final ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();

		if (Minigames.CURRENT_GAME == null && itemInHand.getType().equals(Material.COMPARATOR)) {
			new MainMenu(event.getPlayer());
		}
	}

	@EventHandler
	public void inventoryClickEvent(final InventoryClickEvent event) {
		final MPlayer player = new MPlayer((Player) event.getView().getPlayer());
		if (player.getDisableItemMoving() && player.getGameMode().equals(GameMode.ADVENTURE)) {
			event.setCancelled(true);
		}
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
	public void onDamageTriggerCustomEvent(final EntityDamageByEntityEvent event) {

	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		event.setDeathMessage("");
		Minigames.getInstance().getLogger().warning("A player died: " + event.getEntity().getName());
	}

	@EventHandler
	public void lobbyEffects(final PlayerMoveEvent event){
		if (Minigames.CURRENT_GAME == null){
			final Player player = event.getPlayer();

			final Material type = event.getTo().getBlock().getType();
			final Material below = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();

			if (type == Material.WATER && player.getGameMode() == GameMode.ADVENTURE){
				//player.teleport(new Location(Var.LOBBY_LOCATION.getWorld(), 217.0, 67, 258.0, 90, 0));
				player.teleport(new Location(Var.LOBBY_LOCATION.getWorld(), 213.5, 68, 255.9, 70, 0));
			} else if (below == Material.SLIME_BLOCK) {
				final PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, 30, 7, true, false);
				player.addPotionEffect(jump);
			}
		}
	}

}
