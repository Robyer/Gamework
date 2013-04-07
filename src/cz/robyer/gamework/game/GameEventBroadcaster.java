package cz.robyer.gamework.game;

/**
 * Interface for broadcasting {@link GameEvent}s.
 * @author Robert Pösel
 */
public interface GameEventBroadcaster {
	public void broadcastEvent(GameEvent event);
}
