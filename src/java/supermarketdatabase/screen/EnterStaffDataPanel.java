package supermarketdatabase.screen;

import supermarketdatabase.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.function.BiConsumer;

public class EnterStaffDataPanel extends JPanel {
    public EnterStaffDataPanel(StaffData base, String titleString, String abortButtonName, String continueButtonName, boolean password, Runnable onAbort, BiConsumer<StaffData, String> onContinue) {
        JTextField name0 = new JTextField(base != null ? base.name().firstname() : "");
        JTextField name1 = new JTextField(base != null ? base.name().lastname().orElseThrow() : "");
        JFormattedTextField birthday = new JFormattedTextField(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("dd.MM.yyyy"))));
        birthday.setValue(base != null ? base.birthday().toDate() : Date.from(Instant.now()));
        JTextField city = new JTextField(base != null ? base.city() : "");
        JTextField street = new JTextField(base != null ? base.street() : "");
        JSpinner weeklyHours = new JSpinner(new SpinnerNumberModel(base != null ? base.weeklyHours() : 0, 0, null, 1));
        JFormattedTextField salary = new JFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance())));
        salary.setValue(base != null ? base.salary() : 0);
        JTextField taskType = new JTextField(base != null ? base.taskType() : "");
        JSpinner supervisor = new JSpinner(new SpinnerNumberModel(base != null ? base.supervisorId() : 0, 0, null, 1));
        JTextField password0 = new JTextField();
        JPasswordField password1 = new JPasswordField();

        JLabel title = new JLabel(titleString);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(0, 2, 10, 10));
        textPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        textPanel.add(new JLabel("Vorname:"));
        textPanel.add(name0);
        textPanel.add(new JLabel("Nachname:"));
        textPanel.add(name1);
        textPanel.add(new JLabel("Geburtsdatum:"));
        textPanel.add(birthday);
        textPanel.add(new JLabel("Stadt:"));
        textPanel.add(city);
        textPanel.add(new JLabel("Straße:"));
        textPanel.add(street);
        textPanel.add(new JLabel("Wochenstunden:"));
        textPanel.add(weeklyHours);
        textPanel.add(new JLabel("Gehalt:"));
        textPanel.add(salary);
        textPanel.add(new JLabel("Aufgabenbereich:"));
        textPanel.add(taskType);
        textPanel.add(new JLabel("Vorgesetzter:"));
        textPanel.add(supervisor);
        if(password) {
            textPanel.add(new JLabel("Passwort:"));
            textPanel.add(password0);
            textPanel.add(new JLabel("Passwort wiederholen:"));
            textPanel.add(password1);
        }

        JButton aboutButton = new JButton(abortButtonName);
        aboutButton.addActionListener(actionEvent -> onAbort.run());
        JButton continueButton = new JButton(continueButtonName);
        continueButton.addActionListener(actionEvent -> onContinue.accept(new StaffData(-1, new Name(name0.getText(), name1.getText()), FormattedDate.parseFormatted(birthday.getText()), city.getText(), street.getText(), (int) weeklyHours.getValue(), Integer.parseInt(salary.getText().replace(".", "").replace(",", ".")), taskType.getText(), (int) supervisor.getValue(), null), password0.getText()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel.add(continueButton);
        buttonPanel.add(aboutButton);

        MyDocumentListener documentListener = () -> continueButton.setEnabled((!password && password0.getText().isEmpty() && password1.getPassword().length == 0) || (!password0.getText().isEmpty() && password0.getText().equals(String.valueOf(password1.getPassword()))));
        password0.getDocument().addDocumentListener(documentListener);
        password1.getDocument().addDocumentListener(documentListener);
        continueButton.setEnabled((!password && password0.getText().isEmpty() && password1.getPassword().length == 0) || (!password0.getText().isEmpty() && password0.getText().equals(String.valueOf(password1.getPassword()))));

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
