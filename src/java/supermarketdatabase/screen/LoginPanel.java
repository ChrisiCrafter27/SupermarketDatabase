package supermarketdatabase.screen;

import supermarketdatabase.util.LoginMode;
import supermarketdatabase.sql.MyDatabaseConnector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends DatabasePanel {
    private final LoginMode mode;
    private final Runnable back;

    public LoginPanel(MyDatabaseConnector connector, LoginMode mode, Runnable back) {
        super(connector);
        this.mode = mode;
        this.back = back;

        JTextField firstnameField = new JTextField();
        JTextField lastnameField = new JTextField();
        JSpinner idSpinner = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Zurück");

        ActionListener searchId = actionEvent -> {

        };
        ActionListener searchName = actionEvent -> {

        };

        JLabel titel = new JLabel("Mitarbeiter Login");
        titel.setHorizontalAlignment(SwingConstants.CENTER);
        titel.setFont(titel.getFont().deriveFont(20f));

        JPanel dataPanel = new JPanel();
        dataPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dataPanel.setLayout(new GridLayout(0, 2));
        dataPanel.add(new JLabel("Vorname:"));
        dataPanel.add(firstnameField);
        dataPanel.add(new JLabel("Nachname:"));
        dataPanel.add(lastnameField);
        dataPanel.add(new JLabel("ID:"));
        dataPanel.add(idSpinner);
        dataPanel.add(new JLabel("Passwort:"));
        dataPanel.add(passwordField);
        JPanel dataBorder = new JPanel();
        dataBorder.setLayout(new BorderLayout());
        dataBorder.setBorder(new TitledBorder("Anmeldedaten"));
        dataBorder.add(dataPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(titel, BorderLayout.NORTH);
        add(dataBorder, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
