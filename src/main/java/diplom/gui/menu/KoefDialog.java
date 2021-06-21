package diplom.gui.menu;

import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.text.WebTextField;
import com.alee.laf.window.WebDialog;
import com.alee.laf.window.WebFrame;
import diplom.Application;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class KoefDialog extends WebDialog {

    public KoefDialog(WebFrame frame) {
        setModal(false);
        setTitle("Коэффициенты");
        setLayout(new GridLayout(2, 2, 0, 0));
//        setSize(100, 50);
        setLocationRelativeTo(frame);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        WebStyledLabel label1 = new WebStyledLabel("Значение r{max:sub} для кластеризации");
        label1.setFontSize(13);
        label1.setMargin(0, 0, 0, 7);
        add(label1);

        WebTextField field1 = new WebTextField(Application.config.clustering.separateValue + "");
        add(field1);

        WebStyledLabel label2 = new WebStyledLabel("Значение r{max:sub} для классификации");
        label2.setFontSize(13);
        label2.setMargin(0, 0, 0, 7);
        add(label2);

        WebTextField field2 = new WebTextField(Application.config.classification.separateValue + "");
        add(field2);

        DocumentListener docList1 = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn1(e, field1);
            }

            public void removeUpdate(DocumentEvent e) {
                warn1(e, field1);
            }

            public void insertUpdate(DocumentEvent e) {
                warn1(e, field1);
            }


        };

        DocumentListener docList2 = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn2(e, field2);
            }

            public void removeUpdate(DocumentEvent e) {
                warn2(e, field2);
            }

            public void insertUpdate(DocumentEvent e) {
                warn2(e, field2);
            }
        };

        field1.getDocument().addDocumentListener(docList1);
        field2.getDocument().addDocumentListener(docList2);

        pack();
        setVisible(true);
    }

    private void warn1(DocumentEvent de, WebTextField tf) {
        try {
            String text = de.getDocument().getText(0, de.getDocument().getLength());
            Application.config.clustering.separateValue = Double.parseDouble(text);
            System.out.println("new clustering separateValue:" + Application.config.clustering.separateValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void warn2(DocumentEvent de, WebTextField tf) {
        try {
            String text = de.getDocument().getText(0, de.getDocument().getLength());
            Application.config.classification.separateValue = Double.parseDouble(text);
            System.out.println("new classification separateValue:" + Application.config.classification.separateValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
