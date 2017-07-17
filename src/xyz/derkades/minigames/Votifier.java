package xyz.derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.utils.java.Random;

public class Votifier implements Listener {
	
	@EventHandler
	public void onVote(VotifierEvent event){
		Vote vote = event.getVote();
		int points = Random.getRandomInteger(20, 60);
		Bukkit.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + vote.getUsername() + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " voted at " + ChatColor.AQUA + "" + ChatColor.BOLD + vote.getServiceName() + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " and got " + ChatColor.AQUA + "" + ChatColor.BOLD + points + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " points");
		@SuppressWarnings("deprecation")
		OfflinePlayer player = Bukkit.getOfflinePlayer(vote.getUsername());
		Points.addPoints(player, points);
	}

}
