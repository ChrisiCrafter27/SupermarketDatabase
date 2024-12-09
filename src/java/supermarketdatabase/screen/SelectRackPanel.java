package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class SelectRackPanel extends DatabasePanel {
    public SelectRackPanel(MyDatabaseConnector connector, Runnable back, Consumer<Integer> set) {
        super(connector);

        JLabel title = new JLabel("Regale");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        JPanel rackButtons = new JPanel();
        rackButtons.setLayout(new GridLayout(20, 20, 1, 1));
        for(int y = 1; y <= 20; y++) {
            for(int x = 1; x <= 20; x++) {
                execute(Statements.rackId(x, y)).ifPresentOrElse(id -> {
                    JButton button = new JButton();
                    button.addActionListener(actionEvent -> set.accept(id));
                    rackButtons.add(button);
                }, () -> {
                    JButton button = new JButton();
                    button.setEnabled(false);
                    rackButtons.add(button);
                });
            }
        }

        JButton abort = new JButton("Zurück");
        abort.addActionListener(actionEvent -> back.run());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(rackButtons);
        centerPanel.add(Box.createVerticalStrut(10));

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(abort, BorderLayout.SOUTH);
    }
}
