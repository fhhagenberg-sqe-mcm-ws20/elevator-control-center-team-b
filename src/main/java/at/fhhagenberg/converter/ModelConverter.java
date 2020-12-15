package at.fhhagenberg.converter;

import at.fhhagenberg.model.*;
import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ModelConverter {
    private final IElevator elevatorConnection;
    private Long lastClockTick = 1L;

    public ModelConverter(IElevator elevatorConnection) {
        this.elevatorConnection = elevatorConnection;
    }

    public Building init() throws RemoteException {
        int elevatorNumber = elevatorConnection.getElevatorNum();
        int floorNumber = elevatorConnection.getFloorNum();
        ArrayList<IFloor> floors = getFloors();
        ArrayList<IBuildingElevator> elevators = getElevators(floors);
        return new Building(elevatorNumber, elevatorConnection.getFloorHeight(), floorNumber, elevators, floors);
    }

    public void update(Building building) throws RemoteException {
        long currentClockTick = elevatorConnection.getClockTick();
        if (currentClockTick > lastClockTick) {
            List<IFloor> floors = getFloors();
            List<IBuildingElevator> elevators = getElevators(floors);
            building.update(floors, elevators);
            lastClockTick = currentClockTick;
        }
    }

    private ArrayList<IFloor> getFloors() throws RemoteException {
        ArrayList<IFloor> floors = new ArrayList<>();
        for (int i = 0; i < elevatorConnection.getFloorNum(); i++) {
            floors.add(new Floor(i, elevatorConnection.getFloorButtonUp(i), elevatorConnection.getFloorButtonDown(i)));
        }
        return floors;
    }

    private ArrayList<IBuildingElevator> getElevators(List<IFloor> floors) throws RemoteException {
        ArrayList<IBuildingElevator> elevators = new ArrayList<>();
        int numberOfElevators = elevatorConnection.getElevatorNum();
        for (int i = 0; i < numberOfElevators; i++) {
            ArrayList<Integer> floorButtonsTest = new ArrayList<>();
            ArrayList<Integer> servicedFloorsTest = new ArrayList<>();
            //boolean[] floorButtons = new boolean[floors.size()];
            //boolean[] servicedFloors = new boolean[floors.size()];
            for (int j = 0; j < floors.size(); j++) {
                IFloor currentFloor = floors.get(j);
                //servicedFloors[j] = elevatorConnection.getServicesFloors(i, currentFloor.getNumber());
                //floorButtons[j] = elevatorConnection.getElevatorButton(i, currentFloor.getNumber());
                if (elevatorConnection.getElevatorButton(i, currentFloor.getNumber())) {
                    floorButtonsTest.add(currentFloor.getNumber());
                }
                if (elevatorConnection.getServicesFloors(i, currentFloor.getNumber())) {
                    servicedFloorsTest.add(currentFloor.getNumber());
                }
            }
            /*elevators.add(new Elevator(i, elevatorConnection.getCommittedDirection(i), elevatorConnection.getElevatorAccel(i), floorButtons, elevatorConnection.getElevatorDoorStatus(i),
                    elevatorConnection.getElevatorFloor(i), elevatorConnection.getElevatorPosition(i), elevatorConnection.getElevatorSpeed(i), elevatorConnection.getElevatorWeight(i),
                    elevatorConnection.getElevatorCapacity(i), servicedFloors, elevatorConnection.getTarget(i)));
*/
            elevators.add(new Elevator(i, elevatorConnection.getCommittedDirection(i), elevatorConnection.getElevatorAccel(i), floorButtonsTest, elevatorConnection.getElevatorDoorStatus(i),
                    elevatorConnection.getElevatorFloor(i), elevatorConnection.getElevatorPosition(i), elevatorConnection.getElevatorSpeed(i), elevatorConnection.getElevatorWeight(i),
                    elevatorConnection.getElevatorCapacity(i), servicedFloorsTest, elevatorConnection.getTarget(i)));
        }
        return elevators;
    }
}

