package diplom.gui.menu;

import com.alee.laf.window.WebDialog;
import com.alee.laf.window.WebFrame;

import javax.swing.*;

public class EditClassesGroup extends WebDialog {

    public EditClassesGroup(WebFrame frame) {
        setTitle("Редактирование группы классов");
        setSize(500, 400);
        setLocationRelativeTo(frame);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

}
