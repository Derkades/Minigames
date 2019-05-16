package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.GameTimer;
import xyz.derkades.minigames.utils.Utils;

public class Elytra extends Game {

	public static final int GAME_DURATION = 30;
	public static final int PRE_START_TIME = 3;
	
	Elytra() {
		super("Elytra", new String[]{
				"Fly to the end of the cave without",
				"touching the ground. Good luck!"
		}, 2, 2, 5, null);
	}

	private List<UUID> dead = new ArrayList<>();
	private List<UUID> finished = new ArrayList<>();

	@Override
	void begin(GameMap genericMap) {
		for (Player player : Bukkit.getOnlinePlayers()){
			player.teleport(new Location(Var.WORLD, 163.5, 76.5, 339.5, 120, 25));
			player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
			Utils.giveInfiniteEffect(player, PotionEffectType.SLOW, 5);
			Utils.giveInfiniteEffect(player, PotionEffectType.INVISIBILITY, 2);
		}
		
		new GameTimer() {
			
			int timeLeft = GAME_DURATION + PRE_START_TIME;
			
			public void run() {
				if (timeLeft > GAME_DURATION) {
					sendMessage("The game will start in " + (timeLeft - GAME_DURATION) + " seconds");
				}
				
				if (timeLeft == GAME_DURATION) {
					// game starting, remove slowness
					Utils.clearPotionEffects();
				}
				
				if (timeLeft == 15 || timeLeft < 5) {
					sendMessage("The game will end in " + timeLeft + " seconds");
				}
				
				if (timeLeft <= 0) {
					end();
					this.cancel();
				}
				
				if (Utils.getAliveCountFromDeadList(dead) < 2) {
					end();
					this.cancel();
				}
				
				timeLeft--;
			}
		};
	}
	
	private void end(){
		super.startNextGame(Utils.getPlayerListFromUUIDList(finished));
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){		
		Player player = event.getPlayer();
		Material type = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
		
		if(!player.isFlying() && (type == Material.STONE || type == Material.COBBLESTONE || type == Material.LAVA)) {
			// die
			Utils.clearInventory(player);
			player.teleport(new Location(Var.WORLD, 151.5, 76, 343.5));
		}
			
		if (type == Material.LIME_WOOL) {
			// win
			Utils.clearInventory(player);
			player.teleport(new Location(Var.WORLD, 151.5, 76, 343.5));
			finished.add(player.getUniqueId());
		}
	}

}
