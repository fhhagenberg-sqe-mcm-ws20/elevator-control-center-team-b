package at.fhhagenberg;

import at.fhhagenberg.controller.MainController;
import at.fhhagenberg.model.*;
import at.fhhagenberg.sqe.IElevator;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.EmptyNodeQueryException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.isVisible;

@ExtendWith(ApplicationExtension.class)
class GuiApplicationTest {

    private IElevator system;
    private IBuildingElevator[] elevators;
    private IFloor[] floors;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @SneakyThrows
    @Start
    private void start(Stage stage) {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        Parent root = mainLoader.load();
        stage.setTitle("Elevator System");
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Mock data
        boolean[] temp = new boolean[5];
        boolean[] tempButton = new boolean[5];
        Arrays.fill(temp, true);
        tempButton[0] = true;

        floors = new IFloor[5];
        floors[0] = new Floor(false, true);
        floors[1] = new Floor(false, false);
        floors[2] = new Floor(false, false);
        floors[3] = new Floor(false, false);
        floors[4] = new Floor(false, false);

        elevators = new Elevator[9];
        elevators[0] = new Elevator(5, 200, 300);
        elevators[1] = new Elevator(IBuildingElevator.Direction_State.DOWN.value(), 2, tempButton, IBuildingElevator.Door_State.CLOSED.value(), 3, 30, 2, 1500, 10, temp, 2);
        elevators[2] = new Elevator(IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, new boolean[5], IBuildingElevator.Door_State.OPEN.value(), 1, 10, 0, 1500, 10, temp, 0);
        elevators[3] = new Elevator(5, 250, 300);
        elevators[4] = new Elevator(IBuildingElevator.Direction_State.DOWN.value(), 2, tempButton, IBuildingElevator.Door_State.CLOSED.value(), 3, 30, 2, 1500, 10, temp, 4);
        elevators[5] = new Elevator(IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, new boolean[5], IBuildingElevator.Door_State.OPEN.value(), 1, 10, 0, 1500, 10, temp, 0);
        elevators[6] = new Elevator(5, 150, 300);
        elevators[7] = new Elevator(IBuildingElevator.Direction_State.DOWN.value(), 2, tempButton, IBuildingElevator.Door_State.CLOSED.value(), 3, 30, 2, 360, 300, temp, 2);
        elevators[8] = new Elevator(IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, new boolean[5], IBuildingElevator.Door_State.OPEN.value(), 1, 10, 0, 420, 400, temp, 0);

        Building mockBuilding = new Building(3, 10, 5, elevators, floors);
        system = new ElevatorSystem(mockBuilding, 100L);

        ((MainController) mainLoader.getController()).initModel(system);
    }

    @Test
    void testIsPaneVisible() {
        verifyThat("#elevator_view", isVisible());
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testShowAllEleveators(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#elevator_view").queryAs(JFXMasonryPane.class).getChildren().size() == elevators.length);
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testShowErrorMessageInLeftMenu(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#PAYLOAD5").queryAs(Label.class)).hasText("Elevator 5: Error payload too high.");
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testShowWarningMessageInLeftMenu(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#PAYLOAD7").queryAs(Label.class)).hasText("Elevator 7: Warning payload on a high level.");
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testManualModeWork(FxRobot robot) {
        // Given
        JFXComboBox<Integer> firstElevator = robot.lookup("#elevator1").lookup("#target_field").queryAs(JFXComboBox.class);
        Assertions.assertThat(firstElevator.isDisabled());
        assertEquals(firstElevator.getValue().intValue(), 2);

        //When
        robot.lookup("#mode_button").queryAs(JFXToggleButton.class).setSelected(false);
        Assertions.assertThat(!firstElevator.isDisabled());

        //Then
        robot.clickOn(firstElevator);
        robot.sleep(100);
        // Click on the first entry
        robot.clickOn(robot.offset(firstElevator, new Point2D(0, 20)));
        robot.sleep(100);
        assertEquals(firstElevator.getValue().intValue(), 0);
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testCancelWarning(FxRobot robot) {
        // Given
        Assertions.assertThat(robot.lookup("#PAYLOAD7").queryAs(Label.class)).hasText("Elevator 7: Warning payload on a high level.");

        //When
        Platform.runLater(() -> ((Elevator) elevators[7]).setWeight(290));
        robot.sleep(100);

        //Then
        assertThrows(EmptyNodeQueryException.class, () -> {
            Label WarningLabel = robot.lookup("#PAYLOAD7").queryAs(Label.class);
        });
    }
}
