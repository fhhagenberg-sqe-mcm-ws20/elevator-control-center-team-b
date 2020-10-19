package at.fhhagenberg.elevator;

/**
 * Elevator example class
 *
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
    private int[] buttons;
    private Door_State doorState;
    private int nearestFloor;
    private int positionFromGround;
    private int speed;
    private int weight;
    private int capacity;
    private boolean[] floorServices;
    private int floorTarget;

    public MockElevator(int floors, int weight, int capacity) {
        this.direction = Direction_State.uncommitted;
        this.acceleration = 0;
        this.buttons = new int[floors];
        this.doorState = Door_State.closed;
        this.nearestFloor = 0;
        this.positionFromGround = 0;
        this.speed = 0;
        this.weight = weight;
        this.capacity = capacity;
        this.floorServices = new boolean[floors];
        this.floorTarget = 0;
    }

    @Override
    public int getCommittedDirection() {
        return direction.value();
    }

    @Override
    public int getAccel() {
        return acceleration;
    }

    @Override
    public int getButtonStatus(int floor) {
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
}
