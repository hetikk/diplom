package diplom.gui.menu;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import diplom.gui.Frame;
import diplom.gui.Utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class GroupRow extends WebPanel {

    private final int WIDTH = 385;
    private final int startHeight = 35;
    private final int ROW_OFFSET = 35;
    private int height = 72;

    private List<ClassRow> classRows = new ArrayList<>();
    private int index = 0;

    private Consumer<Integer> consumer = index -> {
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
    };

    public GroupRow() {
        setLayout(null);
//        setBackground(Color.PINK);

        WebLabel classLabel = new WebLabel("Название класса");
        classLabel.setBounds(15, 5, 120, 30);
        classLabel.setPadding(0, 5, 0, 0);
        classLabel.setFontSize(Frame.FONT_SIZE);
        add(classLabel);

        WebTextField classField = new WebTextField();
        classField.setBounds(135, 5, 200, 30);
        classField.setInputPrompt("Название...");
        classField.setPadding(0, 5, 0, 0);
        add(classField);

        WebButton remove = new WebButton(new ImageIcon(Objects.requireNonNull(Utils.loadImage(GroupRow.class, "icons/new.png"))));
        remove.setBounds(340, 5, 30, 30);
        remove.addActionListener(e -> addRow());
        add(remove);

        ClassRow row = new ClassRow(index++, consumer);
        row.setLocation(17, startHeight);
        add(row);
        classRows.add(row);
    }

    private void addRow() {
        ClassRow row = new ClassRow(index++, consumer);
        row.setLocation(17, height);
        add(row);
        classRows.add(row);

        height += ROW_OFFSET;
        setSize(WIDTH, height);
    }

}
