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
        if (building.getElevators().isEmpty() || building.getFloors().isEmpty()) {
            return;
        }

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

        if (doorStatus == IBuildingElevator.Door_State.OPENING.value()) {
            elevator.setFloorTarget(currentFloor);
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
        // no floor or button is available
        if (floors.isEmpty() && elevator.getFloorButtons().isEmpty()) {
            return;
        }

        int largestElevatorButton = 0;
        int largestFloor = 0;

        if (!elevator.getFloorButtons().isEmpty()) {
            largestElevatorButton = Collections.max(elevator.getFloorButtons());
        }

        if (!floors.isEmpty()) {
            largestFloor = Collections.max(floors);
        }

        if (!floors.isEmpty() && !elevator.getFloorButtons().isEmpty()) {
            int nextFloor = Math.max(largestElevatorButton, largestFloor);
            removeAlreadyServicedFloor(nextFloor);
            elevator.setFloorTarget(nextFloor);
        } else if (floors.isEmpty()) {
            removeAlreadyServicedFloor(largestElevatorButton);
            elevator.setFloorTarget(largestElevatorButton);
        } else {
            removeAlreadyServicedFloor(largestFloor);
            elevator.setFloorTarget(largestFloor);
        }
    }

    private void setNextUpTarget(IBuildingElevator elevator, List<Integer> floors) {
        // no floor or button is available
        if (floors.isEmpty() && elevator.getFloorButtons().isEmpty()) {
            return;
        }

        int smallestElevatorButton = 0;
        int smallestFloor = 0;

        if (!elevator.getFloorButtons().isEmpty()) {
            smallestElevatorButton = Collections.min(elevator.getFloorButtons());
        }

        if (!floors.isEmpty()) {
            smallestFloor = Collections.min(floors);
        }

        if (!floors.isEmpty() && !elevator.getFloorButtons().isEmpty()) {
            int nextFloor = Math.max(smallestElevatorButton, smallestFloor);
            removeAlreadyServicedFloor(nextFloor);
            elevator.setFloorTarget(nextFloor);
        } else if (floors.isEmpty()) {
            removeAlreadyServicedFloor(smallestElevatorButton);
            elevator.setFloorTarget(smallestElevatorButton);
        } else {
            removeAlreadyServicedFloor(smallestFloor);
            elevator.setFloorTarget(smallestFloor);
        }
    }
}
