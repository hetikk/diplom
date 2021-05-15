package diplom.gui.menu;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import diplom.classification.Class;
import diplom.gui.Frame;
import diplom.gui.Utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ClassGroup extends WebPanel {

    private final int WIDTH = 395;
    private final int startHeight = 35;
    private final int ROW_OFFSET = 35;
    private int height = 72;

    private List<ClassRow> classRows = new ArrayList<>();
    private int index = 0;
    int rowIndex = 0;

    private CreateClassesGroupDialog dialog;
    private Consumer<Integer> groupDeleteHandler;

    private WebTextField classField;

    private Consumer<Integer> rowDeleteHandler = index -> {
        if (classRows.size() == 1) return;

        int idx = index;
        this.index--;
        height = startHeight;

        classRows.get(idx).hi();
        classRows.remove(idx);

        for (int i = 0; i < classRows.size(); i++) {
            classRows.get(i).setIndex(i);
            classRows.get(i).setY(height);
            height += ROW_OFFSET;
        }

        setSize(WIDTH, height);
        setPreferredSize(WIDTH, height);
    };

    public ClassGroup(CreateClassesGroupDialog dialog, int index, Consumer<Integer> groupDeleteHandler) {
        this.index = index;
        this.dialog = dialog;
        this.groupDeleteHandler = groupDeleteHandler;

        setLayout(null);
//        setBackground(Color.PINK);
        setPreferredSize(WIDTH, height);

        WebLabel classLabel = new WebLabel("Название класса");
        classLabel.setBounds(0, 5, 120, 30);
        classLabel.setPadding(0, 5, 0, 0);
        classLabel.setFontSize(Frame.FONT_SIZE);
        add(classLabel);

        classField = new WebTextField();
        classField.setBounds(120, 5, 200, 30);
        classField.setInputPrompt("Название...");
        classField.setPadding(0, 5, 0, 0);
        add(classField);

        WebButton add = new WebButton(new ImageIcon(Objects.requireNonNull(Utils.loadImage(ClassGroup.class, "icons/new.png"))));
        add.setBounds(325, 5, 30, 30);
        add.addActionListener(e -> addRow());
        add(add);

        WebButton remove = new WebButton(new ImageIcon(Objects.requireNonNull(Utils.loadImage(ClassGroup.class, "icons/remove.png"))));
        remove.setBounds(360, 5, 30, 30);
        remove.addActionListener(e -> groupDeleteHandler.accept(this.index));
        add(remove);

        ClassRow row = new ClassRow(rowIndex++, rowDeleteHandler);
        row.setLocation(0, startHeight);
        add(row);
        classRows.add(row);
    }

    private void addRow() {
        ClassRow row = new ClassRow(rowIndex++, rowDeleteHandler);
        row.setLocation(0, height);
        add(row);
        classRows.add(row);

        height += ROW_OFFSET;
        setSize(WIDTH, height);
        setPreferredSize(WIDTH, height);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void hi() {
        setVisible(false);
    }

    public Class getData() {
        Class cl = new Class();
        cl.name = classField.getText();
        cl.termsFrequency = new LinkedHashMap<>();
        for (ClassRow row : classRows) {
            cl.termsFrequency.put(row.getWord(), row.getCount());
        }
        return cl;
    }
}
