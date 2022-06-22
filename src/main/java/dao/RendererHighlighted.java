package dao;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RendererHighlighted extends DefaultTableCellRenderer {
    private JTextField searchField;

    public RendererHighlighted(JTextField searchField) {
        this.searchField = searchField;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean selected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value,
                selected, hasFocus, row, column);
        JLabel original = (JLabel) c;
        LabelHighlighted label = new LabelHighlighted();
        label.setFont(original.getFont());
        label.setText(original.getText());
        label.setBackground(original.getBackground());
        label.setForeground(original.getForeground());
        label.setHorizontalTextPosition(original.getHorizontalTextPosition());
        label.highlightText(searchField.getText());
        return label;
    }
}
