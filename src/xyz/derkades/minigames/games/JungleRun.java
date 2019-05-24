package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Utils;

public class JungleRun extends Game {

	private static final String SECONDS_LEFT = "%s seconds left.";
	private static final int DURATION = 30;
	
	JungleRun() {
		super("Jungle Run", 
				new String[] {
						"Yet another parkour game.",
				}, 1, 2, 4, null);
	}
	
	private List<UUID> finished;
	private List<UUID> spectator;

	@Override
	void begin(GameMap genericMap) {
		finished = new ArrayList<>();
		spectator = new ArrayList<>();
		
		for (Player player: Bukkit.getOnlinePlayers()){		
			player.teleport(new Location(Var.WORLD, 282.5, 67, 196.5, -90, 0)); //Teleport all online players to the arena
			Utils.giveInvisibility(player);
		}
		
		new BukkitRunnable() {
			
			int secondsLeft = DURATION;
			
			public void run() {
				//End the game if everyone is a spectator
				if (spectator.size() == Bukkit.getOnlinePlayers().size() && secondsLeft > 2) {
					secondsLeft = 2;
				}
				
				if (secondsLeft <= 0) {
					this.cancel();
					startNextGame(Utils.getPlayerListFromUUIDList(finished));
					return;
				}
				
				if (secondsLeft == 15 || secondsLeft <= 5) {
					sendMessage(String.format(SECONDS_LEFT, secondsLeft));
				}
				
				secondsLeft--;
			}
			
		}.runTaskTimer(Minigames.getInstance(), 0, 1*20);
	}

	private void playerDie(Player player){
		//super.sendMessage(player.getName() + " has been eliminated from the game!");
		player.teleport(new Location(Var.WORLD, 296.5, 80, 204.5, 180, 0));
		
		spectator(player);
	}
	
	private void playerWin(Player player){
		super.sendMessage(player.getName() + " has made it to the finish!");
		
		player.teleport(new Location(Var.WORLD, 296.5, 80, 204.5, 180, 0));
		
		//Play particle effect and sound
		Utils.particle(Particle.FLAME, 327.5, 72, 196.5, 0.1, 1000, 0, 2, 2);
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
		
		finished.add(player.getUniqueId());
		
		spectator(player);
	}
	
	private void spectator(Player player) {
		player.setAllowFlight(true);
		spectator.add(player.getUniqueId());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		
		if (spectator.contains(player.getUniqueId())){
			return;	
		}
		
		Material type = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
		
		if (type == Material.SOUL_SAND ||
				type == Material.GREEN_WOOL ||
				type == Material.LIME_WOOL ||
				type == Material.GREEN_CARPET ||
				type == Material.LIME_CARPET ||
				type == Material.GREEN_TERRACOTTA){
			playerDie(player);
		} else if(type == Material.GOLD_BLOCK){
			playerWin(player);
		}
	}

}
