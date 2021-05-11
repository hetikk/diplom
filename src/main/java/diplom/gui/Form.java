package diplom.gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;

public class Form {

    private static void show() {
        JFrame frame = new JFrame("JavaFX in Swing");
        final JFXPanel panel = new JFXPanel();
        frame.add(panel);
        frame.setSize(200, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(panel);
            }
        });
    }

    private static Scene createScene() {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        String html = "<html><h1>Hello</h1><h2>Hello</h2></html>";
        webEngine.loadContent(html);

        Group root = new Group();
        Scene scene = new Scene(root, Color.YELLOW);
        Button button = new Button("Hello World!");
        button.setLayoutX(50);
        button.setLayoutY(50);
        root.getChildren().add(button);

        return scene;
    }

    private static void initFX(JFXPanel panel) {
        Scene scene = createScene();
        panel.setScene(scene);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                show();
            }
        });
    }

}