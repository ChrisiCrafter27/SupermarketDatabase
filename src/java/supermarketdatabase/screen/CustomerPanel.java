package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;

import javax.swing.*;
import java.util.function.Consumer;

public class CustomerPanel extends DatabaseTabbedPane {
    public CustomerPanel(MyDatabaseConnector connector, int id, Consumer<JComponent> setPanel, Runnable resetPanel) {
        super(connector);

        addTab("Übersicht", new CustomerOverviewPanel(connector, id, resetPanel, setPanel, () -> setPanel.accept(new CustomerPanel(connector, id, setPanel, resetPanel))));
        addTab("Alte Bestellungen", new CustomerOrdersPanel(connector, id));
        addTab("Neue Bestellung", new CustomerOrderPanel(connector, id, () -> setPanel.accept(new CustomerPanel(connector, id, setPanel, resetPanel))));
        addTab("Waren", new GoodsPanel(connector, false, setPanel, () -> setPanel.accept(new CustomerPanel(connector, id, setPanel, resetPanel))));
    }
}
