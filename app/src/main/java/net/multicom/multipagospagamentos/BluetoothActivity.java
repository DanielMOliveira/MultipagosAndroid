package net.multicom.multipagospagamentos;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import stone.application.interfaces.StoneCallbackInterface;
import stone.providers.BluetoothConnectionProvider;
import stone.utils.PinpadObject;

public class BluetoothActivity extends Activity implements AdapterView.OnItemClickListener {

    ListView bluetoothDevices;
    BluetoothAdapter mBluetoothAdapter;
    List<BluetoothDevice> bondedDevices;
    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;

    //TODO: MElhorar esta parte no futuro
    public int pairedStringPosition = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bluetooth);
        bondedDevices = new ArrayList<>();

        instanceViews();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void instanceViews() {
        bluetoothDevices = (ListView) findViewById(R.id.bluetoothDevices);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth não ativado", Toast.LENGTH_SHORT).show();
        }
        if (!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,ENABLE_BLUETOOTH);
        }
        else{ListaDevices();}
    }

    private void ListaDevices() {
        /**
         * I create it for show only the names
         * all informations are captured on item click
         * see below
         * */


        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        bondedDevices.addAll(pairedDevices);

        List<String> devicesName = new ArrayList<>();
        for(BluetoothDevice bt : pairedDevices)
        {
            devicesName.add(bt.getName() + "\n" + getDeviceTypeFromName(bt.getName()));
        }

        /* O metodo abaixo serveria para customizar a lista.
        Vale tratar com mais calma no futuro
        bluetoothDevices.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devicesName) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                CheckedTextView textView = (CheckedTextView) super.getView(position, convertView, parent);

                if (position == pairedStringPosition)
                    textView.setChecked(true);

                return textView;
            }
        });
        */
        bluetoothDevices.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devicesName));
        bluetoothDevices.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == ENABLE_BLUETOOTH) {
            if(resultCode == RESULT_OK) {

                Toast.makeText(this,"Bluetooth ativado",Toast.LENGTH_SHORT).show();
                ListaDevices();
            }
            else {
                Toast.makeText(this,"Sem Bluetooh não é possivel continuar",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        // get select device in ListView
        BluetoothDevice bluetoothDevice = bondedDevices.get(position);

        // create the Pinpad to create connection
        PinpadObject pinpadObject = new PinpadObject();
        pinpadObject.setName(bluetoothDevice.getName());
        pinpadObject.setMac_address(bluetoothDevice.getAddress());


        // call bluetooth connection provider to create a connection
        final BluetoothConnectionProvider bluetoothConnectionProvider = new BluetoothConnectionProvider(this, pinpadObject);
        bluetoothConnectionProvider.setWorkInBackground(false); // don't work in background
        bluetoothConnectionProvider.setDialogMessage("Criando conexão");
        bluetoothConnectionProvider.setConnectionCallback(new StoneCallbackInterface() {

            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
                pairedStringPosition = position;
                finish(); // kill this activity
            }

            public void onError() {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro na conexão: " + bluetoothConnectionProvider.getListOfErrors(), Toast.LENGTH_LONG).show();
            }
        });
        bluetoothConnectionProvider.execute(); // call execute


    }

    private String getDeviceTypeFromName(String name){
        if (name.contains("D210") || name.toLowerCase().contains("stone"))
            return "PINPAD";
        else {
            return "Bluetooth";

        }

    }
    private String getBTMajorDeviceClass(int major){
        switch(major){
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                return "AUDIO_VIDEO";
            case BluetoothClass.Device.Major.COMPUTER:
                return "COMPUTER";
            case BluetoothClass.Device.Major.HEALTH:
                return "HEALTH";
            case BluetoothClass.Device.Major.IMAGING:
                return "IMAGING";
            case BluetoothClass.Device.Major.MISC:
                return "MISC";
            case BluetoothClass.Device.Major.NETWORKING:
                return "NETWORKING";
            case BluetoothClass.Device.Major.PERIPHERAL:
                return "PERIPHERAL";
            case BluetoothClass.Device.Major.PHONE:
                return "PHONE";
            case BluetoothClass.Device.Major.TOY:
                return "TOY";
            case BluetoothClass.Device.Major.UNCATEGORIZED:
                return "UNCATEGORIZED";
            case BluetoothClass.Device.Major.WEARABLE:
                return "AUDIO_VIDEO";
            default: return "unknown!";
        }
    }
}
