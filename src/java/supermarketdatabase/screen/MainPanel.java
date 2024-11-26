package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.util.LoginMode;
import supermarketdatabase.util.Util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

public class MainPanel extends JPanel {
    public MainPanel(MyDatabaseConnector connector, Consumer<MyDatabaseConnector> newPath, Consumer<LoginMode> login) {
        JButton openButton = new JButton("Datenbank öffnen");
        JLabel infoLabel0 = new JLabel("- keine Datei geöffnet -", SwingConstants.CENTER);
        JLabel infoLabel1 = new JLabel("", SwingConstants.CENTER);
        JButton loginButton0 = new JButton("Anmelden als Großkunde");
        JButton loginButton1 = new JButton("Anmelden als Mitarbeiter");

        openButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser(new File("./"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("Datenbanken (.db)", "db"));
            int result = fileChooser.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if(file.exists() && file.getName().endsWith(".db")) {
                    try {
                        newPath.accept(new MyDatabaseConnector(file.toPath()));
                        loginButton0.setEnabled(true);
                        loginButton1.setEnabled(true);
                        infoLabel0.setText(file.getPath());
                        infoLabel1.setText("(" + file.getName() + ")");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Datenbank konnte nicht geöffnet werden:\n" + Util.exceptionString(e), "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        loginButton0.addActionListener(actionEvent -> login.accept(LoginMode.Grosskunde));
        loginButton1.addActionListener(actionEvent -> login.accept(LoginMode.Mitarbeiter));
        loginButton0.setEnabled(connector != null);
        loginButton1.setEnabled(connector != null);
        infoLabel0.setText(connector != null ? connector.getPath().toAbsolutePath().toString() : "- keine Datei geöffnet -");
        infoLabel1.setText(connector != null ? connector.getPath().getFileName().toString() : "");

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(0, 1, 10, 10));
        northPanel.add(openButton);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 1));
        infoPanel.add(infoLabel0);
        infoPanel.add(infoLabel1);
        northPanel.add(infoPanel);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(0, 1, 10, 10));
        southPanel.add(loginButton0);
        southPanel.add(loginButton1);

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
    }
}
