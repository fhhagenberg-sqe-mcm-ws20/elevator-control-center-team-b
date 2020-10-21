package at.fhhagenberg.elevatorservice;

import at.fhhagenberg.elevator.IElevator;
import at.fhhagenberg.elevator.MockElevator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorServiceTest {

    private IElevatorSystem system = new MockElevatorSystem();

    @Test
    public void testGetCommittedDirection() throws RemoteException{
        int committedDirection = system.getCommittedDirection(1);
        assertEquals(1, committedDirection);
    }

    @Test
    public void testGetElevatorAccel() throws RemoteException{
        int elevatorAccel = system.getElevatorAccel(1);
        assertEquals(2, elevatorAccel);
    }

    @Test
    public void testGetElevatorButton() throws RemoteException{
        boolean elevatorButton = system.getElevatorButton(1, 4);
        assertFalse(elevatorButton);
    }

    @Test
    public void testGetElevatorDoorStatus() throws RemoteException{
        int elevatorDoorStatus = system.getElevatorDoorStatus(1);
        assertEquals(2, elevatorDoorStatus);
    }

    @Test
    public void testGetElevatorFloor() throws RemoteException {
        int elevatorFloor = system.getElevatorFloor(1);
        assertEquals(3, elevatorFloor);
    }

    @Test
    public void testGetElevatorNum() throws RemoteException{
        int elevatorNum = system.getElevatorNum();
        assertEquals(3, elevatorNum);
    }

    @Test
    public void testGetElevatorPosition() throws RemoteException {
        int elevatorPosition = system.getElevatorPosition(1);
        assertEquals(30, elevatorPosition);
    }

    @Test
    public void testGetElevatorSpeed() throws RemoteException{
        int elevatorSpeed = system.getElevatorSpeed(1);
        assertEquals(2, elevatorSpeed);
    }

    @Test
    public void testGetElevatorWeight() throws RemoteException{
        int elevatorWeight = system.getElevatorWeight(1);
        assertEquals(1500, elevatorWeight);
    }

    @Test
    public void testGetElevatorCapacity() throws RemoteException{
        int elevatorCapacity = system.getElevatorCapacity(2);
        assertEquals(10, elevatorCapacity);
    }

    @Test
    public void testGetFloorButtonDown() throws RemoteException{
        boolean floorButtonDown = system.getFloorButtonDown(3);
        assertFalse(floorButtonDown);
    }

    @Test
    public void testGetFloorButtonUp() throws RemoteException{
        boolean floorButtonUp = system.getFloorButtonUp(3);
        assertFalse(floorButtonUp);
    }

    @Test
    public void testGetFloorHeight() throws RemoteException{
        int getFloorHeight = system.getFloorHeight();
        assertEquals(10, getFloorHeight);
    }

    @Test
    public void testGetFloorNum() throws  RemoteException {
        int amountOfFloors = system.getFloorNum();
        assertEquals(5, amountOfFloors);
    }

    @Test
    public void testGetServicesFloor() throws RemoteException{
        system.setServicesFloors(2, 3, false);
        assertEquals(false, system.getServicesFloors(2, 3));
    }

    @Test
    public void testGetTarget() throws RemoteException{
        system.setTarget(2, 4);
        assertEquals(4, system.getTarget(2));
    }


    @Test
    public void setCommittedDirection() throws  RemoteException{
        MockElevator mockElevator = system.getElevator(1);
        system.setCommittedDirection(2, 1);
        assertEquals(1, mockElevator.getCommittedDirection());
    }

    @Test
    public void testSetServicesFloor() throws RemoteException{
        system.setServicesFloors(2, 3, false);
        assertFalse(system.getServicesFloors(2, 3));
    }


    @Test
    public void testSetTarget() throws RemoteException{
        MockElevator mockElevator = system.getElevator(1);
        system.setTarget(1, 5);
        assertEquals(5, mockElevator.getTarget());
    }

    @Test
    public void testGetClockTick() throws RemoteException {
        long clockTick = system.getClockTick();
        assertEquals(100L, clockTick);
    }



}
