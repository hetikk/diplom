package diplom.gui.menu;

import com.alee.demo.content.menu.MenusGroup;
import com.alee.laf.window.WebFrame;
import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.swing.menu.JMenuBarGenerator;
import com.alee.utils.swing.menu.MenuGenerator;
import diplom.Application;
import diplom.gui.Frame;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar {

    public static final int MENU_Y_OFFSET = 25;

    private static final String CLASSES_NEW = "Создать";
    private static final String CLASSES_EDIT = "Редактировать";
    private static final String EXIT = "Выход";
    private static final String KOEF = "Коэффициенты";
    private static final String ABOUT = "О программе";
    private static final String DEBUG = "Режим отладки";
    private static final String TIME = "Время работы";

    public Menu(WebFrame frame) {
        setBounds(0, 0, frame.getWidth() - Frame.X_OFFSET, MENU_Y_OFFSET);

        ActionListener action = e -> {
            JMenuItem item = (JMenuItem) e.getSource();

            String text = item.getText();
            System.out.println("menu command: " + text);

            switch (text) {
                case CLASSES_NEW:
                    new CreateClassesGroupDialog(frame);
                    break;
                case CLASSES_EDIT:
                    new EditClassesGroupDialog(frame);
                    break;
                case ABOUT:
                    new AboutDialog(frame);
                    break;
                case EXIT:
                    System.exit(0);
                    break;
                case DEBUG:
                    Application.debug = item.isSelected();
                    break;
                case TIME:
                    Application.time = item.isSelected();
                    break;
                case KOEF:
                    new KoefDialog(frame);
                    break;
            }
        };

        JMenuBarGenerator generator = new JMenuBarGenerator(this);
        generator.setLanguagePrefix("demo.example.menus.menu");
        generator.setIconSettings(MenusGroup.class, "icons/menu/", "png");

        MenuGenerator fileMenu = generator.addSubMenu("Файл");
        MenuGenerator classes = fileMenu.addSubMenu("Группа классов");
        classes.addItem("new", CLASSES_NEW, Hotkey.CTRL_C, action);
        classes.addItem("edit", CLASSES_EDIT, Hotkey.CTRL_E, action);
        fileMenu.addItem(KOEF, action);
        fileMenu.addItem("exit", EXIT, Hotkey.ALT_X, action);

        MenuGenerator view = generator.addSubMenu("Вид");
        view.addCheckItem(TIME, Hotkey.CTRL_T, false, action);
        view.addCheckItem(DEBUG, Hotkey.CTRL_D, false, action);

        MenuGenerator helpMenu = generator.addSubMenu("Справка");
        helpMenu.addItem(ABOUT, Hotkey.CTRL_H, action);
    }

}
