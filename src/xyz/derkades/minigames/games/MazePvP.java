package xyz.derkades.minigames.games;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.ItemBuilder;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class MazePvP extends Game {

	private Map<String, Boolean> isDead = new HashMap<>();
	
	@Override
	String[] getDescription() {
		return new String[]{
				"PvP, inside of a changing maze!",
				"Kill = win. No health regeneration."
		};
	}

	@Override
	public String getName() {
		return "MazePvP";
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public GamePoints getPoints() {
		return new GamePoints(2, 6);
	}

	@Override
	public void resetHashMaps(Player player) {
		isDead.put(player.getName(), false);
	}

	@Override
	void begin() {		
		for (Player player : Bukkit.getOnlinePlayers()){
			//player.teleport(new Location(Var.WORLD, 158, 91, 326));
			player.teleport(new Location(Var.WORLD, 136.0, 81, 361.0, -180, 0));
			
			//Main.CAN_TAKE_DAMAGE.put(player.getName(), true); //Allow PvP
			Utils.setCanTakeDamage(player, true); //Allow PvP
			
			Utils.giveInfiniteEffect(player, PotionEffectType.NIGHT_VISION);
		
			//PlayerInventory inv = player.getInventory();
			//inv.addItem(new ItemStack(Material.IRON_SWORD));
			
			new ItemBuilder(Material.IRON_SWORD)
			.setName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "PvP Sword")
			.setLore("Use this to kill other players.")
			.addEnchantment(Enchantment.DURABILITY, 10)
			.addEnchantment(Enchantment.DAMAGE_ALL, 1)
			.addToInventory(player);
			
			//Utils.setArmor(player, 
			//		Material.DIAMOND_HELMET, 
			//		Material.DIAMOND_CHESTPLATE, 
			//		Material.DIAMOND_LEGGINGS, 
			//		Material.DIAMOND_BOOTS);
		}
		
		Utils.setGameRule("naturalRegeneration", false);
	}
	
	public void playerWinEndGame(Player player){
		Utils.setGameRule("naturalRegeneration", true);
		super.startNextGame(Arrays.asList(player));
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		if (!isRunning()) return;
		
		Player player = event.getEntity();
		Player killer = event.getEntity().getKiller();
		
		super.sendMessage(killer.getName() + " has won the game by killing " + player.getName() + ".");
		
		playerWinEndGame(killer);
		
		Scheduler.runTaskLater(3L, new Runnable(){
			public void run(){
				player.spigot().respawn();
			}
		});
		
		event.setDeathMessage("");
	}

}
