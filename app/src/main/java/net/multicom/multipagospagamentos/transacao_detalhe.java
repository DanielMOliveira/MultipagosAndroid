package net.multicom.multipagospagamentos;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

import java.util.List;

import stone.application.enums.TransactionStatusEnum;
import stone.application.interfaces.StoneCallbackInterface;
import stone.database.transaction.TransactionObject;
import stone.providers.PrintProvider;
import stone.user.UserModel;
import stone.utils.GlobalInformations;
import stone.utils.PrintObject;

public class transacao_detalhe extends Activity {

    public static final String EXTRA_PARAM_ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacao_detalhe);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
/*
    protected  void Print(){
        if (GlobalInformations.getPinpadListSize() == 0) {

            Toast.makeText(this, "Nenhum pinpad conectado.", Toast.LENGTH_SHORT);
            return;
        }
        // capture the selected tranaction
        TransactionObject transactionObject = allTransactions.get(position);

        if (transactionObject.getTransactionStatus() == TransactionStatusEnum.APPROVED) {
            UserModel userModel = GlobalInformations.getUserModel(0);
            List<PrintObject> listToPrint = new ComprovantePagamento(transactionObject).ComprovantePadrao();


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
    */

}
