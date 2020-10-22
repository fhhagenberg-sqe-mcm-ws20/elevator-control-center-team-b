package at.fhhagenberg.elevatorservice;

import at.fhhagenberg.elevator.IElevator;
import at.fhhagenberg.elevator.MockElevator;
import at.fhhagenberg.floor.IFloor;
import at.fhhagenberg.floor.MockFloor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Mock class for elevator system
 * Has 3 elevators, 5 floors, 1 active, 2 inactive
 */
public class MockElevatorSystem implements IElevatorSystem{

    @Getter
    private final int elevatorCount = 3;
    @Getter
    private final int floorHeight = 10;
    @Getter
    private final int floorCount = 5;
    @Getter
    private final long clockTick = 100L;

    private IElevator[] elevators;
    private IFloor[] floors;

    public MockElevatorSystem() {
        boolean[] temp = new boolean[floorCount];
        boolean[] tempButton = new boolean[floorCount];
        Arrays.fill(temp, true);
        tempButton[0] = true;

        floors = new IFloor[5];
        floors[0] = new MockFloor(false, true);
        floors[1] = new MockFloor(false, false);
        floors[2] = new MockFloor(false, false);
        floors[3] = new MockFloor(false, false);
        floors[4] = new MockFloor(false, false);

        this.elevators = new MockElevator[elevatorCount];
        this.elevators[0] = new MockElevator(floorCount, 200, 10);
        this.elevators[1] = new MockElevator(IElevator.Direction_State.down.value(), 2, tempButton, IElevator.Door_State.closed.value(), 3, 30, 2, 1500, 10, temp, 0);
        this.elevators[2] = new MockElevator(IElevator.Direction_State.uncommitted.value(), 2, new boolean[floorCount], IElevator.Door_State.open.value(), 1, 10, 0, 1500, 10, temp, 0);
    }

    @Override
    public IElevator getElevator(int elevatorNumber) {
        return elevators[elevatorNumber];
    }

    @Override
    public boolean getFloorButtonUp(int floor) {
        return floors[floor].isUpButton();
    }

    @Override
    public boolean getFloorButtonDown(int floor) {
        return floors[floor].isDownButton();
    }
}
