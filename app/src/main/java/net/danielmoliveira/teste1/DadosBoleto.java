package net.danielmoliveira.teste1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import stone.application.enums.InstalmentTransactionEnum;
import stone.application.enums.TransactionStatusEnum;
import stone.application.enums.TypeOfTransactionEnum;
import stone.application.interfaces.StoneCallbackInterface;
import stone.providers.TransactionProvider;
import stone.utils.GlobalInformations;
import stone.utils.StoneTransaction;

public class DadosBoleto extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_boleto);

        String codBarras = getIntent().getStringExtra("codBarras");
        String dataVencimento = getIntent().getStringExtra("dataVencimento");
        String valor = getIntent().getStringExtra("valor");

        TextView tvCodBarras = (TextView) findViewById(R.id.txtCodigoBarras_Conf);
        tvCodBarras.setText(codBarras);

        TextView tvDataVencimento = (TextView) findViewById(R.id.txtDataVencimento_Conf);
        tvDataVencimento.setText(dataVencimento);

        TextView tvValor = (TextView) findViewById(R.id.txtValor_conf);
        tvValor.setText(valor);
    }

    public void btnRealizarPagamento_Click(View v) {

        // check is there's a pinpad connected
        if (Utils.isConnectedWithPinpad() == true) {

            StoneTransaction stoneTransaction = new StoneTransaction(GlobalInformations.getPinpadFromListAt(0));
            stoneTransaction.setAmount("50"); // R$ 0,50
            stoneTransaction.setRequestId("123465789"); // ID in portal
            stoneTransaction.setShortName("MULTIPAGOS"); // name that will appears in stratum client
            stoneTransaction.setInstalmentTransactionEnum(InstalmentTransactionEnum.ONE_INSTALMENT); // transaction "a vista"
            stoneTransaction.setTypeOfTransaction(TypeOfTransactionEnum.DEBIT);
            stoneTransaction.setUserModel(GlobalInformations.getUserModel(0));

            // create and pass transaction
            final TransactionProvider transactionProvider = new TransactionProvider(this, stoneTransaction, GlobalInformations.getPinpadFromListAt(0));
            transactionProvider.setWorkInBackground(false);
            transactionProvider.setConnectionCallback(new StoneCallbackInterface() {

                public void onSuccess() {
                    if (transactionProvider.getTransactionStatus() == TransactionStatusEnum.APPROVED) {
                        Toast.makeText(getApplicationContext(), "Sua transação foi efetuada com sucesso", Toast.LENGTH_LONG).show();
                        finish();
                    } else if (transactionProvider.getTransactionStatus() == TransactionStatusEnum.REJECTED)
                        Toast.makeText(getApplicationContext(), "Sua transação foi rejeitada pelo autorizador", Toast.LENGTH_LONG).show();
                    else if (transactionProvider.getTransactionStatus() == TransactionStatusEnum.TECHNICAL_ERROR)
                        Toast.makeText(getApplicationContext(), "Houve um erro técnico durante o processamento da transação, tente novamente", Toast.LENGTH_LONG).show();
                    else // DECLINED
                        Toast.makeText(getApplicationContext(), "Sua transação foi negada", Toast.LENGTH_LONG).show();
                }


                public void onError() {

                }
            });
            transactionProvider.execute();

        } else
            Toast.makeText(getApplicationContext(), "Você não possui conexão com Pinpad para passar transações", Toast.LENGTH_LONG).show();
    }

    public void btnCancelarPagamento_Click(View v) {
        onBackPressed();
    }
}
