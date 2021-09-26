package derkades.minigames.games.breaktheblock;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class HollowHills extends BreakTheBlockMap {

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public @NotNull String getName() {
		return "Hollow Hills";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.BTB_HOLLOWHILLS;
	}

	@Override
	public String getCredits() {
		return "Sneewie";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "btb_hollowhills";
	}

	@Override
	public void onPreStart() {
		this.getWorld().getBlockAt(0, 64, 0).setType(Material.GOLD_BLOCK);
		BlockUtils.fillArea(this.getWorld(), -1, 54, -17, 1, 54, -17, Material.COBBLED_DEEPSLATE_WALL);
		BlockUtils.fillArea(this.getWorld(), -1, 54, 17, 1, 54, 17, Material.COBBLED_DEEPSLATE_WALL);
	}

	@Override
	public void onStart() {
		BlockUtils.fillArea(this.getWorld(), -1, 54, -17, 1, 54, -17, Material.AIR);
		BlockUtils.fillArea(this.getWorld(), -1, 54, 17, 1, 54, 17, Material.AIR);
	}

	@Override
	Location[] getStartLocations() {
		return new Location[] {
				new Location(this.getWorld(), 0.5, 54, 19.5, -180, 0),
				new Location(this.getWorld(), 0.5, 54, -18.5, 0, 0),
		};
	}

	@Override
	boolean canTakeDamage(final MPlayer player) {
		return true;
	}

	@Override
	int getMinimumY() {
		return 50;
	}

}
