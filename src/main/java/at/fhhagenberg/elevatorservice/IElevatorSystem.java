package at.fhhagenberg.elevatorservice;

import at.fhhagenberg.elevator.IElevator;
import at.fhhagenberg.floor.IFloor;
import lombok.Getter;

/**
 * High-level interface to access and control the elevator system.
 *
 * <p>The elevator's behavior is impacted by the following parameters:
 * <ul><li><i>Elevator capacity.</i> The maximum number of passengers that can fit on an
 * elevator.</li></ul>
 * <ul><li><i>Elevator speed.</i> The maximum speed an elevator can travel at in feet/sec.
 * It is assumed to be the same for all elevators.</li></ul>
 * <ul><li><i>Elevator acceleration.</i> The rate at which the elevator can increase or decrease
 * speed in ft/sec2. It is assumed to be the same for all elevators. The higher the acceleration,
 * the faster the elevator can accelerate and decelerate, allowing its average speed to be faster.</li></ul>
 * <ul><li><i>Number of floors.</i> The number of floors in the building, including the ground floor. It is
 * assumed there are no floors below ground level. All elevators service the
 * ground floor but may, as described below, not necessarily service all other floors. Each floor has
 * an up and a down button for the purposes of passengers calling the elevator. Floor numbering starts
 * at 0 for floor 1.</li></ul>
 * <ul><li><i>Floor height.</i> The height of the floors in feet. It is assumed that each floor is
 * the same height.</li></ul>
 * <ul><li><i>Number of elevators.</i> The number of elevators in the building. Elevator numbering starts
 * at zero for elevator 1.</li></ul>
 *
 * <p>Some elevator behavior is inherent and cannot be controlled:
 * <ul><li><i>Door opening and closing.</i> Elevator doors automatically open when arriving at a floor and
 * close before departing. When passengers are waiting to board an elevator, the doors will
 * stay open until all passengers are boarded. Each open and close action takes 1 second.</li></ul>
 * <ul><li><i>Elevator buttons.</i> Arriving passengers press the appropriate elevator button and the
 * light is automatically turned off when the elevator arrives at the associated floor.</li></ul>
 * <ul><li><i>Elevator speed and acceleration.</i> The elevator will automatically accelerate to the maximum
 * speed possible until there is only enough time left to slow down to arrive at the target floor.</li></ul>
 *
 * <p>There is substantial elevator status information available:
 * <ul><li><i>Elevator speed.</i> The current speed of the elevator. Positive speed indicates an elevator
 * heading up while negative indicates an elevator going down.</li></ul>
 * <ul><li><i>Elevator acceleration.</i> The acceleration of the elevators.</li></ul>
 * <ul><li><i>Elevator button status.</i> For each elevator, which floor buttons on the elevator have
 * been pressed.</li></ul>
 * <ul><li><i>Elevator position.</i> The current position of the elevator, both in feet and to
 * the closest floor. Position is measured in feet above ground floor with zero being the bottom.</li></ul>
 * <ul><li><i>Elevator door status.</i> Whether the doors for an elevator are open or closed. The
 * status may also indicate a transition between open or closed.</li></ul>
 * <ul><li><i>Elevator target.</i> The current floor target of the elevator as set by the controller. The
 * elevator will travel to that target and stop until directed to the next target.</li></ul>
 * <ul><li><i>Elevator committed direction.</i> The current committed direction of the elevator. Elevators
 * responding to a passenger floor button must have a committed direction, up or down. Otherwise,
 * passengers wishing to go up would board the same elevators as passengers wishing to go down.
 * Accordingly, each elevator can have an up, down or uncommitted committed direction set in response
 * to passenger travel..</li></ul>
 * <ul><li><i>Elevator floor service.</i> Whether a particular elevator services a particular floor. When
 * elevators are allowed to only service certain floors, this can help to achieve greater passenger
 * service. Every elevator must service the ground floor.</li></ul>
 * <ul><li><i>Elevator weight.</i> This provides the current weight of the elevator less the weight of the
 * empty elevator – hence the weight of the passengers on board. This can be useful for detecting
 * when the elevator is getting full.</li></ul>
 *
 * <p>The elevators can be controlled by setting the following parameters:
 * <ul><li><i>Elevator target.</i> Sets the floor target of a particular elevator. Typically, this target
 * is set in response to a given passenger selecting a floor up or down button, or selecting a floor
 * button in an elevator. The elevator will travel to that target and stop until directed to the
 * next target.</li></ul>
 * <ul><li><i>Elevator committed direction.</i> Sets the current committed direction of the elevator. This
 * is set so that passengers board elevators going in their desired direction. This is indicated by the
 * controller by whether the up or down floor button has been pressed on a given floor. This is
 * an important concept and it should be recognized that in some cases, an elevator may be
 * going down, for example, but its committed direction is up – reflecting the fact it has been
 * dispatched down to pick up a passenger who is traveling up. Whenever an elevator is being
 * dispatched to a target floor, it should have a committed direction set.</li></ul>
 * <ul><li><i>Elevator floor service.</i> Sets whether a particular elevator services a particular floor.
 * In larger buildings, it is typically better to set elevator service to a particular subset of floors.
 * Note that the ground floor will always get service.</li></ul>
 *
 * @version 0.1
 */

public interface IElevatorSystem extends java.rmi.Remote {

    /**
     * State variable for elevator doors open.
     */
    public final static int ELEVATOR_DOORS_OPEN = 1;
    /**
     * State variable for elevator doors closed.
     */
    public final static int ELEVATOR_DOORS_CLOSED = 2;
    /**
     * State variable for elevator doors opening.
     */
    public final static int ELEVATOR_DOORS_OPENING = 3;
    /**
     * State variable for elevator doors closing.
     */
    public final static int ELEVATOR_DOORS_CLOSING = 4;

    /**
     * State variable for elevator status when going up
     */
    public final static int ELEVATOR_DIRECTION_UP = 0;
    /**
     * State variable for elevator status when going down.
     */
    public final static int ELEVATOR_DIRECTION_DOWN = 1;
    /**
     * State variables for elevator status stopped and uncommitted.
     */
    public final static int ELEVATOR_DIRECTION_UNCOMMITTED = 2;

    IElevator getElevator(int elevatorNumber);

    /**
     * Provides the status of the Down button on specified floor (on/off).
     *
     * @param floor - floor number whose Down button status is being retrieved
     * @return returns boolean to indicate if button is active (true) or not (false)
     */
    boolean getFloorButtonDown(int floor) throws java.rmi.RemoteException;

    /**
     * Provides the status of the Up button on specified floor (on/off).
     *
     * @param floor - floor number whose Up button status is being retrieved
     * @return returns boolean to indicate if button is active (true) or not (false)
     */
    boolean getFloorButtonUp(int floor) throws java.rmi.RemoteException;

    /**
     * Retrieves the current clock tick of the elevator control system.
     *
     * @return clock tick
     */
    long getClockTick() throws java.rmi.RemoteException;

    /**
     * Retrieves the current elevator count of the elevator control system.
     *
     * @return elevator count
     */
    int getElevatorCount();

    /**
     * Retrieves the current floor height of the elevator control system.
     *
     * @return floor height
     */
    int getFloorHeight();

    /**
     * Retrieves the current floor count of the elevator control system.
     *
     * @return floor count
     */
    int getFloorCount();

}