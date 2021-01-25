package sqelevator;

import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.model.Floor;
import at.fhhagenberg.model.IBuildingElevator;
import at.fhhagenberg.model.IFloor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockElevatorAuto implements IElevator {
    private final ArrayList<IBuildingElevator> elevators = new ArrayList<>();
    private final ArrayList<IFloor> floors = new ArrayList<>();
    private final Long clockTick = 1L;

    public MockElevatorAuto() {
        ArrayList<Integer> floorButtons = IntStream.range(1, 2).boxed().collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Integer> floorButtons2 = IntStream.range(4, 4).boxed().collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Integer> servicedFloors = IntStream.range(0, 5).boxed().collect(Collectors.toCollection(ArrayList::new));

        floors.add(new Floor(0, true, false));
        floors.add(new Floor(1, true, false));
        floors.add(new Floor(2, false, false));
        floors.add(new Floor(3, true, false));
        floors.add(new Floor(4, false, true));

        elevators.add(new Elevator(0, IBuildingElevator.Direction_State.UP.value(), 2, floorButtons, IBuildingElevator.Door_State.OPEN.value(), 0, 10, 2, 100, 10, servicedFloors, 0, null));
        elevators.add(new Elevator(1, IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, new ArrayList<>(), IBuildingElevator.Door_State.OPEN.value(), 2, 20, 2, 100, 10, servicedFloors, 2, null));
        elevators.add(new Elevator(2, IBuildingElevator.Direction_State.DOWN.value(), 2, floorButtons2, IBuildingElevator.Door_State.OPEN.value(), 4, 10, 0, 800, 10, servicedFloors, 4, null));
    }

    private void checkElevatorBounds(int elevator) throws RemoteException {
        if (elevator >= elevators.size()) {
            throw new RemoteException();
        }
    }

    private void checkFloorBounds(int floor) throws RemoteException {
        if (floor >= floors.size()) {
            throw new RemoteException();
        }
    }

    @Override
    public int getCommittedDirection(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators.get(elevatorNumber).getDirection();
    }

    @Override
    public int getElevatorAccel(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators.get(elevatorNumber).getAcceleration();
    }

    @Override
    public boolean getElevatorButton(int elevatorNumber, int floor) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        checkFloorBounds(floor);
        return elevators.get(elevatorNumber).getFloorButtons().contains(floor);
    }

    @Override
    public int getElevatorDoorStatus(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators.get(elevatorNumber).getDoorState();
    }

    @Override
    public int getElevatorFloor(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators.get(elevatorNumber).getNearestFloor();
    }

    @Override
    public int getElevatorNum() throws RemoteException {
        return elevators.size();
    }

    @Override
    public int getElevatorPosition(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators.get(elevatorNumber).getPositionFromGround();
    }

    @Override
    public int getElevatorSpeed(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators.get(elevatorNumber).getSpeed();
    }

    @Override
    public int getElevatorWeight(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators.get(elevatorNumber).getWeight();
    }

    @Override
    public int getElevatorCapacity(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators.get(elevatorNumber).getCapacity();
    }

    @Override
    public boolean getFloorButtonDown(int floor) throws RemoteException {
        checkFloorBounds(floor);
        return floors.get(floor).isDownButtonActive();
    }

    @Override
    public boolean getFloorButtonUp(int floor) throws RemoteException {
        checkFloorBounds(floor);
        return floors.get(floor).isUpButtonActive();
    }

    @Override
    public int getFloorHeight() throws RemoteException {
        return 10;
    }

    @Override
    public int getFloorNum() throws RemoteException {
        return floors.size();
    }

    @Override
    public boolean getServicesFloors(int elevatorNumber, int floor) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        checkFloorBounds(floor);
        return elevators.get(elevatorNumber).getFloorServices().contains(floor);
    }

    @Override
    public int getTarget(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators.get(elevatorNumber).getFloorTarget();
    }

    @Override
    public void setCommittedDirection(int elevatorNumber, int direction) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        elevators.get(elevatorNumber).setDirection(direction);
    }

    @Override
    public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        checkFloorBounds(floor);
        elevators.get(elevatorNumber).setServicesFloor(floor, service);
    }

    @Override
    public void setTarget(int elevatorNumber, int target) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        elevators.get(elevatorNumber).setFloorTarget(target);
    }

    @Override
    public long getClockTick() throws RemoteException {
        return clockTick + 1;
    }

    public void setFloorButtonUp(int floor, boolean upActive) {
        floors.get(floor).setUpButtonActive(upActive);
    }
}