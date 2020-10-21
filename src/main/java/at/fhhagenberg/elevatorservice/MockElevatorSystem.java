package at.fhhagenberg.elevatorservice;

import at.fhhagenberg.elevator.ElevatorConstants;
import at.fhhagenberg.elevator.MockElevator;
import at.fhhagenberg.floor.IFloor;
import at.fhhagenberg.floor.MockFloor;
import lombok.Getter;
import lombok.Setter;

import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Mock class for elevator system
 * Has 3 elevators, 5 floors, 1 active, 2 inactive
 */
public class MockElevatorSystem {

    @Getter
    private static final int ELEVATOR_COUNT = 3;
    @Getter
    private static final int FLOOR_HEIGHT = 10;
    @Getter
    private static final int FLOOR_COUNT = 5;
    @Getter
    private static final long TICK = 100L;

    @Getter
    @Setter
    private MockElevator[] elevators;
    @Getter
    @Setter
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
        this.elevators[1] = new MockElevator(ElevatorConstants.Direction_State.down, 2, tempButton, ElevatorConstants.Door_State.closed, 3, 30, 2, 1500, 10, temp, 0);
        this.elevators[2] = new MockElevator(ElevatorConstants.Direction_State.uncommitted, 2, new boolean[FLOOR_COUNT], ElevatorConstants.Door_State.open, 1, 10, 0, 1500, 10, temp, 0);
    }
}
