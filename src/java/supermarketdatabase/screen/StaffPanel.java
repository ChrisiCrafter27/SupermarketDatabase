package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;

import javax.swing.*;
import java.util.function.Consumer;

public class StaffPanel extends DatabaseTabbedPane {
    public StaffPanel(MyDatabaseConnector connector, int id, Consumer<JComponent> setPanel, Runnable resetPanel) {
        super(connector);

        addTab("Übersicht", new StaffOverviewPanel(connector, id, setPanel, () -> setPanel.accept(new StaffPanel(connector, id, setPanel, resetPanel))));
        addTab("Bestellungen", new OrdersPanel(connector));
        addTab("Regale", new RacksPanel(connector, setPanel, () -> setPanel.accept(new StaffPanel(connector, id, setPanel, resetPanel))));
        addTab("Waren", new GoodsPanel(connector, true, setPanel, () -> setPanel.accept(new StaffPanel(connector, id, setPanel, resetPanel))));
        addTab("Mitarbeiter", new AdminStaffPanel(connector, id == 1, setPanel, () -> setPanel.accept(new StaffPanel(connector, id, setPanel, resetPanel))));
    }
}
