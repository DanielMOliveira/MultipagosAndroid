package net.danielmoliveira.teste1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ContaSemFatura.ContaSemFatura;

public class ListarContasEmAberto extends AppCompatActivity {

    String TAG = "Response";
    String getCel;
    SoapPrimitive resultString ;
    ArrayList<ContaSemFatura> contaSemFaturaList= new ArrayList<ContaSemFatura>(4);

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
            //txtCodigo.setText("# " + resultString.toString());
            txtCodigo.setText("# " + getCel);


            for (int i=0;i<4;i++){

                contaSemFaturaList.add(new ContaSemFatura(i,"0000.0000.0000.0000.0000",
                        "dd/MM/yyyy",
                        resultString.toString()));

            }
            MontaCampos(contaSemFaturaList);


        }
    }

    public void btnPagar_Click(View v)
    {
        Intent i = new Intent(this,DadosBoleto.class);
        i.putExtra("codBarras","8456.58844.48484.5555");
        i.putExtra("dataVencimento","22/12/2015");
        i.putExtra("valor","R$ 325,66");


        startActivity(i);
    }

    private void MontaCampos(List<ContaSemFatura> ContasSemFaturas)
    {
        TableLayout TableLayoutContas = (TableLayout)findViewById(R.id.TableLayoutContas);
        for (int i =0;i<contaSemFaturaList.size();i++)
        {
            TableRow tr = new TableRow(this);
            TextView tv_Valor = new TextView(this);
            tv_Valor.setText("Valor" + i);
            tr.addView(tv_Valor);

            TextView tv_Data = new TextView(this);
            tv_Data.setText("Data" + i);
            tr.addView(tv_Data);

            Button btn_Pagar = new Button(this);
            btn_Pagar.setText("Pagar");
            btn_Pagar.setId(i);
            tr.addView(btn_Pagar);

            TableLayoutContas.addView(tr);
        }

        }

}
