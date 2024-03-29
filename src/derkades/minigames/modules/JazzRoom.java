package derkades.minigames.modules;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.Var;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import xyz.derkades.derkutils.ListUtils;

import java.util.HashMap;
import java.util.UUID;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class JazzRoom extends Module implements Runnable {

	private static final Sound[] JAZZ_MUSIC = new Sound[] {
			Sound.sound(Key.key("arkades", "music.offering"), Sound.Source.MASTER, 1.0f, 1.0f),
			Sound.sound(Key.key("arkades", "music.daybreak"), Sound.Source.MASTER, 1.0f, 1.0f),
	};

	private static final Location BOUNDS_A = new Location(Var.LOBBY_WORLD, 214, 78, 273);
	private static final Location BOUNDS_B = new Location(Var.LOBBY_WORLD, 224, 81, 259);

	private static final HashMap<UUID, Sound> PLAYING = new HashMap<>();

	public JazzRoom() {
		Scheduler.repeat(10, this);
	}

	@Override
	public void run() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (player.isIn3dBounds(BOUNDS_A, BOUNDS_B)) {
				if (!PLAYING.containsKey(player.getUniqueId())) {
					player.sendTitle(
							Component.text("#####").decorate(TextDecoration.OBFUSCATED),
							Component.text("Jazz room!", LIGHT_PURPLE)
							);
					final Sound random = ListUtils.choice(JAZZ_MUSIC);
					Logger.debug("Playing %s to %s", random.name(), player.getOriginalName());
					player.playSound(random);
					PLAYING.put(player.getUniqueId(), random);
				}
			} else if (PLAYING.containsKey(player.getUniqueId())) {
				player.sendTitle(
						Component.text("Bye", GRAY),
						Component.text("You have left jazz room.", RED)
						);
				final Sound sound = PLAYING.get(player.getUniqueId());
				Logger.debug("Stopping sound %s for %s", sound.name(), player.getOriginalName());
				player.bukkit().stopSound(sound);
				PLAYING.remove(player.getUniqueId());
			}
		}
	}

}
