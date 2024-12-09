package supermarketdatabase.screen;

import supermarketdatabase.util.EditableGoodData;
import supermarketdatabase.util.Price;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.function.Consumer;

public class EditGoodInputPanel extends JPanel {
    public EditGoodInputPanel(EditableGoodData base, String titleString, String abortButtonName, String continueButtonName, Runnable onAbort, Consumer<EditableGoodData> onContinue) {
        JTextField name = new JTextField(base.name());
        JFormattedTextField price = new JFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat())), base.price().value());
        JSpinner stock = new JSpinner(new SpinnerNumberModel());
        stock.setValue(base.stock());

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
        textPanel.add(new JLabel("Bestand:"));
        textPanel.add(stock);

        JButton aboutButton = new JButton(abortButtonName);
        aboutButton.addActionListener(actionEvent -> onAbort.run());
        JButton continueButton = new JButton(continueButtonName);
        continueButton.addActionListener(actionEvent -> onContinue.accept(new EditableGoodData(name.getText(), new Price(Double.parseDouble(price.getText().replace(".", "").replace(",", "."))), (int) stock.getValue())));

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
