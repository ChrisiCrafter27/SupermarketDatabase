package supermarketdatabase.screen;

import supermarketdatabase.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.function.Consumer;

public class CreateGoodInputPanel extends JPanel {
    public CreateGoodInputPanel(String titleString, String abortButtonName, String continueButtonName, Runnable onAbort, Consumer<SimpleGoodData> onContinue) {
        JTextField name = new JTextField();
        JFormattedTextField price = new JFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat())), 0);
        JComboBox<Temperature> temperatur = new JComboBox<>(Temperature.values());
        JFormattedTextField requiredSpace = new JFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat())), 0);

        JLabel title = new JLabel(titleString);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(0, 2, 10, 10));
        textPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        textPanel.add(new JLabel("Name:"));
        textPanel.add(name);
        textPanel.add(new JLabel("Preis:"));
        textPanel.add(price);
        textPanel.add(new JLabel("Temperatur:"));
        textPanel.add(temperatur);
        textPanel.add(new JLabel("Platzbedarf:"));
        textPanel.add(requiredSpace);

        JButton aboutButton = new JButton(abortButtonName);
        aboutButton.addActionListener(actionEvent -> onAbort.run());
        JButton continueButton = new JButton(continueButtonName);
        continueButton.addActionListener(actionEvent -> onContinue.accept(new SimpleGoodData(name.getText(), new Price(Double.parseDouble(price.getText().replace(".", "").replace(",", "."))), (Temperature) temperatur.getSelectedItem(), Double.parseDouble(requiredSpace.getText().replace(".", "").replace(",", ".")))));

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
