package at.fhhagenberg.model;

import lombok.Getter;

/**
 * A building contains floors, height and elevators
 */
public class Building {
    @Getter
    private final int elevatorCount;
    @Getter
    private final int floorHeight;
    @Getter
    private final int floorCount;
    @Getter
    private final IBuildingElevator[] elevators;
    @Getter
    private final IFloor[] floors;

    public Building(int elevatorCount, int floorHeight, int floorCount, IBuildingElevator[] elevators, IFloor[] floors) {
        this.elevatorCount = elevatorCount;
        this.floorHeight = floorHeight;
        this.floorCount = floorCount;
        this.elevators = elevators;
        this.floors = floors;
    }

    public IBuildingElevator getElevator(int elevatorNumber) {
        if (elevatorNumber > elevatorCount - 1 || elevatorNumber < 0) return null;
        return elevators[elevatorNumber];
    }
}
