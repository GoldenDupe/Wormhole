package bet.astral.wormhole.managers;

/**
 * Tickable manager
 */
public interface TickableManager {
    /**
     * Starts the manager to tick.
     * @throws IllegalStateException if the manager is already ticking
     */
    void startTicking() throws IllegalStateException;

    /**
     * Stops the manager from ticking
     */
    void stopTicking();
}
