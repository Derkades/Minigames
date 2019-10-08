package xyz.derkades.minigames.constants;

public class BoardConfig {

	public static final int DIE_WINNER_STEPS_MIN = 2;
	public static final int DIE_WINNER_STEPS_MAX = 10;
	public static final int DIE_LOSER_STEPS_MIN = 1;
	public static final int DIE_LOSER_STEPS_MAX = 4;

	public static final int DIE_MENU_INTERVAL_TICKS = 5;
	public static final int DIE_MENU_DURATION_SECONDS = 3;
	public static final int DIE_MENU_FINAL_STATIC_SECONDS = 2;

	public static final int TILE_TITLE_DURATION_TICKS = 3*20;
	public static final int TILE_TELEPORT_DELAY = 10;

	public static final String[] TITLE_WON = {
			"You won!",
			DIE_WINNER_STEPS_MIN + "-" + DIE_WINNER_STEPS_MAX + " tiles forward"
	};

	public static final String[] TITLE_LOST = {
			"You lost",
			DIE_LOSER_STEPS_MIN + "-" + DIE_LOSER_STEPS_MAX + " tiles forward"
	};

	public static final int TILE_COINS_GET_AMOUNT = 10;
	public static final int TILE_COINS_LOSE_AMOUNT = 10;

	public static final int TILE_MOVE_BACKWARDS_AMOUNT = 3;
	public static final int TILE_MOVE_FORWARDS_AMOUNT = 3;

}
