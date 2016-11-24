package net.multicom.multipagospagamentos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.multicom.WebServiceCall.WebRequest;
import net.multicom.contasemfatura.ContaSemFatura;
import net.multicom.contasemfatura.ContaSemFaturaDetalheAdapter;
import net.multicom.transacao.transacao_detalhe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import stone.application.enums.ErrorsEnum;
import stone.application.enums.InstalmentTransactionEnum;
import stone.application.enums.TransactionStatusEnum;
import stone.application.enums.TypeOfTransactionEnum;
import stone.application.interfaces.StoneCallbackInterface;
import stone.providers.LoadTablesProvider;
import stone.providers.TransactionProvider;
import stone.utils.GlobalInformations;
import stone.utils.StoneTransaction;

public class ContaSemFaturaDetalhe2 extends Activity  implements View.OnClickListener{

    public static final String EXTRA_PARAM_ID = "ID";
    public List<ContaSemFatura> Contas;
    public Integer pdvID;
    private static final String TAG_codigoFatura = "CodigoFatura";
    private static final String TAG_dataVencimento = "DataVencimento";
    private static final String TAG_valor = "Valor";
    private static final String TAG_codigoBarras = "CodigoBarras";
    private String CodigoUnidadeConsumidora;

    private RecyclerView recyclerView;
    private ContaSemFaturaDetalheAdapter csfAdapter;

