package at.fhhagenberg;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sqelevator.IElevator;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ElevatorControlSystemTest {

    static IElevator mockSystem;
    static ElevatorControlSystem elevatorControlSystem;
    static Registry rmiRegistry;
    static String connectionString = "elevatorSim";
    static int port = 8080;

    @BeforeAll
    @SneakyThrows
    static void BeforeAll() {
        // We need a serializable object
        mockSystem = mock(IElevator.class, Mockito.withSettings().serializable());
        rmiRegistry = LocateRegistry.createRegistry(port);
        rmiRegistry.bind(connectionString, mockSystem);
        elevatorControlSystem = new ElevatorControlSystem(String.format("rmi://localhost:%d/%s", port, connectionString));
    }

    @Test
    @SneakyThrows
    void testReconnectToSimulator() {
        // Wait for connection
        Thread.sleep(100);
        assertTrue(elevatorControlSystem.getSystemConnected().get());
        // Wait for connection lost
        rmiRegistry.unbind(connectionString);
        Thread.sleep(500);
        elevatorControlSystem.reconnectToSimulator();
        assertFalse(elevatorControlSystem.getSystemConnected().get());
        // Wait again for connection
        rmiRegistry.bind(connectionString, mockSystem);
        Thread.sleep(500);
        assertTrue(elevatorControlSystem.getSystemConnected().get());
    }
}