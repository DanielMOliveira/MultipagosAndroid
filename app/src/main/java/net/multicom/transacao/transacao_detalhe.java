package net.multicom.transacao;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.multicom.multipagospagamentos.ComprovantePagamento;
import net.multicom.multipagospagamentos.R;

import java.util.List;

import stone.application.interfaces.StoneCallbackInterface;
import stone.database.transaction.TransactionDAO;
import stone.providers.PrintProvider;
import stone.user.UserModel;
import stone.utils.GlobalInformations;
import stone.utils.PrintObject;

public class transacao_detalhe extends Activity implements View.OnClickListener {

    public static final String EXTRA_PARAM_ID = "ID";
    public Transacao transacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacao_detalhe);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TransactionDAO dao = new TransactionDAO(this);
        if (!Build.FINGERPRINT.startsWith("generic") ) {

            transacao = new TransacaoData().transacaoList(dao).get(getIntent().getIntExtra(EXTRA_PARAM_ID,0));
        }
        else
            transacao = new TransacaoData().transacaoList().get(getIntent().getIntExtra(EXTRA_PARAM_ID,0));


        ((Button)this.findViewById(R.id.btnImprimirDetalhe)).setOnClickListener(this);

    }
    @Override
    protected void onResume(){
        super.onResume();
        instanceViews();
   }

    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btnImprimirDetalhe:
                Print(transacao);
                break;
        }

    }

    private void instanceViews()
    {
        TextView statusTransacao = (TextView) this.findViewById(R.id.txt_StatusTransacao_detalhe);
        TextView dataTransacao = (TextView) this.findViewById(R.id.txt_DataTransacao_detalhe);
        TextView horaTransacao = (TextView) this.findViewById(R.id.txt_HoraTransacao_detalhe);

        TextView valorTransacaoDetalhe = (TextView) this.findViewById(R.id.txt_ValorTransacao_detalhe);
        TextView codigoAutorizacao = (TextView) this.findViewById(R.id.txt_CodigoAutorizacao_detalhe);
        TextView tipoTransacaoDetalhe = (TextView) this.findViewById(R.id.txt_TipoTransacao_detalhe);

        TextView nomeCartao = (TextView) this.findViewById(R.id.txt_NomeCartao_detalhe);
        TextView numeroCartao = (TextView) this.findViewById(R.id.txt_NumeroCarto_detalhe);

        if (transacao != null){

            valorTransacaoDetalhe.setText("R$ " + transacao.Valor);
            tipoTransacaoDetalhe.setText(transacao.cardBrand.toString() + " - " + transacao.Tipo );

            statusTransacao.setText(transacao.Status);
            dataTransacao.setText(transacao.Data);
            horaTransacao.setText(transacao.Hora);

            codigoAutorizacao.setText("Autorização: " + transacao.authorizationCode);
            if (transacao.authorizationCode == null) {
                ((LinearLayout) this.findViewById(R.id.linearLayoutAutorizacao)).setVisibility(View.INVISIBLE);
                ((Button) this.findViewById(R.id.btnImprimirDetalhe)).setVisibility(View.INVISIBLE);
            }

            nomeCartao.setText(transacao.cardHolderName);
            numeroCartao.setText(transacao.cardHolderNumber);
        }
    }

    protected  void Print(Transacao transacao){

        if (GlobalInformations.getPinpadListSize() == 0) {
            Toast.makeText(this, "Nenhum pinpad conectado.", Toast.LENGTH_SHORT);
            return;
        }

        if (transacao.Status == "Aprovada") {
            UserModel userModel = GlobalInformations.getUserModel(0);
            List<PrintObject> listToPrint = new ComprovantePagamento(transacao).ComprovantePadrao();

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

}
