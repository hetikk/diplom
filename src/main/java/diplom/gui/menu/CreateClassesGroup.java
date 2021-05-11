package diplom.gui.menu;

import com.alee.laf.window.WebFrame;

import javax.swing.*;

public class CreateClassesGroup extends JDialog {

    public CreateClassesGroup(WebFrame frame) {
        setTitle("Создание группы классов");
        setSize(500, 400);
        setLocationRelativeTo(frame);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

}
