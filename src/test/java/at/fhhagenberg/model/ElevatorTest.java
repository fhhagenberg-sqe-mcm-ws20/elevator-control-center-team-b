package at.fhhagenberg.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {
    private final ArrayList<IBuildingElevator> elevators = new ArrayList<>();
    private final ArrayList<IFloor> floors = new ArrayList<>();
    private Building building;

    @BeforeEach
    void Init() {
        ArrayList<Integer> floorButtons = new ArrayList<>(IntStream.range(0, 2).boxed().collect(Collectors.toList()));
        ArrayList<Integer> servicedFloors = new ArrayList<>(IntStream.range(0, 5).boxed().collect(Collectors.toList()));

        floors.add(0, new Floor(0, false, true));
        floors.add(1, new Floor(1, false, false));
        floors.add(2, new Floor(2, false, false));
        floors.add(3, new Floor(3, true, true));
        floors.add(4, new Floor(4, false, true));

        elevators.add(0, new Elevator(0, 5, 200, 10));
        elevators.add(1, new Elevator(1, IBuildingElevator.Direction_State.DOWN.value(), 2, floorButtons, IBuildingElevator.Door_State.CLOSED.value(), 3, 30, 2, 1500, 10, servicedFloors, 0));
        elevators.add(2, new Elevator(2, IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, floorButtons, IBuildingElevator.Door_State.OPEN.value(), 1, 10, 0, 1500, 10, servicedFloors, 0));

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
    void testSetDoorStateValue() {
        IBuildingElevator.Door_State new_state = IBuildingElevator.Door_State.OPEN;

        new_state = new_state.setValue(1);
        assertEquals("Open", IBuildingElevator.Door_State.getDoorStateString(new_state.value()));
        assertEquals(IBuildingElevator.Door_State.OPEN, new_state);

        new_state = new_state.setValue(2);
        assertEquals("Closed", IBuildingElevator.Door_State.getDoorStateString(new_state.value()));
        assertEquals(IBuildingElevator.Door_State.CLOSED, new_state);

        new_state = new_state.setValue(3);
        assertEquals("Opening", IBuildingElevator.Door_State.getDoorStateString(new_state.value()));
        assertEquals(IBuildingElevator.Door_State.OPENING, new_state);

        new_state = new_state.setValue(4);
        assertEquals("Closing", IBuildingElevator.Door_State.getDoorStateString(new_state.value()));
        assertEquals(IBuildingElevator.Door_State.CLOSING, new_state);

        new_state = new_state.setValue(10);
        assertEquals("Closed", IBuildingElevator.Door_State.getDoorStateString(new_state.value()));
        assertEquals(IBuildingElevator.Door_State.CLOSED, new_state);
    }

    @Test
    void testNullElevator() {
        var elevator = building.getElevator(-1);
        var elevator2 = building.getElevator(10);
        assertNull(elevator);
        assertNull(elevator2);
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

        building.getElevator(1).setDirection(10);
        var direction2 = building.getElevator(1).getDirection();
        assertEquals(IBuildingElevator.Direction_State.UNCOMMITTED.value(), direction2);
    }

    @Test
    void testFloorButtons() {
        var floor = building.getFloors().get(0);
        floor.setDownButtonActive(true);
        assertTrue(floor.isDownButtonActive());

        floor.setUpButtonActive(true);
        assertTrue(floor.isUpButtonActive());
    }
}
