package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statements;
import supermarketdatabase.util.FormattedDate;
import supermarketdatabase.util.Good;
import supermarketdatabase.util.MyDocumentListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CustomerOrderPanel extends DatabasePanel {
    public CustomerOrderPanel(MyDatabaseConnector connector, int id, Runnable refresh) {
        super(connector);

        List<Good> goods = execute(Statements.goods());

        JLabel title = new JLabel("Neue Bestellung");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        JComboBox<String> goodSelection = new JComboBox<>(goods.stream().map(good -> good.name() + " (" + good.id() + ")").toArray(String[]::new));
        JSpinner amount = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        JTextField pickupDate = new JTextField(LocalDate.now().format(FormattedDate.FORMATTER));
        JLabel goodInfo = new JLabel("");
        JLabel amountInfo = new JLabel("");
        JLabel orderDateInfo = new JLabel("");
        JLabel pickupDateInfo = new JLabel("");
        JLabel priceInfo = new JLabel("");

        JPanel in = new JPanel();
        in.setLayout(new GridLayout(0, 2));
        in.add(new JLabel("Ware:"));
        in.add(goodSelection);
        in.add(new JLabel("Anzahl:"));
        in.add(amount);
        in.add(new JLabel("Abholdatum:"));
        in.add(pickupDate);

        JPanel info = new JPanel();
        info.setLayout(new GridLayout(0, 2));
        info.setBorder(new TitledBorder("Daten"));
        info.add(new JLabel("Ware:"));
        info.add(goodInfo);
        info.add(new JLabel("Anzahl:"));
        info.add(amountInfo);
        info.add(new JLabel("Bestelldatum:"));
        info.add(orderDateInfo);
        info.add(new JLabel("Abholdatum:"));
        info.add(pickupDateInfo);
        info.add(new JLabel("Gesamtpreis:"));
        info.add(priceInfo);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(10));
        center.add(in);
        center.add(Box.createVerticalStrut(10));
        center.add(info);
        center.add(Box.createVerticalStrut(10));

        JButton button = new JButton("Bestellung aufgeben");
        button.setEnabled(false);
        
        Runnable update = () -> {
            Good good = goods.get(goodSelection.getSelectedIndex());
            amount.setValue(Math.min((int) amount.getValue(), good.stock()));
            ((SpinnerNumberModel) amount.getModel()).setMaximum(good.stock());
            goodInfo.setText(good.name() + " (" + good.id() + ")");
            amountInfo.setText(amount.getValue() + " (Bestand: " + good.stock() + ")");
            orderDateInfo.setText(LocalDate.now().format(FormattedDate.FORMATTER));
            priceInfo.setText(good.price().value() * (int) amount.getValue() + "€");
            try {
                LocalDate date = LocalDate.parse(pickupDate.getText(), FormattedDate.FORMATTER);
                pickupDateInfo.setText(date.format(FormattedDate.FORMATTER));
                button.setEnabled(date.isAfter(LocalDate.now()) && (int) amount.getValue() > 0);
            } catch (DateTimeParseException e) {
                pickupDateInfo.setText("");
                button.setEnabled(false);
            }
        };
        goodSelection.addItemListener(itemEvent -> update.run());
        amount.addChangeListener(changeEvent -> update.run());
        pickupDate.getDocument().addDocumentListener((MyDocumentListener) update::run);
        button.addActionListener(actionEvent -> {
            Good good = goods.get(goodSelection.getSelectedIndex());
            boolean success = execute(Statements.order(id, good.id(), (int) amount.getValue(), LocalDate.now(), LocalDate.parse(pickupDate.getText(), DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
            if(success) {
                JOptionPane.showMessageDialog(this, "Die Bestellung wurde erfolgreich abgeschlossen!", "Bestellung abgeschlossen", JOptionPane.INFORMATION_MESSAGE);
                refresh.run();
            } else JOptionPane.showMessageDialog(this, "Die Bestellung konnte nicht abgeschlossen werden.\nBitte versuchen sie es erneut.", "Bestellung fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
        });

        update.run();

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
    }
}
