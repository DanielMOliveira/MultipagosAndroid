package net.danielmoliveira.teste1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import stone.application.interfaces.StoneCallbackInterface;
import stone.providers.BluetoothConnectionProvider;
import stone.utils.PinpadObject;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView bluetoothDevices;
    List<BluetoothDevice> bondedDevices;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bluetooth);
        bondedDevices = new ArrayList<>();

        instanceViews();
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
            devicesName.add(bt.getName());

        bluetoothDevices.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, devicesName));
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
}
