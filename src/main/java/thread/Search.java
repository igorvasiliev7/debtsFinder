package thread;

import com.opencsv.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.*;
import java.lang.reflect.Array;
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
           ) {
            final CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();
            CSVReader readPayments = new CSVReaderBuilder(brPayment)
                    .withCSVParser(parser)
                    .build();
            CSVWriter csvWriter = new CSVWriter(bw);
            String[] payment;
            String[] debtor;
            int i=0;
            int j = 0;

            while ((payment = readPayments.readNext()) != null) {
                ++i;
                System.out.println(Arrays.toString(payment)+"p"+i);

                BufferedReader brDebts = new BufferedReader(new InputStreamReader(
                        new FileInputStream(debtorsFile), "UTF-8"));
                CSVReader readDebts = new CSVReaderBuilder(brDebts)
                        .withCSVParser(parser)
                        .build();

                list.clear();
                list.add(payment);

                while ((debtor = readDebts.readNext()) != null) {
//                    ++j;
//                    System.out.println(Arrays.toString(debtor)+"d"+j);
                    if (debtor[1].toLowerCase().contains(payment[2].toLowerCase())) {
                        list.add(debtor);
                    }
                }

                if (list.size() >1) {
                    txtResults.appendText(list.get(0)[2] + "\n\n");
                    for (String[] strings : list) {
                      csvWriter.writeNext(strings);
                    }
                }

                brDebts.close();
                readDebts.close();
            }
            System.out.println("The End");
            txtResults.appendText("The end");
            txtError.setText("The end");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in result.csv creating");
        }
    }
}
