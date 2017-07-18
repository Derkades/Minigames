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
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
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
			player.teleport(new Location(Var.WORLD, 136.0, 81, 361.0, -180, 0));
			
			Minigames.setCanTakeDamage(player, true); //Allow PvP
			
			Utils.giveInfiniteEffect(player, PotionEffectType.NIGHT_VISION);
			
			player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD)
					.name(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "PvP Sword")
					.lore("Use this to kill other players.")
					.enchant(Enchantment.DURABILITY, 10)
					.enchant(Enchantment.DAMAGE_ALL, 1)
					.create());
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
