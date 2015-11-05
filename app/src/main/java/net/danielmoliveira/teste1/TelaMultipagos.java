package net.danielmoliveira.teste1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import stone.application.interfaces.StoneCallbackInterface;
import stone.providers.DisplayMessageProvider;
import stone.utils.GlobalInformations;

public class TelaMultipagos extends AppCompatActivity implements View.OnClickListener {

    BootstrapButton showPinpadsButton;
    BootstrapButton showTransactionsButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_multipagos);

        instanceViews();
    }

    private void instanceViews() {

        showPinpadsButton = (BootstrapButton) findViewById(R.id.showPinpadsButton);
        showTransactionsButton = (BootstrapButton) findViewById(R.id.showTransactionsButton);
        showPinpadsButton.setOnClickListener(this);
        showTransactionsButton.setOnClickListener(this);
    }

    public void onClickContaSemFatura(View v){
        Intent i = new Intent(TelaMultipagos.this,CodigodaInstalacao.class);
        startActivity(i);
    }
    public void onClickArrecadacao(View v){
        Intent i = new Intent(TelaMultipagos.this,InformarBoleto.class);
        startActivity(i);
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.showPinpadsButton:
                Intent i = new Intent(this, BluetoothActivity.class);
                startActivity(i);
                break;

            case R.id.showTransactionsButton:
                Intent intent = new Intent(this, ListOfTransactionsActivity.class);
                startActivity(intent);
                break;
        }
    }

    protected void onResume() {
        super.onResume();

        // check if there's any pinpad connected
        if (Utils.isConnectedWithPinpad()) {

            DisplayMessageProvider displayMessageProvider = new DisplayMessageProvider(this, "   MULTIPAGOS   ", GlobalInformations.getPinpadFromListAt(0));
            displayMessageProvider.setConnectionCallback(new StoneCallbackInterface() {
                public void onSuccess() {

                }

                public void onError() {
                    Toast.makeText(getApplicationContext(), "A conexão com o Pinpad foi perdida", Toast.LENGTH_LONG).show();
                }
            });
            displayMessageProvider.execute();
        }

    }
}
