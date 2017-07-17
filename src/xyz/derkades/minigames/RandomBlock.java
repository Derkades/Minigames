package xyz.derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.utils.java.EnumUtils;
import xyz.derkades.minigames.utils.java.Random;

public enum RandomBlock {
	
	/*
	LOBBY_1(222, 66, 258),
	LOBBY_2(218, 66, 258),
	LOBBY_3(218, 66, 254),
	LOBBY_4(222, 66, 254),
	
	LOBBY_MIDDLE(220, 66, 256),
	
	*/
	
	LOBBY_CEILING_EAST_1(222, 72, 255),
	LOBBY_CEILING_EAST_2(222, 72, 256),
	LOBBY_CEILING_EAST_3(222, 72, 257),
	
	LOBBY_CEILING_SOUTH_1(221, 72, 258),
	LOBBY_CEILING_SOUTH_2(220, 72, 258),
	LOBBY_CEILING_SOUTH_3(219, 72, 258),
	
	LOBBY_CEILING_WEST_1(218, 72, 257),
	LOBBY_CEILING_WEST_2(218, 72, 256),
	LOBBY_CEILING_WEST_3(218, 72, 255),
	
	LOBBY_CEILING_NORTH_1(219, 72, 258),
	LOBBY_CEILING_NORTH_2(220, 72, 254),
	LOBBY_CEILING_NORTH_3(221, 72, 254),
	
	/*
	LEADERBOARD_UNDERNEATH_1(221, 67, 261),
	LEADERBOARD_UNDERNEATH_2(220, 67, 261),
	LEADERBOARD_UNDERNEATH_3(219, 67, 261),
	
	LEADERBOARD_MIDDLE_1(221, 68, 261),
	LEADERBOARD_MIDDLE_2(220, 68, 261),
	LEADERBOARD_MIDDLE_3(219, 68, 261),
	
	LEADERBOARD_ABOVE_1(221, 69, 261),
	LEADERBOARD_ABOVE_2(220, 69, 261),
	LEADERBOARD_ABOVE_3(219, 69, 261),
	*/
	;
	
	private Block block;
	
	RandomBlock(int x, int y, int z){
		block = new Location(Var.WORLD, x, y, z).getBlock();
	}
	
	public Block getBlock(){
		return block;
	}
	
	public static class ChangeColorTask extends BukkitRunnable {

		@SuppressWarnings("deprecation")
		@Override
		public void run(){
			if (Bukkit.getOnlinePlayers().size() == 0)
				return;
			
			Block block = EnumUtils.getRandomEnum(RandomBlock.class).getBlock();
			byte data = (byte) Random.getRandomInteger(0, 15);
			block.setData(data);
		}
		
	}

}
