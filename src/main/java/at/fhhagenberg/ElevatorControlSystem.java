package at.fhhagenberg;

import at.fhhagenberg.converter.ModelConverter;
import at.fhhagenberg.model.Building;
import sqelevator.IElevator;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class ElevatorControlSystem {

    private IElevator controller;
    private ModelConverter modelConverter;
    private boolean systemConnected;
    private String connectionString;

    public ElevatorControlSystem(IElevator mockElevator){
        modelConverter = new ModelConverter(mockElevator);
        controller = mockElevator;
        systemConnected = true;
    }

    public ElevatorControlSystem(String connectionString){
        this.connectionString = connectionString;
        reconnectToSimulator();
    }

    public boolean getSystemConnected(){
        return systemConnected;
    }

    public void reconnectToSimulator() {
        systemConnected = false;
        Runnable runnable = () -> {
            while(!systemConnected){
                try{
                    controller = (IElevator) Naming.lookup(connectionString);
                    modelConverter = new ModelConverter(controller);
                    systemConnected = true;
                }catch(Exception e){
                    systemConnected = false;
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

    public void update(Building building){
        try{
            modelConverter.update(building);
        }catch(Exception e){
            reconnectToSimulator();
        }
    }
}
