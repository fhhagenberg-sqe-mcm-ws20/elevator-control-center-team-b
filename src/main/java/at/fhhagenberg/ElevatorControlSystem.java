package at.fhhagenberg;

import at.fhhagenberg.controller.MainController;
import at.fhhagenberg.converter.ModelConverter;
import at.fhhagenberg.model.Building;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import sqelevator.IElevator;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class ElevatorControlSystem implements RemoteExceptionListener {

    private IElevator controller;
    private ModelConverter modelConverter;
    private AutomaticMode mode = new AutomaticMode();
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

    public SimpleBooleanProperty getSystemConnected() {
        return systemConnected;
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

    public void update(Building building, MainController mainController) {

        try {
            modelConverter.update(building);
            if (mainController.autoMode) {
                mode.update(building);
            }
        } catch (Exception e) {
            System.out.println(e);
            reconnectToSimulator();
            Platform.runLater(mainController::clearNotifications);
        }
    }

    @Override
    public void onException() {
        reconnectToSimulator();
    }
}
