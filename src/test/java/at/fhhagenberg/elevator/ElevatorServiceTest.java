package at.fhhagenberg.elevator;

import at.fhhagenberg.model.*;
import at.fhhagenberg.sqe.IElevator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorServiceTest {

    private IElevator system;
    private IBuildingElevator[] elevators;
    private IFloor[] floors;

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
        elevators[1] = new Elevator(IBuildingElevator.Direction_State.down.value(), 2, tempButton, IBuildingElevator.Door_State.closed.value(), 3, 30, 2, 1500, 10, temp, 0);
        elevators[2] = new Elevator(IBuildingElevator.Direction_State.uncommitted.value(), 2, new boolean[5], IBuildingElevator.Door_State.open.value(), 1, 10, 0, 1500, 10, temp, 0);

        Building mockBuilding = new Building(3, 10, 5, elevators, floors);
        system = new ElevatorSystem(mockBuilding, 100L);
    }

    @Test
    void testGetCommittedDirection() throws RemoteException {
        int elevatorDirection = system.getCommittedDirection(1);
        assertEquals(1, elevatorDirection);
    }

    @Test
    void testGetElevatorAccel() throws RemoteException {
        int elevatorAccel = system.getElevatorAccel(1);
        assertEquals(2, elevatorAccel);
    }

    @Test
    void testGetElevatorButton() throws RemoteException {
        boolean elevatorButton = system.getElevatorButton(1, 4);
        assertFalse(elevatorButton);
    }

    @Test
    void testGetElevatorDoorStatus() throws RemoteException {
        int elevatorDoorStatus = system.getElevatorDoorStatus(1);
        assertEquals(2, elevatorDoorStatus);
    }

    @Test
    void testGetNearestElevatorFloor() throws RemoteException {
        int elevatorFloor = system.getElevatorFloor(1);
        assertEquals(3, elevatorFloor);
    }

    @Test
    void testGetElevatorNum() throws RemoteException {
        int elevatorNum = system.getElevatorNum();
        assertEquals(3, elevatorNum);
    }

    @Test
    void testGetElevatorPosition() throws RemoteException {
        int elevatorPosition = system.getElevatorPosition(1);
        assertEquals(30, elevatorPosition);
    }

    @Test
    void testGetElevatorSpeed() throws RemoteException {
        int elevatorSpeed = system.getElevatorSpeed(1);
        assertEquals(2, elevatorSpeed);
    }

    @Test
    void testGetElevatorWeight() throws RemoteException {
        int elevatorWeight = system.getElevatorWeight(1);
        assertEquals(1500, elevatorWeight);
    }

    @Test
    void testGetElevatorCapacity() throws RemoteException {
        int elevatorCapacity = system.getElevatorCapacity(2);
        assertEquals(10, elevatorCapacity);
    }

    @Test
    void testGetFloorButtonDown() throws java.rmi.RemoteException {
        boolean floorButtonDown = system.getFloorButtonDown(0);
        assertFalse(floorButtonDown);
    }

    @Test
    void testGetFloorButtonUp() throws java.rmi.RemoteException {
        boolean floorButtonDown = system.getFloorButtonUp(0);
        assertTrue(floorButtonDown);
    }

    @Test
    void testGetFloorHeight() throws RemoteException {
        int getFloorHeight = system.getFloorHeight();
        assertEquals(10, getFloorHeight);
    }

    @Test
    void testGetFloorNum() throws RemoteException {
        int amountOfFloors = system.getFloorNum();
        assertEquals(5, amountOfFloors);
    }

    @Test
    void testGetServicesFloor() throws RemoteException {
        system.setServicesFloors(2, 3, false);
        assertFalse(system.getServicesFloors(2, 3));
    }

    @Test
    void testGetTarget() throws RemoteException {
        system.setTarget(2, 4);
        assertEquals(4, system.getTarget(2));
    }


    @Test
    void setCommittedDirection() throws RemoteException {
        system.setCommittedDirection(1, 1);
        assertEquals(1, system.getCommittedDirection(1));
    }

    @Test
    void testSetServicesFloor() throws RemoteException {
        system.setServicesFloors(2, 3, false);
        assertFalse(system.getServicesFloors(2, 3));
    }


    @Test
    void testSetTarget() throws RemoteException {
        system.setTarget(1, 5);
        assertEquals(5, system.getTarget(1));
    }

    @Test
    void testGetClockTick() throws RemoteException {
        long clockTick = system.getClockTick();
        assertEquals(100L, clockTick);
    }

    @Test
    void testCreateStateFromValue() {
        IBuildingElevator.Direction_State new_state = IBuildingElevator.Direction_State.up;
        new_state = new_state.createFromValue(1);
        assertEquals(IBuildingElevator.Direction_State.down, new_state);
    }

    @Test
    void testSetValue() {
        IBuildingElevator.Door_State new_state = IBuildingElevator.Door_State.open;
        new_state = new_state.setValue(2);
        assertEquals(IBuildingElevator.Door_State.closed, new_state);
    }
}
