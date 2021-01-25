package at.fhhagenberg;

import at.fhhagenberg.controller.MainController;
import at.fhhagenberg.converter.ModelConverter;
import at.fhhagenberg.model.Building;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import sqelevator.IElevator;
import sqelevator.MockElevatorAuto;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class AutomaticModeTest {

    private Building testBuilding;
    private ModelConverter converter;
    private IElevator elevatorConnection;
    private AutomaticMode mode = new AutomaticMode();

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     */
    @SneakyThrows
    @BeforeEach
    private void start() {
        elevatorConnection = new MockElevatorAuto();
        converter = new ModelConverter(elevatorConnection);
        testBuilding = converter.init();
    }

    @Test
    void testUpdate() {
        assertEquals(0, testBuilding.getElevators().get(0).getFloorTarget());
        assertEquals(2, testBuilding.getElevators().get(1).getFloorTarget());
        assertEquals(4, testBuilding.getElevators().get(2).getFloorTarget());

        Platform.runLater(() -> {
            mode.update(testBuilding);

            assertEquals(1, testBuilding.getElevators().get(0).getFloorTarget());
            assertEquals(0, testBuilding.getElevators().get(1).getFloorTarget());
            assertEquals(4, testBuilding.getElevators().get(2).getFloorTarget());
        });
    }
}