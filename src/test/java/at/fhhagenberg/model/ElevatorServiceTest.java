package at.fhhagenberg.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.Init;
import sqelevator.IElevator;
import sqelevator.MockElevator;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorServiceTest {

    private IElevator system;

    @BeforeEach
    void Init() {
        system = new MockElevator();
    }

    @Test
    void testGetCommittedDirection() throws RemoteException {
        int elevatorDirection = system.getCommittedDirection(1);
        assertEquals(IBuildingElevator.Direction_State.DOWN.value(), elevatorDirection);
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
        assertEquals(IBuildingElevator.Direction_State.UNCOMMITTED.value(), elevatorDoorStatus);
    }

    @Test
    void testGetNearestElevatorFloor() throws RemoteException {
        int elevatorFloor = system.getElevatorFloor(1);
        assertEquals(3, elevatorFloor);
    }

    @Test
    void testGetElevatorNum() throws RemoteException {
        int elevatorNum = system.getElevatorNum();
        assertEquals(9, elevatorNum);
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
        assertEquals(1, system.getTarget(2));
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
        assertEquals(1, system.getTarget(1));
    }

    @Test
    void testGetClockTick() throws RemoteException {
        long clockTick = system.getClockTick();
        assertEquals(2L, clockTick);
    }

    @Test
    void testElevatorCount() throws RemoteException {
        assertEquals(9, system.getElevatorNum());
    }

    @Test
    void testGetElevator() {
        if (system instanceof ElevatorSystem) {
            var elevator = ((ElevatorSystem) system).getElevator(0);
            assertNotNull(elevator);
        }
    }
}
