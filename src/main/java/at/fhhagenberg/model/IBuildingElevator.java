package at.fhhagenberg.model;

import java.util.List;

public interface IBuildingElevator {


    /**
     * Enum for state of elevator doors
     */
    enum Door_State {
        /**
         * Open door state
         */
        OPEN(1),
        /**
         * Closed door state
         */
        CLOSED(2),
        /**
         * Door is opening state
         */
        OPENING(3),
        /**
         * Door is closing state
         */
        CLOSING(4);

        private final int state;

        /**
         * Default constructor
         *
         * @param value state value
         */
        Door_State(final int value) {
            this.state = value;
        }

        /**
         * Get numerical value of the door state
         *
         * @return value
         */
        public int value() {
            return this.state;
        }

        /**
         * Create enum from value
         *
         * @param value door state value
         * @return door state or default value
         */
        public Door_State setValue(int value) {
            switch (value) {
                case 1:
                    return Door_State.OPEN;
                case 3:
                    return Door_State.OPENING;
                case 4:
                    return Door_State.CLOSING;
                default:
                    return Door_State.CLOSED;
            }
        }

        public static String getDoorStateString(int value) {
            switch (value) {
                case 1:
                    return "Open";
                case 3:
                    return "Opening";
                case 4:
                    return "Closing";
                default:
                    return "Closed";
            }
        }
    }

    /**
     * Enum state for direction of the elevator
     */
    enum Direction_State {
        /**
         * Up direction state of the elevator
         */
        UP(0),
        /**
         * Down state of the elevator
         */
        DOWN(1),
        /**
         * Uncommitted state of the elevator
         */
        UNCOMMITTED(2);

        private final int state;

        /**
         * Default constructor
         *
         * @param value state
         */
        Direction_State(final int value) {
            this.state = value;
        }

        /**
         * Get numerical value of the door state
         *
         * @return value
         */
        public int value() {
            return this.state;
        }

        /**
         * Create enum from value
         *
         * @param value direction state value
         * @return direction state or default value
         */
        public Direction_State createFromValue(int value) {
            switch (value) {
                case 0:
                    return Direction_State.UP;
                case 1:
                    return Direction_State.DOWN;
                default:
                    return Direction_State.UNCOMMITTED;
            }
        }
    }

    /**
     * Direction of the elevator
     *
     * @return direction state
     */
    int getDirection();

    /**
     * Acceleration of the elevator
     *
     * @return acceleration
     */
    int getAcceleration();

    /**
     * Status of a button for a certain floor
     *
     * @param floor requested floor
     * @return state
     */
    boolean getButtonStatus(int floor);

    /**
     * Door status of the elevator
     *
     * @return door status
     */
    int getDoorState();

    /**
     * Nearest floor to the elevator
     *
     * @return nearest floor
     */
    int getNearestFloor();

    /**
     * Position of the elevator from ground in feet
     *
     * @return position from ground
     */
    int getPositionFromGround();

    /**
     * Current speed of the elevator
     *
     * @return speed
     */
    int getSpeed();

    /**
     * Weight of the elevator
     *
     * @return weight
     */
    int getWeight();

    /**
     * Allowed capacity of the elevator
     *
     * @return capacity
     */
    int getCapacity();

    /**
     * Check if elevator serves floor
     *
     * @param floor floor number
     * @return true | false
     */
    boolean servesFloor(int floor);

    /**
     * Returns the current target of the elevator
     *
     * @return target
     */
    int getFloorTarget();

    /**
     * Set the direction of the elevator
     *
     * @param direction direction state
     */
    void setDirection(int direction);

    /**
     * Set if an elevator serves a particular floor
     *
     * @param floor   floor number
     * @param service service
     */
    void setServicesFloor(int floor, boolean service);

    /**
     * Sets target of the elevator
     *
     * @param floor floor
     */
    void setFloorTarget(int floor);

    int getNumber();

    List<Integer> getFloorButtons();

    List<Integer> getFloorServices();

    void update(IBuildingElevator elevator);

    void addPressedFloorButton(int floorNumber);
}
