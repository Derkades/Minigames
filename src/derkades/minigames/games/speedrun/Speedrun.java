package derkades.minigames.games.speedrun;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Speedrun extends Game<SpeedrunMap> {

	private static final PotionEffect SPEED_EFFECT = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 30, true, false);

	public Speedrun() {
		super(
				"speedrun",
				"Speedrun",
				new String[] {
						"Jump to the finish at super speed"
				},
				Material.POTION,
				new SpeedrunMap[] {
//						new Backwards(),
						new Classic(),
//						new Construction(),
				},
				1,
				50,
				EnumSet.of(GameLabel.SINGLEPLAYER, GameLabel.PARKOUR, GameLabel.NO_TEAMS)
		);
	}

	@Override
	public int getPreDuration() {
		return 0;
	}

	private Set<UUID> finished;

	@Override
	public void onPreStart() {
		this.finished = new HashSet<>();

		for (final MPlayer player : Minigames.getOnlinePlayers()){
			player.queueTeleport(this.map.getStartLocation());
			player.giveEffect(SPEED_EFFECT);
		}
	}

	@Override
	public void onStart() {}

	@Override
	public int gameTimer(final int secondsLeft) {
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return false;
	}

	@Override
	public void onEnd() {
		Speedrun.this.endGame(this.finished);
		this.finished = null;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		final MPlayer player = new MPlayer(event.getPlayer());

		if (this.finished.contains(player.getUniqueId())) {
			return;
		}

		if (player.bukkit().isSneaking() || player.bukkit().isSprinting()){
			player.sendActionBar(Component.text("Sprinting is not allowed", NamedTextColor.RED));
			player.teleport(this.map.getStartLocation());
			return;
		}

		final Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
		final Material type = block.getType();

		if (type == this.map.getFloorBlock()){
			player.teleport(this.map.getStartLocation());
			return;
		}

		if (type == this.map.getEndBlock()){
			player.clearPotionEffects();
			if (this.finished.isEmpty()){
				player.addPoints(1);
				this.sendMessage(player.getDisplayName().append(Component.text(" finished first and got an extra point!")));
			} else {
				this.sendMessage(player.getDisplayName().append(Component.text(" finished.")));
			}
			this.finished.add(player.getUniqueId());
			player.finishTo(this.map.getSpectatorLocation());
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		if (this.finished.contains(player.getUniqueId())) {
			player.teleport(this.map.getSpectatorLocation());
		} else {
			player.teleport(this.map.getStartLocation());
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
	}

}
