package com.meadowsapps.jgameboy;

import com.meadowsapps.jgameboy.core.CoreFactory;
import com.meadowsapps.jgameboy.core.CoreType;
import com.meadowsapps.jgameboy.core.EmulatorCore;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by Dylan on 2/6/17.
 */
public class JGameBoyFrame extends JFrame {

    private Canvas canvas;

    private ResourceBundle bundle = ResourceBundle.getBundle("JGameBoyFrameResource");

    public JGameBoyFrame() {
        setTitle(bundle.getString("title"));
        setJMenuBar(buildMenuBar());

        canvas = new Canvas();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(canvas, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(buildFileMenu());
        menuBar.add(buildEmulationMenu());
        return menuBar;
    }

    private JMenu buildFileMenu() {
        JMenu file = new JMenu(bundle.getString("FileMenu.text"));

        // Open...
        JMenuItem openItem = new JMenuItem(bundle.getString("OpenMenuItem.text"));
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, getControlKey()));
        openItem.addActionListener(event -> {
            openActionPerformed(event);
        });
        file.add(openItem);
        // Open Recent
        JMenuItem openRecentItem = new JMenuItem(bundle.getString("OpenRecentMenuItem.text"));
        file.add(openRecentItem);

        // Separator
        file.addSeparator();

        // Save State
        JMenuItem saveStateItem = new JMenuItem(bundle.getString("SaveStateItem.text"));
        file.add(saveStateItem);
        // Load State
        JMenuItem loadStateItem = new JMenuItem(bundle.getString("LoadStateItem.text"));
        file.add(loadStateItem);

        return file;
    }

    private JMenu buildEmulationMenu() {
        JMenu emulation = new JMenu(bundle.getString("EmulationMenu.text"));
        return emulation;
    }

    private void openActionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f != null && f.getName().endsWith("gb")
                        || f.getName().endsWith("gbc")
                        || f.getName().endsWith("gba");
            }

            @Override
            public String getDescription() {
                return "GB/GBC/GBA ROMs";
            }
        });
        int rv = chooser.showOpenDialog(this);
        switch (rv) {
            case JFileChooser.APPROVE_OPTION:
                try {
                    File file = chooser.getSelectedFile();
                    CoreType type = CoreType.getType(file);
                    EmulatorCore core = CoreFactory.getFactory().getCore(type);
                    core.cartridge().load(file);
                    core.start();

                    JGameBoy.getInstance().setCore(core);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case JFileChooser.CANCEL_OPTION:
                break;
        }
    }

    private void openRecentActionPerformed(ActionEvent e) {

    }

    private int getControlKey() {
        String os = System.getProperty("os.name");
        return (os.contains("Mac")) ? KeyEvent.META_MASK : KeyEvent.CTRL_MASK;
    }

    class Canvas extends JPanel {

        @Override
        public void paint(Graphics g) {
            JGameBoy instance = JGameBoy.getInstance();
            EmulatorCore core = instance.getCore();
            if (core != null) {
                core.gpu().draw(g);
            }
        }
    }

}
