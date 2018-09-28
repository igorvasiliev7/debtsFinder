package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML
    private Text txtPathDebtors;
    @FXML
    private Text txtPathPayments;
    @FXML
    private Text txtError;
    @FXML
    private TextArea txtResults;
    @FXML
    private Button btnPathDebtors;
    @FXML
    private Button btnPathPayment;
    @FXML
    private Button btnSearch;

    private File debtorsFile;
    private File paymentsFile;

    public void initialize(URL location, ResourceBundle resources) {
        btnSearch.setOnAction(e-> search());
        btnPathDebtors.setOnAction(event -> chooseDebtors());
        btnPathPayment.setOnAction(event -> choosePayments());
    }

    private void search() {
        String payment;
        String debtor;
        String paymentsShort = "";
        String debtorShort = "";
        List<String> list = new LinkedList<>();
        String [] debtsArr;
        String [] paymentsArr;

        if(paymentsFile == null||debtorsFile == null){
            txtError.setText("One of lists is not chosen");
            return;
        } else txtError.setText("Searching...");

        BufferedReader readDebts;
        BufferedReader readPayments = reader(paymentsFile);

        try {
            BufferedWriter result = writer("d://result.txt");

            while ((payment = readPayments.readLine()) != null) {
                readDebts = reader(debtorsFile);
                list.clear();
                payment = payment.toLowerCase();
                while ((debtor = readDebts.readLine()) != null) {
                    debtor = debtor.toLowerCase();
                    paymentsArr = payment.split(";");
                    debtsArr = debtor.split(";");

                    if(paymentsArr.length>2&&debtsArr.length>2){
                        paymentsShort=paymentsArr[2];
                        debtorShort=debtsArr[2];
                    }else continue;
                    if (debtorShort.contains(paymentsShort)) {
                        list.add(debtor);
                    }
                }
                readDebts.close();
                if (list.size() != 0) {
                    txtResults.setText("Client: \n" + payment + "\nDebtors: ");
                    result.write("Client: " + payment + "\nDebtors: \n");
                    for (int i = 0; i < list.size(); i++) {
                        txtResults.setText(list.get(i) + "\n\n");
                        result.write(list.get(i) + "\n\n");
                    }
                }
            }

            readPayments.close();
            result.close();
            System.out.println("The End");
        }
        catch (IOException e){
            System.out.println("Error in result.txt creating");
        }
    }

    private BufferedWriter writer(String path) throws FileNotFoundException {
        return new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(path)));
    }
    private BufferedReader reader(File file){
        try {
            return new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "Cp1251"));
             } catch (FileNotFoundException | UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    private void choosePayments() {
        FileChooser fc=new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Csv files", "*.csv"));
        paymentsFile = fc.showOpenDialog(null);
       if(paymentsFile!=null) {
           txtPathPayments.setText(paymentsFile.getPath());
       }
    }

    private void chooseDebtors() {
        FileChooser fc=new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Csv files", "*.csv"));
       debtorsFile = fc.showOpenDialog(null);
      if(debtorsFile!=null){
          txtPathDebtors.setText(debtorsFile.getPath());
      }
    }
}