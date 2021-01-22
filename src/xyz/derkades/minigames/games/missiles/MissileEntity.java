package xyz.derkades.minigames.games.missiles;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;

public class MissileEntity extends MissileObject {
	
	private final EntityType type;
	
	MissileEntity(final int lr, final int ud, final int fb, final EntityType type) {
		super(lr, ud, fb);
		this.type = type;
	}

	@Override
	void place(final Location location, final BlockFace[] directions) {
		location.getWorld().spawnEntity(location, this.type);
	}

}
