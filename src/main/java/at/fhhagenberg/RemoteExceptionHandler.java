package at.fhhagenberg;

import java.util.ArrayList;

interface RemoteExceptionListener {
    void onException();
}

public final class RemoteExceptionHandler {

    private static RemoteExceptionHandler instance;
    private final ArrayList<RemoteExceptionListener> observers = new ArrayList<>();

    private RemoteExceptionHandler() {
    }

    /**
     * Singleton get instance
     *
     * @return RemoteExceptionHandler
     */
    public static RemoteExceptionHandler instance() {
        if (instance == null) {
            instance = new RemoteExceptionHandler();
        }

        return instance;
    }

    /**
     * Subscribe to updates
     *
     * @param listener observer
     */
    public void addObserver(RemoteExceptionListener listener) {
        observers.add(listener);
    }

    /**
     * Notify all observers
     */
    public void update() {
        observers.forEach(RemoteExceptionListener::onException);
    }


}