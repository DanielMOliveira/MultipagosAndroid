package net.danielmoliveira.teste1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import stone.application.enums.TransactionStatusEnum;
import stone.application.interfaces.StoneCallbackInterface;
import stone.database.transaction.TransactionDAO;
import stone.database.transaction.TransactionObject;
import stone.providers.CancellationProvider;
import stone.providers.PrintReceiptProvider;
import stone.utils.GlobalInformations;

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

        for (TransactionObject currentTransaction : allTransactions)
            transactionsToShow.add(String.format("%s\n%s", currentTransaction.getAmount(), currentTransaction.getTransactionStatus()));

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionsToShow));
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

    }

    // to cancel transaction
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
    }

    // to print (if pinpad is print support)
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        // capture the selected tranaction
        TransactionObject transactionObject = allTransactions.get(position);

        final PrintReceiptProvider printReceiptProvider = new PrintReceiptProvider(this, GlobalInformations.getPinpadFromListAt(0), transactionObject.getIdFromBase(), GlobalInformations.getUserModel(0));
        printReceiptProvider.setDialogMessage("Imprimindo..");
        printReceiptProvider.setWorkInBackground(false);
        printReceiptProvider.setConnectionCallback(new StoneCallbackInterface() {

            public void onSuccess() {}

            public void onError() {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro durante a impressão: " + printReceiptProvider.getListOfErrors(), Toast.LENGTH_LONG).show();
            }
        });
        printReceiptProvider.execute();

        return false;
    }
}
