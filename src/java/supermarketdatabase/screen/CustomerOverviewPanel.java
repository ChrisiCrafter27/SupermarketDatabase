package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statements;
import supermarketdatabase.util.CustomerData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.function.Consumer;

public class CustomerOverviewPanel extends DatabasePanel {
    public CustomerOverviewPanel(MyDatabaseConnector connector, int id, Runnable reset, Consumer<JComponent> setPanel, Runnable refresh) {
        super(connector);

        CustomerData customerData = execute(Statements.customerData(id)).orElseThrow();

        JButton changeButton = new JButton("Daten ändern");
        JButton deleteButton = new JButton("Konto löschen");

        changeButton.addActionListener(actionEvent -> setPanel.accept(new EnterCustomerDataPanel(customerData.simlify(), "Daten anpassen", "Abbrechen", "Daten ändern", refresh, simpleCustomerData -> {
            boolean success = execute(Statements.updateCustomerData(id, simpleCustomerData));
            if(success) {
                JOptionPane.showMessageDialog(this, "Die Daten wurden erfolgreich angepasst!", "Daten angepasst", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Beim Anpassen der Daten ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Anpassen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            }
            refresh.run();
        })));
        deleteButton.addActionListener(actionEvent -> {
            if(JOptionPane.showConfirmDialog(this, "Konto wirklich unwiderruflich löschen?", "Konto löschen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                boolean success = execute(Statements.deleteCustomer(id));
                if(success) {
                    JOptionPane.showMessageDialog(this, "Das Konto wurde erfolgreich gelöscht!", "Konto gelöscht", JOptionPane.INFORMATION_MESSAGE);
                    reset.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Beim Löschen des Kontos ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Löschen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel title = new JLabel("Übersicht");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        JPanel dataPanel = new JPanel();
        dataPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dataPanel.setLayout(new GridLayout(0, 2));
        dataPanel.add(new JLabel("Name:"));
        dataPanel.add(new JLabel(customerData.name()));
        dataPanel.add(new JLabel("Adresse:"));
        dataPanel.add(new JLabel(customerData.street() + ", " + customerData.city()));
        dataPanel.add(new JLabel("Registrierung:"));
        dataPanel.add(new JLabel(customerData.dateOfJoining()));
        dataPanel.add(new JLabel("Ausgaben:"));
        dataPanel.add(new JLabel(customerData.totaleExpenditure() + "€"));
        JPanel dataBorder = new JPanel();
        dataBorder.setLayout(new BorderLayout());
        dataBorder.setBorder(new TitledBorder("Kundendaten"));
        dataBorder.add(dataPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel.add(changeButton);
        buttonPanel.add(deleteButton);
        JPanel buttonBorder = new JPanel();
        buttonBorder.setLayout(new BorderLayout());
        buttonBorder.add(buttonPanel, BorderLayout.CENTER);
        buttonBorder.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(dataBorder, BorderLayout.CENTER);
        add(buttonBorder, BorderLayout.SOUTH);
    }
}
