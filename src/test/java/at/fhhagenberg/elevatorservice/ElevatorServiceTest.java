package at.fhhagenberg.elevatorservice;

import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorServiceTest {

    private IElevatorSystem system = new MockElevatorSystem();

    @Test
    public void testGetCommittedDirection() {
        int commitedDirection = system.getElevator(1).getDirection();
        assertEquals(1, commitedDirection);
    }

    @Test
    public void testGetElevatorAccel() {
        int elevatorAccel = system.getElevator(1).getAcceleration();
        assertEquals(2, elevatorAccel);
    }

    @Test
    public void testGetElevatorButton() {
        boolean elevatorButton = system.getElevator(1).getButtonStatus(4);
        assertFalse(elevatorButton);

    }

    @Test
    public void testGetElevatorDoorStatus() {
        int elevatorDoorStatus = system.getElevator(1).getDoorState();
        assertEquals(2, elevatorDoorStatus);
    }

    @Test
    public void testGetNearestElevatorFloor() {
        int elevatorFloor = system.getElevator(1).getNearestFloor();
        assertEquals(3, elevatorFloor);
    }

    @Test
    public void testGetElevatorNum() {
        int elevatorNum = system.getElevatorCount();
        assertEquals(3, elevatorNum);
    }

    @Test
    public void testGetElevatorPosition() {
        int elevatorPosition = system.getElevator(1).getPositionFromGround();
        assertEquals(30, elevatorPosition);
    }

    @Test
    public void testGetElevatorSpeed() {
        int elevatorSpeed = system.getElevator(1).getSpeed();
        assertEquals(2, elevatorSpeed);
    }

    @Test
    public void testGetElevatorWeight() {
        int elevatorWeight = system.getElevator(1).getWeight();
        assertEquals(1500, elevatorWeight);
    }

    @Test
    public void testGetElevatorCapacity() {
        int elevatorCapacity = system.getElevator(2).getCapacity();
        assertEquals(10, elevatorCapacity);
    }

    @Test
    public void testGetFloorButtonDown() throws java.rmi.RemoteException{
        boolean floorButtonDown = system.getFloorButtonDown(0);
        assertFalse(floorButtonDown);
    }

    @Test
    public void testGetFloorButtonUp() throws java.rmi.RemoteException{
        boolean floorButtonDown = system.getFloorButtonUp(0);
        assertTrue(floorButtonDown);
    }

    @Test
    public void testGetFloorHeight() {
        int getFloorHeight = system.getFloorHeight();
        assertEquals(10, getFloorHeight);
    }

    @Test
    public void testGetFloorNum() {
        int amountOfFloors = system.getFloorCount();
        assertEquals(5, amountOfFloors);
    }

    @Test
    public void testGetServicesFloor() {
        system.getElevator(2).setServicesFloor(3, false);
        assertFalse(system.getElevator(2).servesFloor(3));
    }

    @Test
    public void testGetTarget() {
        system.getElevator(2).setFloorTarget(4);
        assertEquals(4, system.getElevator(2).getFloorTarget());
    }


    @Test
    public void setCommittedDirection() {
        system.getElevator(1).setDirection(1);
        assertEquals(1, system.getElevator(1).getDirection());
    }

    @Test
    public void testSetServicesFloor() {
        system.getElevator(2).setServicesFloor(3, false);
        assertFalse(system.getElevator(2).servesFloor(3));
    }


    @Test
    public void testSetTarget() {
        system.getElevator(1).setFloorTarget(5);
        assertEquals(5, system.getElevator(1).getFloorTarget());
    }

    @Test
    public void testGetClockTick() throws RemoteException {
        long clockTick = system.getClockTick();
        assertEquals(100L, clockTick);
    }
}
