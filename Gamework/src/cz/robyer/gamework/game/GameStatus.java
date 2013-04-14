package cz.robyer.gamework.game;

/**
 * Represents status of game service.
 * @author Robert Pösel
 */
public enum GameStatus {
	GAME_NONE, 		/** No game is loaded. */
	GAME_LOADING, 	/** Game is loading needed resources. */
	GAME_WAITING,	/** Game was loaded and now is waiting for starting event.
						Only location (no timer) updates are running here (but they are NOT distributed to Scenario objects). */  
	GAME_RUNNING,	/** Game was started and now is running. */
	GAME_PAUSED,	/** Game was paused and now is waiting for starting event.
						No location (or timer) updates are running here. */
	GAME_WON,		/** Game was completed and user won it. */
	GAME_LOST, 		/** Game was completed and user lost it. */
};
