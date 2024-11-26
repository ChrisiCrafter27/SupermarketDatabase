package supermarketdatabase.screen;

import supermarketdatabase.util.LoginMode;
import supermarketdatabase.sql.MyDatabaseConnector;

import javax.swing.*;
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
        JSpinner idSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Zurück");

        ActionListener searchId = actionEvent -> {

        };
    }
}
