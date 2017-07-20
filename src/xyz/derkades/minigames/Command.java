package xyz.derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.menu.MainMenu;

public class Command implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command arg1, String arg2, String[] args) {
		Player player = (Player) sender;
		
		if (args.length == 2){
			if (args[0].equals("staff")){
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				VIP.setStaff(target, true);
			} else if (args[0].equals("unstaff")){
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				VIP.setStaff(target, false);
			}
		} else if (args.length == 1){
			if (args[0].equalsIgnoreCase("start") &&
					player.isOp()){
				AutoRotate.startNewRandomGame();
				Minigames.STOP_GAMES = false;
			} else if (args[0].equalsIgnoreCase("stop") &&
					player.isOp()){
				player.sendMessage("! STOPPED GAMES !");
				Minigames.STOP_GAMES = true;
			} else if (args[0].equals("test")) {
				ItemStack shovel = new ItemBuilder(Material.DIAMOND_SPADE)
						.name(ChatColor.GREEN + "The Dig Dug Digger")
						.create();
				
				shovel.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
						
				net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(shovel);
				NBTTagCompound tag = nms.getTag();
				
				NBTTagList list = new NBTTagList();
				list.add(new NBTTagString("minecraft:dirt"));
				list.add(new NBTTagString("minecraft:grass"));
				
				tag.set("CanDestroy", list);
				tag.setBoolean("Unbreakable", true);
				
				shovel = CraftItemStack.asBukkitCopy(nms);
				
				for (Player online : Bukkit.getOnlinePlayers()) {
					online.getInventory().addItem(shovel);
				}
			} else if (player.isOp()) {
				Game game = Game.fromString(args[0].replace("_", " "));
				if (game == null){
					player.sendMessage("Unknown game");
					return true;
				} else {
					Minigames.NEXT_GAME = game;
					AutoRotate.startNewRandomGame();
				}
			}
		} else if (args.length == 0){
			new MainMenu(player).open();
		}
		
		return true;
	}

}
