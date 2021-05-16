package diplom.gui.menu;

import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.laf.window.WebDialog;
import com.alee.laf.window.WebFrame;
import com.alee.managers.style.StyleId;
import com.google.gson.reflect.TypeToken;
import diplom.Application;
import diplom.classification.Class;
import diplom.gui.Frame;
import diplom.gui.Utils;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EditClassesGroupDialog extends WebDialog {

    static final int dialogWidth = 500;
    private final int dialogHeight = 500;
    private final int startY = 145;
    private List<Class> classes;
    private int currentY = 145;

    private WebButton newClassBtn;

    private WebPanel classesPanel;
    private List<ClassGroup> classGroups = new ArrayList<>();

    private Consumer<Integer> groupDeleteHandler = index -> {
        if (classGroups.size() == 1) return;

        int idx = index;
        System.out.println(idx);
        ClassGroup classGroup = classGroups.get(idx);

        classesPanel.remove(classGroup);
        classesPanel.revalidate();
        classesPanel.repaint();

        for (int i = 0; i < classGroups.size(); i++) {
            classGroups.get(i).setIndex(i);
        }

        this.revalidate();
    };

    public EditClassesGroupDialog(WebFrame frame) {
        WebFileChooser chooser = new WebFileChooser();
        chooser.setCurrentDirectory(Application.currentDir);
        File selectedFile = null;
        int result = chooser.showOpenDialog(this);
        if (result == WebFileChooser.APPROVE_OPTION) {
            try {
                selectedFile = chooser.getSelectedFile();
                String json = FileUtils.readFileToString(selectedFile, Charset.defaultCharset());
                classes = Application.gson.fromJson(json, new TypeToken<List<Class>>() {
                }.getType());
                System.out.println(classes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            WebOptionPane.showMessageDialog(
                    this,
                    "Вы не выбрали файл для редактирования",
                    "Ошибка",
                    WebOptionPane.ERROR_MESSAGE
            );
        }

        if (selectedFile == null) {
            setVisible(false);
        }

        setTitle("Редактирование группы классов");
        setSize(dialogWidth, dialogHeight + 20);
        setLocationRelativeTo(frame);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setLayout(null);

        WebLabel groupLabel = new WebLabel("Название группы");
        groupLabel.setBounds(15, 5, 120, 30);
        groupLabel.setFontSize(Frame.FONT_SIZE);
        add(groupLabel);

        WebTextField groupField = new WebTextField();
        groupField.setText(selectedFile.getName());
        groupField.setInputPrompt("Название группы...");
        groupField.setBounds(130, 7, 300, 28);
        groupField.setPadding(0, 5, 0, 0);
        add(groupField);

        WebButton save = new WebButton(new ImageIcon(Objects.requireNonNull(Utils.loadImage(EditClassesGroupDialog.class, "icons/edit.png"))));
        save.setBounds(440, 7, 30, 30);
        File finalSelectedFile = selectedFile;
        save.addActionListener(e -> {
            String json = Application.gson.toJson(getClasses());
            try {
                FileUtils.write(finalSelectedFile, json, Charset.defaultCharset());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            setVisible(false);
        });
        add(save);

        classesPanel = new WebPanel(new VerticalLayout());
//        classesPanel.setBackground(Color.PINK);

        for (Class cl : classes) {
            ClassGroup classGroup = new ClassGroup(this, classGroups.size(), groupDeleteHandler, cl);
            classesPanel.add(classGroup);
            classGroups.add(classGroup);
        }

        newClassBtn = new WebButton("Добавить класс");
//        newClassBtn.setBounds(15, currentY, 150, 30);
        newClassBtn.setPreferredSize(150, 30);

        classesPanel.add(newClassBtn);

        WebScrollPane scrollPane = new WebScrollPane(StyleId.scrollpaneTransparent, classesPanel);
        scrollPane.setBounds(10, 45, dialogWidth - 5, dialogHeight - 75);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.revalidate();
        add(scrollPane);
        newClassBtn.addActionListener(e -> {
            ClassGroup newGroup = new ClassGroup(this, classGroups.size(), groupDeleteHandler);
            classesPanel.add(newGroup);
            classGroups.add(newGroup);
            classesPanel.revalidate();
            classesPanel.repaint();
        });
        setVisible(true);
    }

    private List<Class> getClasses() {
        return classGroups.stream()
                .map(ClassGroup::getData)
                .collect(Collectors.toList());
    }

}
