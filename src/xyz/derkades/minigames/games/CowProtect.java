package xyz.derkades.minigames.games;

/*import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public class CowProtect extends Game {

	@Override
	String[] getDescription() {
		return new String[] {
			"Insert description here",
		};
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public GamePoints getPoints() {
		return new GamePoints(5, 10);
	}

	@Override
	public void resetHashMaps(Player player) {}

	@Override
	void begin() {
		ItemStack wool = new ItemBuilder(Material.WOOL)
				.name(ChatColor.DARK_GREEN + "Building Blocks")
				.lore(ChatColor.GREEN + "Use this to protect your cow.")
				.amount(25)
				.create();
		
		net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(wool);
		NBTTagCompound tag = nms.getTag();
		NBTTagList list = new NBTTagList();
		list.add(new NBTTagString("minecraft:stained_glass"));
		list.add(new NBTTagString("minecraft:wool"));
		tag.set("CanPlaceOn", list);
		nms.setTag(tag);
		wool = CraftItemStack.asBukkitCopy(nms);
	}

}*/
