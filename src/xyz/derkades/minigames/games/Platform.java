package xyz.derkades.minigames.games;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.Main;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Console;
import xyz.derkades.minigames.utils.ItemBuilder;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class Platform extends Game {

	private HashMap<String, Boolean> isDead = new HashMap<>();
	
	@Override
	public String[] getDescription(){
		return new String[]{
				"Platform is pretty self explanatory, you spawn",
				"on a platform. The goal of this game is to try",
				"to knock off other players from the platform.",
				"At the start of the game and at the 10 seconds",
				"left mark you will get a knockback sword, which",
				"you can only use once."
		};
	}
	
	@Override
	public String getName(){
		return "Platform";
	}
	
	@Override
	public int getRequiredPlayers(){
		return 2;
	}
	
	@Override
	public GamePoints getPoints() {
		return new GamePoints(2, 5);
	}
	
	@Override
	public void resetHashMaps(Player player){
		isDead.put(player.getName(), false);
	}
	
	@Override
	public void begin(){
		//Teleport everyone to the arena and set them as not dead
		for (Player player : Bukkit.getOnlinePlayers()){
			player.teleport(new Location(Var.WORLD, 229.5, 89, 199.5, 180, 90));
			isDead.put(player.getName(), false);
			
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.DAMAGE_RESISTANCE, 100000, 255, true));
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.NIGHT_VISION, 100000, 255, true));
		}
		
		startGameTimer();
		
		new BukkitRunnable(){
			public void run(){
				for (Player player : Bukkit.getOnlinePlayers()){
					//Main.CAN_TAKE_DAMAGE.put(player.getName(), true); //Allow PvP
					Utils.setCanTakeDamage(player, true); //Allow PvP
					giveSwords();
				}
			}
		}.runTaskLater(Main.getInstance(), 3*20);
	}
	
	private void giveSwords(){
		ItemStack sword = new ItemBuilder(Material.WOOD_SWORD)
				.setDamage(59)
				.setName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Knockback Sword")
				.setLore(ChatColor.AQUA + "You can only use this once!")
				.addEnchantment(Enchantment.KNOCKBACK, 1)
				.create();
		for (Player player: Bukkit.getOnlinePlayers()){
			if (!isDead.get(player.getName())){ //Don't give sword to spectators
				player.getInventory().setItem(0, sword);
			}
		}
	}
	
	private void startGameTimer(){
		Scheduler.runTaskLater(5*20, new Runnable(){
			public void run(){
				sendMessage("20 seconds left!");
				Scheduler.runTaskLater(10*20, new Runnable(){
					public void run(){
						sendMessage("10 seconds left!");
						giveSwords(); //Give everyone who is still alive a sword
						Scheduler.runTaskLater(5*20, new Runnable(){
							public void run(){
								sendMessage("5 seconds left!");
								Scheduler.runTaskLater(5*20, new Runnable(){
									public void run(){
										endGame();
									}
								});
							}
						});
					}
				});
			}
		});
	}
	
	private void endGame(){
		Console.sendCommand("setblock 228 77 217 redstone_block"); //Activates fireworks command blocks
	
		sendMessage("Game has ended!");
		
		super.startNextGame(Utils.getWinnersFromIsDeadHashMap(isDead));
	}
	
	private void playerDie(Player player){
		sendMessage(player.getName() + " has been eliminated from the game!");
		Location loc = player.getLocation();
		Var.WORLD.spigot().strikeLightning(new Location(Var.WORLD, loc.getX(), loc.getY(), loc.getZ()), false);
		player.teleport(new Location(Var.WORLD, 224, 95, 199, -90, 0));
		isDead.put(player.getName(), true);
		player.getInventory().clear();
		
		Utils.setCanTakeDamage(player, false); //Disallow PvP
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent event){
		if (!isRunning()) return;
		
		Player player = event.getPlayer();
		if(event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.STAINED_CLAY){
			playerDie(player);
		}
	}

}
