package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statements;
import supermarketdatabase.util.GoodRef;
import supermarketdatabase.util.Order;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerOrdersPanel extends DatabasePanel {
    public CustomerOrdersPanel(MyDatabaseConnector connector, int id) {
        super(connector);

        List<Order> orders = execute(Statements.orders(id));

        JLabel title = new JLabel("Bestellungen");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        OrderTableModel tableModel = new OrderTableModel(orders);
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
        table.setPreferredScrollableViewportSize(new Dimension(450, 0));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(10));
        center.add(new JScrollPane(table));

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private static class OrderTableModel extends AbstractTableModel {
        private final List<Order> orders;
        private final String[] columnNames = {"ID", "Ware", "Anzahl", "Bestelldatum", "Abholdatum", "Gesamtpreis"};
        private final Class<?>[] columnClasses = {Integer.class, GoodRef.class, Integer.class, LocalDate.class, LocalDate.class, Double.class};

        public OrderTableModel(List<Order> orders) {
            this.orders = new ArrayList<>(orders);
        }

        @Override
        public int getRowCount() {
            return orders.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnClasses[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Order order = orders.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> order.id();
                case 1 -> order.good();
                case 2 -> order.amount();
                case 3 -> order.orderDate();
                case 4 -> order.pickupDate();
                case 5 -> order.price();
                default -> null;
            };
        }
    }
}
