package xyz.goldendupe.utils;

// Implemented from Dev Render System (net.dev.renderer)

/**
 * Ripped from net.dev.astral.api (with some tweaks to rid of the event manager)
 */
@SuppressWarnings("unused")
public final class Timer {

    private long last;

    /**
     * Resets the timer.
     */
    public void reset() {
        last = (System.nanoTime() / 1000000L);
    }

    /**
     * Gets the time since last reset.
     */
    public long get() {
        return (System.nanoTime() / 1000000L) - last;
    }

    /**
     * Sets the time since last reset.
     */
    public void set(long last) {
        this.last = last;
    }

    /**
     * Checks to see if the timer has reached the specified milliseconds.
     */
    public boolean hasReached(long millis) {
        return (System.nanoTime() / 1000000L) - last >= millis;
    }

    /**
     * Checks to see if the timer has reached the specified milliseconds, gives the option to reset the timer.
     */
    public boolean hasReached(long millis, boolean reset) {
        if ((System.nanoTime() / 1000000L) - last >= millis) {
            if (reset) this.reset();
            return true;
        }
        return false;
    }

}
