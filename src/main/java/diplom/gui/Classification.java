package diplom.gui;

import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.window.WebFrame;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Classification extends WebPanel {

    public Classification(WebFrame frame, WebTabbedPane tabbedPane) {
        setLayout(null);
        setBounds(0, 0, tabbedPane.getWidth(), tabbedPane.getHeight());
        setBackground(Color.PINK);

        tabbedPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Classification.this.setSize(tabbedPane.getWidth(), tabbedPane.getHeight());
            }
        });

        WebButton background = new WebButton("Classification");
        background.setBounds(25, 80, 115, 30);
        background.setFocusPainted(false);
        background.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(background);
    }

}
