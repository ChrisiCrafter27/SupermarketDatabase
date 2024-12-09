package supermarketdatabase.screen;

import supermarketdatabase.util.MyDocumentListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class EnterStaffPasswordPanel extends JPanel {
    public EnterStaffPasswordPanel(String titleString, String abortButtonName, String continueButtonName, Runnable onAbort, Consumer<String> onContinue) {
        JTextField password0 = new JTextField();
        JPasswordField password1 = new JPasswordField();

        JLabel title = new JLabel(titleString);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(0, 2, 10, 10));
        textPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        textPanel.add(new JLabel("Passwort:"));
        textPanel.add(password0);
        textPanel.add(new JLabel("Passwort wiederholen:"));
        textPanel.add(password1);

        JButton aboutButton = new JButton(abortButtonName);
        aboutButton.addActionListener(actionEvent -> onAbort.run());
        JButton continueButton = new JButton(continueButtonName);
        continueButton.addActionListener(actionEvent -> onContinue.accept(password0.getText()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel.add(continueButton);
        buttonPanel.add(aboutButton);

        MyDocumentListener documentListener = () -> continueButton.setEnabled(!password0.getText().isEmpty() && password0.getText().equals(String.valueOf(password1.getPassword())));
        password0.getDocument().addDocumentListener(documentListener);
        password1.getDocument().addDocumentListener(documentListener);
        continueButton.setEnabled(!password0.getText().isEmpty());

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
