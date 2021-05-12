package diplom.gui.menu;

import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import diplom.gui.Panel;
import diplom.gui.Utils;

import javax.swing.*;
import java.util.Objects;
import java.util.function.Consumer;

public class ClassRow extends WebPanel {

    public static final int HEIGHT = 40;
    private WebTextField word;
    private WebTextField count;
    private int index;

    public ClassRow(int index, Consumer<Integer> consumer) {
        this.index = index;

        setLayout(null);
        setSize(CreateClassesGroupDialog.dialogWidth, HEIGHT);

        word = new WebTextField();
        word.setBounds(0, 5, 165, 30);
        word.setInputPrompt("Ключевое слово #" + (index + 1));
        word.setPadding(0, 5, 0, 0);
        add(word);

        count = new WebTextField();
        count.setInputPrompt("Кол-во повторений");
        count.setBounds(169, 5, 115, 30);
        count.setPadding(0, 5, 0, 0);
        add(count);

        WebButton remove = new WebButton(new ImageIcon(Objects.requireNonNull(Utils.loadImage(Panel.class, "icons/remove.png"))));
        remove.setBounds(288, 5, 30, 30);
        remove.addActionListener(e -> consumer.accept(this.index));
        add(remove);
    }

    public String getWord() {
        return word.getText();
    }

    public int getCount() {
        return Integer.parseInt(count.getText());
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        word.setInputPrompt("Ключевое слово #" + (index + 1));
    }

    public void hi() {
        setVisible(false);
    }

    public void setY(int y) {
        setLocation(getX(), y);
    }

}
