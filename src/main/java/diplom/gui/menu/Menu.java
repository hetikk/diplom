package diplom.gui.menu;

import com.alee.demo.content.menu.MenusGroup;
import com.alee.laf.window.WebFrame;
import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.swing.menu.JMenuBarGenerator;
import com.alee.utils.swing.menu.MenuGenerator;
import diplom.gui.Frame;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar {

    public static final int MENU_Y_OFFSET = 25;

    private static final String CLASSES_NEW = "Создать";
    private static final String CLASSES_EDIT = "Изменить";
    private static final String EXIT = "Выход";
    private static final String ABOUT = "О программе";

    public Menu(WebFrame frame) {
//        putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
        setBounds(0, 0, frame.getWidth() - Frame.X_OFFSET, MENU_Y_OFFSET);

        ActionListener action = e -> {
            JMenuItem item = (JMenuItem) e.getSource();

            String text = item.getText();
            System.out.println("menu command: " + text);

            switch (text) {
                case CLASSES_NEW:
                    new CreateClassesGroup(frame);
                    break;
                case CLASSES_EDIT:
                    new EditClassesGroup(frame);
                    break;
            }
        };

        // Filling menu bar with items
        JMenuBarGenerator generator = new JMenuBarGenerator(this);
        generator.setLanguagePrefix("demo.example.menus.menu");
        generator.setIconSettings(MenusGroup.class, "icons/menu/", "png");

        MenuGenerator fileMenu = generator.addSubMenu("Файл");
        MenuGenerator classes = fileMenu.addSubMenu("Группа классов");
        classes.addItem("new", CLASSES_NEW, Hotkey.CTRL_C, action);
        classes.addItem("edit", CLASSES_EDIT, Hotkey.CTRL_E, action);
        fileMenu.addItem("exit", EXIT, Hotkey.ALT_X, action);

        MenuGenerator helpMenu = generator.addSubMenu("Справка");
        helpMenu.addItem(ABOUT, Hotkey.CTRL_H, action);
    }

}
