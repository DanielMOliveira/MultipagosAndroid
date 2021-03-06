package net.multicom.contasemfatura;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import net.multicom.WebServiceCall.WebRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by Daniel on 04/12/2015.
 */
public class ContaSemFaturaData {

    // JSON Node names
    private final String TAG_ContaSemFaturaArray = "";
    private final String TAG_ID = "id";
    private final String TAG_codigoFatura = "codigoFatura";
    private final String TAG_dataVencimento = "dataVencimento";
    private final String TAG_valor = "valor";
    private final String TAG_codigoBarras = "codigoBarras";
    String  CodigoCliente;
    String  url;
    Context mContext;
    String jsonStr;
    ArrayList<ContaSemFatura> list = new ArrayList<ContaSemFatura>();

    public ContaSemFaturaData(Context context,String CodigoCliente){
        this.mContext= context;
        url = "http://webapiproxymultipagos.azurewebsites.net/api/pdv/ConsultarContasSemFatura?CodigoCliente="+CodigoCliente+"&pdvID=99";
        // Creating service handler class instance
        this.CodigoCliente = CodigoCliente;
        new GetStudents().execute();
    }
    public ArrayList<ContaSemFatura> ContaSemFaturaListWS(){



        return (list);


    }
    public ArrayList<ContaSemFatura> ContaSemFaturaList() {
        ArrayList<ContaSemFatura> list = new ArrayList<ContaSemFatura>();
        for (int i = 0; i < 25; i++) {
            Random rnd = new Random();
            int numero = rnd.nextInt(30);
            Date d = new Date();
            SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");

            ContaSemFatura csf = new ContaSemFatura();
            csf.setCodigoConta("0000" + numero);
            csf.setUnidadeConsumidora(numero + "0000" + numero * 2);
            csf.setCliente("Cliente Jose das couves");
            csf.setDataVencimento(data.format(d));
            csf.setDataPagamento("");
            csf.setCodigodeBarras("00000.00000.00000.000000.00000");
            csf.setValor(String.valueOf((numero * numero) * 30 / 60 * 15 / 100));
            list.add(csf);
        }
        return (list);
    }

    private class GetStudents extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> studentList;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            jsonStr = webreq.makeWebServiceCall(url, WebRequest.GETRequest);

            Log.d("Response: ", "> " + jsonStr);



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            studentList = ParseJSON(jsonStr);

            if (pDialog.isShowing())
                pDialog.dismiss();

            Log.d("debug","fechando caixa de dialogo");
            /**
             * Updating parsed JSON data into ListView
             * */


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
                    ContaSemFatura csf = new ContaSemFatura();
                    csf.setCodigoConta(codigoFatura);
                    csf.setUnidadeConsumidora(CodigoCliente);
                    csf.setCliente("");
                    csf.setDataVencimento(dataVencimento);
                    csf.setDataPagamento("");
                    csf.setCodigodeBarras(codigoBarras);
                    csf.setValor(valor);
                    list.add(csf);
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
