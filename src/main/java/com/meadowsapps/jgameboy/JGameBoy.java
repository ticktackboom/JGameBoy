package com.meadowsapps.jgameboy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Created by Dylan on 1/6/17.
 */
public class JGameBoy extends Application {

    private ResourceBundle bundle;

    public JGameBoy() {
        bundle = ResourceBundle.getBundle("JGameBoyResource");
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(bundle.getString("title"));

        JGameBoyView view = JGameBoyView.getView();
        Scene scene = new Scene(view);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
