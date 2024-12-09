package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statements;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class MyFrame {
    private final JFrame frame;

    private MyDatabaseConnector connector;

    public MyFrame() {
        frame = new JFrame("Supermarkt Datenbank");
        frame.setMinimumSize(new Dimension(400, 200));

        Path path = Path.of("src/resources/supermarkt.db");
        if(Files.exists(path)) {
            connector = new MyDatabaseConnector(path);
            Statements.enableForeignKeys().execute(connector);
        }
        setComponent(createMainPanel());

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private MainPanel createMainPanel() {
        return new MainPanel(connector, newConnector -> {
            if(Statements.enableForeignKeys().execute(newConnector)) {
                connector = newConnector;
            }
        }, this::setComponent, () -> setComponent(createMainPanel()), mode -> {
            if(connector != null) setComponent(new LoginPanel(connector, mode, frame.getRootPane()::setDefaultButton, () -> setComponent(createMainPanel()), (loginMode, id) -> {
                switch (loginMode) {
                    case Grosskunde -> setComponent(new CustomerPanel(connector, id, this::setComponent, () -> setComponent(createMainPanel())));
                    case Mitarbeiter -> setComponent(new StaffPanel(connector, id, this::setComponent, () -> setComponent(createMainPanel())));
                }
            }));
        });
    }

    private void setComponent(JComponent component) {
        frame.getContentPane().removeAll();
        frame.add(component);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    // LAYOUTS
    //Flow: multiple rows
    //Grid: a grid & for buttons
    //Border: 5 areas
    //Box: one row
    //Card: multiple pages
}
