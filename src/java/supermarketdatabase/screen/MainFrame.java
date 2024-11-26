package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;

import javax.swing.*;
import java.awt.*;

public class MainFrame {
    private final JFrame frame;

    private MyDatabaseConnector connector;

    public MainFrame() {
        frame = new JFrame("Supermarkt Datenbank");
        frame.setMinimumSize(new Dimension(400, 200));

        setPanel(new MainPanel(newConnector -> connector = newConnector, loginMode -> {
            if(connector != null) setPanel(new LoginPanel(connector, loginMode, () -> {}));
        }));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setPanel(JPanel panel) {
        frame.getContentPane().removeAll();
        frame.add(panel);
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
