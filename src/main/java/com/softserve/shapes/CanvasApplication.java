package com.softserve.shapes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class CanvasApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(DrawerApplication.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        Scene scene = new Scene(mainNode);
        stage.setScene(scene);
        stage.show();

        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
}