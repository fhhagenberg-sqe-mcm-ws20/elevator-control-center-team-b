package at.fhhagenberg.model;

import at.fhhagenberg.converter.ModelConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sqelevator.IElevator;
import sqelevator.MockElevator;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ModelConverterTest {

    private ModelConverter converter;
    private Building testBuilding;
    private IElevator elevatorConnection;

    @BeforeEach
    void Init() throws RemoteException {
        elevatorConnection = new MockElevator();
        converter = new ModelConverter(elevatorConnection);
        testBuilding = converter.init();
    }

    @Test
    void testInit() {
        assertNotNull(testBuilding);
        assertEquals(5, testBuilding.getFloorCount());
        assertEquals(10, testBuilding.getFloorHeight());
        assertEquals(9, testBuilding.getElevatorCount());
    }

    @Test
    void testSetTarget() throws RemoteException {
        converter.setTarget(1, 1);
        assertEquals(1, elevatorConnection.getTarget(1));
    }

    @Test
    void testSetCommittedDirection() throws RemoteException {
        converter.setCommittedDirection(1, 1);
        assertEquals(1, elevatorConnection.getCommittedDirection(1));
    }

}
