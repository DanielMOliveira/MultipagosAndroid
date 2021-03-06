package net.multicom.multipagospagamentos;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;

import stone.utils.GlobalInformations;

/**
 * Created by JGabrielFreitas on 30/10/15.
 */
public class Utils {

    public static boolean isConnectedWithPinpad() {
        return GlobalInformations.getPinpadListSize() != null && GlobalInformations.getPinpadListSize() > 0;
    }

 /*Metodo para limpar o cache da aplicação*/
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /*Metodo para limpar o diretorio caso exista*/
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static boolean verificaConexao(ConnectivityManager manager) {
        boolean conectado;
        //ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager conectivtyManager = manager;
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    public static boolean verificaBluetooth(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                return false;
            }
        }
        return true;
    }

    public static String GetPDVID (Context context){
        String pdvID = PreferenceManager.getDefaultSharedPreferences(context).getString("pdv_id","00");
        return pdvID;
    }

    public static boolean ValidaConfiguracao(Boolean possuiinternet, Boolean possuiBluettoh,Integer pdvID,Context context) {
        String data = "";
        if (!possuiBluettoh)
            data = data + "\n" + "Sem conexão bluetooth";
        if (!possuiinternet)
            data = data + "\n" + "Sem conexão a internet";

        if (pdvID == 99 || pdvID == 0)
            data = data +"\n" + "Numero de PDV invalido.";

        if (data != "") {
            Toast.makeText(context, "A aplicação apresentou o(s) seguinte(s) problema(s):" + data, Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }

}