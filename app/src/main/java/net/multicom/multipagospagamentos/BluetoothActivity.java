package net.multicom.multipagospagamentos;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
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
    List<BluetoothDevice> bondedDevices;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bluetooth);
        bondedDevices = new ArrayList<>();

        instanceViews();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void instanceViews() {

        /**
         * I create it for show only the names
         * all informations are captured on item click
         * see below
         * */

        bluetoothDevices = (ListView) findViewById(R.id.bluetoothDevices);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        bondedDevices.addAll(pairedDevices);

        List<String> devicesName = new ArrayList<>();
        for(BluetoothDevice bt : pairedDevices)
        {
            devicesName.add(bt.getName() + "\n" + getDeviceTypeFromName(bt.getName()));
        }



        bluetoothDevices.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, devicesName){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                CheckedTextView textView = (CheckedTextView ) super.getView(position, convertView, parent);

                if (position == 1)
                    textView.setChecked(true);

                return textView;
            }
        });
        bluetoothDevices.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
