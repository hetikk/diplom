package diplom.gui;


import com.alee.laf.WebLookAndFeel;
import com.alee.laf.window.WebFrame;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Frame extends WebFrame {

    public static final int FONT_SIZE = 13;

    public static final int X_OFFSET = 16;
    public static final int Y_OFFSET = 39;

    private Frame() {
        setTitle("CGraphic");
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 700);
        setLocationRelativeTo(null);
//        setExtendedState(MAXIMIZED_BOTH);

        Panel panel = new Panel(this);
        add(panel);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
//                panel.setSize(getWidth() - X_OFFSET, getHeight() - Y_OFFSET);
            }
        });

        setVisible(true);
    }

    public static void showForm() {
        SwingUtilities.invokeLater(() -> {
            WebLookAndFeel.install();
            new Frame();
        });
    }

//    public Frame() {
//        setTitle("CGraphic");
//        setLayout(null);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setSize(800, 800);
//        setLocationRelativeTo(null);
//        setResizable(false);
//
//        WebLabel clusteringDocsDirLbl = new WebLabel("Директория с документами");
//        clusteringDocsDirLbl.setBounds(10, 10, 150, 20);
//        add(clusteringDocsDirLbl);
//
//        WebPathField pathField = new WebPathField("webpathfield");
//        pathField.setSelectedPath(new File(System.getProperty("user.home")));
//        pathField.setBounds(10, 30, 400, 26);
//        for (Component component : pathField.getComponents()) {
//            component.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mousePressed(MouseEvent e) {
//                    if (e.getButton() == MouseEvent.BUTTON3) {
//                        File file = WebDirectoryChooser.showDialog(Frame.this, "title");
//                        if (file != null) {
//                            pathField.setSelectedPath(file);
//                        }
//                    }
//                }
//            });
//        }
//        add(pathField);
//
//        setVisible(true);
//    }

}