package at.fhhagenberg;

import at.fhhagenberg.controller.MainController;
import at.fhhagenberg.converter.ModelConverter;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.Elevator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.EmptyNodeQueryException;
import sqelevator.MockElevator;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.isVisible;

@ExtendWith(ApplicationExtension.class)
class GuiApplicationTest {
    private MainController controller;
    private Building testBuilding;

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

        ModelConverter converter = new ModelConverter(new MockElevator());
        testBuilding = converter.init();

        controller.initModel(testBuilding);
    }

    @Test
    void testControllerInitException() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            controller.initModel(testBuilding);
        });
    }

    @Test
    void testIsPaneVisible() {
        verifyThat("#elevator_view", isVisible());
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testShowAllElevators(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#elevator_view").queryAs(JFXMasonryPane.class).getChildren().size() == testBuilding.getElevators().size()).isTrue();
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
        JFXComboBox<Integer> firstElevator = robot.lookup("#elevator1").lookup("#targetField").queryAs(JFXComboBox.class);
        Assertions.assertThat(firstElevator.isDisabled()).isTrue();
        robot.sleep(100);
        assertEquals(2, firstElevator.getValue().intValue());

        //When
        Platform.runLater(() -> robot.lookup("#mode_button").queryAs(JFXToggleButton.class).setSelected(false));
        Platform.runLater(() -> assertFalse(robot.lookup("#mode_button").queryAs(JFXToggleButton.class).isSelected()));
        Assertions.assertThat(!firstElevator.isDisabled());
        robot.sleep(200);

        //Then
        robot.clickOn(firstElevator);
        robot.sleep(100);
        // Click on the first entry
        robot.press(KeyCode.UP);
        robot.release(KeyCode.UP);
        robot.press(KeyCode.UP);
        robot.release(KeyCode.UP);
        robot.press(KeyCode.ENTER);
        robot.sleep(100);
        assertEquals(0, firstElevator.getValue().intValue());

        Platform.runLater(() -> robot.lookup("#mode_button").queryAs(JFXToggleButton.class).setSelected(true));
        Platform.runLater(() -> assertTrue(robot.lookup("#mode_button").queryAs(JFXToggleButton.class).isSelected()));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testCancelWarning(FxRobot robot) {
        // Given
        Assertions.assertThat(robot.lookup("#PAYLOAD7").queryAs(Label.class)).hasText("Elevator 7: Warning payload on a high level.");

        //When
        Platform.runLater(() -> ((Elevator) testBuilding.getElevator(7)).setWeight(290));
        robot.sleep(100);

        //Then
        assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#PAYLOAD7").queryAs(Label.class));
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

            controller.getElevatorControllers().get(0).deleteInfo("PAYLOAD7");
            controller.getElevatorControllers().get(0).deleteInfo("PAYLOAD8");
            controller.getElevatorControllers().get(0).deleteInfo("PAYLOAD5");
            controller.getElevatorControllers().get(0).deleteInfo("PAYLOAD1");
            controller.getElevatorControllers().get(0).deleteInfo("PAYLOAD2");
            controller.getElevatorControllers().get(0).deleteInfo("PAYLOAD4");
            controller.getElevatorControllers().get(0).deleteInfo("test32");
            controller.getElevatorControllers().get(0).deleteInfo("test33");

            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#PAYLOAD7").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#PAYLOAD8").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#PAYLOAD5").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#PAYLOAD1").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#PAYLOAD2").queryAs(Label.class));
            assertThrows(EmptyNodeQueryException.class, () -> robot.lookup("#PAYLOAD4").queryAs(Label.class));
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
    void createFloorButton(){
        JFXButton test = controller.getElevatorControllers().get(0).createButton(1);
        assertNotNull(test);
        assertEquals("FLOOR_BUTTON1", test.getId());
    }

    @Test
    void testFloorDisplay() {
        // Given
        // When
        // Then
        assertEquals(5, controller.floor_list_right.getChildren().size());
    }
}
