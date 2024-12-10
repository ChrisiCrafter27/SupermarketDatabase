package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statements;
import supermarketdatabase.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AdminStaffPanel extends DatabasePanel {
    public AdminStaffPanel(MyDatabaseConnector connector, boolean editable, Consumer<JComponent> setPanel, Runnable refresh) {
        super(connector);

        List<StaffData> staffs = execute(Statements.staffs());

        JButton delete = new JButton("Mitarbeiter löschen");
        delete.setEnabled(false);
        JButton edit = new JButton("Mitarbeiter bearbeiten");
        edit.setEnabled(false);
        JButton editPassword = new JButton("Passwort ändern");
        editPassword.setEnabled(false);
        JButton create = new JButton("Mitarbeiter hinzufügen");

        JLabel title = new JLabel("Mitarbeiter");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        OrderTableModel tableModel = new OrderTableModel(staffs);
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
        table.setPreferredScrollableViewportSize(new Dimension(450, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            delete.setEnabled(table.getSelectedRow() >= 0);
            edit.setEnabled(table.getSelectedRow() >= 0);
            editPassword.setEnabled(table.getSelectedRow() >= 0);
        });

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(10));
        center.add(new JScrollPane(table));
        center.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        JPanel subButtonPanel = new JPanel();
        subButtonPanel.setLayout(new GridLayout(0, 3, 10, 10));
        subButtonPanel.add(delete);
        subButtonPanel.add(edit);
        subButtonPanel.add(editPassword);
        buttonPanel.add(subButtonPanel);
        buttonPanel.add(create);

        delete.addActionListener((actionEvent -> {
            if(JOptionPane.showConfirmDialog(this, "Mitarbeiter wirklich unwiderruflich löschen?", "Mitarbeiter löschen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                boolean success = execute(Statements.deleteStaff(staffs.get(table.convertRowIndexToModel(table.getSelectedRow())).id()));
                if(success) {
                    JOptionPane.showMessageDialog(this, "Der Mitarbeiter wurde erfolgreich gelöscht!", "Mitarbeiter gelöscht", JOptionPane.INFORMATION_MESSAGE);
                    refresh.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Beim Löschen des Mitarbeiters ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Löschen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
                }
            }
        }));
        edit.addActionListener((actionEvent -> setPanel.accept(new EnterStaffDataPanel(staffs.get(table.convertRowIndexToModel(table.getSelectedRow())), "Mitarbeiter bearbeiten", "Abbrechen", "Bestätigen", false, refresh, (staffData, password) -> {
            boolean success = execute(Statements.updateStaff(staffs.get(table.convertRowIndexToModel(table.getSelectedRow())).id(), staffData));
            if(success) {
                JOptionPane.showMessageDialog(this, "Die Daten wurden erfolgreich angepasst!", "Daten angepasst", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Beim Anpassen der Daten ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Anpassen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            }
            refresh.run();
        }))));
        editPassword.addActionListener((actionEvent -> setPanel.accept(new EnterStaffPasswordPanel("Passwort ändern", "Abbrechen", "Bestätigen", refresh, password -> {
            boolean success = execute(Statements.updateStaffPasswort(staffs.get(table.convertRowIndexToModel(table.getSelectedRow())).id(), password));
            if(success) {
                JOptionPane.showMessageDialog(this, "Das Passwort wurde erfolgreich geändert!", "Passwort geändert", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Beim Ändern des Passworts ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Ändern fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            }
            refresh.run();
        }))));
        create.addActionListener((actionEvent -> setPanel.accept(new EnterStaffDataPanel(null, "Neuer Mitarbeiter", "Abbrechen", "Bestätigen", true, refresh, (staffData, password) -> {
            boolean success = execute(Statements.registerStaff(staffData, password));
            if(success) {
                JOptionPane.showMessageDialog(this, "Der Mitarbeiter wurde erfolgreich erstellt!", "Mitarbeiter erstellt", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Beim Erstellen des Mitarbeiters ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Erstellen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            }
            refresh.run();
        }))));

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        if(editable) add(buttonPanel, BorderLayout.SOUTH);
    }

    private static class OrderTableModel extends AbstractTableModel {
        private final List<StaffData> staffs;
        private final String[] columnNames = {"ID", "Name", "Geburtsdatum", "Adresse", "Wochenstunden", "Gehalt", "Aufgabenbereich", "Vorgesetzter"};
        private final Class<?>[] columnClasses = {Integer.class, String.class, FormattedDate.class, String.class, Integer.class, Integer.class, String.class, NameRef.class};

        public OrderTableModel(List<StaffData> staffs) {
            this.staffs = new ArrayList<>(staffs);
        }

        @Override
        public int getRowCount() {
            return staffs.size();
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
            StaffData staff = staffs.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> staff.id();
                case 1 -> staff.name();
                case 2 -> staff.birthday();
                case 3 -> staff.street() + ", " + staff.city();
                case 4 -> staff.weeklyHours();
                case 5 -> new Price(staff.salary());
                case 6 -> staff.taskType();
                case 7 -> staff.supervisorId() == 0 ? null : new NameRef(staff.supervisorId(), staff.supervisorName());
                default -> null;
            };
        }
    }
}
