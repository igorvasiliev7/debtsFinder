package thread;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Search implements Runnable {
    @FXML
    private Text txtError;
    @FXML
    private TextArea txtResults;

    private File debtorsFile;
    private File paymentsFile;

    private String payment;
    private String debtor;
    private String paymentsShort = "";
    private String debtorShort = "";
    private List<String> list = new LinkedList<>();
    private String[] debtsArr;
    private String[] paymentsArr;

    public Search(Text txtError, TextArea txtResults, File debtorsFile, File paymentsFile) {
        this.txtError = txtError;
        this.txtResults = txtResults;
        this.debtorsFile = debtorsFile;
        this.paymentsFile = paymentsFile;
    }

    @Override
    public void run() {
        if (paymentsFile == null || debtorsFile == null) {
            txtError.setText("One of lists is not chosen");
            return;
        } else txtError.setText("Searching..."); //Write it at the end of search() method

        BufferedReader readDebts;
        BufferedReader readPayments = reader(paymentsFile);
        if (readPayments == null) {
            System.out.println("Bed payments file, choose again");
            return;
        }

        try {
            BufferedWriter result = writer();

            while ((payment = readPayments.readLine()) != null) {
                readDebts = reader(debtorsFile);
                if (readDebts == null) {
                    System.out.println("Bed debtors file, choose again");
                    return;
                }
                list.clear();
                payment = payment.toLowerCase();

                while ((debtor = readDebts.readLine()) != null) {
                    debtor = debtor.toLowerCase();
                    paymentsArr = payment.split(";");
                    debtsArr = debtor.split(";");

                    if (paymentsArr.length > 3 && debtsArr.length > 2) {
                        paymentsShort = paymentsArr[2];
                        debtorShort = debtsArr[1];
                    } else continue;
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
        } catch (IOException e) {
            System.out.println("Error in result.txt creating");
        }
    }

    private BufferedWriter writer() throws FileNotFoundException {
        return new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("d://result22.txt")));
    }

    private BufferedReader reader(File file) {
        try {
            return new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "Cp1251"));
        } catch (FileNotFoundException | UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
