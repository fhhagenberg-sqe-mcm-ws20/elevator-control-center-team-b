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
        building.getElevators().forEach(elevator -> updateTarget(elevator, requestedFloors.stream().map(Floor::getNumber).filter(elevator::servesFloor).collect(Collectors.toList())));
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
        if (floors.isEmpty() && elevator.getFloorButtons().isEmpty()) {
            return;
        }

        var currentFloor = elevator.getNearestFloor();
        var doorStatus = elevator.getDoorState();

        if (doorStatus != IBuildingElevator.Door_State.OPEN.value()) {
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
            } else {
                if (elevator.getLastDirection() == IBuildingElevator.Direction_State.UP.value()) {
                    setNextUpTarget(elevator, floors);
                } else {
                    setNextDownTarget(elevator, floors);
                }
            }
        }
    }

    private void setNextDownTarget(IBuildingElevator elevator, List<Integer> floors) {
        int largestElevatorButton = -1;
        int largestFloor = -1;
        int nextFloor = 0;
        int currentFloor = elevator.getNearestFloor();

        if (!elevator.getFloorButtons().isEmpty()) {
            largestElevatorButton = Collections.max(elevator.getFloorButtons());
        }

        if (!floors.isEmpty()) {
            largestFloor = Collections.max(floors);
        }

        if (largestFloor == -1 && largestElevatorButton == -1) {
            return;
        } else if (largestFloor != -1 && largestElevatorButton == -1) {
            nextFloor = largestFloor;
        } else if (largestFloor == -1) {
            nextFloor = largestElevatorButton;
        } else {
            nextFloor = Math.max(largestElevatorButton, largestFloor);

            if (currentFloor == nextFloor) {
                nextFloor = Math.min(largestElevatorButton, largestFloor);
            }
        }

        removeAlreadyServicedFloor(nextFloor);
        elevator.setFloorTarget(nextFloor);
    }

    private void setNextUpTarget(IBuildingElevator elevator, List<Integer> floors) {
        int smallestElevatorButton = -1;
        int smallestFloor = -1;
        int nextFloor = 0;
        int currentFloor = elevator.getNearestFloor();

        if (!elevator.getFloorButtons().isEmpty()) {
            smallestElevatorButton = Collections.min(elevator.getFloorButtons());
        }

        if (!floors.isEmpty()) {
            smallestFloor = Collections.min(floors);
        }

        if (smallestFloor == -1 && smallestElevatorButton == -1) {
            return;
        } else if (smallestFloor != -1 && smallestElevatorButton == -1) {
            nextFloor = smallestFloor;
        } else if (smallestFloor == -1) {
            nextFloor = smallestElevatorButton;
        } else {
            nextFloor = Math.min(smallestElevatorButton, smallestFloor);

            if (currentFloor == nextFloor) {
                nextFloor = Math.max(smallestElevatorButton, smallestFloor);
            }
        }

        removeAlreadyServicedFloor(nextFloor);
        elevator.setFloorTarget(nextFloor);
    }
}
