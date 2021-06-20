package diplom.gui;

import com.alee.extended.filechooser.WebDirectoryChooser;
import com.alee.extended.filechooser.WebFileDrop;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.grouping.GroupPane;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.text.WebTextArea;
import com.alee.laf.window.WebFrame;
import com.alee.utils.filefilter.FilesFilter;
import diplom.Application;
import diplom.ExperimentsInterface;
import diplom.classification.Class;
import diplom.classification.Classification;
import diplom.clustering.Clustering;
import diplom.distance.Distance;
import diplom.gui.menu.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Panel extends WebPanel {

    private int mode = 0;
    private WebButton classesChooser;

    private WebTextArea textArea;
    private File classes;
    private File[] selectedFiles;

    public Panel(WebFrame frame) {
        setLayout(null);
        setBounds(0, Menu.MENU_Y_OFFSET, frame.getWidth() - Frame.X_OFFSET, frame.getHeight() - Frame.Y_OFFSET);

        WebLabel modeLabel = new WebLabel("Режим работы");
        modeLabel.setBounds(25, 15, 200, 30);
        modeLabel.setFontSize(Frame.FONT_SIZE);
        add(modeLabel);

        WebToggleButton cluster = new WebToggleButton("Кластеризация", true);
        cluster.setPreferredSize(100, 30);
        cluster.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                mode = 0;
                classesChooser.setVisible(false);
                classes = null;
            }
        });

        WebToggleButton classify = new WebToggleButton("Классификация", false);
        classify.setPreferredSize(100, 30);
        classify.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                mode = 1;
                classesChooser.setText("Выберите файл c классами");
                classesChooser.setVisible(true);
            }
        });

        GroupPane togglePane = new GroupPane(cluster, classify);
        togglePane.setBounds(120, 15, 200, 30);
        add(togglePane);

        classesChooser = new WebButton("Выберите файл c классами");
        classesChooser.setBounds(330, 15, 150, 30);
        classesChooser.setFocusPainted(false);
        classesChooser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        classesChooser.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(false);
            chooser.setCurrentDirectory(Application.currentDir);
            int result = chooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                classes = chooser.getSelectedFile();
                classesChooser.setText(classes.getName());
            }
        });
        classesChooser.setVisible(false);
        add(classesChooser);

        WebButton run = new WebButton(new ImageIcon(Objects.requireNonNull(Utils.loadImage(Panel.class, "icons/start.jpg"))));
        run.setSize(30, 30);
        run.setLocation(getWidth() - 25 - 30, 15);
        run.setFocusPainted(false);
        run.setCursor(new Cursor(Cursor.HAND_CURSOR));
        run.addActionListener(e -> {
            try {
                if (selectedFiles == null || selectedFiles.length == 0) {
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

                ////////////////////////////////////////////////////////////////////////////////////////////////////////

                if (Application.experiments) {
                    double workTime = 0;

                    for (int i = 1; i <= Application.REPETITION_COUNT - 1; i++) {
                        System.out.println(i);
                        try {
                            workTime += runAlgorithm();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.err.println(i);
                        }
                    }

                    workTime /= 100;
                    System.out.printf("Число повторений: %d; Кол-во файлов: %d; Среднее время: %.2f мс\n",
                            Application.REPETITION_COUNT, selectedFiles.length, workTime);
                }

                ////////////////////////////////////////////////////////////////////////////////////////////////////////
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        add(run);


        WebFileDrop fileDrop = new WebFileDrop();
        fileDrop.setFont(new Font("Dialog", Font.PLAIN, 12));
        fileDrop.setDropText("Перетащите сюда файлы для обратотки или кликните для выбора всей папки");
        fileDrop.setMargin(0);
        fileDrop.setFileFilter(new FilesFilter() {
            @Override
            public boolean accept(File file) {
                return Application.isSupportableFile(file);
            }
        });
        fileDrop.addFileSelectionListener(files -> {
            selectedFiles = files.toArray(new File[0]);
        });
        fileDrop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    WebPopupMenu popup = new WebPopupMenu();

                    WebMenuItem clear = new WebMenuItem("Очистить");
                    clear.setEnabled(true);
                    clear.addActionListener(event -> {
                        fileDrop.removeAllSelectedFiles();
                        selectedFiles = null;
                    });
                    popup.add(clear);

                    popup.show(fileDrop, e.getPoint().x, e.getPoint().y);
                }
                if (SwingUtilities.isLeftMouseButton(e)) {
                    WebDirectoryChooser chooser = new WebDirectoryChooser(frame, "Выберите папку");
                    chooser.setFilter(new FilesFilter() {
                        @Override
                        public boolean accept(File file) {
                            return file.isDirectory();
                        }
                    });

                    chooser.setSelectedDirectory(Application.currentDir);
                    final int result = chooser.showDialog();
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File dir = chooser.getSelectedDirectory();
                        selectedFiles = dir.listFiles();
                        System.out.println("chooser: " + Arrays.toString(dir.list()));
                        fileDrop.setSelectedFiles(Arrays.stream(Objects.requireNonNull(selectedFiles)).collect(Collectors.toList()));
                    }
                }
            }
        });
        fileDrop.setCursor(new Cursor(Cursor.HAND_CURSOR));
        WebScrollPane filesDropScroll = new WebScrollPane(fileDrop);
//        filesDropScroll.setLayout();
        filesDropScroll.setBounds(25, 55, getWidth() - 50, 150);
        add(filesDropScroll);

        WebSeparator separator2 = new WebSeparator(WebSeparator.HORIZONTAL);
        separator2.setBounds(25, 170, getWidth() - 25, 1);
        add(separator2);

        textArea = new WebTextArea(getStyleId(), 3, 20);
        WebScrollPane scrollPane = new WebScrollPane(textArea);
        scrollPane.setBounds(25, 220, getWidth() - 50, 420);
        add(scrollPane);
    }

    private double runAlgorithm() throws Exception {
        double workTime;
        Map<String, List<String>> result;
        ExperimentsInterface experiments;
        File[] clone = selectedFiles.clone();
        if (mode == 0) {
            Distance distance = Distance.distance(Application.config.clustering.similarityMeasure);
            experiments = new Clustering(Application.config.clustering.separateValue, distance);
            result = experiments.run(clone);
            workTime = experiments.getWorkTime();
            textArea.setText(experiments.getBuilder());
        } else {
            Distance distance = Distance.distance(Application.config.classification.similarityMeasure);
            List<Class> classes = diplom.classification.Classification.parseClasses(this.classes);
            experiments = new Classification(classes, distance);
            result = experiments.run(clone);
            workTime = experiments.getWorkTime();
            textArea.setText(experiments.getBuilder());
        }

        return workTime;
    }

}
