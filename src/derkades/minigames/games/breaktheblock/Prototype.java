package derkades.minigames.games.breaktheblock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Prototype extends BreakTheBlockMap {

	@Override
	public @NotNull String getName() {
		return "Prototype";
	}

	@Override
	public void onPreStart() {
		new Location(this.getWorld(), 16, 66, 0).getBlock().setType(Material.GOLD_BLOCK);
	}

	@Override
	Location[] getStartLocations() {
		return new Location[] {
				new Location(this.getWorld(), 0.5, 65, 0.5, -90f, 0f),
		};
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.BTB_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.SMALL;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "breaktheblock_prototype";
	}
	
	@Override
	public boolean isDisabled() {
		return true;
	}

	@Override
	boolean canTakeDamage(final MPlayer player) {
		final Block block = player.getBlockOn();
		return
				block.getType() == Material.RED_CONCRETE ||
				block.getRelative(BlockFace.DOWN).getType() == Material.RED_CONCRETE ||
				block.getType() == Material.GOLD_BLOCK ||
				block.getRelative(BlockFace.DOWN).getType() == Material.GOLD_BLOCK;
	}

}
