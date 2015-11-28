package net.danielmoliveira.teste1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;
import java.util.List;

import stone.application.StoneStart;
import stone.application.interfaces.StoneCallbackInterface;
import stone.providers.ActiveApplicationProvider;
import stone.providers.DisplayMessageProvider;
import stone.user.Partner;
import stone.user.UserModel;
import stone.utils.GlobalInformations;

public class TelaMultipagos extends AppCompatActivity implements View.OnClickListener {

    BootstrapButton showPinpadsButton;
    BootstrapButton showTransactionsButton;
    private final String STONE_CODE = "505986781"; // your Stone Code

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

        // The SDK need start, call this to initialize a session
        // the method 'init' will return a List of UserModel.
        // UserModel represent all user in cache, get it just to check if is != null
        List<UserModel> userModels = StoneStart.init(this);



        // if userModels == null, your SDK has not been activated, yet.
        // if userModels != null, you don't need do nothing.5
        if (userModels == null) {

            // the SDK can active multi merchants.
            // But in this case, we'll use only one Stone code
            List<String> stoneCodeList = new ArrayList<>();
            stoneCodeList.add(STONE_CODE);

            final ActiveApplicationProvider activeApplicationProvider = new ActiveApplicationProvider(this, stoneCodeList);

            // by default, all 'providers' work in background
            // but if you want give to your user a feedback, pass false in this method
            activeApplicationProvider.setWorkInBackground(false);
            activeApplicationProvider.setDialogTitle("Ativando o aplicativo....");
            activeApplicationProvider.setDialogMessage("Ativando SDK, por favor, aguarde..");
            activeApplicationProvider.setConnectionCallback(new StoneCallbackInterface() {

                public void onSuccess() {
                    // your SDK now has a key to pass transactions and your
                    Toast.makeText(getApplicationContext(), "Ativado com sucesso, iniciando o aplicativo", Toast.LENGTH_LONG).show();
                }

                public void onError() {
                    // occurred a error, check the logcat
                    Toast.makeText(getApplicationContext(), "Erro na ativação, derifique o erro na documentação: " + activeApplicationProvider.getListOfErrors(), Toast.LENGTH_LONG).show();
                }
            });
            activeApplicationProvider.execute(); // call this method to run the provider
        }
        else{
            Partner partnerDeveloper = new Partner(userModels.get(0));
            userModels.set(0,partnerDeveloper);
            GlobalInformations.sessionApplication.setUserModelList(userModels);
        }


        // check if there's any pinpad connected
        if (Utils.isConnectedWithPinpad()) {

            DisplayMessageProvider displayMessageProvider = new DisplayMessageProvider(this, "ELEKTRO      ", GlobalInformations.getPinpadFromListAt(0));
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
