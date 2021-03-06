package at.fhhagenberg;

import at.fhhagenberg.controller.MainController;
import at.fhhagenberg.converter.ModelConverter;
import at.fhhagenberg.model.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.EmptyNodeQueryException;
import sqelevator.IElevator;
import sqelevator.MockElevator;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static at.fhhagenberg.controller.GuiConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.isVisible;

@ExtendWith(ApplicationExtension.class)
class GuiApplicationTest {

    private MainController controller;
    private Building testBuilding;
    private ModelConverter converter;
    private IElevator elevatorConnection;

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
        controller = mainLoader.getController();

        elevatorConnection = new MockElevator();
        converter = new ModelConverter(elevatorConnection);
        testBuilding = converter.init();

        controller.setModel(testBuilding);
    }

    @Test
    void testIsPaneVisible() {
        verifyThat("#elevatorView", isVisible());
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testShowAllElevators(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#elevatorView").queryAs(JFXMasonryPane.class).getChildren().size() == testBuilding.getElevators().size()).isTrue();
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testShowErrorMessageInLeftMenu(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#" + PAYLOAD_ID_PREFIX + "5").queryAs(Label.class)).hasText("Elevator 5: Error payload too high.");
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testShowWarningMessageInLeftMenu(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#" + PAYLOAD_ID_PREFIX + "7").queryAs(Label.class)).hasText("Elevator 7: Warning payload on a high level.");
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testCancelWarning(FxRobot robot) {
        // Given
        Assertions.assertThat(robot.lookup("#" + PAYLOAD_ID_PREFIX + "7").queryAs(Label.class)).hasText("Elevator 7: Warning payload on a high level.");

        //When
        Platform.runLater(() -> ((Elevator) testBuilding.getElevator(7)).setWeight(290));
        robot.sleep(100);

        //Then
        assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#" + PAYLOAD_ID_PREFIX + "7").queryAs(Label.class));
    }

    @Test
    void testInitException() {
        assertThrows(IllegalStateException.class, () -> controller.getElevatorControllers().get(0).initModel(null, 0, 0, null));
    }

    @Test
    void testCreateInfo(FxRobot robot) {
        Platform.runLater(() -> {
            controller.getElevatorControllers().get(0).createInfo("WARNING", "test", "Test Warning");
            controller.getElevatorControllers().get(0).createInfo("WARNING", "test", "Test test");
            Assertions.assertThat(robot.lookup("#test").queryAs(Label.class)).hasText("Test Warning");

            controller.getElevatorControllers().get(0).createInfo("TEST", "test_nonexistent", "Test test");
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#test_nonexistent").queryAs(Label.class));
        });

        Platform.runLater(() -> {
            controller.getElevatorControllers().get(0).createInfo("ERROR", "test2", "Test test2");
            controller.getElevatorControllers().get(0).createInfo("ERROR", "test2", "Test test");
            Assertions.assertThat(robot.lookup("#test2").queryAs(Label.class)).hasText("Test test2");
        });
    }

    @Test
    void testDeleteInfo(FxRobot robot) {
        Platform.runLater(() -> {
            controller.getElevatorControllers().get(0).createInfo("WARNING", "test3", "Test");
            controller.getElevatorControllers().get(0).createInfo("WARNING", "test32", "Test");
            controller.getElevatorControllers().get(0).createInfo("ERROR", "test33", "Test");
            controller.getElevatorControllers().get(0).deleteInfo("test3");
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#test3").queryAs(Label.class));

            controller.getElevatorControllers().get(0).deleteInfo(PAYLOAD_ID_PREFIX + "7");
            controller.getElevatorControllers().get(0).deleteInfo(PAYLOAD_ID_PREFIX + "8");
            controller.getElevatorControllers().get(0).deleteInfo(PAYLOAD_ID_PREFIX + "5");
            controller.getElevatorControllers().get(0).deleteInfo(PAYLOAD_ID_PREFIX + "1");
            controller.getElevatorControllers().get(0).deleteInfo(PAYLOAD_ID_PREFIX + "2");
            controller.getElevatorControllers().get(0).deleteInfo(PAYLOAD_ID_PREFIX + "4");
            controller.getElevatorControllers().get(0).deleteInfo("test32");
            controller.getElevatorControllers().get(0).deleteInfo("test33");

            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#" + PAYLOAD_ID_PREFIX + "7").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#" + PAYLOAD_ID_PREFIX + "8").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#" + PAYLOAD_ID_PREFIX + "5").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#" + PAYLOAD_ID_PREFIX + "1").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#" + PAYLOAD_ID_PREFIX + "2").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#" + PAYLOAD_ID_PREFIX + "4").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#test32").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#test33").queryAs(Label.class));
        });
    }

    @Test
    void testAddInfoToLeftPane(FxRobot robot) {
        Platform.runLater(() -> {
            controller.getElevatorControllers().get(0).addInfoToLeftMenu("test4", "WARNING", "Hello warning", "left-bar-info", FontAwesomeIcon.EXCLAMATION);
            controller.getElevatorControllers().get(0).addInfoToLeftMenu("test4", "WARNING", "Hello warning", "left-bar-info", FontAwesomeIcon.EXCLAMATION);
            Assertions.assertThat(robot.lookup("#test4").queryAs(Label.class)).hasText("Hello warning");

            controller.getElevatorControllers().get(0).addInfoToLeftMenu("test4", "WARNING", "Hello warning 2", "left-bar-test", FontAwesomeIcon.EXCLAMATION);
            Assertions.assertThat(robot.lookup("#test4").queryAs(Label.class)).hasText("Hello warning 2");

            controller.getElevatorControllers().get(0).addInfoToLeftMenu("test4", "ERROR", "Hello error", "left-bar-test", FontAwesomeIcon.EXCLAMATION);
            Assertions.assertThat(robot.lookup("#test4").queryAs(Label.class)).hasText("Hello error");

            controller.getElevatorControllers().get(0).addInfoToLeftMenu("test4", "WARNING", "Hello error 2", "left-bar-test", FontAwesomeIcon.EXCLAMATION);
            Assertions.assertThat(robot.lookup("#test4").queryAs(Label.class)).hasText("Hello error 2");
        });
    }

    @Test
    void createFloorButton() {
        JFXButton test = controller.getElevatorControllers().get(0).createButton(1);
        assertNotNull(test);
        assertEquals(FLOOR_BTN_ID_PREFIX + "1", test.getId());
    }

    @Test
    void testFloorDisplayHasAllFloors() {
        // Given 5 floors
        // Then
        assertEquals(5, controller.floorListRight.getChildren().size());
    }

    @Test
    void testFloorListDownChange(FxRobot robot) {
        // Given
        FontAwesomeIconView downArrow = robot.lookup("#" + FLOOR_BTN_ID_PREFIX + 3).lookup("#" + DOWN_ARROW_ID).tryQueryAs(FontAwesomeIconView.class).get();
        assertNotNull(downArrow);
        // When
        testBuilding.getFloors().get(3).setDownButtonActive(true);
        // Then
        assertTrue(downArrow.getStyleClass().contains(CLICKED_STYLE));
        robot.sleep(100);
        // When
        testBuilding.getFloors().get(3).setDownButtonActive(false);
        // Then
        assertFalse(downArrow.getStyleClass().contains(CLICKED_STYLE));
    }

    @Test
    void testFloorListUpChange(FxRobot robot) {
        // Given
        FontAwesomeIconView downArrow = robot.lookup("#" + FLOOR_BTN_ID_PREFIX + 3).lookup("#" + UP_ARROW_ID).tryQueryAs(FontAwesomeIconView.class).get();
        assertNotNull(downArrow);
        // When
        testBuilding.getFloors().get(3).setUpButtonActive(true);
        // Then
        assertTrue(downArrow.getStyleClass().contains(CLICKED_STYLE));
        robot.sleep(100);
        // When
        testBuilding.getFloors().get(3).setUpButtonActive(false);
        // Then
        assertFalse(downArrow.getStyleClass().contains(CLICKED_STYLE));
    }

    @Test
    void testSetTargetWithApi(FxRobot robot) throws RemoteException {
        // Given
        elevatorConnection.setTarget(1, 4);
        Platform.runLater(() -> {
            try {
                converter.update(testBuilding);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        // When
        robot.sleep(100);
        JFXComboBox<Integer> firstElevator = robot.lookup("#elevator1").lookup("#targetField").queryAs(JFXComboBox.class);
        //Then
        assertEquals(4, firstElevator.getValue().intValue());
    }


    @Test
    void testClearNotifications(FxRobot robot) {
        // Given
        assertNotNull(robot.lookup("#" + PAYLOAD_ID_PREFIX + "1").queryAs(Label.class));
        assertNotNull(robot.lookup("#" + PAYLOAD_ID_PREFIX + "5").queryAs(Label.class));
        assertNotNull(robot.lookup("#" + PAYLOAD_ID_PREFIX + "7").queryAs(Label.class));
        // When
        Platform.runLater(() -> controller.clearNotifications());
        robot.sleep(100);
        // Then
        assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#" + PAYLOAD_ID_PREFIX + "1").queryAs(Label.class));
        assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#" + PAYLOAD_ID_PREFIX + "5").queryAs(Label.class));
        assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#" + PAYLOAD_ID_PREFIX + "7").queryAs(Label.class));
    }

    @Test
    void testNewModelInit(FxRobot robot) throws IOException {
        // Given
        assertEquals(9, robot.lookup("#elevatorView").queryAs(JFXMasonryPane.class).getChildren().size());
        assertEquals(5, robot.lookup("#floorListRight").queryAs(VBox.class).getChildren().size());
        assertEquals(2, robot.lookup("#errorBox").queryAs(VBox.class).getChildren().size());
        assertEquals(1, robot.lookup("#warningBox").queryAs(VBox.class).getChildren().size());
        assertTrue(robot.lookup("#modeButton").queryAs(JFXToggleButton.class).isSelected());
        // When
        ArrayList<IFloor> floors = new ArrayList<>();
        floors.add(new Floor(0, true, false));
        ArrayList<IBuildingElevator> elevators = new ArrayList<>();
        elevators.add(new Elevator(0, 7, 0, 10));
        controller.setModel(new Building(3, 10, 1, elevators, floors));
        robot.sleep(100);
        // Then
        assertEquals(1, robot.lookup("#elevatorView").queryAs(JFXMasonryPane.class).getChildren().size());
        assertEquals(1, robot.lookup("#floorListRight").queryAs(VBox.class).getChildren().size());
        assertEquals(0, robot.lookup("#errorBox").queryAs(VBox.class).getChildren().size());
        assertEquals(0, robot.lookup("#warningBox").queryAs(VBox.class).getChildren().size());
    }
}
