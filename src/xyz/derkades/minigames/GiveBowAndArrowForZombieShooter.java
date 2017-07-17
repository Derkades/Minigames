package xyz.derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.utils.ItemBuilder;
import xyz.derkades.minigames.utils.Utils;

public class GiveBowAndArrowForZombieShooter extends BukkitRunnable {

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()){
			Block block = Utils.getBlockStandingOn(player);
			if (block.getType() == Material.LAPIS_BLOCK){
				PlayerInventory inv = player.getInventory();
				if (!inv.contains(Material.BOW))
					inv.setItem(0, new ItemBuilder(Material.BOW)
							.setName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Zombie Shooter")
							.setLore(ChatColor.AQUA + "Use this to shoot zombies.")
							.create());
				
				if (!inv.contains(Material.ARROW))
					inv.addItem(new ItemStack(Material.ARROW));
			} else {
				if (!Main.IS_IN_GAME){
					PlayerInventory inv = player.getInventory();
					inv.remove(Material.ARROW);
					if (inv.contains(Material.BOW)) inv.remove(Material.BOW);
				}
			}
		}
	}

}
