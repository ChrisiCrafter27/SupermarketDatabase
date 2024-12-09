package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statements;
import supermarketdatabase.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RacksPanel extends DatabasePanel {
    public RacksPanel(MyDatabaseConnector connector, Consumer<JComponent> setPanel, Runnable refresh) {
        super(connector);

        List<GoodInRack> goods = new ArrayList<>();

        JLabel title = new JLabel("Regale");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        RackTableModel tableModel = new RackTableModel(List.of());
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
        table.setPreferredScrollableViewportSize(new Dimension(450, 200));

        JSpinner rackSelection = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        JLabel position = new JLabel("-");
        JLabel capacity = new JLabel("-");
        JLabel temperature = new JLabel("-");
        JButton deleteGood = new JButton("Ware löschen");
        deleteGood.setEnabled(false);
        JButton reduceGood = new JButton("Anzahl reduzieren");
        reduceGood.setEnabled(false);
        JButton addGood = new JButton("Ware einsortieren");
        addGood.setEnabled(false);
        JButton deleteRack = new JButton("Regal löschen");
        deleteRack.setEnabled(false);
        JButton moveRack = new JButton("Regal bewegen");
        moveRack.setEnabled(false);
        JButton createRack = new JButton("Regal erstellen");

        rackSelection.addChangeListener(changeEvent -> {
            goods.clear();
            goods.addAll(execute(Statements.goodsInRack((int) rackSelection.getValue())));
            execute(Statements.rack((int) rackSelection.getValue())).ifPresentOrElse(rack -> {
                deleteRack.setEnabled(true);
                moveRack.setEnabled(true);
                addGood.setEnabled(true);
                position.setText("(" + rack.x() + "|" + rack.y() + ")");
                capacity.setText("" + rack.capacity());
                temperature.setText(rack.temperature().toString());
            }, () -> {
                deleteRack.setEnabled(false);
                moveRack.setEnabled(false);
                addGood.setEnabled(false);
                position.setText("-");
                capacity.setText("-");
                temperature.setText("-");
            });
            table.setModel(new RackTableModel(goods));
            deleteGood.setEnabled(false);
            reduceGood.setEnabled(false);
        });

        table.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            deleteGood.setEnabled(true);
            reduceGood.setEnabled(true);
        });

        deleteGood.addActionListener(actionEvent -> {
            if(JOptionPane.showConfirmDialog(this, "Ware wirklich unwiderruflich löschen?", "Ware löschen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                boolean success = execute(Statements.deleteGoodInRack(goods.get(table.getSelectedRow()).id()));
                if(success) {
                    JOptionPane.showMessageDialog(this, "Die Ware wurde erfolgreich gelöscht!", "Ware gelöscht", JOptionPane.INFORMATION_MESSAGE);
                    refresh.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Beim Löschen der Ware ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Löschen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        reduceGood.addActionListener(actionEvent -> setPanel.accept(new EnterGoodAmountPanel(goods.get(table.getSelectedRow()).amount(), "Anzahl reduzieren", "Abbrechen", "Bestätigen", refresh, amount -> {
            boolean success = execute(Statements.reduceGoodInRack(goods.get(table.getSelectedRow()).id(), amount));
            if(success) {
                JOptionPane.showMessageDialog(this, "Die Anzahl wurde erfolgreich reduziert!", "Anzahl reduziert", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Beim Reduzieren der Anzahl ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Reduzieren fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            }
            refresh.run();
        })));
        addGood.addActionListener(actionEvent -> setPanel.accept(new PutGoodInRackPanel(execute(Statements.rack((int) rackSelection.getValue())).orElseThrow(), execute(Statements.goods()), "Ware einsortieren", "Abbrechen", "Bestätigen", refresh, (goodId, amount) -> {
            boolean success = execute(Statements.putGoodInRack((int) rackSelection.getValue(), goodId, amount));
            if(success) {
                JOptionPane.showMessageDialog(this, "Die Ware wurde erfolgreich einsortiert!", "Ware einsortiert", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Beim Einsortieren der Ware ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Einsortieren fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            }
            refresh.run();
        })));
        deleteRack.addActionListener(actionEvent -> {
            if(JOptionPane.showConfirmDialog(this, "Regal wirklich unwiderruflich löschen?", "Regal löschen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                boolean success = execute(Statements.deleteRack((int) rackSelection.getValue()));
                if(success) {
                    JOptionPane.showMessageDialog(this, "Das Regal wurde erfolgreich gelöscht!", "Regal gelöscht", JOptionPane.INFORMATION_MESSAGE);
                    refresh.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Beim Löschen des Regals ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Löschen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        moveRack.addActionListener(actionEvent -> setPanel.accept(new EnterRackPositionPanel("Regal bewegen", "Abbrechen", "Bestätigen", refresh, (x, y) -> {
            boolean success = execute(Statements.moveRack((int) rackSelection.getValue(), x, y));
            if(success) {
                JOptionPane.showMessageDialog(this, "Das Regal wurde erfolgreich einsortiert!", "Regal erstellt", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Beim Erstellen des Regals ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Erstellen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            }
            refresh.run();
        })));
        createRack.addActionListener(actionEvent -> setPanel.accept(new CreateRackInputPanel("Regal erstellen", "Abbrechen", "Bestätigen", refresh, rack -> {
            boolean success = execute(Statements.createRack(rack));
            if(success) {
                JOptionPane.showMessageDialog(this, "Das Regal wurde erfolgreich einsortiert!", "Regal erstellt", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Beim Erstellen des Regals ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Erstellen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            }
            refresh.run();
        })));

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new GridLayout(0, 2));
        selectionPanel.add(new JLabel("Regal-ID:"));
        selectionPanel.add(rackSelection);

        JPanel rackPanel = new JPanel();
        rackPanel.setLayout(new GridLayout(0, 2));
        rackPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        rackPanel.add(new JLabel("Position:"));
        rackPanel.add(position);
        rackPanel.add(new JLabel("Kapazität:"));
        rackPanel.add(capacity);
        rackPanel.add(new JLabel("Temperatur:"));
        rackPanel.add(temperature);
        JPanel rackBorder = new JPanel();
        rackBorder.setLayout(new BorderLayout());
        rackBorder.setBorder(new TitledBorder("Regal"));
        rackBorder.add(rackPanel, BorderLayout.CENTER);

        JPanel goodsButtonPanel = new JPanel();
        goodsButtonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        goodsButtonPanel.add(deleteGood);
        goodsButtonPanel.add(reduceGood);
        goodsButtonPanel.add(addGood);

        JPanel goodsPanel = new JPanel();
        goodsPanel.setLayout(new BoxLayout(goodsPanel, BoxLayout.Y_AXIS));
        goodsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        goodsPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        goodsPanel.add(Box.createVerticalStrut(10));
        goodsPanel.add(goodsButtonPanel);
        JPanel goodsBorder = new JPanel();
        goodsBorder.setLayout(new BorderLayout());
        goodsBorder.setBorder(new TitledBorder("Enthält"));
        goodsBorder.add(goodsPanel, BorderLayout.CENTER);

        JButton selectButton = new JButton("Regal nach Position auswählen");
        CardLayout cards = new CardLayout();
        selectButton.addActionListener(actionEvent -> cards.show(this, "Select"));
        JPanel buttonPanel0 = new JPanel();
        buttonPanel0.setLayout(new GridLayout(0, 1));
        buttonPanel0.add(selectButton);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(10));
        center.add(buttonPanel0);
        center.add(Box.createVerticalStrut(10));
        center.add(selectionPanel);
        center.add(Box.createVerticalStrut(10));
        center.add(rackBorder);
        center.add(Box.createVerticalStrut(10));
        center.add(goodsBorder);
        center.add(Box.createVerticalStrut(10));

        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel1.add(deleteRack);
        buttonPanel1.add(moveRack);
        buttonPanel1.add(createRack);

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBorder(new EmptyBorder(10, 10, 10, 10));
        main.add(title, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);
        main.add(buttonPanel1, BorderLayout.SOUTH);

        setLayout(cards);
        add(main, "Main");
        add(new SelectRackPanel(connector, () -> cards.show(this, "Main"), id -> {
            rackSelection.setValue(id);
            cards.show(this, "Main");
        }), "Select");
        cards.show(this, "Main");
    }

    private static class RackTableModel extends AbstractTableModel {
        private final List<GoodInRack> goods;
        private final String[] columnNames = {"Ware", "Menge", "Platzbedarf"};
        private final Class<?>[] columnClasses = {GoodRef.class, Integer.class, Integer.class};

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
                case 2 -> good.requiredSpace();
                default -> null;
            };
        }
    }
}
