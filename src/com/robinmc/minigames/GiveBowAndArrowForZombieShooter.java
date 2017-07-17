package com.robinmc.minigames;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.robinmc.minigames.utils.ItemBuilder;
import com.robinmc.minigames.utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class GiveBowAndArrowForZombieShooter extends BukkitRunnable {

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()){
			/*
			final short lessX = 217;
			final short moreX = 66;
			final short lessZ = 258;
			final short moreZ = 254;
			Location loc = player.getLocation();
			if (loc.getX() < lessX &&
					loc.getX() > moreX &&
					loc.getZ() < lessZ &&
					loc.getZ() > moreZ){
					*/
			Block block = Utils.getBlockStandingOn(player);
			if (block.getType() == Material.LAPIS_BLOCK){
				PlayerInventory inv = player.getInventory();
				if (!inv.contains(Material.BOW))
					inv.setItem(0, new ItemBuilder(Material.BOW)
							.setName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Zombie Shooter")
							.setLore(ChatColor.AQUA + "Use this to shoot zombies.")
							.create());
				
				if (inv.getItemInOffHand().getType() != Material.ARROW)
					inv.setItemInOffHand(new ItemStack(Material.ARROW));
			} else {
				if (!Main.IS_IN_GAME){
					PlayerInventory inv = player.getInventory();
					if (inv.getItemInOffHand().getType() == Material.ARROW) inv.setItemInOffHand(new ItemStack(Material.AIR));
					if (inv.contains(Material.BOW)) inv.remove(Material.BOW);
				}
			}
		}
	}

}
