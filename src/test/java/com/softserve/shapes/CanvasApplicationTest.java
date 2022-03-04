package com.softserve.shapes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

public class CanvasApplicationTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
    }

    @Test
    public void testDrawLineShouldGenerateErrorMessageWhenInputsAreOutsideCanvas() {
        clickOn("#textField");
        write("L 1 1 -1 1");
        write('\n');
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText("All coordinates must lie on the canvas"));
    }

    @Test
    public void testDrawLineShouldGenerateErrorMessageWhenLinesAreNotVerticalOrHorizontal() {
        clickOn("#textField");
        write("L 10 10 30 30");
        write('\n');
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText("Only vertical and horizontal lines are allowed"));
    }

    @Test
    public void testDrawLineShouldSucceedWhenInputsAreValid() {
        clickOn("#textField");
        write("L 10 10 10 100");
        write('\n');
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText(""));
    }

    @Test
    public void testDrawRectangleShouldGenerateErrorMessageWhenInputsAreOutsideCanvas() {
        clickOn("#textField");
        write("R 10 10 -10 40");
        write('\n');
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText("All coordinates must lie on the canvas"));
    }

    @Test
    public void testDrawRectangleShouldSucceedWhenInputsAreValid() {
        clickOn("#textField");
        write("R 10 10 100 100");
        write('\n');
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText(""));
    }

    @Test
    public void testFillShouldGenerateErrorMessageWhenInputsAreOutsideCanvas() {
        clickOn("#textField");
        write("F 10 -10");
        write('\n');
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText("Fill coordinates must lie on the canvas"));
    }

    @Test
    public void testFillShouldShouldSucceedWhenInputsAreValid() {
        clickOn("#textField");
        write("F 50 50");
        write('\n');
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText(""));
    }
}