package thread;

import com.opencsv.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Search implements Runnable {
    @FXML
    private Text txtError;
    @FXML
    private TextArea txtResults;

    private File debtorsFile;
    private File paymentsFile;

    private String[] payment;
    private String[] debtor;
    private String paymentsShort = "";
    private String debtorShort = "";
    private List<String[]> list = new LinkedList<>();

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

        try(BufferedWriter bw = Files.newBufferedWriter(Paths.get("result.csv"), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            BufferedReader brPayment = new BufferedReader(new InputStreamReader(
                    new FileInputStream(paymentsFile), "UTF-8"));
            BufferedReader brDebts = new BufferedReader(new InputStreamReader(
                    new FileInputStream(debtorsFile), "UTF-8"));) {

            final CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();
            CSVReader readPayments = new CSVReaderBuilder(brPayment)
                    .withCSVParser(parser)
                    .build();
            CSVWriter csvWriter = new CSVWriter(bw);

            while ((payment = readPayments.readNext()) != null) {
                CSVReader readDebts = new CSVReaderBuilder(brDebts)
                        .withCSVParser(parser)
                        .build();
                list.clear();

                while ((debtor = readDebts.readNext()) != null) {
                        paymentsShort = payment[2].toLowerCase();
                        debtorShort = debtor[1].toLowerCase();

                    if (debtorShort.contains(paymentsShort)) {
                        list.add(debtor);
                    }
                }

                if (list.size() != 0) {
                    for (String[] strings : list) {
                        txtResults.appendText(strings[1] + "\n\n");
                        csvWriter.writeNext(strings);
                    }
                }
            }
            System.out.println("The End");
            txtResults.appendText("The end");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in result.txt creating");
        }
    }
}
