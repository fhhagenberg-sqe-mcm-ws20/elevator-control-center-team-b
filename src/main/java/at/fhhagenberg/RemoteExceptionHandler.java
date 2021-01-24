package at.fhhagenberg;

import java.util.ArrayList;

interface RemoteExceptionListener {
    void onException();
}

public final class RemoteExceptionHandler {

    private static RemoteExceptionHandler INSTANCE;
    private final ArrayList<RemoteExceptionListener> observers = new ArrayList<>();

    private RemoteExceptionHandler() {
    }

    /**
     * Singleton get instance
     * @return RemoteExceptionHandler
     */
    public static RemoteExceptionHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteExceptionHandler();
        }

        return INSTANCE;
    }

    /**
     * Subscribe to updates
     * @param listener observer
     */
    public void observe(RemoteExceptionListener listener) {
        observers.add(listener);
    }

    /**
     * Notify all observers
     */
    public void update() {
        observers.forEach(RemoteExceptionListener::onException);
    }


}