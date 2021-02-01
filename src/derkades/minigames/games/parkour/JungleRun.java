package derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;

public class JungleRun extends ParkourMap {

	@Override
	public String getName() {
		return "Jungle";
	}

	@Override
	public Location getStartLocation() {
		return new Location(this.getWorld(), 18.5, 66, 20.5, -90f, 0f);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(this.getWorld(), 31.5, 80, 25.5, -135f, 0f);
	}

	@Override
	public boolean hasFinished(final MPlayer player, final Material blockType) {
		return blockType == Material.GOLD_BLOCK;
	}

	@Override
	public boolean hasDied(final MPlayer player, final Material blockType) {
		return  blockType == Material.SOUL_SAND ||
				blockType == Material.GREEN_WOOL ||
				blockType == Material.GREEN_CARPET ||
				blockType == Material.GREEN_TERRACOTTA;
	}

	@Override
	public boolean spectatorFreeFlight() {
		return true;
	}

	@Override
	public int getDuration() {
		return 60;
	}

	@Override
	public void onPlayerFinish(final Player player) {
		this.getWorld().spawnParticle(Particle.FLAME, 61, 72, 20, 1000, 0, 2, 2, 0.2);
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.PARKOUR_JUNGLE;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "parkour_jungle";
	}

}
