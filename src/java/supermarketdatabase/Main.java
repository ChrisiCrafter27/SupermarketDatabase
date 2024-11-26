package supermarketdatabase;

import supermarketdatabase.screen.MainFrame;
import supermarketdatabase.sql.lib.DatabaseConnector;
import supermarketdatabase.sql.lib.QueryResult;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        new MainFrame();
    }

    public static void run(String dbPath){
        DatabaseConnector db = new DatabaseConnector("", 0, dbPath, "", "");

        db.executeStatement("SELECT * FROM professoren");

        QueryResult result = db.getCurrentQueryResult();
        if(result == null){
            System.out.println(db.getErrorMessage());
            return;
        }

        String[][] rData = result.getData();
        String[] columnNames = result.getColumnNames();

        for(int i = 0; i < columnNames.length; i++){
            System.out.print(" | " + columnNames[i]);
        }
        System.out.println("\n");

        for(int i = 0; i < rData.length; i++){
            for(int j = 0; j < rData[i].length; j++){
                System.out.print(" | " + rData[i][j]);
            }
            System.out.println();
        }
    }

    public static void runOnProfessorendb(){
        run("D:/FACH Informatik/Stufe Q2/Professoren.db");
    }
}
