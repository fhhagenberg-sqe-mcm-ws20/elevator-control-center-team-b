package at.fhhagenberg;

import at.fhhagenberg.controller.GuiConstants;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class AppTest {

    private Stage stage;
    App testApp;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @SneakyThrows
    @Start
    private void start(Stage stage) {
        testApp = new App();
        testApp.start(stage);
    }

    @SneakyThrows
    @Test
    void testSetStatusUi(FxRobot robot) {
        // Given
        Assertions.assertThat(robot.lookup("#statusLabel").queryAs(javafx.scene.control.Label.class)).hasText(GuiConstants.MSG_CONNECTING);
        assertTrue(robot.lookup("#elevatorView").queryAs(JFXMasonryPane.class).isDisabled());
        //  When
        testApp.setStatusUI(true);
        robot.sleep(100);
        //Then
        Assertions.assertThat(robot.lookup("#statusLabel").queryAs(javafx.scene.control.Label.class)).hasText(GuiConstants.MSG_IS_CONNECTED);
        assertFalse(robot.lookup("#elevatorView").queryAs(JFXMasonryPane.class).isDisabled());
    }
}