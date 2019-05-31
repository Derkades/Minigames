package xyz.derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
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
import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.MaterialLists;
import xyz.derkades.minigames.menu.MainMenu;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class GlobalListeners implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event){
		final Player player = event.getPlayer();

		if (Minigames.IS_IN_GAME) {
			player.teleport(Var.IN_GAME_LOBBY_LOCATION);
		} else {
			player.teleport(Var.LOBBY_LOCATION);
		}

		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false); //Just in case the player was spectator
		Utils.clearInventory(player);
		Minigames.setCanTakeDamage(player, false);

		player.setExp(0.0f);
		player.setLevel(0);

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join global " + player.getName());

		//final String minMax = ChatColor.GRAY + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")";

		//event.setJoinMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + player.getName() + ChatColor.GOLD + "" + ChatColor.BOLD + " has joined! " + minMax);

		event.setJoinMessage(String.format("[%s+%s] %s| %s%s", ChatColor.GREEN, ChatColor.RESET, ChatColor.DARK_GRAY, ChatColor.GREEN, player.getName()));

		Scheduler.delay(1, () -> {
			player.spigot().sendMessage(
					new ComponentBuilder("For feature requests and bug reports, click here.")
					.color(ChatColor.GRAY)
					.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Derkades/Minigames/issues"))
					.create());
		});

		if (player.hasPermission("games.torch")) {
			player.getInventory().setItem(7, new ItemBuilder(Material.REDSTONE_TORCH)
					.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Staff lounge key")
					.lore(ChatColor.YELLOW + "Place in upper-south-east-corner on gray terracotta")
					.canPlaceOn("cyan_terracotta")
					.create());
		}

		player.getInventory().setItem(8, new ItemBuilder(Material.COMPARATOR)
				.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Menu")
				.lore(ChatColor.YELLOW + "Click to open menu")
				.create());
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event){
		final Player player = event.getPlayer();
		//event.setQuitMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + player.getName() + ChatColor.GOLD + "" + ChatColor.BOLD + " has left.");
		event.setQuitMessage(String.format("[%s-%s] %s| %s%s", ChatColor.RED, ChatColor.RESET, ChatColor.DARK_GRAY, ChatColor.RED, player.getName()));
	}

	@EventHandler
	public void onPlayerDropItem(final PlayerDropItemEvent event){
		if (event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
			event.setCancelled(true); //Cancel players dropping items
		}
	}

	@EventHandler
	public void damage(final EntityDamageEvent event){
		if (event.getEntity() instanceof Villager){
			event.setCancelled(true);
		}

		if (!(event.getEntity() instanceof Player))
			return;

		final Player player = (Player) event.getEntity();
		if (!Minigames.canTakeDamage(player)){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event){
		if (!Minigames.IS_IN_GAME){
			final Player player = event.getPlayer();

			final Material type = event.getTo().getBlock().getType();
			final Material below = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();

			if (type == Material.WATER && player.getGameMode() == GameMode.ADVENTURE){
				player.teleport(new Location(Var.WORLD, 217.0, 67, 258.0, 90, 0));
			} else if (below == Material.SLIME_BLOCK) {
				final PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, 30, 7, true, false);
				player.addPotionEffect(jump);
			}
		}
	}

	@EventHandler
	public void onEntityInteract(final PlayerInteractEntityEvent event){
		if (!event.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}

		final Player player = event.getPlayer();
		final Entity entity = event.getRightClicked();
		if (entity instanceof Villager){
			event.setCancelled(true);
			new MainMenu(player).open();
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

		if (!Minigames.IS_IN_GAME && itemInHand.getType().equals(Material.COMPARATOR)) {
			new MainMenu(event.getPlayer()).open();
		}
	}

	@EventHandler
	public void inventoryClickEvent(final InventoryClickEvent event) {
		if (event.getView().getPlayer().getGameMode() == GameMode.ADVENTURE) {
			event.setCancelled(true);
		}
	}

}
