package supermarketdatabase.screen;

import supermarketdatabase.sql.Statements;
import supermarketdatabase.util.LoginMode;
import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.util.MyDocumentListener;
import supermarketdatabase.util.Name;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LoginPanel extends DatabasePanel {
    private boolean ignoreEvents;

    public LoginPanel(MyDatabaseConnector connector, LoginMode mode, Consumer<JButton> enterButton, Runnable back, BiConsumer<LoginMode, Integer> done) {
        super(connector);

        JTextField firstnameField = new JTextField();
        JTextField lastnameField = new JTextField();
        JSpinner idSpinner = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Zurück");

        MyDocumentListener findId = () -> {
            if(!ignoreEvents) {
                ignoreEvents = true;
                execute(Statements.findId(new Name(firstnameField.getText(), Optional.ofNullable(lastnameField.getText())), mode)).ifPresentOrElse(idSpinner::setValue, () -> idSpinner.setValue(0));
                ignoreEvents = false;
            }
        };
        ChangeListener findName = changeEvent -> {
            if(!ignoreEvents) {
                ignoreEvents = true;
                execute(Statements.findName((int) idSpinner.getValue(), mode)).ifPresentOrElse(name -> {
                    firstnameField.setText(name.firstname());
                    name.lastname().ifPresentOrElse(lastnameField::setText, () -> lastnameField.setText(""));
                }, () -> {
                    firstnameField.setText("");
                    lastnameField.setText("");
                });
                ignoreEvents = false;
            }
        };
        ActionListener login = actionEvent -> {
            if(execute(Statements.checkPassword((int) idSpinner.getValue(), String.valueOf(passwordField.getPassword()), mode))) {
                done.accept(mode == LoginMode.Mitarbeiter && ((int) idSpinner.getValue()) == 1 ? LoginMode.Admin : mode, (int) idSpinner.getValue());
            } else {
                JOptionPane.showMessageDialog(this, "ID und Passwort stimmen nicht überein!", "Login Fehlgeschlagen", JOptionPane.WARNING_MESSAGE);
            }
        };

        firstnameField.getDocument().addDocumentListener(findId);
        lastnameField.getDocument().addDocumentListener(findId);
        idSpinner.addChangeListener(findName);
        loginButton.addActionListener(login);
        backButton.addActionListener(actionEvent -> back.run());

        JLabel titel = new JLabel(mode == LoginMode.Grosskunde ? "Kunden Login" : "Mitarbeiter Login");
        titel.setHorizontalAlignment(SwingConstants.CENTER);
        titel.setFont(titel.getFont().deriveFont(20f));

        JPanel dataPanel = new JPanel();
        dataPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dataPanel.setLayout(new GridLayout(0, 2));
        if(mode == LoginMode.Grosskunde) {
            dataPanel.add(new JLabel("Name:"));
            dataPanel.add(firstnameField);
        } else {
            dataPanel.add(new JLabel("Vorname:"));
            dataPanel.add(firstnameField);
            dataPanel.add(new JLabel("Nachname:"));
            dataPanel.add(lastnameField);
        }
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
        JPanel buttonBorder = new JPanel();
        buttonBorder.setLayout(new BorderLayout());
        buttonBorder.add(buttonPanel, BorderLayout.CENTER);
        buttonBorder.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

        enterButton.accept(loginButton);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(titel, BorderLayout.NORTH);
        add(dataBorder, BorderLayout.CENTER);
        add(buttonBorder, BorderLayout.SOUTH);
    }
}
