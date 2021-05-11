package diplom.gui.menu;

import com.alee.laf.window.WebFrame;

import javax.swing.*;

public class CreateClassesGroupDialog extends JDialog {

    public CreateClassesGroupDialog(WebFrame frame) {
        setTitle("Создание группы классов");
        setSize(500, 400);
        setLocationRelativeTo(frame);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

}
