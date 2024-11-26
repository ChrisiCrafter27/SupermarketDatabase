package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statement;

import javax.swing.*;

public class DatabasePanel extends JPanel {
    private final MyDatabaseConnector connector;

    public DatabasePanel(MyDatabaseConnector connector) {
        this.connector = connector;
    }

    public <R> R execute(Statement<R> statement) {
        return statement.execute(connector);
    }
}
