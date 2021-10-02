package derkades.minigames.games;

public enum GameLabel {

	PLAYER_COMBAT, ENTITY_COMBAT, BLOCKS, LARGE_MAP, PARKOUR, TEAMS, NO_TEAMS, SINGLEPLAYER, MULTIPLAYER, THIRD_PERSON, ELYTRA, DROPPER;

	@Override
	public String toString() {
		return "#" + this.name().toLowerCase().replace("_", "-");
	}

}
