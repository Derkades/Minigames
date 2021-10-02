package derkades.minigames.games.tntrun;

import derkades.minigames.GameState;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TntRun extends Game<TntRunMap> {

	public TntRun() {
		super(
				"tnt_run",
				"TNT Run",
				new String[]{
						"The floor disappears where you walk. Avoid",
						"falling for as long as possible.",
				},
				Material.TNT,
				new TntRunMap[] {
						new Aqua(),
						new Future(),
						new Jungle(),
						new WaterLava(),
				},
				2
		);
	}

	@Override
	public int getDuration() {
		return 150;
	}

	private Set<UUID> alive;
	private Set<Block> removedBlocks;

	@Override
	public void onPreStart() {
		this.removedBlocks = new HashSet<>();

		this.map.restore();

		Minigames.getOnlinePlayers().forEach((player) -> player.queueTeleport(this.map.spawnLocation()));
	}

	@Override
	public void onStart() {
		this.alive = Utils.getOnlinePlayersUuidSet();
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		Minigames.getOnlinePlayers().forEach((p) -> {
			if (this.alive.contains(p.getUniqueId())) {
				TntRun.this.removeBlocks(p);
			}
		});

		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return this.alive.size() < 2;
	}

	@Override
	public void onEnd() {
		this.endGame(this.alive, true);
		this.removedBlocks = null;
		this.alive = null;
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		if (!GameState.getCurrentState().gameIsRunning() ||
				!this.alive.contains(event.getPlayer().getUniqueId())) {
			return;
		}

		final MPlayer player = new MPlayer(event);

		final Block belowPlayer = event.getFrom().getBlock().getRelative(BlockFace.DOWN);

		if (belowPlayer.getType().equals(Material.RED_TERRACOTTA)) {
			this.alive.remove(player.getUniqueId());
			if (this.alive.size() > 1) {
				this.sendMessage(player.getDisplayName().append(Component.text(" has died. " + this.alive.size() + " players left.", NamedTextColor.GRAY)));
			} else {
				this.sendMessage(player.getDisplayName().append(Component.text(" has died.", NamedTextColor.GRAY)));
			}
			player.dieUp(10);
			return;
		}

		removeBlocks(player);
	}

	private void removeBlocks(final MPlayer player) {
		final Deque<Block> toRemove = new ArrayDeque<>();

		final double offset = 0.31;

		final Location loc = player.getLocation();
		loc.setY(loc.getY() - 1);

		final Block mainBlock = loc.getBlock();
		toRemove.add(mainBlock);

		final double origX = loc.getX();
		final double origZ = loc.getZ();

		final double[] xValues = new double[] {origX-offset, origX, origX+offset};
		final double[] zValues = new double[] {origZ-offset, origZ, origZ+offset};

		for (final double x : xValues) {
			for (final double z : zValues) {
				loc.setX(x);
				loc.setZ(z);
				addIfNotMain(toRemove, loc, mainBlock);
			}
		}

		Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), () -> {
			if (this.removedBlocks == null) {
				// game ended
				return;
			}

			while(!toRemove.isEmpty()) {
				final Block block = toRemove.pop();
				if ((block.getType() != this.map.floorMaterial()) || this.removedBlocks.contains(block)) {
					continue;
				}

				this.removedBlocks.add(block);
				block.setType(Material.AIR);
			}
		}, 7);
	}

	private void addIfNotMain(final Deque<Block> stack, final Location loc, final Block mainBlock) {
		final Block block = loc.getBlock();
		if (!mainBlock.equals(block)) {
			stack.add(block);
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
		player.dieTo(this.map.spawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

}
