package cz.robyer.gamework.game;

/**
 * Represents game event.
 * @author Robert Pösel
 */
public enum GameEvent {
	GAME_START,			/** Game start/continue. */
	GAME_PAUSE,			/** Event for pausing game service. */
	GAME_WIN,			/** Player won the game. */
	GAME_LOSE,			/** Player lost the game. */
	GAME_QUIT,			/** Event for stop game service. */
	UPDATED_LOCATION, 	/** Player's location was updated. */
	UPDATED_TIME,		/** Game time was updated. */
	UPDATED_MESSAGES,	/** Game message was received */
}
