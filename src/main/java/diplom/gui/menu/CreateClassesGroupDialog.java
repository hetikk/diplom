package diplom.gui.menu;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.text.WebTextField;
import com.alee.laf.window.WebFrame;
import diplom.gui.Frame;

import javax.swing.*;

public class CreateClassesGroupDialog extends JDialog {

    static final int dialogWidth = 465;
    private static final int height = 145;

    public CreateClassesGroupDialog(WebFrame frame) {
        super(frame, true);

        setTitle("Создание группы классов");
        setSize(dialogWidth, 400);
        setLocationRelativeTo(frame);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);

        WebLabel groupLabel = new WebLabel("Название группы");
        groupLabel.setBounds(15, 5, 120, 30);
        groupLabel.setFontSize(Frame.FONT_SIZE);
        add(groupLabel);

        WebTextField groupField = new WebTextField();
        groupField.setInputPrompt("Название группы...");
        groupField.setBounds(130, 7, 300, 28);
        groupField.setPadding(0, 5, 0, 0);
        add(groupField);

        GroupRow groupRow = new GroupRow();
        groupRow.setBounds(0, 50, 385, 82);
        add(groupRow);

        WebButton newClass = new WebButton("Добавить класс");
        newClass.setBounds(15, height, 150, 30);
//        add(newClass);

        setVisible(true);
    }

}
