package net.danielmoliveira.teste1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;

public class ListarContasEmAberto extends AppCompatActivity {

    String TAG = "Response";
    String getCel;
    SoapPrimitive resultString ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contas_em_aberto);

        //TextView textView = (TextView)findViewById(R.id.txtTituloContasEmAberto);
        //
        // textView.append(codigo);
        getCel = getIntent().getStringExtra("codigo");
        AsyncCallWS task = new AsyncCallWS();
        task.execute();
    }

    public void calculateTesteElektro(){

        String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
        String METHOD_NAME = "CelsiusToFahrenheit";
        String NAMESPACE = "http://www.w3schools.com/webservices/";
        String URL = "http://www.w3schools.com/webservices/tempconvert.asmx?op=CelsiusToFahrenheit";

        try{
            SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);
            Request.addProperty("Celsius", getCel);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            transportSE.call(SOAP_ACTION,soapEnvelope);
            resultString = (SoapPrimitive)soapEnvelope.getResponse();

            Log.i(TAG,"Result Celsius: " + resultString);
        }
        catch (Exception ex) {
            Log.e(TAG,ex.getMessage());
        }
    }
    public void calculate(){

        String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
        String METHOD_NAME = "CelsiusToFahrenheit";
        String NAMESPACE = "http://www.w3schools.com/webservices/";
        String URL = "http://www.w3schools.com/webservices/tempconvert.asmx?op=CelsiusToFahrenheit";

        try{
            SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);
            Request.addProperty("Celsius", getCel);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            transportSE.call(SOAP_ACTION,soapEnvelope);
            resultString = (SoapPrimitive)soapEnvelope.getResponse();

            Log.i(TAG,"Result Celsius: " + resultString);
        }
        catch (Exception ex) {
            Log.e(TAG,ex.getMessage());
        }
    }
    private class AsyncCallWS extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            Log.i(TAG, "doInBackground");
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Log.i(TAG, "doInBackground");
            calculate();
            return null;
        }

        @Override
        protected void onPostExecute (Void result){
            Log.i(TAG,"onPostExecute");
            Toast.makeText(getApplicationContext(),"Response" + resultString.toString(),Toast.LENGTH_LONG).show();

            //Alterando o valor apos a execução do thread
            TextView txtCodigo = (TextView)findViewById(R.id.txtCodigoInstalacao);
            txtCodigo.setText("# " + resultString.toString());
        }
    }

    public void btnPagar_Click(View v)
    {
        Intent i = new Intent(this,DadosBoleto.class);
        i.putExtra("codBarras","");
        i.putExtra("dataVencimento","");
        i.putExtra("valor","");


        startActivity(i);
    }




}
