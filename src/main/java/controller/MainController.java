package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import thread.Search;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
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
        btnSearch.setOnAction(e -> new Thread(new Search(txtError, txtResults, debtorsFile, paymentsFile)).start());
        btnPathDebtors.setOnAction(event -> chooseDebtors());
        btnPathPayment.setOnAction(event -> choosePayments());
    }

    private void choosePayments() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Csv files", "*.csv"));
        paymentsFile = fc.showOpenDialog(null);
        if (paymentsFile != null) {
            txtPathPayments.setText(paymentsFile.getPath());
        }
    }

    private void chooseDebtors() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Csv files", "*.csv"));
        debtorsFile = fc.showOpenDialog(null);
        if (debtorsFile != null) {
            txtPathDebtors.setText(debtorsFile.getPath());
        }
    }
}