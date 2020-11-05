package at.fhhagenberg.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorTest {
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

    @Test
    void testCreateStateFromValue() {
        IBuildingElevator.Direction_State new_state = IBuildingElevator.Direction_State.UP;
        new_state = new_state.createFromValue(0);
        assertEquals(IBuildingElevator.Direction_State.UP, new_state);
        new_state = new_state.createFromValue(1);
        assertEquals(IBuildingElevator.Direction_State.DOWN, new_state);
        new_state = new_state.createFromValue(2);
        assertEquals(IBuildingElevator.Direction_State.UNCOMMITTED, new_state);
        new_state = new_state.createFromValue(10);
        assertEquals(IBuildingElevator.Direction_State.UNCOMMITTED, new_state);
    }

    @Test
    void testSetValue() {
        IBuildingElevator.Door_State new_state = IBuildingElevator.Door_State.OPEN;
        new_state = new_state.setValue(1);
        assertEquals(IBuildingElevator.Door_State.OPEN, new_state);
        new_state = new_state.setValue(2);
        assertEquals(IBuildingElevator.Door_State.CLOSED, new_state);
        new_state = new_state.setValue(3);
        assertEquals(IBuildingElevator.Door_State.OPENING, new_state);
        new_state = new_state.setValue(4);
        assertEquals(IBuildingElevator.Door_State.CLOSING, new_state);
        new_state = new_state.setValue(10);
        assertEquals(IBuildingElevator.Door_State.CLOSED, new_state);
    }

    @Test
    void testNullElevator() {
        var elevator = building.getElevator(-1);
        assertNull(elevator);
    }

    @Test
    void testElevatorButtonStatus() {
        var status = building.getElevator(1).getButtonStatus(0);
        assertTrue(status);
    }

    @Test
    void testElevatorFloorService() {
        building.getElevator(1).setServicesFloor(0, true);
        var service = building.getElevator(1).servesFloor(0);

        assertTrue(service);
    }

    @Test
    void testSetCorrectDirection() {
        building.getElevator(1).setDirection(2);
        var direction = building.getElevator(1).getDirection();

        assertEquals(IBuildingElevator.Direction_State.UNCOMMITTED.value(), direction);
    }

    @Test
    void testSetWrongDirection() {
        building.getElevator(1).setDirection(-1);
        var direction = building.getElevator(1).getDirection();

        assertEquals(IBuildingElevator.Direction_State.UNCOMMITTED.value(), direction);
    }

    @Test
    void testFloorButtons() {
        var floor = building.getFloors()[0];
        floor.setDownButton(true);
        assertTrue(floor.isDownButton());

        floor.setUpButton(true);
        assertTrue(floor.isUpButton());
    }
}
