package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statements;
import supermarketdatabase.util.CustomerData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CustomerOverviewPanel extends DatabasePanel {
    public CustomerOverviewPanel(MyDatabaseConnector connector, int id) {
        super(connector);

        CustomerData customerData = execute(Statements.customerData(id)).orElseThrow();

        JButton changeButton = new JButton("Daten ändern");
        JButton deleteButton = new JButton("Konto löschen");
        changeButton.addActionListener(actionEvent -> {

        });
        deleteButton.addActionListener(actionEvent -> {

        });

        JLabel titel = new JLabel("Übersicht");
        titel.setHorizontalAlignment(SwingConstants.CENTER);
        titel.setFont(titel.getFont().deriveFont(20f));

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
        dataPanel.add(new JLabel("" + customerData.totaleExpenditure()));
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
        add(titel, BorderLayout.NORTH);
        add(dataBorder, BorderLayout.CENTER);
        add(buttonBorder, BorderLayout.SOUTH);
    }
}
