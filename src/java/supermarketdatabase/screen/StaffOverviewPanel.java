package supermarketdatabase.screen;

import supermarketdatabase.sql.MyDatabaseConnector;
import supermarketdatabase.sql.Statements;
import supermarketdatabase.util.FormattedDate;
import supermarketdatabase.util.StaffData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.function.Consumer;

public class StaffOverviewPanel extends DatabasePanel {
    public StaffOverviewPanel(MyDatabaseConnector connector, int id, Runnable reset, Consumer<JComponent> setPanel, Runnable refresh) {
        super(connector);

        StaffData staffData = execute(Statements.staffData(id)).orElseThrow();

        JButton changeButton = new JButton("Passwort ändern");

        changeButton.addActionListener(actionEvent -> setPanel.accept(new EnterStaffPasswordPanel("Passwort ändern", "Abbrechen", "Passwort ändern", refresh, password -> {
            boolean success = execute(Statements.updateStaffPasswort(id, password));
            if(success) {
                JOptionPane.showMessageDialog(this, "Das Passwort wurde erfolgreich geändert!", "Passwort geändert", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Beim Ändern des Passworts ist ein Fehler aufgetreten.\nBitte versuchen sie es erneut.", "Ändern fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            }
            refresh.run();
        })));

        JLabel title = new JLabel("Übersicht");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        JPanel dataPanel = new JPanel();
        dataPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dataPanel.setLayout(new GridLayout(0, 2));
        dataPanel.add(new JLabel("Name:"));
        dataPanel.add(new JLabel(staffData.name().toString()));
        dataPanel.add(new JLabel("Geburtsdatum:"));
        dataPanel.add(new JLabel(staffData.birthday().toString()));
        dataPanel.add(new JLabel("Adresse:"));
        dataPanel.add(new JLabel(staffData.street() + ", " + staffData.city()));
        dataPanel.add(new JLabel("Wochenstunden:"));
        dataPanel.add(new JLabel("" + staffData.weeklyHours()));
        dataPanel.add(new JLabel("Gehalt:"));
        dataPanel.add(new JLabel(staffData.salary() + "€"));
        dataPanel.add(new JLabel("Aufgabenbereich:"));
        dataPanel.add(new JLabel(staffData.taskType()));
        if(staffData.supervisorId() != -1) {
            dataPanel.add(new JLabel("Vorgesetzter:"));
            dataPanel.add(new JLabel(staffData.supervisorName().toString() + " (" + staffData.supervisorId() + ")"));
        }
        JPanel dataBorder = new JPanel();
        dataBorder.setLayout(new BorderLayout());
        dataBorder.setBorder(new TitledBorder("Mitarbeiterdaten"));
        dataBorder.add(dataPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(dataBorder, BorderLayout.CENTER);
    }
}
