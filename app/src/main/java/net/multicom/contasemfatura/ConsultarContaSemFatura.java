package net.multicom.contasemfatura;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.multicom.WebServiceCall.WebRequest;
import net.multicom.multipagospagamentos.ContaSemFaturaDetalhe2;
import net.multicom.multipagospagamentos.R;
import net.multicom.multipagospagamentos.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConsultarContaSemFatura extends Activity {

    private Menu menu;
    private boolean isListView;

    String Codigo = "";

    /*Novo*/
    private List<ContaSemFatura> contaSemFaturaList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContaSemFaturaAdapter csfAdapter;
    private Button btnSelection;
    private static final String TAG = "ConsultarContaSemFatura";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setSubtitle("Elektro");
        setContentView(R.layout.activity_consultar_conta_sem_fatura);
        isListView = true;

        Button btnConsultar = (Button) findViewById(R.id.btnConsultarCSF);
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Codigo = ((EditText)findViewById(R.id.txtCodigoUnidade)).getText().toString();
                String url = "http://webapiproxymultipagos.azurewebsites.net/api/pdv/ConsultarContasSemFatura?CodigoCliente=" + Codigo + "&pdvID="+Utils.GetPDVID(getApplicationContext());
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                new AysncLoadContaSemFatura().execute(url,Codigo);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btnSelection = (Button) findViewById(R.id.btnEfetuarPagamentoCSF);
        btnSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "";
                List<ContaSemFatura> csfSelectedList = csfAdapter.getList();
                ArrayList<ContaSemFatura> aux = new ArrayList<>();
                for (int i = 0; i < csfSelectedList.size(); i++) {
                    if (csfSelectedList.get(i).getIsSelected()) {
                        data = data + "\n" + csfSelectedList.get(i).getID();
                        aux.add(csfSelectedList.get(i));
                    }
                }

                if (aux.size() == 0)
                    Toast.makeText(ConsultarContaSemFatura.this,"Selecione ao menos uma conta",Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(ConsultarContaSemFatura.this, ContaSemFaturaDetalhe2.class);
                    intent.putExtra(ContaSemFaturaDetalhe.EXTRA_PARAM_ID, -1);
                    intent.putExtra("PDVID", Utils.GetPDVID(ConsultarContaSemFatura.this));
                    intent.putExtra("csfSelected", aux);
                    startActivity(intent);
                }
            }
        });
    }



    /**
     * Async task class to get json by making HTTP call
     */
    private class AysncLoadContaSemFatura extends AsyncTask<String, Void, Integer> {

        // Hashmap for ListView
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ConsultarContaSemFatura.this);
            pDialog.setTitle("Multipagos");
            pDialog.setMessage("Aguarde...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(String... args) {
            Integer result = 0;
            String url  = args[0];
            String CodigoUnidade = args[1];

            try {
                // Creating service handler class instance
                WebRequest webreq = new WebRequest();

                // Making a request to url and getting response
                String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GETRequest);
                Log.d(TAG, "> " + jsonStr);
                //prepareDate();
                contaSemFaturaList.clear();
                ParseJSON(jsonStr,CodigoUnidade);

                result = 1;
            }
            catch(Exception e)
            {
                Log.d(TAG,e.getLocalizedMessage());
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

                //Adcionar a lista de contas sem fatura

                if (recyclerView != null) {


                    csfAdapter = new ContaSemFaturaAdapter(contaSemFaturaList);
                    recyclerView.setAdapter(csfAdapter);
                    btnSelection.setVisibility(View.VISIBLE);

                }

            }
            else //Error
            {
                Toast.makeText(getApplicationContext(), "Falha consultando com sem fatura", Toast.LENGTH_SHORT).show();
                btnSelection.setVisibility(View.INVISIBLE);
            }

        }

    }

    private void ParseJSON(String json,String Codigo) {
        String TAG_codigoFatura = "CodigoFatura";
        String TAG_dataVencimento = "DataVencimento";
        String TAG_valor = "Valor";
        String TAG_codigoBarras = "CodigoBarras";


        if (json != null) {
            try {
                //JSONObject jsonObj = new JSONObject(json);
                // Getting JSON Array node

                JSONArray contaSemFaturaJsonArray = new JSONArray(json);
                // looping through All Students
                for (int i = 0; i < contaSemFaturaJsonArray.length(); i++) {
                    JSONObject c = contaSemFaturaJsonArray.getJSONObject(i);

                    //String id = c.getString(TAG_ID);
                    String codigoFatura = c.getString(TAG_codigoFatura);
                    String dataVencimento = c.getString(TAG_dataVencimento);
                    String valor = c.getString(TAG_valor);
                    String codigoBarras = c.getString(TAG_codigoBarras);
                    Integer _id = c.getInt("ID");


                    // adding student to students list
                    ContaSemFatura csf = new ContaSemFatura();
                    csf.setCodigoConta(codigoFatura);
                    csf.setUnidadeConsumidora(Codigo);
                    csf.setCliente("");
                    csf.setDataVencimento(dataVencimento);
                    csf.setDataPagamento("");
                    csf.setCodigodeBarras(codigoBarras);
                    csf.setValor(valor);
                    csf.setID(_id);
                    contaSemFaturaList.add(csf);
                }



            } catch (JSONException e) {
                e.printStackTrace();

            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");

        }

    }



}
