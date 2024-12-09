package supermarketdatabase.screen;

import supermarketdatabase.util.Good;
import supermarketdatabase.util.Rack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;

public class PutGoodInRackPanel extends JPanel {
    public PutGoodInRackPanel(Rack rack, List<Good> goods, String titleString, String abortButtonName, String continueButtonName, Runnable onAbort, BiConsumer<Integer, Integer> onContinue) {
        JComboBox<Good> goodSelection = new JComboBox<>(goods.stream().filter(good -> good.stock() > 0).filter(good -> good.temperature() == rack.temperature()).filter(good -> good.requiredSpace() <= rack.capacity()).toArray(Good[]::new));

        JSpinner amount = new JSpinner();

        if(goodSelection.getSelectedIndex() >= 0) amount.setModel(new SpinnerNumberModel(1, 1, (int) Math.min(rack.capacity() / goodSelection.getItemAt(goodSelection.getSelectedIndex()).requiredSpace(), goodSelection.getItemAt(goodSelection.getSelectedIndex()).stock()), 1));
        goodSelection.addItemListener(itemEvent -> amount.setModel(new SpinnerNumberModel(1, 1, (int) Math.min(rack.capacity() / goodSelection.getItemAt(goodSelection.getSelectedIndex()).requiredSpace(), goodSelection.getItemAt(goodSelection.getSelectedIndex()).stock()), 1)));

        JLabel title = new JLabel(titleString);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(0, 2, 10, 10));
        textPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        textPanel.add(new JLabel("Ware:"));
        textPanel.add(goodSelection);
        textPanel.add(new JLabel("Menge:"));
        textPanel.add(amount);

        JButton aboutButton = new JButton(abortButtonName);
        aboutButton.addActionListener(actionEvent -> onAbort.run());
        JButton continueButton = new JButton(continueButtonName);
        continueButton.addActionListener(actionEvent -> onContinue.accept(goodSelection.getItemAt(goodSelection.getSelectedIndex()).id(), (int) amount.getValue()));
        continueButton.setEnabled(goodSelection.getSelectedIndex() >= 0);

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
