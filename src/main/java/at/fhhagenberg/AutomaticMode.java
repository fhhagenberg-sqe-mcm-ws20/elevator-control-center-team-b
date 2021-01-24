package at.fhhagenberg;

import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.Floor;
import at.fhhagenberg.model.IBuildingElevator;
import at.fhhagenberg.model.IFloor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AutomaticMode {
    private List<Floor> requestedFloors = new ArrayList<>();

    public void update(Building building) {
        //update elevator queues
        requestedFloors = getRequestedFloors(building);
        updateElevatorQueues(building);
    }

    private void updateElevatorQueues(Building building) {
        // get nearest elevator button
        building.getElevators().forEach(elevator -> {
            updateTarget(elevator, requestedFloors.stream().map(Floor::getNumber).filter(elevator::servesFloor).collect(Collectors.toList()));
        });
    }

    private void removeAlreadyServicedFloor(int servicedFloor) {
        requestedFloors.removeIf(floor -> floor.getNumber() == servicedFloor);
    }

    private List<Floor> getRequestedFloors(Building building) {
        ArrayList<Floor> result = new ArrayList<>();

        for (IFloor floor : building.getFloors()) {
            if (floor.isDownButtonActive() || floor.isUpButtonActive()) {
                result.add((Floor) floor);
            }
        }
        return result;
    }

    private void updateTarget(IBuildingElevator elevator, List<Integer> floors) {
        var currentFloor = elevator.getNearestFloor();
        var targetFloor = elevator.getFloorTarget();
        var doorStatus = elevator.getDoorState();


        if (currentFloor == targetFloor || doorStatus == IBuildingElevator.Door_State.OPENING.value()) {
            return;
        }

        List<Integer> servicedFloors = elevator.getFloorServices();
        int smallestServicedFloorNumber = Collections.min(servicedFloors);
        int largestServicedFloorNumber = Collections.max(servicedFloors);

        if (currentFloor == smallestServicedFloorNumber) {
            setNextUpTarget(elevator, floors);
        } else if (currentFloor == largestServicedFloorNumber) {
            setNextDownTarget(elevator, floors);
        } else {
            //stay in same direction
            if (elevator.getDirection() == IBuildingElevator.Direction_State.UP.value()) {
                setNextUpTarget(elevator, floors);
            } else if (elevator.getDirection() == IBuildingElevator.Direction_State.DOWN.value()) {
                setNextDownTarget(elevator, floors);
            }
        }
    }

    private void setNextDownTarget(IBuildingElevator elevator, List<Integer> floors) {
        int largestElevatorButton = Collections.max(elevator.getFloorButtons());
        int largestFloor = Collections.max(floors);
        // take either floor or floor button, whichever is nearer
        int nextFloor = Math.min(largestElevatorButton, largestFloor);

        System.out.println("next floor " + nextFloor);

        removeAlreadyServicedFloor(nextFloor);
        elevator.setFloorTarget(nextFloor);
    }

    private void setNextUpTarget(IBuildingElevator elevator, List<Integer> floors) {
        int smallestElevatorButton = Collections.min(elevator.getFloorButtons());
        int smallestFloor = Collections.min(floors);
        // take either floor or floor button, whichever is nearer
        int nextFloor = Math.min(smallestElevatorButton, smallestFloor);

        System.out.println("next floor " + nextFloor);

        removeAlreadyServicedFloor(nextFloor);
        elevator.setFloorTarget(nextFloor);
    }
}
