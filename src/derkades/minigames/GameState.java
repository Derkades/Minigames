package derkades.minigames;

import derkades.minigames.games.Game;
import derkades.minigames.games.GameMap;

public enum GameState {

	IDLE                (false, false, false),
	IDLE_MAINTENANCE    (false, false, false),
	COUNTDOWN           (true,  false, false),
	RUNNING_COUNTDOWN   (true,  true,  false),
	RUNNING_STARTED     (true,  true,  true),
	RUNNING_SKIPPED     (true,  true,  false),
	RUNNING_ENDED_EARLY (true,  true,  false);

	private final boolean hasGame;
	private final boolean inGame;
	private final boolean running;

	GameState(final boolean hasGame, final boolean inGame, final boolean running) {
		if (inGame && !hasGame) {
			throw new IllegalStateException("hasGame must be true if inGame is true");
		}
		this.hasGame = hasGame;
		this.inGame = inGame;
		this.running = running;
	}

	/**
	 * @return True if the current game is known in this state. Note that the game is not necessarily running.
	 */
	public boolean hasGame() {
		return this.hasGame;
	}

	/**
	 * @return True if players are physically in a game world.
	 */
	public boolean isInGame() {
		return this.inGame;
	}

	/**
	 * @return True if players are physically in a game world, and the game is running (past the prestart phase)
	 */
	public boolean gameIsRunning() {
		return this.running;
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

	public static boolean currentGameIsRunning() {
		return CURRENT_STATE.gameIsRunning();
	}

	public static boolean currentlyHasGame() {
		return CURRENT_STATE.hasGame();
	}

}
