package com.meadowsapps.jgameboy;

import com.meadowsapps.jgameboy.core.EmulatorCore;

import javax.swing.*;

/**
 * Created by Dylan on 1/6/17.
 */
public class JGameBoy {

    private EmulatorCore core;

    private JGameBoyFrame frame;

    private static JGameBoy instance;

    public JGameBoy() {
        frame = new JGameBoyFrame();
        instance = this;
    }

    public EmulatorCore getCore() {
        return core;
    }

    void setCore(EmulatorCore core) {
        this.core = core;
    }

    public JGameBoyFrame getFrame() {
        return frame;
    }

    public static JGameBoy getInstance() {
        return instance;
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        String laf = UIManager.getSystemLookAndFeelClassName();
        UIManager.setLookAndFeel(laf);

        JGameBoy emulator = new JGameBoy();
        emulator.frame.setVisible(true);
    }
}
