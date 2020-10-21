package at.fhhagenberg.elevator;

/**
 * Elevator interface
 */
public class ElevatorConstants {

    /**
     * Enum for state of elevator doors
     */
    public enum Door_State {
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
        public static Door_State setValue(int value) {
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
    public enum Direction_State {
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
        public static Direction_State createFromValue(int value) {
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
}