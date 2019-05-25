package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.parkour.ParkourMap;
import xyz.derkades.minigames.utils.Utils;

public class Parkour extends Game {

	Parkour() {
		super("Parkour", new String[] {
				"Jump to the finish without touching the ground",
		}, 1, 3, 5, null);
	}

	private ParkourMap map;
	private List<UUID> finished;
	
	@Override
	void begin(GameMap genericMap) {
		this.map = (ParkourMap) genericMap;
		finished = new ArrayList<>();
		
		for (Player player: Bukkit.getOnlinePlayers()){
			player.teleport(map.getStartLocation());
			Utils.hideForEveryoneElse(player);
		}
		
		new GameTimer(this, map.getDuration(), 2) {

			@Override
			public void onStart() {
				Bukkit.getOnlinePlayers().forEach(Utils::hideForEveryoneElse);
			}

			@Override
			public int gameTimer(final int secondsLeft) {
				if (Bukkit.getOnlinePlayers().size() == finished.size() && secondsLeft > 1) {
					return 1;
				}
				
				return secondsLeft;
			}

			@Override
			public void onEnd() {
				endGame(Utils.getPlayerListFromUUIDList(finished));
			}
			
		};
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		final Player player = event.getPlayer();
		
		if (finished.contains(player.getUniqueId())) {
			return;
		}
		
		final Material blockType = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
		
		if (map.hasDied(player, blockType)) {
			player.teleport(new Location(Var.WORLD, 277.5, 70, 273.5, -90, 0));
		}
		
		if (map.hasFinished(player, blockType)) {
			if (finished.isEmpty()) {
				super.sendMessage(player.getName() + " finished first and got an extra point!");
				Points.addPoints(player, 1);
			} else {
				sendMessage(player.getName() + " has made it to the finish!");
			}
			
			finished.add(player.getUniqueId());
			
			if (map.getSpectatorLocation() != null) player.teleport(map.getSpectatorLocation());

			Utils.playSoundForAllPlayers(Sound.ENTITY_PLAYER_LEVELUP, 1);
			
			// Show all players to the spectator (the spectator is still invisible to others)
			Bukkit.getOnlinePlayers().forEach((player2) -> player.showPlayer(Minigames.getInstance(), player2));
			
			if (map.spectatorFreeFlight()) {
				player.setAllowFlight(true);
				player.setFlying(true);
				Utils.giveInvisibility(player);
			}
		}
			
	}
	
}
