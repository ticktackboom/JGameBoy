package com.meadowsapps.jgameboy;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Created by Dylan on 1/6/17.
 */
public class JGameBoy extends Application {

    private Menu file;
    private Canvas canvas;
    private ResourceBundle bundle;

    public JGameBoy() {
        bundle = ResourceBundle.getBundle("JGameBoyResource");
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("JGameBoy");
        canvas = new Canvas();

        BorderPane layout = new BorderPane();
        MenuBar menu = new MenuBar();
        file = buildFileMenu();


        layout.setTop(menu);

        GridPane gridPane = new GridPane();
        GridPane.setConstraints(canvas, 0, 0, 1, 1,
                HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        gridPane.getChildren().add(canvas);
        layout.setCenter(gridPane);

        stage.setScene(new Scene(layout));
        stage.show();
    }

    private Menu buildFileMenu() {
        Menu menu = new Menu(bundle.getString("File.text"));
        return menu;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
