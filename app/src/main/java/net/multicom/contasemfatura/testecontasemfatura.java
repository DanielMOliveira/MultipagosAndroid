package net.multicom.contasemfatura;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import net.multicom.WebServiceCall.WebRequest;
import net.multicom.multipagospagamentos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Daniel on 20/01/2016.
 */
public class testecontasemfatura extends ListActivity implements View.OnClickListener {

    // URL to get contacts JSON


    // JSON Node names
    // JSON Node names
    private static final String TAG_ContaSemFaturaArray = "";
    private static final String TAG_ID = "id";
    private static final String TAG_codigoFatura = "codigoFatura";
    private static final String TAG_dataVencimento = "dataVencimento";
    private static final String TAG_valor = "valor";
    private static final String TAG_codigoBarras = "codigoBarras";
    String Codigo = "";
    private String url = "http://webapiproxymultipagos.azurewebsites.net/api/pdv/ConsultarContasSemFatura?CodigoCliente="+Codigo+"&pdvID=99";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_testecontasemfatura);
        ((Button)findViewById(R.id.btnConsultarCSF_Teste)).setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnConsultarCSF_Teste:
                Codigo = ((TextView)findViewById(R.id.txtCodigoUnidade2)).getText().toString();
                url = "http://webapiproxymultipagos.azurewebsites.net/api/pdv/ConsultarContasSemFatura?CodigoCliente="+Codigo+"&pdvID=99";
                new GetStudents().execute();
                break;


        }
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetStudents extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> studentList;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(testecontasemfatura.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GETRequest);

            Log.d("Response: ", "> " + jsonStr);

            studentList = ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    testecontasemfatura.this,
                    studentList,
                    R.layout.list_item_contasemfatura,
                    new String[]{TAG_codigoFatura, TAG_dataVencimento,TAG_valor},
                    new int[]{R.id.CodigoConta,R.id.Vencimento, R.id.ValorDevido}
            );


            setListAdapter(adapter);

            getListView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(testecontasemfatura.this,"Clicado na classe filha",Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

                //JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray students = new JSONArray(json);


                // looping through All Students
                for (int i = 0; i < students.length(); i++) {
                    JSONObject c = students.getJSONObject(i);

                    //String id = c.getString(TAG_ID);
                    String codigoFatura = c.getString(TAG_codigoFatura);
                    String dataVencimento = c.getString(TAG_dataVencimento);
                    String valor = c.getString(TAG_valor);
                    String codigoBarras = c.getString(TAG_codigoBarras);

                    // tmp hashmap for single student
                    HashMap<String, String> student = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    //student.put(TAG_ID, id);
                    student.put(TAG_codigoFatura, codigoFatura);
                    student.put(TAG_dataVencimento, dataVencimento);
                    student.put(TAG_valor, valor);
                    student.put(TAG_codigoBarras, codigoBarras);

                    // adding student to students list
                    studentList.add(student);
                }
                return studentList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }

}
