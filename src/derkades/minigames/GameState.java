package derkades.minigames;

import derkades.minigames.games.Game;
import derkades.minigames.games.maps.GameMap;

public enum GameState {

	IDLE(false, false),
	IDLE_MAINTENANCE(false, false),
	COUNTDOWN(true, false),
	RUNNING_COUNTDOWN(true, true),
	RUNNING_STARTED(true, true),
	RUNNING_SKIPPED(true, true),
	RUNNING_ENDED_EARLY(true, true);

	private boolean hasGame;
	private boolean inGame;

	GameState(final boolean hasGame, final boolean inGame) {
		if (inGame && !hasGame) {
			throw new IllegalStateException("hasGame must be true if inGame is true");
		}
		this.hasGame = hasGame;
		this.inGame = inGame;
	}

	public boolean hasGame() {
		return this.hasGame;
	}

	public boolean isInGame() {
		return this.inGame;
	}

	private static GameState CURRENT_STATE = GameState.IDLE_MAINTENANCE;
	private static Game<?> CURRENT_GAME = null;

	public static void setState(final GameState state) {
		if (state.hasGame) {
			throw new UnsupportedOperationException("Use setState(GameState, Game) for setting running states");
		} else {
			CURRENT_STATE = state;
			CURRENT_GAME = null;
		}
	}

	public static void setState(final GameState state, final Game<? extends GameMap> game) {
		if (state.hasGame) {
			CURRENT_STATE = state;
			CURRENT_GAME = game;
		} else {
			throw new UnsupportedOperationException("Use setState(GameState) for setting idle states");
		}
	}

	public static GameState getCurrentState() {
		return CURRENT_STATE;
	}

	public static Game<? extends GameMap> getCurrentGame() {
		if (CURRENT_STATE.hasGame) {
			return CURRENT_GAME;
		} else {
			throw new UnsupportedOperationException("Cannot get current game when in idle state");
		}
	}

	public static boolean isCurrentlyInGame() {
		return CURRENT_STATE.isInGame();
	}

	public static boolean currentlyHasGame() {
		return CURRENT_STATE.hasGame();
	}

}
