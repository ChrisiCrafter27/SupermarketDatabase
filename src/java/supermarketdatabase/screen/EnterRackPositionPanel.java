package supermarketdatabase.screen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.function.BiConsumer;

public class EnterRackPositionPanel extends JPanel {
    public EnterRackPositionPanel(String titleString, String abortButtonName, String continueButtonName, Runnable onAbort, BiConsumer<Integer, Integer> onContinue) {
        JFormattedTextField x = new JFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance())), 0);
        JFormattedTextField y = new JFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance())), 0);

        JLabel title = new JLabel(titleString);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(0, 2, 10, 10));
        textPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        textPanel.add(new JLabel("Reihe:"));
        textPanel.add(y);
        textPanel.add(new JLabel("Spalte:"));
        textPanel.add(x);

        JButton aboutButton = new JButton(abortButtonName);
        aboutButton.addActionListener(actionEvent -> onAbort.run());
        JButton continueButton = new JButton(continueButtonName);
        continueButton.addActionListener(actionEvent -> onContinue.accept(Integer.parseInt(x.getText().replace(".", "")), Integer.parseInt(y.getText().replace(".", ""))));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel.add(continueButton);
        buttonPanel.add(aboutButton);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
