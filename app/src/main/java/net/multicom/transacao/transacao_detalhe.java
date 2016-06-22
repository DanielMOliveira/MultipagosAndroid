package net.multicom.transacao;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.multicom.contasemfatura.ContaSemFatura;
import net.multicom.multipagospagamentos.ComprovantePagamento;
import net.multicom.multipagospagamentos.R;

import java.util.ArrayList;
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
    public String CodigoBarras = "";
    public String UnidadeConsumidora = "";
    ArrayList<ContaSemFatura> ContasPagas;
    GerenciadorTransacao gerenciadorTransacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacao_detalhe);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        gerenciadorTransacao = new GerenciadorTransacao(this);
        TransactionDAO dao = new TransactionDAO(this);
        if (!Build.FINGERPRINT.startsWith("generic") ) {

            transacao = new TransacaoData().transacaoList(dao).get(getIntent().getIntExtra(EXTRA_PARAM_ID,0));
        }
        else
            transacao = new TransacaoData().transacaoList().get(getIntent().getIntExtra(EXTRA_PARAM_ID,0));


        String origem = "";
        try{
            origem = getIntent().getStringExtra("Origem");
        }
        catch (Exception ex){}
        if (origem.contains("ContaSemFaturaDetalhe")) {
            // Get the Bundle Object
            Bundle bundleObject = getIntent().getExtras();

            // Get ArrayList Bundle
            ContasPagas = (ArrayList<ContaSemFatura>) bundleObject.getSerializable("ContasPagas");
            for (int i = 0; i < ContasPagas.size(); i++) {
                CodigoBarras = CodigoBarras + ContasPagas.get(i).getCodigodeBarras() + "\n";
            }

            UnidadeConsumidora = getIntent().getStringExtra("UnidadeConsumidora");

            transacao.CodigoBarras = CodigoBarras;
            transacao.UnidadeConsumidora = UnidadeConsumidora;

            transacao.listToPrint = new ComprovantePagamento(transacao).ComprovantePadrao();

            gerenciadorTransacao.InsertTransacao(transacao.Tipo,
                    transacao.Data,
                    transacao.Hora,
                    transacao.Cartao,
                    transacao.Status,
                    transacao.GetValor(),
                    transacao.timeToPassTransaction,
                    transacao.cardHolderNumber,
                    transacao.cardHolderName,
                    transacao.authorizationCode,
                    transacao.pinpadUsed,
                    transacao.cardBrand.name(),
                    transacao.RecipientTransactionIdentification,
                    transacao.GetCodigoBarras(),
                    transacao.GetUnidadeConsumidora(),
                    transacao.GetComprovante());
        }
        else{ //Caso venha da listagem de transacao
            transacao = (Transacao)getIntent().getSerializableExtra("transacao");
            UnidadeConsumidora = transacao.GetUnidadeConsumidora();
            transacao.listToPrint = new ComprovantePagamento(transacao).ComprovantePadrao();
        }

        ((Button)this.findViewById(R.id.btnImprimirDetalhe)).setOnClickListener(this);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        gerenciadorTransacao.close();
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

        TextView txtCodigoBarras = (TextView)this.findViewById(R.id.txt_CodigoBarras_detalhe);

        if (transacao != null){

            valorTransacaoDetalhe.setText("R$ " + transacao.GetValor());
            try {
                tipoTransacaoDetalhe.setText(transacao.cardBrand.toString() + " - " + transacao.Tipo);
            }
            catch (Exception ex) {
                tipoTransacaoDetalhe.setText(transacao.CardBrandName + " - " + transacao.Tipo);
            }

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
            txtCodigoBarras.setText(CodigoBarras);
        }
    }

    public  void Print(Transacao transacao){

        if (GlobalInformations.getPinpadListSize() == 0) {
            Toast.makeText(this, "Nenhum pinpad conectado.", Toast.LENGTH_SHORT);
            return;
        }

        if (transacao.Status.contains("Aprovada")) {
            UserModel userModel = GlobalInformations.getUserModel(0);
            List<PrintObject> listToPrint = transacao.listToPrint;

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
