package derkades.minigames.games.missile_racer;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class Prototype extends MissileRacerMap {

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.MISSILERACER_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "missileracer_prototype";
	}
	
	@Override
	public void onPreStart() {
		BlockUtils.fillArea(this.getWorld(), -5, 66, -2, -19, 66, -2, Material.OAK_FENCE);
	}
	
	@Override
	public void onTimer(final int secondsLeft) {
		BlockUtils.fillArea(this.getWorld(), -2, 75, 11, 5, 75, 11, Material.LIME_TERRACOTTA);
		BlockUtils.fillArea(this.getWorld(), -2, 75, 20, 5, 75, 20, Material.LIME_TERRACOTTA);
		BlockUtils.fillArea(this.getWorld(), -2, 75, 11, -2, 75, 20, Material.LIME_TERRACOTTA);
		BlockUtils.fillArea(this.getWorld(), 5, 75, 11, 5, 75, 20, Material.LIME_TERRACOTTA);
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), -15, 66, -4);
	}

	@Override
	public boolean isInFinishBounds(final MPlayer player) {
		return player.isIn3dBounds(this.getWorld(), 5, 75, 11, -2, 80, 20);
	}

	@Override
	public int getMinimumY() {
		return 48;
	}

}
