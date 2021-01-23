package at.fhhagenberg.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    private final List<IBuildingElevator> elevators;
    @Getter
    private final List<IFloor> floors;

    public Building() {
        elevatorCount = 0;
        floorHeight = 0;
        floorCount = 0;
        elevators = new ArrayList<>();
        floors = new ArrayList<>();
    }

    public Building(int elevatorCount, int floorHeight, int floorCount, List<IBuildingElevator> elevators, List<IFloor> floors) {
        this.elevatorCount = elevatorCount;
        this.floorHeight = floorHeight;
        this.floorCount = floorCount;
        this.elevators = elevators;
        this.floors = floors;
    }

    public IBuildingElevator getElevator(int elevatorNumber) {
        if (elevatorNumber > elevatorCount - 1 || elevatorNumber < 0) return null;
        return elevators.get(elevatorNumber);
    }

    public void update(List<IFloor> floors, List<IBuildingElevator> elevators) {
        for (IFloor floor : floors) {
            this.floors.get(floor.getNumber()).update(floor);
        }
        for (IBuildingElevator elevator : elevators) {
            System.out.println("UPDATE FLOOR: " + elevator.getNumber());
            System.out.println(elevator.getWeight());
            this.elevators.get(elevator.getNumber()).update(elevator);
        }
    }
}
