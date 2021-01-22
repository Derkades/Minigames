package xyz.derkades.minigames;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Sound;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Scheduler;

public class JazzRoom implements Runnable {
	
	private static final Sound[] JAZZ_MUSIC = new Sound[] {
			Sound.MUSIC_DISC_13,
			Sound.MUSIC_DISC_CHIRP,
//			Sound.MUSIC_DISC_FAR,
	};
	
	private static final Location BOUNDS_A = new Location(Var.LOBBY_WORLD, 213, 76, 273);
	private static final Location BOUNDS_B = new Location(Var.LOBBY_WORLD, 225, 82, 258);
	
	private static final HashMap<String, Sound> PLAYING = new HashMap<>();
	
	public JazzRoom() {
		Scheduler.repeat(10, this);
	}
	
	@Override
	public void run() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (player.isIn3dBounds(BOUNDS_A, BOUNDS_B)) {
				if (!PLAYING.containsKey(player.getName())) {
					player.sendTitle(ChatColor.MAGIC + "aaaaa", ChatColor.LIGHT_PURPLE + "Jazz room!");
					final Sound random = ListUtils.getRandomValueFromArray(JAZZ_MUSIC);
					Logger.debug("Playing %s to %s", random.name(), player.getName());
					player.playSound(random, 1.0f);
					PLAYING.put(player.getName(), random);
				}
			} else {
				if (PLAYING.containsKey(player.getName())) {
					player.sendTitle("Bye", ChatColor.RED + "You have left Jazz room.");
					final Sound sound = PLAYING.get(player.getName());
					Logger.debug("Stopping sound %s for %s", sound.name(), player.getName());
					player.bukkit().stopSound(sound);
					player.removeMetadata("jazz-room");
					PLAYING.remove(player.getName());
				}
			}
		}
	}

}
