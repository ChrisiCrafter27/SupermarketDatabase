package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;

public class CustomerPanel extends DatabaseTabbedPane {
    public CustomerPanel(MyDatabaseConnector connector, int id) {
        super(connector);

        addTab("Übersicht", new CustomerOverviewPanel(connector, id));
        addTab("Alte Bestellungen", new CustomerOrdersPanel(connector, id));
        addTab("Neue Bestellung", new CustomerOrderPanel(connector, id));
    }
}
