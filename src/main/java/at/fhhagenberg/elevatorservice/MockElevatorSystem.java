package at.fhhagenberg.elevatorservice;

import at.fhhagenberg.elevator.IElevator;
import at.fhhagenberg.elevator.MockElevator;
import at.fhhagenberg.floor.IFloor;
import at.fhhagenberg.floor.MockFloor;

import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Mock class for elevator system
 * Has 3 elevators, 5 floors, 1 active, 2 inactive
 */
public class MockElevatorSystem implements IElevatorSystem {

    private static final int ELEVATOR_COUNT = 3;
    private static final int FLOOR_HEIGHT = 10;
    private static final int FLOOR_COUNT = 5;
    private static final long TICK = 100L;

    private IElevator[] elevators;
    private IFloor[] floors;

    public MockElevatorSystem() {
        boolean[] temp = new boolean[FLOOR_COUNT];
        boolean[] tempButton = new boolean[FLOOR_COUNT];
        Arrays.fill(temp, true);
        tempButton[0] = true;

        floors = new IFloor[5];
        floors[0] = new MockFloor(false, true);
        floors[1] = new MockFloor(false, false);
        floors[2] = new MockFloor(false, false);
        floors[3] = new MockFloor(false, false);
        floors[4] = new MockFloor(false, false);

        this.elevators = new MockElevator[ELEVATOR_COUNT];
        this.elevators[0] = new MockElevator(FLOOR_COUNT, 200, 10);
        this.elevators[1] = new MockElevator(IElevator.Direction_State.down, 2, tempButton, IElevator.Door_State.closed, 3, 30, 2, 1500, 10, temp, 0);
        this.elevators[2] = new MockElevator(IElevator.Direction_State.uncommitted, 2, new boolean[FLOOR_COUNT], IElevator.Door_State.open, 1, 10, 0, 1500, 10, temp, 0);
    }

    @Override
    public IElevator getElevator(int elevatorNumber) {
        return elevators[elevatorNumber];
    }

    @Override
    public int getCommittedDirection(int elevatorNumber) throws RemoteException {
        return elevators[elevatorNumber].getCommittedDirection();
    }

    @Override
    public int getElevatorAccel(int elevatorNumber) throws RemoteException {
        return elevators[elevatorNumber].getAccel();
    }

    @Override
    public boolean getElevatorButton(int elevatorNumber, int floor) throws RemoteException {
        return elevators[elevatorNumber].getButtonStatus(floor);
    }

    @Override
    public int getElevatorDoorStatus(int elevatorNumber) throws RemoteException {
        return elevators[elevatorNumber].getDoorStatus();
    }

    @Override
    public int getElevatorFloor(int elevatorNumber) throws RemoteException {
        return elevators[elevatorNumber].getCurrentFloor();
    }

    @Override
    public int getElevatorNum() throws RemoteException {
        return ELEVATOR_COUNT;
    }

    @Override
    public int getElevatorPosition(int elevatorNumber) throws RemoteException {
        return elevators[elevatorNumber].getPosition();
    }

    @Override
    public int getElevatorSpeed(int elevatorNumber) throws RemoteException {
        return elevators[elevatorNumber].getSpeed();
    }

    @Override
    public int getElevatorWeight(int elevatorNumber) throws RemoteException {
        return elevators[elevatorNumber].getWeight();
    }

    @Override
    public int getElevatorCapacity(int elevatorNumber) throws RemoteException {
        return elevators[elevatorNumber].getCapacity();
    }

    @Override
    public boolean getFloorButtonDown(int floor) throws RemoteException {
        return floors[floor].downButtonActive();
    }

    @Override
    public boolean getFloorButtonUp(int floor) throws RemoteException {
        return floors[floor].upButtonActive();
    }

    @Override
    public int getFloorHeight() throws RemoteException {
        return FLOOR_HEIGHT;
    }

    @Override
    public int getFloorNum() throws RemoteException {
        return FLOOR_COUNT;
    }

    @Override
    public boolean getServicesFloors(int elevatorNumber, int floor) throws RemoteException {
        return elevators[elevatorNumber].servesFloor(floor);
    }

    @Override
    public int getTarget(int elevatorNumber) throws RemoteException {
        return elevators[elevatorNumber].getTarget();
    }

    @Override
    public void setCommittedDirection(int elevatorNumber, int direction) throws RemoteException {
        elevators[elevatorNumber].setDirection(direction);
    }

    @Override
    public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws RemoteException {
        elevators[elevatorNumber].setServicesFloor(floor, service);
    }

    @Override
    public void setTarget(int elevatorNumber, int target) throws RemoteException {
        elevators[elevatorNumber].setTarget(target);
    }

    @Override
    public long getClockTick() throws RemoteException {
        return TICK;
    }
}
