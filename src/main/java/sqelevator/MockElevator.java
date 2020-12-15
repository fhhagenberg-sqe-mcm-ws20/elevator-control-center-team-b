package sqelevator;

import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.model.Floor;
import at.fhhagenberg.model.IBuildingElevator;
import at.fhhagenberg.model.IFloor;

import java.rmi.RemoteException;
import java.util.Arrays;

public class MockElevator implements IElevator {
    private final IBuildingElevator[] elevators;
    private final IFloor[] floors;
    private Long clockTick = 1L;

    // Mock data
    boolean[] temp = new boolean[5];
    boolean[] tempButton = new boolean[5];

    public MockElevator() {
        Arrays.fill(temp, true);
        tempButton[0] = true;
        floors = new IFloor[5];
        floors[0] = new Floor(0, false, true);
        floors[1] = new Floor(1, false, false);
        floors[2] = new Floor(2, true, false);
        floors[3] = new Floor(3, false, false);
        floors[4] = new Floor(4, true, true);

        elevators = new Elevator[9];
        elevators[0] = new Elevator(0, 5, 200, 300);
        elevators[1] = new Elevator(1, IBuildingElevator.Direction_State.DOWN.value(), 2, tempButton, IBuildingElevator.Door_State.CLOSED.value(), 3, 30, 2, 1500, 10, temp, 2);
        elevators[2] = new Elevator(2, IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, new boolean[5], IBuildingElevator.Door_State.OPEN.value(), 1, 10, 0, 1500, 10, temp, 0);
        elevators[3] = new Elevator(3, 5, 250, 300);
        elevators[4] = new Elevator(3, IBuildingElevator.Direction_State.DOWN.value(), 2, tempButton, IBuildingElevator.Door_State.CLOSED.value(), 3, 30, 2, 1500, 10, temp, 4);
        elevators[5] = new Elevator(5, IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, new boolean[5], IBuildingElevator.Door_State.OPEN.value(), 1, 10, 0, 1500, 10, temp, 0);
        elevators[6] = new Elevator(6, 5, 150, 300);
        elevators[7] = new Elevator(7, IBuildingElevator.Direction_State.DOWN.value(), 2, tempButton, IBuildingElevator.Door_State.CLOSED.value(), 3, 30, 2, 360, 300, temp, 2);
        elevators[8] = new Elevator(8, IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, new boolean[5], IBuildingElevator.Door_State.OPEN.value(), 1, 10, 0, 420, 400, temp, 0);
    }

    private void checkElevatorBounds(int elevator) throws RemoteException {
        if (elevator >= elevators.length) {
            throw new RemoteException();
        }
    }

    private void checkFloorBounds(int floor) throws RemoteException {
        if (floor >= floors.length) {
            throw new RemoteException();
        }
    }

    @Override
    public int getCommittedDirection(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators[elevatorNumber].getDirection();
    }

    @Override
    public int getElevatorAccel(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators[elevatorNumber].getAcceleration();
    }

    @Override
    public boolean getElevatorButton(int elevatorNumber, int floor) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        checkFloorBounds(floor);
        return elevators[elevatorNumber].getFloorButtons()[floor];
    }

    @Override
    public int getElevatorDoorStatus(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators[elevatorNumber].getDoorState();
    }

    @Override
    public int getElevatorFloor(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators[elevatorNumber].getNearestFloor();
    }

    @Override
    public int getElevatorNum() throws RemoteException {
        return elevators.length;
    }

    @Override
    public int getElevatorPosition(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators[elevatorNumber].getPositionFromGround();
    }

    @Override
    public int getElevatorSpeed(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators[elevatorNumber].getSpeed();
    }

    @Override
    public int getElevatorWeight(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators[elevatorNumber].getWeight();
    }

    @Override
    public int getElevatorCapacity(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators[elevatorNumber].getCapacity();
    }

    @Override
    public boolean getFloorButtonDown(int floor) throws RemoteException {
        checkFloorBounds(floor);
        return floors[floor].isDownButton();
    }

    @Override
    public boolean getFloorButtonUp(int floor) throws RemoteException {
        checkFloorBounds(floor);
        return floors[floor].isUpButton();
    }

    @Override
    public int getFloorHeight() throws RemoteException {
        return 10;
    }

    @Override
    public int getFloorNum() throws RemoteException {
        return floors.length;
    }

    @Override
    public boolean getServicesFloors(int elevatorNumber, int floor) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        checkFloorBounds(floor);
        return elevators[elevatorNumber].getFloorServices()[floor];
    }

    @Override
    public int getTarget(int elevatorNumber) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        return elevators[elevatorNumber].getFloorTarget();
    }

    @Override
    public void setCommittedDirection(int elevatorNumber, int direction) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        elevators[elevatorNumber].setDirection(direction);
    }

    @Override
    public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        checkFloorBounds(floor);
        elevators[elevatorNumber].setServicesFloor(floor, service);
    }

    @Override
    public void setTarget(int elevatorNumber, int target) throws RemoteException {
        checkElevatorBounds(elevatorNumber);
        elevators[elevatorNumber].setFloorTarget(target);
    }

    @Override
    public long getClockTick() throws RemoteException {
        return clockTick += 1;
    }
}
