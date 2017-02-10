package com.meadowsapps.jgameboy;

import com.meadowsapps.jgameboy.gbc.core.GbcConstants;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.ResourceBundle;

/**
 * Created by Dylan on 2/6/17.
 */
public class JGameBoyView extends BorderPane {

    private MenuBar menu;

    private Menu fileMenu;

    private Canvas canvas;

    private GridPane layout;

    private ResourceBundle bundle = ResourceBundle.getBundle("JGameBoyViewResource");

    private static JGameBoyView view = new JGameBoyView();

    private JGameBoyView() {
        menu = new MenuBar();
        fileMenu = buildFileMenu();
        menu.getMenus().add(fileMenu);
        setTop(menu);

        canvas = new Canvas(GbcConstants.WIDTH, GbcConstants.HEIGHT);
        layout = new GridPane();
        GridPane.setConstraints(canvas, 0, 0, 1, 1,
                HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        layout.getChildren().add(canvas);
        setCenter(layout);
    }

    public GraphicsContext getContext() {
        return canvas.getGraphicsContext2D();
    }

    public static JGameBoyView getView() {
        return view;
    }

    private Menu buildFileMenu() {
        String text = bundle.getString("FileMenu.text");
        Menu file = new Menu(text);
        return file;
    }
}
