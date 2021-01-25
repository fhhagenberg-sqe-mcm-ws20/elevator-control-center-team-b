package at.fhhagenberg;

import at.fhhagenberg.controller.MainController;
import at.fhhagenberg.converter.ModelConverter;
import at.fhhagenberg.model.Building;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import sqelevator.IElevator;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class ElevatorControlSystem implements RemoteExceptionListener {

    private IElevator controller;
    private ModelConverter modelConverter;
    private final AutomaticMode mode = new AutomaticMode();
    @Getter
    private final SimpleBooleanProperty systemConnected = new SimpleBooleanProperty(false);
    private String connectionString;

    public ElevatorControlSystem(IElevator mockElevator) {
        modelConverter = new ModelConverter(mockElevator);
        controller = mockElevator;
        systemConnected.set(true);
    }

    public ElevatorControlSystem(String connectionString) {
        this.connectionString = connectionString;
        reconnectToSimulator();
    }

    public void reconnectToSimulator() {
        systemConnected.set(false);
        Runnable runnable = () -> {
            while (!systemConnected.get()) {
                try {
                    controller = (IElevator) Naming.lookup(connectionString);
                    modelConverter = new ModelConverter(controller);
                    systemConnected.set(true);
                } catch (Exception e) {
                    systemConnected.set(false);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    systemConnected.set(false);
                }
            }
        };
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        t.start();
    }

    public Building initBuilding() throws RemoteException {
        return modelConverter.init();
    }

    public void updateMode(Building building, MainController mainController) {
        if (building == null) {
            return;
        }

        try {
            if (mainController.autoMode) {
                mode.update(building);
            }
        } catch (Exception e) {
            reconnectToSimulator();
            Platform.runLater(mainController::clearNotifications);
        }
    }

    public void update(Building building, MainController mainController) {
        if (building == null) {
            reconnectToSimulator();
            return;
        }

        try {
            modelConverter.update(building);
        } catch (Exception e) {
            reconnectToSimulator();
            Platform.runLater(mainController::clearNotifications);
        }
    }

    @Override
    public void onException() {
        reconnectToSimulator();
    }
}
