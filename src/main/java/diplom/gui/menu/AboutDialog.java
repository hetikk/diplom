package diplom.gui.menu;

import com.alee.laf.window.WebDialog;
import com.alee.laf.window.WebFrame;

import javax.swing.*;

public class AboutDialog extends WebDialog {

    public AboutDialog(WebFrame frame) {
        setTitle("О програаме");
        setSize(500, 400);
        setLocationRelativeTo(frame);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

}
