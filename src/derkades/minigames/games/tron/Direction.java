package derkades.minigames.games.tron;

enum Direction {

	NORTH(180),
	EAST(270),
	SOUTH(0),
	WEST(90),

	;

	float yaw;

	Direction(final float yaw){
		this.yaw = yaw;
	}

}
