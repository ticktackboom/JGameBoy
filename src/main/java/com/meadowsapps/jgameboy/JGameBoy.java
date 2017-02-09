package com.meadowsapps.jgameboy;

import com.meadowsapps.jgameboy.core.CoreFactory;
import com.meadowsapps.jgameboy.core.CoreType;
import com.meadowsapps.jgameboy.core.EmulatorCore;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Created by Dylan on 1/6/17.
 */
public class JGameBoy extends Application {

    private EmulatorCore core;

    private ResourceBundle bundle;

    private static JGameBoy instance;

    public JGameBoy() {
        bundle = ResourceBundle.getBundle("JGameBoyResource");
        instance = this;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(bundle.getString("title"));

        JGameBoyView view = JGameBoyView.getView();
        CoreFactory factory = CoreFactory.getFactory();
        core = factory.getCore(CoreType.GAMEBOY);

        Scene scene = new Scene(view);
        scene.setOnKeyPressed(core.joypad());
        scene.setOnKeyReleased(core.joypad());
        stage.setScene(scene);

        stage.show();
    }

    public EmulatorCore getCore() {
        return core;
    }

    public static JGameBoy getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
