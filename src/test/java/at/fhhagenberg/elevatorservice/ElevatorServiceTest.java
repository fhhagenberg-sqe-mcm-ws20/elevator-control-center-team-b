package at.fhhagenberg.elevatorservice;

import at.fhhagenberg.elevator.ElevatorConstants;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorServiceTest {

    private MockElevatorSystem system = new MockElevatorSystem();

    @Test
    public void testGetCommittedDirection() throws RemoteException {
        ElevatorConstants.Direction_State commitedDirection = system.getElevators()[1].getDirection();
        assertEquals(ElevatorConstants.Direction_State.down, commitedDirection);
    }

    @Test
    public void testGetElevatorAccel() throws RemoteException {
        int elevatorAccel = system.getElevators()[1].getAcceleration();
        assertEquals(2, elevatorAccel);
    }

    @Test
    public void testGetElevatorButton() throws RemoteException {
        boolean elevatorButton = system.getElevators()[1].getButtons()[4];
        assertFalse(elevatorButton);

    }

    @Test
    public void testGetElevatorDoorStatus() throws RemoteException {
        ElevatorConstants.Door_State elevatorDoorStatus = system.getElevators()[1].getDoorState();
        assertEquals(ElevatorConstants.Door_State.closed, elevatorDoorStatus);
    }

    @Test
    public void testGetNearestElevatorFloor() throws RemoteException {
        int elevatorFloor = system.getElevators()[1].getNearestFloor();
        assertEquals(3, elevatorFloor);
    }

    @Test
    public void testGetElevatorNum() throws RemoteException {
        int elevatorNum = system.getElevators().length;
        assertEquals(3, elevatorNum);
    }

    @Test
    public void testGetElevatorPosition() throws RemoteException {
        int elevatorPosition = system.getElevators()[1].getPositionFromGround();
        assertEquals(30, elevatorPosition);
    }

    @Test
    public void testGetElevatorSpeed() throws RemoteException {
        int elevatorSpeed = system.getElevators()[1].getSpeed();
        assertEquals(2, elevatorSpeed);
    }

    @Test
    public void testGetElevatorWeight() throws RemoteException {
        int elevatorWeight = system.getElevators()[1].getWeight();
        assertEquals(1500, elevatorWeight);
    }

    @Test
    public void testGetElevatorCapacity() throws RemoteException {
        int elevatorCapacity = system.getElevators()[2].getCapacity();
        assertEquals(10, elevatorCapacity);
    }

    @Test
    public void testGetFloorButtonDown() throws RemoteException {
        boolean floorButtonDown =  system.getFloors()[0].downButtonActive();
        assertFalse(floorButtonDown);
    }

    @Test
    public void testGetFloorButtonUp() throws RemoteException {
        boolean floorButtonDown =  system.getFloors()[0].upButtonActive();
        assertTrue(floorButtonDown);
    }

    @Test
    public void testGetFloorHeight() throws RemoteException {
        int getFloorHeight = MockElevatorSystem.getFLOOR_HEIGHT();
        assertEquals(10, getFloorHeight);
    }

    @Test
    public void testGetFloorNum() throws RemoteException {
        int amountOfFloors = MockElevatorSystem.getFLOOR_COUNT();
        assertEquals(5, amountOfFloors);
    }

    @Test
    public void testGetServicesFloor() throws RemoteException {
        system.getElevators()[2].setServicesFloor(3, false);
        assertFalse(system.getElevators()[2].servesFloor(3));
    }

    @Test
    public void testGetTarget() throws RemoteException {
        system.getElevators()[2].setFloorTarget(4);
        assertEquals(4, system.getElevators()[2].getFloorTarget());
    }


    @Test
    public void setCommittedDirection() throws RemoteException {
        system.getElevators()[2].setDirection(ElevatorConstants.Direction_State.down);
        assertEquals(ElevatorConstants.Direction_State.down, system.getElevators()[1].getDirection());
    }

    @Test
    public void testSetServicesFloor() throws RemoteException {
        system.getElevators()[2].setServicesFloor(3, false);
        assertFalse(system.getElevators()[2].getFloorServices()[3]);
    }


    @Test
    public void testSetTarget() throws RemoteException {
        system.getElevators()[1].setFloorTarget(5);
        assertEquals(5, system.getElevators()[1].getFloorTarget());
    }

    @Test
    public void testGetClockTick() throws RemoteException {
        long clockTick = MockElevatorSystem.getTICK();
        assertEquals(100L, clockTick);
    }
}
