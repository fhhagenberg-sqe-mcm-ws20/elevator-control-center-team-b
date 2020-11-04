package at.fhhagenberg;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.isVisible;

@ExtendWith(ApplicationExtension.class)
class GuiApplicationTest {

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @SneakyThrows
    @Start
    private void start(Stage stage) {
        new App().start(stage);
    }

    @Test
    void testIsPaneVisible() {
        verifyThat("#mainPane", isVisible());
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testIsRightTextInPaneDisplayed(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#label").queryAs(Label.class)).hasText("Drag components from Library here…");
    }
}
