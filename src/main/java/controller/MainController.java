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

    File debtorsFile;
    File paymentsFile;

    public void initialize(URL location, ResourceBundle resources) {
        btnSearch.setOnAction(e-> search());
        btnPathDebtors.setOnAction(event -> chooseDebtors());
        btnPathPayment.setOnAction(event -> choosePayments());
    }

    private void choosePayments() {
        FileChooser fc=new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Csv files", "*.csv"));
        debtorsFile = fc.showOpenDialog(null);
        txtPathPayments.setText(debtorsFile.getPath());
    }

    private void chooseDebtors() {
        FileChooser fc=new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Csv files", "*.csv"));
        paymentsFile = fc.showOpenDialog(null);
        txtPathDebtors.setText(paymentsFile.getPath());
    }

    private BufferedReader reader(File file){
        try {
            return new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "Cp1251"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    private void search() {
        String payment;
        String debtor;
        String paymentsShort;
        String debtorShort;
        List<String> list;

        if(paymentsFile==null||debtorsFile==null){
            txtError.setText("Payment list is not chosen");
            return;
        }
        BufferedReader readPayments = reader(paymentsFile);
        BufferedReader readDebts = reader(debtorsFile);

        BufferedWriter result = null;
        try {
            result = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream("/results.txt"), "Cp1251"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
        while ((payment = readPayments.readLine()) != null) {
            list = new ArrayList<>();
            payment = payment.toLowerCase();
            while ((debtor = readDebts.readLine()) != null) {
                debtor = debtor.toLowerCase();
                paymentsShort = payment.split(";")[1];
                debtorShort = debtor.split(";")[1];
                if (debtorShort.contains(paymentsShort)) list.add(debtor);
        }
            if (list.size() != 0) {
             txtResults.setText("Client: \n" + payment + "\nDebtors: ");
             result.write("Client: " + payment + "\nDebtors: \n");
                for (int i = 0; i < list.size(); i++) {
                    txtResults.setText(list.get(i) + "\n\n");
                    result.write(list.get(i) + "\n\n");
                }
             }
        }
        readDebts.close();
        readPayments.close();
        result.close();}
        catch (IOException e){

        }
    }
}
