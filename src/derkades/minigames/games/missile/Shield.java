package derkades.minigames.games.missile;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import derkades.minigames.games.GameTeam;

public enum Shield {

	RED(GameTeam.RED, new MissileBlock[] {
			new MissileBlock(0, 0, 0, Material.ORANGE_STAINED_GLASS),
			new MissileBlock(-1, 0, 0, Material.RED_STAINED_GLASS),
			new MissileBlock(-2, 0, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(-3, 0, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(0, 1, 0, Material.RED_STAINED_GLASS),
			new MissileBlock(0, 2, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(0, 3, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(1, 0, 0, Material.RED_STAINED_GLASS),
			new MissileBlock(2, 0, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(3, 0, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(0, -1, 0, Material.RED_STAINED_GLASS),
			new MissileBlock(0, -2, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(0, -3, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(-1, -1, 0, Material.WHITE_STAINED_GLASS),
			new MissileBlock(1, -1, 0, Material.WHITE_STAINED_GLASS),
			new MissileBlock(-1, 1, 0, Material.WHITE_STAINED_GLASS),
			new MissileBlock(1, 1, 0, Material.WHITE_STAINED_GLASS),
			new MissileBlock(-2, 1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-3, 1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-2, -1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-3, -1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, -1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(3, -1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, 1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(3, 1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, -2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, -3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, -2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, -3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-2, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-2, -2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, -2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-3, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-2, 3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, 3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(3, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-2, -3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-3, -2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, -3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(3, -2, 0, Material.BLACK_STAINED_GLASS),
	}),

	BLUE(GameTeam.RED, new MissileBlock[] {
			new MissileBlock(0, 0, 0, Material.LIGHT_BLUE_STAINED_GLASS),
			new MissileBlock(-1, 0, 0, Material.BLUE_STAINED_GLASS),
			new MissileBlock(-2, 0, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(-3, 0, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(0, 1, 0, Material.BLUE_STAINED_GLASS),
			new MissileBlock(0, 2, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(0, 3, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(1, 0, 0, Material.BLUE_STAINED_GLASS),
			new MissileBlock(2, 0, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(3, 0, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(0, -1, 0, Material.BLUE_STAINED_GLASS),
			new MissileBlock(0, -2, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(0, -3, 0, Material.LIGHT_GRAY_STAINED_GLASS),
			new MissileBlock(-1, -1, 0, Material.WHITE_STAINED_GLASS),
			new MissileBlock(1, -1, 0, Material.WHITE_STAINED_GLASS),
			new MissileBlock(-1, 1, 0, Material.WHITE_STAINED_GLASS),
			new MissileBlock(1, 1, 0, Material.WHITE_STAINED_GLASS),
			new MissileBlock(-2, 1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-3, 1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-2, -1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-3, -1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, -1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(3, -1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, 1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(3, 1, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, -2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, -3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, -2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, -3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-2, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-2, -2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, -2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-3, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-2, 3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, 3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(3, 2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-2, -3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-3, -2, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(2, -3, 0, Material.BLACK_STAINED_GLASS),
			new MissileBlock(3, -2, 0, Material.BLACK_STAINED_GLASS),
	}),

	;

	private GameTeam team;
	private MissileObject[] objects;

	Shield(final GameTeam team, final MissileObject[] objects) {
		this.team = team;
		this.objects = objects;
	}

	public GameTeam getTeam() {
		return this.team;
	}

	public void build(final Location center, final BlockFace direction) {
		MissileObject.build(this.objects, center, direction, null);
	}

}
