package net.danielmoliveira.teste1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import stone.application.enums.TransactionStatusEnum;
import stone.application.interfaces.StoneCallbackInterface;
import stone.database.transaction.TransactionDAO;
import stone.database.transaction.TransactionObject;
import stone.providers.CancellationProvider;
import stone.providers.PrintProvider;
import stone.providers.PrintReceiptProvider;
import stone.user.UserModel;
import stone.utils.GlobalInformations;
import stone.utils.PrintObject;

/**
 * Created by JGabrielFreitas on 30/10/15.
 */
public class ListOfTransactionsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ListView listView;
    List<TransactionObject> allTransactions;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // I put the same layout of bluetooth devices because there's only a list view
        setContentView(R.layout.content_bluetooth);

        instanceViews();
    }

    private void instanceViews() {

        listView = (ListView) findViewById(R.id.bluetoothDevices);

        // this DAO is access the transaction base and return to you a List of transactions or a specific transaction
        TransactionDAO transactionDAO = new TransactionDAO(this);

        // get all transactions in base by id DESC
        // if there's no transaction, returns a empty List
        allTransactions = transactionDAO.getAllTransactionsOrderByIdDesc();

        // simple feedback if there's no transaction in base
        if (allTransactions.size() <= 0)
            Toast.makeText(getApplicationContext(), "Sem transações no banco", Toast.LENGTH_LONG).show();

        List<String> transactionsToShow = new ArrayList<>();

        for (TransactionObject currentTransaction : allTransactions) {
            String status = "";
            if ( currentTransaction.getTransactionStatus() == TransactionStatusEnum.APPROVED)
                status = "Aprovada";
            else
                status = "Cancelada";

            transactionsToShow.add(String.format("%s\n%s", currentTransaction.getAmount(), status));
        }

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionsToShow));
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

    }



    // to print (if pinpad is print support)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (GlobalInformations.getPinpadListSize() == 0) {

            Toast.makeText(this,"Nenhum pinpad conectado.",Toast.LENGTH_SHORT);
            return;
        }
        // capture the selected tranaction
        TransactionObject transactionObject = allTransactions.get(position);

        if (transactionObject.getTransactionStatus() == TransactionStatusEnum.APPROVED) {
            UserModel userModel = GlobalInformations.getUserModel(0);

            //TESTE DE IMPRESSAO
            List<PrintObject> listToPrint = new ArrayList<PrintObject>();

            String[] date = transactionObject.getDate().split("-");
            String[] hour = transactionObject.getTime().split(":");
            String dateHour = String.format("%s/%s/%s %s:%s", new Object[]{date[2], date[1], date[0], hour[0], hour[1]});
            String dividerLine = "________________________";
            Double amountAsDouble = Double.valueOf(Double.parseDouble(transactionObject.getAmount()) / 100.0D);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
            listToPrint.add(new PrintObject("Credenciadora Banco PAN", PrintObject.SMALL, PrintObject.CENTER));
            listToPrint.add(new PrintObject("MULTIPAGOS ARRECADAÇÃO DIGITAL", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("Multilink Tecnologia", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("01.104.875/0001-04", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));

            listToPrint.add(new PrintObject("V1.3.11.0 TERM: 101", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject(" ", PrintObject.BIG, PrintObject.CENTER));

            listToPrint.add(new PrintObject("ELEKTRO", PrintObject.BIG, PrintObject.LEFT));
            listToPrint.add(new PrintObject(" ", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("Codigo de Barras:", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("86680000002-2 54091557856-6", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("90057519408-0 10061000000-0", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject(" ", PrintObject.MEDIUM, PrintObject.CENTER));
            if(decimalFormat.format(amountAsDouble).length() == 3) {
                listToPrint.add(new PrintObject("Valor Pago: R$ 0" + decimalFormat.format(amountAsDouble), PrintObject.MEDIUM, PrintObject.LEFT));
            } else {
                listToPrint.add(new PrintObject("Valor Pago: R$ " + decimalFormat.format(amountAsDouble), PrintObject.MEDIUM, PrintObject.LEFT));
            }
            listToPrint.add(new PrintObject("Pagamento  :" + transactionObject.getDate().toString(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("Autorização:"+transactionObject.getAid(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("NSU        :"+transactionObject.getRecipientTransactionIdentification(), PrintObject.MEDIUM, PrintObject.LEFT));

            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("** VIA CLIENTE **", PrintObject.SMALL, PrintObject.CENTER));
            listToPrint.add(new PrintObject("STONE - VIA CLIENTE", PrintObject.SMALL, PrintObject.CENTER));
            listToPrint.add(new PrintObject(String.format("%s - %s", new Object[]{transactionObject.getCardBrand(), "DEBITO A VISTA"}), PrintObject.MEDIUM, PrintObject.CENTER));
            listToPrint.add(new PrintObject(String.format("%s %s ",new Object[]{transactionObject.getCardHolderNumber(),dateHour}), PrintObject.SMALL, PrintObject.LEFT));
            listToPrint.add(new PrintObject("AID: " + transactionObject.getAid(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("ARCQ: " + transactionObject.getArcq(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("Serial: " + transactionObject.getPinpadUsed(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("STONE ID: " + transactionObject.getRecipientTransactionIdentification(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));

            listToPrint.add(new PrintObject(" ", PrintObject.MEDIUM, PrintObject.CENTER));
            listToPrint.add(new PrintObject(" ", PrintObject.MEDIUM, PrintObject.CENTER));

            final PrintProvider printProvider = new PrintProvider(this,listToPrint,GlobalInformations.getPinpadFromListAt(0));
            printProvider.setWorkInBackground(false);
            printProvider.setDialogMessage("Aguarde...");
            printProvider.setDialogTitle("Imprimindo Comprovante");
            printProvider.setConnectionCallback(new StoneCallbackInterface() {
                @Override
                public void onSuccess() {
                    //TODO redirect
                }

                @Override
                public void onError() {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro durante a impressão: " + printProvider.getListOfErrors(), Toast.LENGTH_LONG).show();
                }
            });
            printProvider.execute();

        }
        else
            Toast.makeText(getApplicationContext(), "Transaçao não finalizada com sucesso. Não é possivel imprimir", Toast.LENGTH_LONG).show();


    }

    // to cancel transaction
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // capture the selected tranaction
        TransactionObject transactionObject = allTransactions.get(position);

        final CancellationProvider cancellationProvider = new CancellationProvider(this, transactionObject.getIdFromBase(), GlobalInformations.getUserModel(0));
        cancellationProvider.setWorkInBackground(false);
        cancellationProvider.setDialogMessage("Cancelando transação");
        cancellationProvider.setDialogTitle("Aguarde");
        cancellationProvider.setConnectionCallback(new StoneCallbackInterface() {

            // the request and the response have been successfully performed
            public void onSuccess() {

                // get status of transaction to know if is cancelled,
                // the cancellation can return a declined cancellation, so the transaction stay approved
                if (cancellationProvider.getTransactionStatus() == TransactionStatusEnum.CANCELLED) {
                    Toast.makeText(getApplicationContext(), "Transação cancelada com sucesso!", Toast.LENGTH_LONG).show();
                    finish(); // kill activity
                } else
                    Toast.makeText(getApplicationContext(), "O cancelamento negado", Toast.LENGTH_LONG).show();
            }


            // if occurred a internal error
            public void onError() {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro durante o cancelamento, tente novamente", Toast.LENGTH_LONG).show();
            }
        });
        cancellationProvider.execute();
        return false;
    }
}
