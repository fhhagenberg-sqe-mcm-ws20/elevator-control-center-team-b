package at.fhhagenberg.model;

import at.fhhagenberg.sqe.IElevator;
import lombok.Getter;

import java.rmi.RemoteException;

/**
 * Mock class for elevator system
 * Has 3 elevators, 5 floors, 1 active, 2 inactive
 */
public class ElevatorSystem implements IElevator {

    @Getter
    private final Building building;

    @Getter
    private final long clockTick;

    public ElevatorSystem(Building building, long clockTick) {
        this.building = building;
        this.clockTick = clockTick;
    }

    public IBuildingElevator getElevator(int elevatorNumber) {
        return building.getElevators()[elevatorNumber];
    }

    @Override
    public boolean getFloorButtonUp(int floor) {
        return building.getFloors()[floor].isUpButton();
    }

    @Override
    public int getFloorHeight() {
        return building.getFloorHeight();
    }

    @Override
    public int getFloorNum() throws RemoteException {
        return building.getFloorCount();
    }

    @Override
    public boolean getServicesFloors(int elevatorNumber, int floor) throws RemoteException {
        return building.getElevators()[elevatorNumber].servesFloor(floor);
    }

    @Override
    public int getTarget(int elevatorNumber) throws RemoteException {
        return building.getElevators()[elevatorNumber].getFloorTarget();
    }

    @Override
    public void setCommittedDirection(int elevatorNumber, int direction) throws RemoteException {
        building.getElevators()[elevatorNumber].setDirection(direction);
    }

    @Override
    public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws RemoteException {
        building.getElevators()[elevatorNumber].setServicesFloor(floor, service);
    }

    @Override
    public void setTarget(int elevatorNumber, int target) throws RemoteException {
        building.getElevators()[elevatorNumber].setFloorTarget(target);
    }

    @Override
    public int getCommittedDirection(int elevatorNumber) throws RemoteException {
        return building.getElevators()[elevatorNumber].getDirection();
    }

    @Override
    public int getElevatorAccel(int elevatorNumber) throws RemoteException {
        return building.getElevators()[elevatorNumber].getAcceleration();
    }

    @Override
    public boolean getElevatorButton(int elevatorNumber, int floor) throws RemoteException {
        return building.getElevators()[elevatorNumber].getButtonStatus(floor);
    }

    @Override
    public int getElevatorDoorStatus(int elevatorNumber) throws RemoteException {
        return building.getElevators()[elevatorNumber].getDoorState();
    }

    @Override
    public int getElevatorFloor(int elevatorNumber) throws RemoteException {
        return building.getElevators()[elevatorNumber].getNearestFloor();
    }

    @Override
    public int getElevatorNum() throws RemoteException {
        return building.getElevators().length;
    }

    @Override
    public int getElevatorPosition(int elevatorNumber) throws RemoteException {
        return building.getElevators()[elevatorNumber].getPositionFromGround();
    }

    @Override
    public int getElevatorSpeed(int elevatorNumber) throws RemoteException {
        return building.getElevators()[elevatorNumber].getSpeed();
    }

    @Override
    public int getElevatorWeight(int elevatorNumber) throws RemoteException {
        return building.getElevators()[elevatorNumber].getWeight();
    }

    @Override
    public int getElevatorCapacity(int elevatorNumber) throws RemoteException {
        return building.getElevators()[elevatorNumber].getCapacity();
    }

    @Override
    public boolean getFloorButtonDown(int floor) {
        return building.getFloors()[floor].isDownButton();
    }
}
