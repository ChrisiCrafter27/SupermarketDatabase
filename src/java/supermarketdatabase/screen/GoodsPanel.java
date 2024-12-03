package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statements;
import supermarketdatabase.util.Good;
import supermarketdatabase.util.Price;
import supermarketdatabase.util.Temperature;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GoodsPanel extends DatabasePanel {
    public GoodsPanel(MyDatabaseConnector connector, boolean editable, Consumer<JComponent> setPanel, Runnable refresh) {
        super(connector);

        List<Good> goods = execute(Statements.goods());

        JButton delete = new JButton("Ware löschen");
        delete.setEnabled(false);
        JButton edit = new JButton("Ware ändern");
        edit.setEnabled(false);
        JButton create = new JButton("Ware hinzufügen");

        JLabel title = new JLabel("Waren");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        OrderTableModel tableModel = new OrderTableModel(goods);
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
        table.setPreferredScrollableViewportSize(new Dimension(450, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            delete.setEnabled(table.getSelectedRow() >= 0);
            edit.setEnabled(table.getSelectedRow() >= 0);
        });

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(10));
        center.add(new JScrollPane(table));
        center.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        JPanel subButtonPanel = new JPanel();
        subButtonPanel.setLayout(new GridLayout(0, 2, 10, 10));
        subButtonPanel.add(delete);
        subButtonPanel.add(edit);
        buttonPanel.add(subButtonPanel);
        buttonPanel.add(create);

        delete.addActionListener((actionEvent -> {
            if(JOptionPane.showConfirmDialog(this, "Ware wirklich unwiderruflich löschen?", "Ware löschen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                boolean success = execute(Statements.deleteGood(goods.get(table.getSelectedRow()).id()));
                if(success) {
                    JOptionPane.showMessageDialog(this, "Die Ware wurde erfolgreich gelöscht!", "Ware gelöscht", JOptionPane.INFORMATION_MESSAGE);
                    refresh.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Beim Löschen der Ware ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Löschen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
                }
            }
        }));
        edit.addActionListener((actionEvent -> {
            setPanel.accept(new EditGoodInputPanel(goods.get(table.getSelectedRow()).editable(), "", "", "", refresh, editableGoodData -> {
                boolean success = execute(Statements.updateGoodNameAndPrice(goods.get(table.getSelectedRow()).id(), editableGoodData));
                if(success) {
                    JOptionPane.showMessageDialog(this, "Die Daten wurden erfolgreich angepasst!", "Daten angepasst", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Beim Anpassen der Daten ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Anpassen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
                }
                refresh.run();
            }));
        }));
        create.addActionListener((actionEvent -> {
            setPanel.accept(new CreateGoodInputPanel("Neue Ware", "Abbrechen", "Bestätigen", refresh, simpleGoodData -> {
                boolean success = execute(Statements.createGood(simpleGoodData));
                if(success) {
                    JOptionPane.showMessageDialog(this, "Die Ware wurde erfolgreich erstellt!", "Ware erstellt", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Beim Erstellen der Ware ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Erstellen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
                }
                refresh.run();
            }));
        }));

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        if(editable) add(buttonPanel, BorderLayout.SOUTH);
    }

    private static class OrderTableModel extends AbstractTableModel {
        private final List<Good> goods;
        private final String[] columnNames = {"ID", "Name", "Preis", "Bestand", "Temperatur", "Platzbedarf"};
        private final Class<?>[] columnClasses = {Integer.class, String.class, Price.class, Integer.class, Temperature.class, Double.class};

        public OrderTableModel(List<Good> goods) {
            this.goods = new ArrayList<>(goods);
        }

        @Override
        public int getRowCount() {
            return goods.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnClasses[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Good good = goods.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> good.id();
                case 1 -> good.name();
                case 2 -> good.price();
                case 3 -> good.stock();
                case 4 -> good.temperature();
                case 5 -> good.requiredSpace();
                default -> null;
            };
        }
    }
}
