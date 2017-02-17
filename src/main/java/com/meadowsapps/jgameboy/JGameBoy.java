package com.meadowsapps.jgameboy;

import com.meadowsapps.jgameboy.core.EmulatorCore;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Dylan on 1/6/17.
 */
public class JGameBoy extends Application {

    private EmulatorCore core;

    private static JGameBoy instance;

    public JGameBoy() {
        instance = this;
    }

    @Override
    public void start(Stage stage) throws Exception {
        JGameBoyView view = new JGameBoyView(stage);
        stage.setScene(new Scene(view));
        stage.show();
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

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
