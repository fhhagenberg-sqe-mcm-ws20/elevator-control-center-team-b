package at.fhhagenberg.elevator;

import java.util.Arrays;

/**
 * Elevator example class
 * <p>
 * This elevator presumes that the initial state is:
 * - on floor 0
 * - no speed
 * - no target
 * - door closed
 * - all floors are serviced
 */
public class MockElevator implements IElevator {

    private Direction_State direction;
    private int acceleration;
    private boolean[] buttons;
    private Door_State doorState;
    private int nearestFloor;
    private int positionFromGround;
    private int speed;
    private int weight;
    private int capacity;
    private boolean[] floorServices;
    private int floorTarget;

    /**
     * Default constructor where no specific state is used
     *
     * @param floors   Amount of floors the elevator serves
     * @param weight   Weight of the elevator
     * @param capacity Capacity of the elevator
     */
    public MockElevator(int floors, int weight, int capacity) {
        this.direction = Direction_State.uncommitted;
        this.acceleration = 0;
        this.buttons = new boolean[floors];
        this.doorState = Door_State.closed;
        this.nearestFloor = 0;
        this.positionFromGround = 0;
        this.speed = 0;
        this.weight = weight;
        this.capacity = capacity;
        this.floorServices = new boolean[floors];
        Arrays.fill(floorServices, true);
        this.floorTarget = 0;
    }

    /**
     * Constructor to set a specific elevator state
     *
     * @param direction          Current direction of the elevator
     * @param acceleration       Acceleration of the elevator
     * @param buttons            Active buttons
     * @param doorState          Door state of the elevator
     * @param nearestFloor       Floor that is closest to the elevator in feet
     * @param positionFromGround Position of the elevator from ground in feet
     * @param speed              Current speed of the elevator
     * @param weight             Overall weight of the elevator with no passengers
     * @param capacity           Capacity of the elevator (amount of people)
     * @param floorServices      Floors that the elevator stops at
     * @param floorTarget        Current active target the elevator will go to
     */
    public MockElevator(Direction_State direction, int acceleration, boolean[] buttons, Door_State doorState, int nearestFloor, int positionFromGround, int speed, int weight, int capacity, boolean[] floorServices, int floorTarget) {
        this.direction = direction;
        this.acceleration = acceleration;
        this.buttons = buttons;
        this.doorState = doorState;
        this.nearestFloor = nearestFloor;
        this.positionFromGround = positionFromGround;
        this.speed = speed;
        this.weight = weight;
        this.capacity = capacity;
        this.floorServices = floorServices;
        this.floorTarget = floorTarget;
    }

    //region ElevatorAPI
    @Override
    public int getCommittedDirection() {
        return direction.value();
    }

    @Override
    public int getAccel() {
        return acceleration;
    }

    @Override
    public boolean getButtonStatus(int floor) {
        return buttons[floor];
    }

    @Override
    public int getDoorStatus() {
        return doorState.value();
    }

    @Override
    public int getCurrentFloor() {
        return nearestFloor;
    }

    @Override
    public int getPosition() {
        return positionFromGround;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean servesFloor(int floor) {
        return floorServices[floor];
    }

    @Override
    public int getTarget() {
        return floorTarget;
    }

    @Override
    public void setDirection(int direction) {
        if (direction < Direction_State.up.value() || direction > Direction_State.uncommitted.value())
            this.direction = Direction_State.uncommitted;
            //throw new IllegalArgumentException("[Elevator] State was not inside Door_State range");
        else
            this.direction = this.direction.createFromValue(direction);
    }

    @Override
    public void setServicesFloor(int floor, boolean service) {
        floorServices[floor] = service;
    }

    @Override
    public void setTarget(int floor) {
        floorTarget = floor;
    }
    //endregion


    //region Mock
    public void setDirection(Direction_State direction) {
        this.direction = direction;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public void setButtons(boolean[] buttons) {
        this.buttons = buttons;
    }

    public void setDoorState(Door_State doorState) {
        this.doorState = doorState;
    }

    public void setNearestFloor(int nearestFloor) {
        this.nearestFloor = nearestFloor;
    }

    public void setPositionFromGround(int positionFromGround) {
        this.positionFromGround = positionFromGround;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setFloorServices(boolean[] floorServices) {
        this.floorServices = floorServices;
    }
    //endregion
}
