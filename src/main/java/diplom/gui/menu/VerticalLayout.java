package diplom.gui.menu;

import com.alee.laf.button.WebButton;

import java.awt.*;

class VerticalLayout implements LayoutManager {

    private int leftPadding = 0;
    private int topPadding = 25;
    private int offset = 30;

    private Dimension size = new Dimension();

    // Метод определения минимального размера для контейнера
    public Dimension minimumLayoutSize(Container c) {
        return calculateBestSize(c);
    }

    // Метод определения предпочтительного размера для контейнера
    public Dimension preferredLayoutSize(Container c) {
        return calculateBestSize(c);
    }

    // Метод расположения компонентов в контейнере
    public void layoutContainer(Container container) {
        // Список компонентов
        Component[] list = container.getComponents();

        int ii = -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i] instanceof WebButton) {
                ii = i;
                break;
            }
        }

        Component c = list[ii];
        list[ii] = list[list.length - 1];
        list[list.length - 1] = c;

        int currentY = topPadding;
        for (Component component : list) {
            // Определение предпочтительного размера компонента
            Dimension pref = component.getPreferredSize();

            // Размещение компонента на экране
            component.setBounds(leftPadding, currentY, pref.width, pref.height);

            // Учитываем промежуток
            currentY += offset;

            // Смещаем вертикальную позицию компонента
            currentY += pref.height;
        }
    }

    // Метод вычисления оптимального размера контейнера
    private Dimension calculateBestSize(Container c) {
        // Вычисление длины контейнера
        Component[] list = c.getComponents();

        int width, maxWidth = 0;
        for (Component component : list) {
            width = component.getWidth();
            // Поиск компонента с максимальной длиной
            if (width > maxWidth)
                maxWidth = width;
        }

        // Размер контейнера в длину с учетом левого отступа
        size.width = maxWidth + leftPadding;

        // Вычисление высоты контейнера
        int height = 0;
        for (Component component : list) {
            height += offset;
            height += component.getHeight();
        }
        size.height = height;

        return size;
    }

    // Следующие два метода не используются
    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

}