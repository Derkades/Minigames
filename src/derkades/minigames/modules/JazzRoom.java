package derkades.minigames.modules;

import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Sound;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.Var;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.derkades.derkutils.ListUtils;

public class JazzRoom extends Module implements Runnable {

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
					player.sendTitle(
							Component.text("aaaaa").decorate(TextDecoration.OBFUSCATED),
							Component.text("Jazz room!", LIGHT_PURPLE)
							);
					final Sound random = ListUtils.choice(JAZZ_MUSIC);
					Logger.debug("Playing %s to %s", random.name(), player.getName());
					player.playSound(random, 1.0f);
					PLAYING.put(player.getName(), random);
				}
			} else if (PLAYING.containsKey(player.getName())) {
				player.sendTitle(
						Component.text("Bye", GRAY),
						Component.text("You have left jazz room.", RED)
						);
				final Sound sound = PLAYING.get(player.getName());
				Logger.debug("Stopping sound %s for %s", sound.name(), player.getName());
				player.bukkit().stopSound(sound);
				player.removeMetadata("jazz-room");
				PLAYING.remove(player.getName());
			}
		}
	}

}
