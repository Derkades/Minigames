package xyz.derkades.minigames.games.hungergames;

import org.bukkit.Location;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Windmill extends HungerGamesMap {

	@Override
	public Location[] getStartLocations() {
		return new Location[] {
				new Location(this.getWorld(), 227.5, 70.5, 203.5),
				new Location(this.getWorld(), 227.5, 70.5, 200.5),
				new Location(this.getWorld(), 234.5, 70.5, 210.5),
				new Location(this.getWorld(), 234.5, 70.5, 193.5),
				new Location(this.getWorld(), 234.5, 70.5, 193.5),
				new Location(this.getWorld(), 244.5, 70.5, 200.5),
				new Location(this.getWorld(), 244.5, 70.5, 203.5),

				new Location(this.getWorld(), 242.5, 70.5, 195.5),
				new Location(this.getWorld(), 229.5, 70.5, 208.5),
				new Location(this.getWorld(), 228.5, 70.5, 195.5),
		};
	}

	@Override
	public Location[] getLootLevelOneLocations() {
		return new Location[] {
				//center chests
				new Location(this.getWorld(), 237, 70, 199),
				new Location(this.getWorld(), 238, 70, 203),
				new Location(this.getWorld(), 238, 70, 203),
				new Location(this.getWorld(), 237, 70, 204),
				new Location(this.getWorld(), 234, 70, 204),
				new Location(this.getWorld(), 233, 70, 203),
				new Location(this.getWorld(), 234, 70, 199),
				new Location(this.getWorld(), 233, 70, 200),

				//on/near brick path
				new Location(this.getWorld(), 265, 68, 211),
				new Location(this.getWorld(), 272, 70, 220),
				new Location(this.getWorld(), 316, 71, 183),
				new Location(this.getWorld(), 291, 71, 161),
				new Location(this.getWorld(), 247, 62, 135),
				new Location(this.getWorld(), 183, 61, 185),
				new Location(this.getWorld(), 196, 62, 168),
				new Location(this.getWorld(), 153, 63, 187),
				new Location(this.getWorld(), 169, 64, 136),
				new Location(this.getWorld(), 209, 63, 84),
				new Location(this.getWorld(), 242, 64, 101),
				new Location(this.getWorld(), 194, 64, 226),
				new Location(this.getWorld(), 222, 63, 254),
				new Location(this.getWorld(), 222, 64, 273),
				new Location(this.getWorld(), 244, 66, 260),
				new Location(this.getWorld(), 263, 70, 282),
				new Location(this.getWorld(), 330, 74, 223),
				new Location(this.getWorld(), 277, 71, 249),
				new Location(this.getWorld(), 143, 71, 163),

				//orange/lemon trees
				new Location(this.getWorld(), 156, 69, 232),
				new Location(this.getWorld(), 159, 67, 219),
				new Location(this.getWorld(), 138, 67, 206),

				//blueberries/grapes
				new Location(this.getWorld(), 178, 63, 153),
				new Location(this.getWorld(), 198, 63, 127),
				new Location(this.getWorld(), 206, 63, 131),
				new Location(this.getWorld(), 228, 63, 123),
				new Location(this.getWorld(), 226, 63, 102),

				//XXX look up where these chests are and put in categories above
				new Location(this.getWorld(), 222, 72, 208),
				new Location(this.getWorld(), 351, 76, 178),
		};
	}

	@Override
	public Location[] getLootLevelTwoLocations() {
		return new Location[] {
				//house next to windmill
				new Location(this.getWorld(), 258, 69, 171),
				new Location(this.getWorld(), 252, 69, 173),

				//houses
				new Location(this.getWorld(), 273, 71, 296),
				new Location(this.getWorld(), 169, 72, 267),
				new Location(this.getWorld(), 184, 73, 277),
				new Location(this.getWorld(), 182, 74, 273),
				new Location(this.getWorld(), 234, 65, 81),
				new Location(this.getWorld(), 240, 64, 83),
				new Location(this.getWorld(), 339, 80, 139),
				new Location(this.getWorld(), 354, 81, 231),
				new Location(this.getWorld(), 273, 71, 296),
				new Location(this.getWorld(), 265, 71, 292),

				//sunflowers
				new Location(this.getWorld(), 263, 69, 101),
				new Location(this.getWorld(), 325, 86, 96),
				new Location(this.getWorld(), 305, 81, 95),
				new Location(this.getWorld(), 310, 79, 117),
				new Location(this.getWorld(), 299, 78, 110),
				new Location(this.getWorld(), 302, 72, 140),

				//lemon trees
				new Location(this.getWorld(), 302, 70, 301),
				new Location(this.getWorld(), 285, 72, 280),
		};
	}

	@Override
	public String getName() {
		return "Windmill";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.HG_WINDMILL;
	}

	@Override
	public String getCredits() {
		return "Maps list on MCProHosting";
	}

	@Override
	public Size getSize() {
		return Size.LARGE;
	}

	@Override
	public String getIdentifier() {
		return "hungergames_windmill";
	}

	@Override
	public Location getCenterLocation() {
		return new Location(this.getWorld(), 232.5, 73, 201.5);
	}

	@Override
	public double getMaxBorderSize() {
		return 400;
	}

	@Override
	public double getMinBorderSize() {
		return 40;
	}

}
