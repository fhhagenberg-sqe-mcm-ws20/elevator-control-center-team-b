package at.fhhagenberg.elevator;

public interface IElevator {

    /**
     * Enum for state of elevator doors
     */
    enum Door_State {
        /**
         * Open door state
         */
        open(1),
        /**
         * Closed door state
         */
        closed(2),
        /**
         * Door is opening state
         */
        opening(3),
        /**
         * Door is closing state
         */
        closing(4);

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
                    return Door_State.open;
                case 2:
                    return Door_State.closed;
                case 3:
                    return Door_State.opening;
                case 4:
                    return Door_State.closing;
            }

            return Door_State.closed;
        }
    }

    /**
     * Enum state for direction of the elevator
     */
    enum Direction_State {
        /**
         * Up direction state of the elevator
         */
        up(0),
        /**
         * Down state of the elevator
         */
        down(1),
        /**
         * Uncommitted state of the elevator
         */
        uncommitted(2);

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
                    return Direction_State.up;
                case 1:
                    return Direction_State.down;
                case 2:
                    return Direction_State.uncommitted;
            }

            return Direction_State.uncommitted;
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
     * @return position
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

}
