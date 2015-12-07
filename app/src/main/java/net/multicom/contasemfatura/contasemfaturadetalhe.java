package net.multicom.contasemfatura;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.multicom.contasemfatura.ContaSemFatura;
import net.multicom.multipagospagamentos.R;
import net.multicom.multipagospagamentos.Utils;

import stone.application.enums.InstalmentTransactionEnum;
import stone.application.enums.TransactionStatusEnum;
import stone.application.enums.TypeOfTransactionEnum;
import stone.application.interfaces.StoneCallbackInterface;
import stone.database.transaction.TransactionDAO;
import stone.providers.TransactionProvider;
import stone.utils.GlobalInformations;
import stone.utils.StoneTransaction;

public class contasemfaturadetalhe extends Activity  implements View.OnClickListener {

    public static final String EXTRA_PARAM_ID = "ID";
    public ContaSemFatura contaSemFatura;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contasemfatura_detalhe);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TransactionDAO dao = new TransactionDAO(this);
        if (!Build.FINGERPRINT.startsWith("generic") ) {

            contaSemFatura = new ContaSemFaturaData().ContaSemFaturaList().get(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));
        }
        else
            contaSemFatura = new ContaSemFaturaData().ContaSemFaturaList().get(getIntent().getIntExtra(EXTRA_PARAM_ID,0));


        ((Button)this.findViewById(R.id.btnEfetuarPagamento)).setOnClickListener(this);

    }
    @Override
    protected void onResume(){
        super.onResume();
        instanceViews();
    }

    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btnEfetuarPagamento:
                EfetuarPagamento(contaSemFatura.Valor);

                break;
        }

    }

    private void instanceViews()
    {
        TextView ucDetalhe = (TextView) this.findViewById(R.id.txt_UC_detalhe);
        TextView nomeCliente = (TextView) this.findViewById(R.id.txt_NomeCliente_detalhe);


        TextView dataVencimento = (TextView) this.findViewById(R.id.txt_DataVencimento_detalhe);
        TextView numeroDocumento = (TextView) this.findViewById(R.id.txt_NumeroDocumento_detalhe);
        TextView boleto = (TextView) this.findViewById(R.id.txt_CodigoBoleto_detalhe);
        TextView valorTransacaoDetalhe = (TextView) this.findViewById(R.id.txt_ValorCSF_Detalhe);

        TextView codigoAutorizacao = (TextView) this.findViewById(R.id.txt_CodigoAutorizacao_detalhe);

        if (ucDetalhe != null){

            valorTransacaoDetalhe.setText("R$ " + contaSemFatura.Valor);
            nomeCliente.setText(contaSemFatura.Cliente);

            dataVencimento.setText(contaSemFatura.DataVencimento);
            numeroDocumento.setText(contaSemFatura.CodigoConta);
            boleto.setText(contaSemFatura.CodigodeBarras);

            codigoAutorizacao.setText(contaSemFatura.CodigoConta);

        }
    }
    public void EfetuarPagamento(String Valor ) {
//TOOD: Testar timeout de pagamento
        // check is there's a pinpad connected
        if (Utils.isConnectedWithPinpad() == true) {

            final StoneTransaction stoneTransaction = new StoneTransaction(GlobalInformations.getPinpadFromListAt(0));
            stoneTransaction.setAmount(Valor); // R$ 0,50
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


                    } else if (transactionProvider.getTransactionStatus() == TransactionStatusEnum.REJECTED) {
                        Toast.makeText(getApplicationContext(), "Sua transação foi rejeitada pelo autorizador", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), transactionProvider.getMessageFromAuthorize().toString(), Toast.LENGTH_SHORT).show();
                    }
                    else if (transactionProvider.getTransactionStatus() == TransactionStatusEnum.TECHNICAL_ERROR)
                        Toast.makeText(getApplicationContext(), "Houve um erro técnico durante o processamento da transação, tente novamente", Toast.LENGTH_LONG).show();
                    else // DECLINED
                        Toast.makeText(getApplicationContext(), "Sua transação foi negada", Toast.LENGTH_LONG).show();


                }


                public void onError() {
                    Toast.makeText(getApplicationContext(), "Houve um erro técnico durante o processamento da transação, tente novamente", Toast.LENGTH_LONG).show();

                }
            });
            transactionProvider.execute();

        } else
            Toast.makeText(getApplicationContext(), "Você não possui conexão com Pinpad para passar transações", Toast.LENGTH_LONG).show();
    }
}
