package com.meadowsapps.jgameboy;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ResourceBundle;

/**
 * Created by Dylan on 2/6/17.
 */
public class JGameBoyView extends BorderPane {

    private ResourceBundle bundle = ResourceBundle.getBundle("JGameBoyFrameResource");

    public JGameBoyView(Stage stage) {
        setTop(buildMenuBar(stage));
    }

    private MenuBar buildMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(buildFileMenu(stage));
        menuBar.getMenus().add(buildEmulationMenu(stage));
        return menuBar;
    }

    private Menu buildFileMenu(Stage stage) {
        Menu file = new Menu(bundle.getString("FileMenu.text"));

        // Open...
        MenuItem openItem = new MenuItem(bundle.getString("OpenMenuItem.text"));
        // openItem.setAccelerator(new KeyCodeCombination());
        openItem.setOnAction(event -> {
            openActionPerformed(stage, event);
        });
        file.getItems().add(openItem);
        // Open Recent
        MenuItem openRecentItem = new MenuItem(bundle.getString("OpenRecentMenuItem.text"));
        file.getItems().add(openRecentItem);

        // Separator
        // set separator

        // Save State
        MenuItem saveStateItem = new MenuItem(bundle.getString("SaveStateItem.text"));
        file.getItems().add(saveStateItem);
        // Load State
        MenuItem loadStateItem = new MenuItem(bundle.getString("LoadStateItem.text"));
        file.getItems().add(loadStateItem);

        return file;
    }

    private Menu buildEmulationMenu(Stage stage) {
        Menu emulation = new Menu(bundle.getString("EmulationMenu.text"));
        return emulation;
    }

    private void openActionPerformed(Stage stage, ActionEvent event) {
        FileChooser chooser = new FileChooser();
        String description = "GB/GBC/GBA ROMs";
        String[] extensions = {"*.gb", "*.gbc", "*.gba"};
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(description, extensions);
        chooser.getExtensionFilters().add(filter);

        File rom = chooser.showOpenDialog(stage);
        if (rom != null) {

        }
    }

    private void openRecentActionPerformed(ActionEvent e) {

    }

    private int getControlKey() {
//        String os = System.getProperty("os.name");
//        return (os.contains("Mac")) ? KeyEvent.META_MASK : KeyEvent.CTRL_MASK;
        return -1;
    }

}
