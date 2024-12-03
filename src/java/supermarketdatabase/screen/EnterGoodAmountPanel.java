package supermarketdatabase.screen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class EnterGoodAmountPanel extends JPanel {
    public EnterGoodAmountPanel(int base, String titleString, String abortButtonName, String continueButtonName, Runnable onAbort, Consumer<Integer> onContinue) {
        JSpinner amount = new JSpinner(new SpinnerNumberModel(base, 1, null, 1));

        JLabel title = new JLabel(titleString);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(0, 2, 10, 10));
        textPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        textPanel.add(new JLabel("Menge:"));
        textPanel.add(amount);

        JButton aboutButton = new JButton(abortButtonName);
        aboutButton.addActionListener(actionEvent -> onAbort.run());
        JButton continueButton = new JButton(continueButtonName);
        continueButton.addActionListener(actionEvent -> onContinue.accept((int) amount.getValue()));

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
