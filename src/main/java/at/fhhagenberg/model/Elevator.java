package at.fhhagenberg.model;

import at.fhhagenberg.RemoteExceptionHandler;
import at.fhhagenberg.converter.ModelConverter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lombok.Getter;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public int lastDirection;
    @Getter
    private int acceleration;
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
    private int floorTarget;

    // Properties for GUI
    @Getter
    public SimpleIntegerProperty directionProperty = new SimpleIntegerProperty();
    @Getter
    public SimpleIntegerProperty floorTargetProperty = new SimpleIntegerProperty();
    @Getter
    public SimpleIntegerProperty speedProperty;
    @Getter
    public SimpleStringProperty doorStateProperty;
    @Getter
    public SimpleIntegerProperty weightProperty;
    @Getter
    public SimpleIntegerProperty nearestFloorProperty;
    @Getter
    private ObservableList<Integer> floorButtons;
    @Getter
    private ObservableList<Integer> floorServices;

    private ModelConverter modelConverter;

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
        this.doorState = Door_State.CLOSED.value();
        this.nearestFloor = 0;
        this.positionFromGround = 0;
        this.positionFromGround = 0;
        this.speed = 0;
        floorButtons = FXCollections.observableArrayList();
        floorServices = FXCollections.observableArrayList(IntStream.range(0, floorCount).boxed().collect(Collectors.toList()));
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
    public Elevator(int number, int direction, int acceleration, List<Integer> floorButtons, int doorState, int nearestFloor,
                    int positionFromGround, int speed, int weight, int capacity, List<Integer> floorServices, int floorTarget,
                    ModelConverter modelConverter) {
        this.number = number;
        this.direction = direction;
        this.acceleration = acceleration;
        this.doorState = doorState;
        this.nearestFloor = nearestFloor;
        this.positionFromGround = positionFromGround;
        this.speed = speed;
        this.weight = weight;
        this.capacity = capacity;
        this.floorButtons = FXCollections.observableArrayList(floorButtons);
        this.floorServices = FXCollections.observableArrayList(floorServices);
        if (floorButtons.contains(nearestFloor)) {
            floorButtons.remove(Integer.valueOf(nearestFloor));
        }
        this.floorTarget = floorTarget;
        this.modelConverter = modelConverter;
        setGuiProperties();
    }

    public void setGuiProperties() {
        directionProperty.set(direction);
        directionProperty.addListener((observableValue, oldValue, newValue) -> {
            // ModelConverter might be null due to test classes
            if (modelConverter != null) {
                try {
                    modelConverter.setCommittedDirection(number, newValue.intValue());
                } catch (RemoteException e) {
                    RemoteExceptionHandler.instance().update();
                }
            }

        });
        setFloorButtons();
        //setFloorTarget(floorTarget);
        floorTargetProperty.addListener((observableValue, oldValue, newValue) -> {
            if (floorTarget != newValue.intValue()) {
                setFloorTarget(newValue.intValue());
            }
        });
        speedProperty = new SimpleIntegerProperty(speed);
        doorStateProperty = new SimpleStringProperty(IBuildingElevator.Door_State.getDoorStateString(doorState));
        weightProperty = new SimpleIntegerProperty(weight);
        nearestFloorProperty = new SimpleIntegerProperty(nearestFloor);
    }

    public void setDirection(int direction) {
        if (this.direction != Direction_State.UNCOMMITTED.value()) {
            lastDirection = this.direction;
        }

        if (direction < Direction_State.UP.value() || direction > Direction_State.UNCOMMITTED.value())
            this.direction = Direction_State.UNCOMMITTED.value();
        else
            this.direction = direction;
        directionProperty.set(this.direction);
    }

    public void setServicesFloor(int floor, boolean service) {
        if (service) {
            floorServices.add(floor);
        } else {
            floorServices.remove(Integer.valueOf(floor));
        }
    }

    public boolean servesFloor(int floor) {
        return floorServices.contains(floor);
    }

    public boolean getButtonStatus(int floor) {
        return floorButtons.contains(floor);
    }

    public void setFloorTarget(int floor) {
        int newDirection = nearestFloor - floor;
        floorTarget = floor;
        floorTargetProperty.setValue(floor);

        setFloorButtons();

        if (newDirection == 0) {
            setDirection(Direction_State.UNCOMMITTED.value());
        } else if (newDirection < 0) {
            setDirection(Direction_State.UP.value());
        } else {
            setDirection(Direction_State.DOWN.value());
        }

        // ModelConverter might be null due to test classes
        if (modelConverter != null) {
            try {
                modelConverter.setTarget(number, floorTarget);
            } catch (RemoteException e) {
                RemoteExceptionHandler.instance().update();
            }
        }
    }

    private void setFloorButtons() {
        if (!floorButtons.contains(floorTarget) && floorTarget != nearestFloor) {
            floorButtons.add(0, floorTarget);
        } else if (floorButtons.contains(floorTarget) && floorTarget != nearestFloor) {
            floorButtons.remove(Integer.valueOf(floorTarget));
            floorButtons.add(0, floorTarget);
        }
    }

    @Override
    public void update(IBuildingElevator elevator) {
        number = elevator.getNumber();
        setDirection(elevator.getDirection());
        floorTarget = elevator.getFloorTarget();
        floorTargetProperty.set(floorTarget);
        acceleration = elevator.getAcceleration();
        floorButtons = FXCollections.observableArrayList(elevator.getFloorButtons());
        doorState = elevator.getDoorState();
        doorStateProperty.set(Door_State.getDoorStateString(doorState));
        positionFromGround = elevator.getPositionFromGround();
        setSpeed(elevator.getSpeed());
        setWeight(elevator.getWeight());
        setNearestFloor(elevator.getNearestFloor());
        capacity = elevator.getCapacity();
        floorServices = FXCollections.observableArrayList(elevator.getFloorServices());
        setFloorButtons();
    }

    private void setSpeed(int speed) {
        this.speed = speed;
        speedProperty.set(speed);
    }

    public void setNearestFloor(int nearestFloor) {
        this.nearestFloor = nearestFloor;
        nearestFloorProperty.set(nearestFloor);
    }

    public void setWeight(int weight) {
        this.weight = weight;
        weightProperty.setValue(weight);
    }

    /**
     * Method to add a floor button if it was pressed
     *
     * @param floorNumber number of the floor
     */
    public void addPressedFloorButton(int floorNumber) {
        // If pressed floor is not pressed and is not the current floor we add it
        if (!floorButtons.contains(floorNumber) && nearestFloor != floorNumber) {
            floorButtons.add(floorNumber);
        } else {
            floorButtons.remove(Integer.valueOf(floorNumber));
        }
        if (!floorButtons.isEmpty()) {
            setFloorTarget(floorButtons.get(0));
        } else {
            setFloorTarget(nearestFloor);
        }
    }
}
