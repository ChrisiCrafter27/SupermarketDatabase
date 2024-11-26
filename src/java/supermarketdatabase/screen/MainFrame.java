package supermarketdatabase.screen;

import supermarketdatabase.util.LoginMode;
import supermarketdatabase.util.Util;
import supermarketdatabase.sql.MyDatabaseConnector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MainFrame {
    private final JFrame frame;
    private final JButton openButton;
    private final JLabel infoLabel;
    private final JButton loginButton0;
    private final JButton loginButton1;
    private final JButton loginButton2;

    private MyDatabaseConnector connector;

    public MainFrame() {
        //Create components
        frame = new JFrame("Supermarkt Datenbank");
        openButton = new JButton("Datenbank öffnen");
        infoLabel = new JLabel("- keine Datei geöffnet -", SwingConstants.CENTER);
        openButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser(new File("./"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("Datenbanken (.db)", "db"));
            int result = fileChooser.showOpenDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if(file.exists() && file.getName().endsWith(".db")) {
                    try {
                        connector = new MyDatabaseConnector(file.getAbsolutePath());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Datenbank konnte nicht geöffnet werden:\n" + Util.exceptionString(e), "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                }
                infoLabel.setText(file.getPath());
            }
            update();
        });
        loginButton0 = new JButton("Anmelden als Großkunde");
        loginButton0.addActionListener(actionEvent -> {
            if(connector != null) {
                //new LoginFrame(this, connector, LoginFrame.Mode.Grosskunde);
            }
            update();
        });
        loginButton1 = new JButton("Anmelden als Mitarbeiter");
        loginButton1.addActionListener(actionEvent -> {
            if(connector != null) {
                //new LoginFrame(this, connector, LoginFrame.Mode.Mitarbeiter);
            }
            update();
        });
        loginButton2 = new JButton("Anmelden als Admin");
        loginButton2.addActionListener(actionEvent -> {
            if(connector != null) {
                //new LoginFrame(this, connector, LoginFrame.Mode.Admin);
            }
            update();
        });

        //Layout components
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(openButton);
        northPanel.add(infoLabel);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(loginButton0);
        southPanel.add(loginButton1);
        southPanel.add(loginButton2);
        southPanel.add(new JTextField());
        southPanel.add(new JComboBox<>());
        JPanel anotherPanel = new JPanel(new BorderLayout());
        anotherPanel.add(southPanel, BorderLayout.NORTH);
        //frame.setLayout(new BorderLayout());
        //frame.add(northPanel, BorderLayout.NORTH);
        //frame.add(new JLabel("SQLite"), BorderLayout.CENTER);
        //frame.add(anotherPanel, BorderLayout.SOUTH);
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new TitledBorder("Panel"));
        mainPanel.setLayout(new GridLayout(3, 1));
        mainPanel.add(northPanel);
        mainPanel.add(new JLabel("SQLite"));
        mainPanel.add(southPanel);
        frame.add(mainPanel);

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        cardPanel.add(new LoginPanel(connector, LoginMode.Admin, () -> {}), "Login");
        cardLayout.show(cardPanel, "Login");
        //frame.add(cardPanel);

        //Temporary
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        update();
    }

    private void update() {
        loginButton0.setEnabled(connector != null);
        loginButton1.setEnabled(connector != null);
        loginButton2.setEnabled(connector != null);
        infoLabel.setText(connector != null ? connector.getPath() : "- keine Datei geöffnet -");
    }

    // LAYOUTS
    //Flow: multiple rows
    //Grid: a grid & for buttons
    //Border: 5 areas
    //Box: one row
    //Card: multiple pages
}
