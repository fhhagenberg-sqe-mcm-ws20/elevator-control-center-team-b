package at.fhhagenberg.model;

import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

public class ElevatorControllerTest {
    private IBuildingElevator[] elevators;
    private IFloor[] floors;
    private Building building;

    @BeforeEach
    void Init() {
        boolean[] temp = new boolean[5];
        boolean[] tempButton = new boolean[5];
        Arrays.fill(temp, true);
        tempButton[0] = true;

        floors = new IFloor[5];
        floors[0] = new Floor(false, true);
        floors[1] = new Floor(false, false);
        floors[2] = new Floor(false, false);
        floors[3] = new Floor(false, false);
        floors[4] = new Floor(false, false);

        elevators = new Elevator[3];
        elevators[0] = new Elevator(5, 200, 10);
        elevators[1] = new Elevator(IBuildingElevator.Direction_State.DOWN.value(), 2, tempButton, IBuildingElevator.Door_State.CLOSED.value(), 3, 30, 2, 1500, 10, temp, 0);
        elevators[2] = new Elevator(IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, new boolean[5], IBuildingElevator.Door_State.OPEN.value(), 1, 10, 0, 1500, 10, temp, 0);

        building = new Building(3, 10, 5, elevators, floors);
    }
}
