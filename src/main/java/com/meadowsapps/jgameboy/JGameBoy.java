package com.meadowsapps.jgameboy;

import com.meadowsapps.jgameboy.core.CoreFactory;
import com.meadowsapps.jgameboy.core.CoreType;
import com.meadowsapps.jgameboy.core.EmulatorCore;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by Dylan on 1/6/17.
 */
public class JGameBoy extends Application {

    private Stage stage;

    private FileChooser fileChooser;

    private ResourceBundle bundle;

    private EmulatorCore core;

    private static JGameBoy instance;

    public JGameBoy() {
        bundle = ResourceBundle.getBundle(getClass().getSimpleName());
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                bundle.getString("fileChooser.filter.description"), "*.gb", "*.gbc", "*.gba");
        fileChooser.getExtensionFilters().add(filter);
        instance = this;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle(bundle.getString("title"));
        Parent root = initComponents();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void onOpenAction(ActionEvent event) {
        File selected = fileChooser.showOpenDialog(stage);
        if (selected != null) {
            try {
                load(selected);
            } catch (Exception e) {
                // TODO: 3/1/2017 log and display this exception
                Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    private void onClearRecentAction(ActionEvent event) {
        System.out.println("clearRecentMenuItem::onAction");
    }

    private void load(File rom) throws IOException {
        CoreType type = CoreType.getType(rom);
        core = CoreFactory.getFactory().getCore(type);
        core.cartridge().load(rom);
        core.start();
    }

    public EmulatorCore getCore() {
        return core;
    }

    void setCore(EmulatorCore core) {
        this.core = core;
    }

    public static JGameBoy getInstance() {
        return instance;
    }

    private Parent initComponents() {
        BorderPane layout = new BorderPane();
        // menuBar
        {
            menuBar = new MenuBar();
            menuBar.setUseSystemMenuBar(true);
            // fileMenu
            {
                fileMenu = new Menu(bundle.getString("fileMenu.text"));
                // openMenuItem
                {
                    openMenuItem = new MenuItem(bundle.getString("openMenuItem.text"));
                    openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCodeCombination.SHORTCUT_DOWN));
                    openMenuItem.setOnAction(event -> {
                        onOpenAction(event);
                    });
                    fileMenu.getItems().add(openMenuItem);
                }
                // openRecentMenuItem
                {
                    openRecentMenu = new Menu(bundle.getString("openRecentMenu.text"));
                    openRecentMenu.getItems().add(new SeparatorMenuItem());
                    // clearRecentMenuItem
                    {
                        clearRecentMenuItem = new MenuItem(bundle.getString("clearRecentMenuItem.text"));
                        clearRecentMenuItem.setOnAction(event -> {
                            onClearRecentAction(event);
                        });
                        clearRecentMenuItem.setDisable(true);
                        openRecentMenu.getItems().add(clearRecentMenuItem);
                    }
                    fileMenu.getItems().add(openRecentMenu);
                }
                // Separator
                fileMenu.getItems().add(new SeparatorMenuItem());
                // saveStateMenu
                {
                    saveStateMenu = new Menu(bundle.getString("saveStateMenu.text"));
                    saveStateMenu.setDisable(true);
                    fileMenu.getItems().add(saveStateMenu);
                }
                // loadStateMenu
                {
                    loadStateMenu = new Menu(bundle.getString("loadStateMenu.text"));
                    loadStateMenu.setDisable(true);
                    fileMenu.getItems().add(loadStateMenu);
                }
                // Separator
                fileMenu.getItems().add(new SeparatorMenuItem());

                menuBar.getMenus().add(fileMenu);
            }

            // emulationMenu
            {
                emulationMenu = new Menu(bundle.getString("emulationMenu.text"));
                menuBar.getMenus().add(emulationMenu);
            }
            layout.setTop(menuBar);
        }
        // contentPane
        {
            contentPane = new BorderPane();
            // imageView
            {
                imageView = new ImageView();
                contentPane.setCenter(imageView);
            }
            layout.setCenter(contentPane);
        }
        return layout;
    }

    // <editor-fold desc="// JGameBoy - variables declaration - DO NOT MODIFY">
    private MenuBar menuBar;
    private Menu fileMenu;
    private MenuItem openMenuItem;
    private Menu openRecentMenu;
    private MenuItem clearRecentMenuItem;
    private Menu saveStateMenu;
    private Menu loadStateMenu;
    private Menu emulationMenu;
    private BorderPane contentPane;
    private ImageView imageView;
    // </editor-fold>

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
