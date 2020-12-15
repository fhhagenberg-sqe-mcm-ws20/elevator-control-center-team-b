package at.fhhagenberg.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

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
public class Elevator implements IBuildingElevator {

    @Getter
    private int number;
    @Getter
    private int direction;
    @Getter
    private int acceleration;
    @Getter
    private boolean[] floorButtons;
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
    @Getter
    private final ObservableList<Integer> pressedFloorBtnList = FXCollections.observableArrayList();
    @Getter
    private final ObservableList<Integer> servingFloorsList = FXCollections.observableArrayList();


    /**
     * Default constructor where no specific state is used
     *
     * @param floorCount Amount of floors the elevator serves
     * @param weight     Weight of the elevator
     * @param capacity   Capacity of the elevator
     */
    public Elevator(int number, int floorCount, int weight, int capacity) {
        this.number = number;
        this.weight = weight;
        this.capacity = capacity;
        this.direction = Direction_State.UNCOMMITTED.value();
        this.acceleration = 0;
        this.floorButtons = new boolean[floorCount];
        this.doorState = Door_State.CLOSED.value();
        this.nearestFloor = 0;
        this.positionFromGround = 0;
        this.speed = 0;
        this.floorServices = new boolean[floorCount];
        Arrays.fill(floorServices, true);
        setGuiProperties();
    }

    /**
     * Constructor to set a specific elevator state
     *
     * @param number             Number of elevator
     * @param direction          Current direction of the elevator
     * @param acceleration       Acceleration of the elevator
     * @param floorButtons       Active buttons, buttons pressed on specific floor
     * @param doorState          Door state of the elevator
     * @param nearestFloor       Floor that is closest to the elevator in feet
     * @param positionFromGround Position of the elevator from ground in feet
     * @param speed              Current speed of the elevator
     * @param weight             Overall weight of the elevator with no passengers
     * @param capacity           Capacity of the elevator (amount of people)
     * @param floorServices      Floors that the elevator stops at
     * @param floorTarget        Current active target the elevator will go to
     */
    public Elevator(int number, int direction, int acceleration, boolean[] floorButtons, int doorState, int nearestFloor, int positionFromGround, int speed, int weight, int capacity, boolean[] floorServices, int floorTarget) {
        this.number = number;
        this.direction = direction;
        this.acceleration = acceleration;
        this.floorButtons = floorButtons;
        this.doorState = doorState;
        this.nearestFloor = nearestFloor;
        this.positionFromGround = positionFromGround;
        this.speed = speed;
        this.weight = weight;
        this.capacity = capacity;
        this.floorServices = floorServices;
        if (nearestFloor != floorTarget) {
            this.floorTarget = floorTarget;
            this.pressedFloorBtnList.add(floorTarget);
        }
        setGuiProperties();
    }

    public void setGuiProperties() {
        directionProperty = new SimpleIntegerProperty(direction);
        floorTargetProperty = new SimpleIntegerProperty(floorTarget);
        floorTargetProperty.addListener((observableValue, oldValue, newValue) -> {
            if (floorTarget != newValue.intValue()) {
                setFloorTarget(newValue.intValue());
            }
        });
        speedProperty = new SimpleIntegerProperty(speed);
        doorStateProperty = new SimpleStringProperty(IBuildingElevator.Door_State.getDoorStateString(doorState));
        payloadProperty = new SimpleIntegerProperty(weight);
        nearestFloorProperty = new SimpleIntegerProperty(nearestFloor);
        for (int i = 0; i < floorServices.length; i++) {
            if (floorServices[i]) {
                servingFloorsList.add(i);
            }
        }
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
        return floorButtons[floor];
    }

    public void setFloorTarget(int floor) {
        int newDirection = nearestFloor - floor;
        floorTarget = floor;
        floorTargetProperty.set(floorTarget);
        if (!pressedFloorBtnList.contains(floorTarget) && floorTarget != nearestFloor) {
            pressedFloorBtnList.add(0, floorTarget);
        } else if (pressedFloorBtnList.contains(floorTarget) && floorTarget != nearestFloor) {
            pressedFloorBtnList.remove(Integer.valueOf(floorTarget));
            pressedFloorBtnList.add(0, floorTarget);
        }
        if (newDirection == 0) {
            setDirection(Direction_State.UNCOMMITTED.value());
        } else if (newDirection < 0) {
            setDirection(Direction_State.UP.value());
        } else {
            setDirection(Direction_State.DOWN.value());
        }
    }

    @Override
    public void update(IBuildingElevator elevator) {
        this.number = elevator.getNumber();
        this.direction = elevator.getDirection();
        this.acceleration = elevator.getAcceleration();
        this.floorButtons = elevator.getFloorButtons();
        this.doorState = elevator.getDoorState();
        this.nearestFloor = elevator.getNearestFloor();
        this.positionFromGround = elevator.getPositionFromGround();
        this.speed = elevator.getSpeed();
        this.weight = elevator.getWeight();
        this.capacity = elevator.getCapacity();
        this.floorServices = elevator.getFloorServices();
        this.floorTarget = elevator.getFloorTarget();
    }

    public void setWeight(int weight) {
        this.weight = weight;
        payloadProperty.setValue(weight);
    }

    /**
     * Method to add a floor button if it was pressed
     *
     * @param floorNumber number of the floor
     */
    public void addPressedFloorButton(int floorNumber) {
        // If it does not exist and is not the current floor we add it
        if (!pressedFloorBtnList.contains(floorNumber) && nearestFloor != floorNumber) {
            pressedFloorBtnList.add(floorNumber);
        } else {
            // TODO remove else as it is only used for testing!
            pressedFloorBtnList.remove(Integer.valueOf(floorNumber));
        }
        if (!pressedFloorBtnList.isEmpty()) {
            setFloorTarget(pressedFloorBtnList.get(0));
        } else {
            setFloorTarget(nearestFloor);
        }
    }
}
