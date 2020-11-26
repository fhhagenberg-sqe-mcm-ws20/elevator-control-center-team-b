package at.fhhagenberg.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;

import java.util.Arrays;
import java.util.Random;

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
public class Elevator implements IBuildingElevator {

    @Getter
    private int direction;
    @Getter
    private int acceleration;
    @Getter
    private boolean[] buttons;
    @Getter
    private int doorState;
    @Getter
    private int nearestFloor;
    @Getter
    private int positionFromGround;
    @Getter
    private int speed;
    @Getter
    private int weight;
    @Getter
    private int capacity;
    @Getter
    private boolean[] floorServices;
    @Getter
    private int floorTarget;

    // Properties for GUI
    @Getter
    public SimpleIntegerProperty directionProperty;
    @Getter
    public SimpleIntegerProperty floorTargetProperty;
    @Getter
    public SimpleIntegerProperty speedProperty;
    @Getter
    public SimpleStringProperty doorStateProperty;
    @Getter
    public SimpleIntegerProperty payloadProperty;
    @Getter
    public SimpleIntegerProperty nearestFloorProperty;


    /**
     * Default constructor where no specific state is used
     *
     * @param floors   Amount of floors the elevator serves
     * @param weight   Weight of the elevator
     * @param capacity Capacity of the elevator
     */
    public Elevator(int floors, int weight, int capacity) {
        this.direction = Direction_State.UNCOMMITTED.value();
        this.acceleration = 0;
        this.buttons = new boolean[floors];
        this.doorState = Door_State.CLOSED.value();
        this.nearestFloor = 0;
        this.positionFromGround = 0;
        this.speed = 0;
        this.weight = weight;
        this.capacity = capacity;
        this.floorServices = new boolean[floors];
        Arrays.fill(floorServices, true);
        this.floorTarget = 0;
        setGuiProperties();
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
    public Elevator(int direction, int acceleration, boolean[] buttons, int doorState, int nearestFloor, int positionFromGround, int speed, int weight, int capacity, boolean[] floorServices, int floorTarget) {
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
        setGuiProperties();
    }

    public void setGuiProperties() {
        directionProperty = new SimpleIntegerProperty(direction);
        floorTargetProperty = new SimpleIntegerProperty(floorTarget);
        floorTargetProperty.addListener((observableValue, oldValue, newValue) -> {
            // TODO: This lines are only for testing, remove them!
            setFloorTarget(1);
        });
        speedProperty = new SimpleIntegerProperty(speed);
        doorStateProperty = new SimpleStringProperty(IBuildingElevator.Door_State.getDoorStateString(doorState));
        payloadProperty = new SimpleIntegerProperty(weight);
        nearestFloorProperty = new SimpleIntegerProperty(nearestFloor);
    }

    public void setDirection(int direction) {
        if (direction < Direction_State.UP.value() || direction > Direction_State.UNCOMMITTED.value())
            this.direction = Direction_State.UNCOMMITTED.value();
        else
            this.direction = direction;
        directionProperty.set(this.direction);
    }

    public void setServicesFloor(int floor, boolean service) {
        floorServices[floor] = service;
    }

    public boolean servesFloor(int floor) {
        return floorServices[floor];
    }

    public boolean getButtonStatus(int floor) {
        return buttons[floor];
    }

    public void setFloorTarget(int floor) {
        int newDirection = nearestFloor - floor;
        this.floorTarget = floor;
        floorTargetProperty.set(this.floorTarget);
        if (newDirection == 0) {
            setDirection(Direction_State.UNCOMMITTED.value());
        } else if (newDirection < 0) {
            setDirection(Direction_State.UP.value());
        } else {
            setDirection(Direction_State.DOWN.value());
        }
    }

    public void setWeight(int weight) {
        this.weight = weight;
        payloadProperty.setValue(weight);
    }
}