    String boletoAux = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_sem_fatura_detalhe2);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Integer positionID = getIntent().getIntExtra(EXTRA_PARAM_ID,0);
        Contas = (ArrayList<ContaSemFatura>)getIntent().getSerializableExtra("csfSelected");

        pdvID = Integer.parseInt(Utils.GetPDVID(getApplicationContext()));
        CodigoUnidadeConsumidora = Contas.get(0).getUnidadeConsumidora().toString();

        ((TextView)findViewById(R.id.txt_UC_detalhe)).setText("UC: " + CodigoUnidadeConsumidora);



        ((Button) this.findViewById(R.id.btnEfetuarPagamento)).setOnClickListener(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_detalhe);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //instanceViews();
        String ids = "";
        for (ContaSemFatura csf: Contas) {
            ids += csf.getID() + ";";
        }
        new AsyncConsultarStatusContas().execute(ids, CodigoUnidadeConsumidora, pdvID.toString());

    }

    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btnEfetuarPagamento:
                EfetuarPagamento(SomatorioValores(Contas));

                break;
        }

    }

    private String SomatorioValores(List<ContaSemFatura> contaSemFaturas){
        Double valor = 0.0;
        if (contaSemFaturas.size() > 0){
            for (int i=0;i<contaSemFaturas.size();i++) {
                Double valorDouble = 0.0;
                if (contaSemFaturas.get(i).getStatus().contentEquals("Recebimento Aprovado")) {
                    valorDouble = Double.parseDouble(contaSemFaturas.get(i).getValor().replace(",","").replace("$", ""));
                }
                valor += valorDouble;
            }
        }
        NumberFormat numberFormat = new DecimalFormat("###,##0.00");
        String s = numberFormat.format(valor);

        return s.replace(".", ",");
    }

    private String FormataCodigoBarras(List<ContaSemFatura> contaSemFaturas){
        String CBaux = "";
        if (contaSemFaturas.size() > 0){
            for (int i=0;i<contaSemFaturas.size();i++) {
                if (contaSemFaturas.get(i).getStatus().contentEquals("Recebimento Aprovado")) {
                    CBaux += contaSemFaturas.get(i).getCodigodeBarras() + ";";
                }
            }
        }
        return CBaux;
    }

    private ArrayList<ContaSemFatura> ContasPagas(List<ContaSemFatura> contaSemFaturas){
        ArrayList<ContaSemFatura> result = new ArrayList<ContaSemFatura>();
        if (contaSemFaturas.size() > 0){
            for (int i=0;i<contaSemFaturas.size();i++) {
                if (contaSemFaturas.get(i).getStatus().contentEquals("Recebimento Aprovado")) {
                    result.add(contaSemFaturas.get(i));
                }
            }
        }
        return result;
    }

    public void EfetuarPagamento(final String Valor ) {
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
            transactionProvider.setDialogTitle("Efetuando pagamento...");
            transactionProvider.setConnectionCallback(new StoneCallbackInterface() {

                public void onSuccess() {
                    if (transactionProvider.getTransactionStatus() == TransactionStatusEnum.APPROVED) {
                        Toast.makeText(getApplicationContext(), "Sua transação foi efetuada com sucesso", Toast.LENGTH_LONG).show();
                        new AsyncNotificarPagamento().execute("OK", FormataCodigoBarras(Contas), pdvID.toString());

                        //Todo: Passar as faturas que foram pagas;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("ContasPagas", ContasPagas(Contas));

                        Intent intent = new Intent(ContaSemFaturaDetalhe2.this, transacao_detalhe.class);
                        intent.putExtras(bundle);
                        intent.putExtra("Origem","ContaSemFaturaDetalhe");
                        intent.putExtra("UnidadeConsumidora", CodigoUnidadeConsumidora);
                        startActivity(intent);


                    } else if (transactionProvider.getTransactionStatus() == TransactionStatusEnum.REJECTED) {
                        Toast.makeText(getApplicationContext(), "Sua transação foi rejeitada pelo autorizador", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), transactionProvider.getMessageFromAuthorize().toString(), Toast.LENGTH_SHORT).show();
                        new AsyncNotificarPagamento().execute(transactionProvider.getMessageFromAuthorize().toString(), FormataCodigoBarras(Contas), pdvID.toString());

                    } else if (transactionProvider.getTransactionStatus() == TransactionStatusEnum.TECHNICAL_ERROR) {
                        Toast.makeText(getApplicationContext(), "Houve um erro técnico durante o processamento da transação, tente novamente", Toast.LENGTH_LONG).show();
                        new AsyncNotificarPagamento().execute("Houve um erro técnico durante o processamento da transação, tente novamente", FormataCodigoBarras(Contas), pdvID.toString());

                    } else {// DECLINED
                        Toast.makeText(getApplicationContext(), "Sua transação foi negada", Toast.LENGTH_LONG).show();
                        new AsyncNotificarPagamento().execute("Sua transação foi negada", FormataCodigoBarras(Contas), pdvID.toString());


                    }
                }
                public void onError() {
                    Toast.makeText(getApplicationContext(), "Houve um erro técnico durante o processamento da transação, tente novamente", Toast.LENGTH_LONG).show();
                    if (transactionProvider.theListHasError(ErrorsEnum.NEED_LOAD_TABLES) == true) {
                        LoadTablesProvider loadTablesProvider = new LoadTablesProvider(ContaSemFaturaDetalhe2.this, transactionProvider.getGcrRequestCommand(), GlobalInformations.getPinpadFromListAt(0));
                        loadTablesProvider.setDialogMessage("Subindo as Tabelas");
                        loadTablesProvider.setWorkInBackground(true);
                        loadTablesProvider.setConnectionCallback(new StoneCallbackInterface() {
                            @Override
                            public void onSuccess() { //Caso tenha sucesso efetuo uma rechamada ao metodo
                                EfetuarPagamento(Valor);
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(getApplicationContext(), "sucesso.", Toast.LENGTH_LONG).show();
                            }
                        });
                        loadTablesProvider.execute();
                    }

                }
            });
            transactionProvider.execute();

        } else
            Toast.makeText(getApplicationContext(), "Você não possui conexão com Pinpad para passar transações", Toast.LENGTH_LONG).show();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class AsyncNotificarPagamento extends AsyncTask<String, Void, Integer> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ContaSemFaturaDetalhe2.this);
            pDialog.setTitle("Multipagos");
            pDialog.setMessage("Notificando servidores Multipagos...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(String... args) {

            String status = args[0];
            String codigoBarras = args[1];
            String pdvID = args[2];
            String url = "http://webapiproxymultipagos.azurewebsites.net/api/pdv/ProcessarPagamento?CodigoBarras="+codigoBarras+"&pdvID="+pdvID+"&status="+status;
            Integer result = 0;

            try {
                // Creating service handler class instance
                WebRequest webreq = new WebRequest();
                // Making a request to url and getting response
                String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GETRequest);
                if (jsonStr.contains("Documento não autorizado a efetuar o pagamento")) {
                    result = 2;
                    return result;
                }
                result = 1;


            }
            catch(Exception e)
            {
                Log.d("FinalizarPagamento", e.getLocalizedMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (result == 1){


            }
            else //Error
                Toast.makeText(ContaSemFaturaDetalhe2.this, "Falha consultando conta sem fatura", Toast.LENGTH_SHORT).show();

        }

    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class AsyncConsultarStatusContas extends AsyncTask<String, Void, Integer> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ContaSemFaturaDetalhe2.this);
            pDialog.setTitle("Multipagos");
            pDialog.setMessage("Carregando Informações das contas...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(String... args) {

            String positions = args[0];
            String unidadeConsumidora = args[1];
            String pdvID = args[2];
            String url = "http://webapiproxymultipagos.azurewebsites.net/api/pdv/ExibirContaSemFatura?contasemFaturaid="+positions+"&CodigoCliente="+unidadeConsumidora+"&pdvID="+pdvID;
            //url = "http://localhost:63315/api/pdv/ExibirContaSemFatura?contasemFaturaid="+positions+"&CodigoCliente="+unidadeConsumidora+"&pdvID="+pdvID;
            Integer result = 0;

            try {
                // Creating service handler class instance
                WebRequest webreq = new WebRequest();
                // Making a request to url and getting response
                String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GETRequest);
                if (jsonStr.contains("Documento não autorizado a efetuar o pagamento")) {
                    result = 2;
                    return result;
                }

                ParseJSON(jsonStr);
                result = 1;
            }
            catch(Exception e)
            {
                Log.d("ConsultaCSF",e.getLocalizedMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (result == 1){
                //TOOD: Preencher campos na tela e no comprovante
                if (recyclerView != null) {
                    csfAdapter = new ContaSemFaturaDetalheAdapter(Contas);
                    recyclerView.setAdapter(csfAdapter);
                    ((TextView)findViewById(R.id.txt_NomeCliente_detalhe)).setText("Total a pagar: R$ " + SomatorioValores(Contas));
                }
            }
            if (result == 2)
                Toast.makeText(ContaSemFaturaDetalhe2.this, "Tipo de documento não autorizado.Boleto", Toast.LENGTH_SHORT).show();
            else //Error
              Toast.makeText(ContaSemFaturaDetalhe2.this, "Falha consultando conta sem fatura - Timeout", Toast.LENGTH_SHORT).show();

        }
        private void ParseJSON(String json) {
            if (json != null) {

                try {
                    //JSONObject jsonObj = new JSONObject(json);
                    // Getting JSON Array node
                    Contas = new ArrayList<>();
                    JSONArray contaSemFatura = new JSONArray(json);
                    // looping through All Students
                    for (int i = 0; i < contaSemFatura.length(); i++) {
                        JSONObject c = contaSemFatura.getJSONObject(i);

                        //String id = c.getString(TAG_ID);
                        String codigoFatura = c.getString(TAG_codigoFatura);
                        String dataVencimento = c.getString(TAG_dataVencimento);
                        String valor = c.getString(TAG_valor);
                        String codigoBarras = c.getString(TAG_codigoBarras);
                        String statusCodigoBarras = c.getString("StatusCodigoBarras");
                        Integer _id = c.getInt("ID");


                        // adding student to students list
                        ContaSemFatura csf = new ContaSemFatura();
                        csf.setCodigoConta(codigoFatura);
                        csf.setUnidadeConsumidora(CodigoUnidadeConsumidora);
                        csf.setCliente("");
                        csf.setDataVencimento(dataVencimento);
                        csf.setDataPagamento("");
                        csf.setCodigodeBarras(codigoBarras);
                        csf.setValor(valor);
                        csf.setStatus(statusCodigoBarras);
                        csf.setID(_id);
                        Contas.add(csf);
                    }

                }

                catch (JSONException e) {
                    if (e.getMessage().contains("cannot be converted")){
                        return;

                    }
                    else {
                        e.printStackTrace();
                        Toast.makeText(ContaSemFaturaDetalhe2.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }
        }

    }
}
