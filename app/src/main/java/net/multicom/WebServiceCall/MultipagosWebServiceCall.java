package net.multicom.WebServiceCall;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsTransportSE;

/**
 * Created by Daniel on 08/12/2015.
 */
public class MultipagosWebServiceCall extends AsyncTask<String,Void,String>
{
    String namespace = "http://www.ccsdiscovery.com.br/MultiPagos/IPontoDeVenda/";
    private String url = "https://homolog-wspdvmultipagos.azurewebsites.net:443/PontoDeVendaService.svc";
    String SOAP_ACTION;
    SoapObject request = null, objMessages = null;
    SoapSerializationEnvelope envelope;
    HttpsTransportSE androidHttpTransport;

    public MultipagosWebServiceCall() {
    }

    @Override
    protected String doInBackground(String... params) {
       return this.NotificacaoOnline(params[0]);
    }

    protected void SetEnvelope(){
        try{
            //Criando envelope soap
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            androidHttpTransport = new HttpsTransportSE("homolog-wspdvmultipagos.azurewebsites.net",443,"/PontoDeVendaService.svc",600);
            androidHttpTransport.debug = true;
        }
        catch (Exception e) {
            Log.e("Soap Exception",e.toString());
            System.out.println(("exception ------->>>" + e.toString()));
        }
    }

    public String NotificacaoOnline(String posID){
        try {
            String MethodName = "OnlineNotification";
            SOAP_ACTION = namespace + MethodName;
            request = new SoapObject("http://tempuri.org/",MethodName);

            PropertyInfo posIDProp = new PropertyInfo();
            posIDProp.setName("posID");
            posIDProp.setValue(posID);
            posIDProp.setType(int.class);
            request.addProperty(posIDProp);
            
            SetEnvelope();
            
            try{
                androidHttpTransport.call(SOAP_ACTION,envelope);
                String result = envelope.getResponse().toString();
                
                return result;
            }
            catch (Exception e ){return e.toString();}

        }
        catch (Exception e ){return e.toString();}
    }
}

