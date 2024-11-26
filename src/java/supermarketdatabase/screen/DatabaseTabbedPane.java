package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statement;

import javax.swing.*;

public class DatabaseTabbedPane extends JTabbedPane {
    private final MyDatabaseConnector connector;

    public DatabaseTabbedPane(MyDatabaseConnector connector) {
        this.connector = connector;
    }

    public <R> R execute(Statement<R> statement) {
        return statement.execute(connector);
    }
}
