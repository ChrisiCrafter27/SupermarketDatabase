package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statements;
import supermarketdatabase.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.State;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RacksPanel extends DatabasePanel {
    public RacksPanel(MyDatabaseConnector connector, Consumer<JComponent> setPanel, Runnable refresh) {
        super(connector);

        List<GoodInRack> goods = new ArrayList<>();

        JLabel title = new JLabel("Bestellungen");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        RackTableModel tableModel = new RackTableModel(List.of());
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
        table.setPreferredScrollableViewportSize(new Dimension(450, 200));

        JSpinner rackSelection = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        JButton delete = new JButton("Ware löschen");
        delete.setEnabled(false);
        JButton create = new JButton("Ware einsortieren");

        JPanel rackButtons = new JPanel();
        rackButtons.setLayout(new GridLayout(20, 20, 1, 1));
        for(int y = 1; y <= 20; y++) {
            for(int x = 1; x <= 20; x++) {
                execute(Statements.rackId(x, y)).ifPresentOrElse(id -> {
                    JButton button = new JButton();
                    button.setPreferredSize(new Dimension(10, 10));
                    button.addActionListener(actionEvent -> {
                        rackSelection.setValue(id);
                        goods.clear();
                        goods.addAll(execute(Statements.goodsInRack((int) rackSelection.getValue())));
                        table.setModel(new RackTableModel(goods));
                        delete.setEnabled(false);
                    });
                    rackButtons.add(button);
                }, () -> {
                    JButton button = new JButton();
                    button.setEnabled(false);
                    rackButtons.add(button);
                });
            }
        }

        rackSelection.addChangeListener(changeEvent -> {
            goods.clear();
            goods.addAll(execute(Statements.goodsInRack((int) rackSelection.getValue())));
            table.setModel(new RackTableModel(goods));
            delete.setEnabled(false);
        });

        //delete.addActionListener((actionEvent -> {
        //    if(JOptionPane.showConfirmDialog(this, "Ware wirklich unwiderruflich löschen?", "Ware löschen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
        //        boolean success = execute(Statements.deleteGoodInRack(goods.get()));
        //        if(success) {
        //            JOptionPane.showMessageDialog(this, "Die Ware wurde erfolgreich gelöscht!", "Ware gelöscht", JOptionPane.INFORMATION_MESSAGE);
        //            refresh.run();
        //        } else {
        //            JOptionPane.showMessageDialog(this, "Beim Löschen der Ware ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Löschen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
        //        }
        //    }
        //}));
        //create.addActionListener((actionEvent -> {
        //    setPanel.accept(new EnterGoodAmountPanel(null, "Ware einsortieren", "Abbrechen", "Bestätigen", true, refresh, amount -> {
        //        boolean success = execute(Statements.putGoodInRack());
        //        if(success) {
        //            JOptionPane.showMessageDialog(this, "Die Ware wurde erfolgreich einsortiert!", "Ware einsortiert", JOptionPane.INFORMATION_MESSAGE);
        //        } else {
        //            JOptionPane.showMessageDialog(this, "Beim Einsortieren der Ware ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Einsortieren fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
        //        }
        //        refresh.run();
        //    }));
        //}));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(10));
        center.add(rackButtons);
        center.add(Box.createVerticalStrut(10));
        center.add(new JLabel("Regal-ID:"));
        center.add(rackSelection);
        center.add(Box.createVerticalStrut(10));
        center.add(new JScrollPane(table));
        center.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel.add(delete);
        buttonPanel.add(create);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private static class RackTableModel extends AbstractTableModel {
        private final List<GoodInRack> goods;
        private final String[] columnNames = {"Ware", "Menge", "Preis", "Bestand", "Temperatur", "Platzbedarf"};
        private final Class<?>[] columnClasses = {GoodRef.class, Integer.class, Price.class, Integer.class, Temperature.class, Double.class};

        public RackTableModel(List<GoodInRack> goods) {
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
            GoodInRack good = goods.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> good.ref();
                case 1 -> good.amount();
                case 2 -> good.price();
                case 3 -> good.stock();
                case 4 -> good.temperature();
                case 5 -> good.requiredSpace();
                default -> null;
            };
        }
    }
}
