package derkades.minigames.modules;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.BigDripleaf;
import org.bukkit.block.data.type.BigDripleaf.Tilt;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;

public class FastDripleafReset extends Module {

	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		if (event.getAction() != Action.PHYSICAL ||
				event.getClickedBlock().getType() != Material.BIG_DRIPLEAF) {
			return;
		}

		final Block block = event.getClickedBlock();

		new BukkitRunnable() {

			int count = 0;

			@Override
			public void run() {
				if (block.getType() != Material.BIG_DRIPLEAF) {
					Logger.warning("Big dripleaf disappeared");
					this.cancel();
					return;
				}

				final BigDripleaf dripleaf = (BigDripleaf) block.getBlockData();
				Logger.debug("Current tilt: %s", dripleaf.getTilt());
				if (dripleaf.getTilt() == Tilt.FULL) {
					dripleaf.setTilt(Tilt.NONE);
					block.setBlockData(dripleaf);
					this.cancel();
					return;
				}

				if (this.count > 200) {
					Logger.warning("Waited too long for dripleaf to tilt, cancelling task");
					this.cancel();
					return;
				}

				this.count++;
			}
		}.runTaskTimer(Minigames.getInstance(), 10, 1);
	}

}
