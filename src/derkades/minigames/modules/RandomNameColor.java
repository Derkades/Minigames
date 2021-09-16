package derkades.minigames.modules;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import xyz.derkades.derkutils.Colors;

public class RandomNameColor extends Module {

	/**
	 * 0.0 - 5.0
	 * Lower value means more visible gradient
	 */
	private static final float MARGIN = 0.2f;

	@EventHandler(priority = EventPriority.LOWEST) // needs to run before any other events that use display name
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();

		final int rgb1 = Colors.randomPastelColor().getRGB();
		final int rgb2 = Colors.randomPastelColor().getRGB();
		final TextComponent.Builder b = Component.text();
		float f = MARGIN;
		final float f_step = (1.0f - 2*MARGIN) / (player.getName().length() - 1);
		for (final char c : player.getName().toCharArray()) {
			final int r1 = (int) ((1-f) * (0xFF & rgb1));
			final int g1 = (int) ((1-f) * ((0xFF00 & rgb1) >> 8)) << 8;
			final int b1 = (int) ((1-f) * ((0xFF0000 & rgb1) >> 16)) << 16;
			final int r2 = (int) (f * (0xFF & rgb2));
			final int g2 = (int) (f * ((0xFF00 & rgb2) >> 8)) << 8;
			final int b2 = (int) (f * ((0xFF0000 & rgb2) >> 16)) << 16;
			b.append(Component.text(c, TextColor.color(r1 + g1 + b1 + r2 + g2 + b2)));
			f += f_step;
			if (f > 1.0f) {
				f = 1.0f;
			}
		}

		final Component name = b.asComponent();
		player.displayName(name);
		final String padding = "    ";
		player.playerListName(Component.text(padding).append(name).append(Component.text(padding)));
	}

}
