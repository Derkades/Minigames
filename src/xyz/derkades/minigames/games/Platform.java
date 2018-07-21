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
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.platform.PlatformMap;
import xyz.derkades.minigames.utils.Utils;

public class Platform extends Game {
	
	Platform() {
		super("Platform", new String[] {
				"Knock other players off a platform.",
				"Use your knockback swords wisely, you",
				"only get two knockback swords (one at",
				"the start, one at " + KNOCKBACK_SWORDS_TIME + " seconds.",
		}, 2, 2, 5);
	}

	private static final int SPREAD_TIME = 5;
	private static final int GAME_DURATION = 40;
	private static final int KNOCKBACK_SWORDS_TIME = 20;
	
	private static final String SECONDS_LEFT = "%s seconds left.";
	
	private List<UUID> dead;
	
	private PlatformMap map;
	
	@Override
	public void begin(){
		dead = new ArrayList<>();
		
		map = ListUtils.getRandomValueFromArray(PlatformMap.MAPS);
		
		sendMessage("Map: " + map.getName());
		
		for (Player player : Bukkit.getOnlinePlayers()){
			player.teleport(map.spawnLocation());
			Utils.giveInfiniteEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 255);
		}
		
		sendMessage("You have " + SPREAD_TIME + " seconds to spread before the game starts.");
		
		new BukkitRunnable() {
			
			int secondsLeft = GAME_DURATION + SPREAD_TIME;
			
			@Override
			public void run() {
				if (secondsLeft == GAME_DURATION) {
					for (Player player : Bukkit.getOnlinePlayers()) {
						Minigames.setCanTakeDamage(player, true);
					}
					sendMessage("The game has started.");
				}
				
				if (Utils.getAliveCountFromDeadList(dead) <= 1 && secondsLeft > 2) {
					secondsLeft = 2;
				}
				
				if (secondsLeft == 20) {
					giveSwords();
				}
				
				if (secondsLeft <= 0) {
					this.cancel();
					endGame();
					return;
				}
				
				if (secondsLeft == 15 || secondsLeft <= 5) {
					sendMessage(String.format(SECONDS_LEFT, secondsLeft));
				}
				
				secondsLeft--;
			}
			
		}.runTaskTimer(Minigames.getInstance(), 0, 20);
	}
	
	private void giveSwords(){
		ItemStack sword = new ItemBuilder(Material.WOOD_SWORD)
				.data(59)
				.name(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Knockback Sword")
				.lore(ChatColor.AQUA + "You can only use this once!")
				.enchant(Enchantment.KNOCKBACK, 1)
				.create();
		
		for (Player player: Bukkit.getOnlinePlayers()){
			if (!dead.contains(player.getUniqueId())){ //Don't give sword to spectators
				player.getInventory().setItem(0, sword);
			}
		}
	}
	
	private void endGame(){
		//Console.sendCommand("setblock 228 77 217 redstone_block"); //Activates fireworks command blocks
		sendMessage("The game has ended!");
		super.startNextGame(Utils.getWinnersFromDeadList(dead));
	}
	
	private void playerDie(Player player){
		sendMessage(player.getName() + " has been eliminated from the game!");
		Var.WORLD.spigot().strikeLightningEffect(player.getLocation(), false);
		player.teleport(map.spectatorLocation());
		dead.add(player.getUniqueId());
		player.getInventory().clear();
		Utils.giveInvisibility(player);
		player.setAllowFlight(true);
		
		Minigames.setCanTakeDamage(player, false); //Disallow PvP
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		
		if (dead.contains(player.getUniqueId())) {
			return;
		}
		
		if(event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.STAINED_CLAY){
			playerDie(player);
		}
	}

}
