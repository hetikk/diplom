package diplom.gui;

import com.alee.extended.filechooser.WebDirectoryChooser;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.text.WebTextArea;
import com.alee.laf.window.WebFrame;
import com.alee.managers.style.StyleId;
import com.alee.utils.filefilter.FilesFilter;
import diplom.Application;
import diplom.classification.Class;
import diplom.classification.Classification;
import diplom.clustering.Clustering;
import diplom.distance.Distance;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Panel extends WebPanel {

    private int mode = 0;

    private WebLabel classesChooserLabel;
    private WebButton classesChooser;
    private WebSeparator choosersSeparator;
    private File currentDir;

    private WebTextArea textArea;

    private static final List<String> FILE_EXTENSION = Arrays.asList(".txt", ".docx");
    private File classes;
    private File dir;

    public Panel(WebFrame frame) {
        currentDir = new File(System.getProperty("user.dir") + "/sets");

        setLayout(null);
        setBounds(0, 0, frame.getWidth() - Frame.X_OFFSET, frame.getHeight() - Frame.Y_OFFSET);

        WebLabel modeLabel = new WebLabel("Режим работы");
        modeLabel.setBounds(25, 15, 200, 30);
        modeLabel.setFontSize(Frame.FONT_SIZE);
        add(modeLabel);

        WebComboBox mode = new WebComboBox(StyleId.combobox, new String[]{"Кластеризация", "Классификация"});
        mode.setBounds(225, 15, 125, 30);
        mode.addActionListener(e -> {
            Panel.this.mode = mode.getSelectedIndex();
            System.out.println(Panel.this.mode);
            changeMode();
        });
        add(mode);

        WebLabel docsChooserLabel = new WebLabel("Выберите файлы для обратотки");
        docsChooserLabel.setBounds(25, 55, 200, 30);
        docsChooserLabel.setFontSize(Frame.FONT_SIZE);
        add(docsChooserLabel);

        WebButton docsChooser = new WebButton("Выбрать");
        docsChooser.setBounds(225, 55, 125, 30);
        docsChooser.setFocusPainted(false);
        docsChooser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        docsChooser.addActionListener(e -> {
            WebDirectoryChooser chooser = new WebDirectoryChooser(frame, "Выберите папку");
            chooser.setFilter(new FilesFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });

            chooser.setSelectedDirectory(currentDir);
            final int result = chooser.showDialog();
            if (result == JFileChooser.APPROVE_OPTION) {
                dir = chooser.getSelectedDirectory();
                System.out.println("chooser: " + Arrays.toString(dir.list()));
            }
        });
        add(docsChooser);

        choosersSeparator = new WebSeparator(WebSeparator.VERTICAL);
        choosersSeparator.setBounds(getWidth() / 2, 55, 1, 30);
        choosersSeparator.setVisible(false);
        add(choosersSeparator);

        classesChooserLabel = new WebLabel("Выберите файл c классами");
        classesChooserLabel.setBounds(getWidth() / 2 + 25, 55, 200, 30);
        classesChooserLabel.setFontSize(Frame.FONT_SIZE);
        classesChooserLabel.setVisible(false);
        add(classesChooserLabel);

        classesChooser = new WebButton("Выбрать");
        classesChooser.setBounds(getWidth() / 2 + 25 + 170, 55, 125, 30);
        classesChooser.setFocusPainted(false);
        classesChooser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        classesChooser.setVisible(false);
        classesChooser.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(false);
            chooser.setCurrentDirectory(currentDir);
            int result = chooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                classes = chooser.getSelectedFile();
            }
        });
        add(classesChooser);

        WebSeparator separator1 = new WebSeparator(WebSeparator.HORIZONTAL);
        separator1.setBounds(25, 105, getWidth() - 25, 1);
        add(separator1);

        WebButton run = new WebButton("Начать");
        run.setSize(100, 30);
        run.setLocation(getWidth() / 2 - run.getWidth() / 2, 120);
        run.setFocusPainted(false);
        run.setCursor(new Cursor(Cursor.HAND_CURSOR));
        run.addActionListener(e -> {
            try {
                if (dir == null) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Вы не выбрали файлы для обработки",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (Panel.this.mode == 1 && classes == null) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Вы не выбрали файл с классами",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                runAlgorithm();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        add(run);

        WebSeparator separator2 = new WebSeparator(WebSeparator.HORIZONTAL);
        separator2.setBounds(25, 170, getWidth() - 25, 1);
        add(separator2);

        textArea = new WebTextArea(getStyleId(), 3, 20);
        WebScrollPane scrollPane = new WebScrollPane(textArea);
        scrollPane.setBounds(25, 200, getWidth() - 50, 440);
        add(scrollPane);
    }

    private void changeMode() {
        classesChooserLabel.setVisible(mode != 0);
        classesChooser.setVisible(mode != 0);
        choosersSeparator.setVisible(mode != 0);
    }

    private void runAlgorithm() throws Exception {
        Distance distance = Distance.distance(Application.config.clustering.similarityMeasure);
        Map<String, List<String>> result;

        File[] filteredFiles = getFilteredFiles(dir);

        if (mode == 0) {
            Clustering clustering = new Clustering(Application.config.clustering.separateValue, distance);
            result = clustering.cluster(filteredFiles);
            textArea.setText(clustering.getBuilder());
        } else {
            List<Class> classes = diplom.classification.Classification.parseClasses(this.classes);
            diplom.classification.Classification classification = new Classification(classes, distance);
            result = classification.classify(filteredFiles);
            textArea.setText(classification.getBuilder());
        }
    }

    private File[] getFilteredFiles(File dir) {
        List<File> files = new ArrayList<>();
        File[] listFiles = dir.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File file : listFiles) {
                for (String ext : FILE_EXTENSION) {
                    if (file.getName().endsWith(ext)) {
                        files.add(file);
                    }
                }
            }
        }
//        System.out.print("filtered files: " + files);
        return files.toArray(new File[0]);
    }

}
