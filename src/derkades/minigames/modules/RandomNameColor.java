package derkades.minigames.modules;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import net.kyori.adventure.text.format.TextColor;
import xyz.derkades.derkutils.Colors;

public class RandomNameColor extends Module {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		player.displayName(player.displayName().color(TextColor.color(Colors.randomPastelColor().getRGB())));
	}

}
