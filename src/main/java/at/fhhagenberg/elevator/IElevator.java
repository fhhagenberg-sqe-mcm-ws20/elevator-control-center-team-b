package at.fhhagenberg.elevator;

public interface IElevator {

    /**
     * Enum for state of elevator doors
     */
    enum Door_State {
        open(1),
        closed(2),
        opening(3),
        closing(4);

        private int state;
        Door_State(final int value){
            this.state = value;
        }

        public int value() {
            return this.state;
        }

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
        up(0),
        down(1),
        uncommitted(2);

        private int state;
        Direction_State(final int value){
            this.state = value;
        }

        public int value() {
            return this.state;
        }

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

    int getCommittedDirection();
    int getAccel();
    boolean getButtonStatus(int floor);
    int getDoorStatus();
    int getCurrentFloor();
    int getPosition();
    int getSpeed();
    int getWeight();
    int getCapacity();
    boolean servesFloor(int floor);
    int getTarget();
    void setDirection(int direction);
    void setServicesFloor(int floor, boolean service);
    void setTarget(int floor);
}
