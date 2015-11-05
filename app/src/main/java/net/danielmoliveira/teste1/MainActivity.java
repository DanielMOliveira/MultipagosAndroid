package net.danielmoliveira.teste1;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Context;
import android.os.Bundle;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;

import java.util.ArrayList;
import java.util.List;

import stone.application.StoneStart;
import stone.application.interfaces.StoneCallbackInterface;
import stone.providers.ActiveApplicationProvider;
import stone.user.UserModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    BootstrapButton btnLogin;
    EditText txtUserName;
    BootstrapEditText bootstrapEditText;
    private final String STONE_CODE = "505986781"; // your Stone Code

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instanceViews();
    }

    // link the views with objects
    private void instanceViews() {

        btnLogin    = (BootstrapButton)   findViewById(R.id.btnLogin);
        txtUserName = (EditText)          findViewById(R.id.txtUserID);
        bootstrapEditText = (BootstrapEditText)findViewById(R.id.txtUserID);

        // create listener to respond when user with
        btnLogin.setOnClickListener(this);
    }

    public void onClick(View v) {

        String userName = txtUserName.getText().toString();
        if (userName.equals("101")){

            Toast.makeText(getApplicationContext(),"Efetuando login...",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this,TelaMultipagos.class);
            i.putExtra("username",userName);
            startActivity(i);

            finish(); // kill this Activity
        } else {
            Toast.makeText(getApplicationContext(),"Login invalido",Toast.LENGTH_LONG).show();
            txtUserName.setVisibility(View.VISIBLE);

            bootstrapEditText.setBootstrapBrand(DefaultBootstrapBrand.DANGER);

        }
    }

    protected void onResume() {
        super.onResume();

        // The SDK need start, call this to initialize a session
        // the method 'init' will return a List of UserModel.
        // UserModel represent all user in cache, get it just to check if is != null
        List<UserModel> userModels = StoneStart.init(this);

        // if userModels == null, your SDK has not been activated, yet.
        // if userModels != null, you don't need do nothing.
        if (userModels == null) {

            // the SDK can active multi merchants.
            // But in this case, we'll use only one Stone code
            List<String> stoneCodeList = new ArrayList<>();
            stoneCodeList.add(STONE_CODE);

            final ActiveApplicationProvider activeApplicationProvider = new ActiveApplicationProvider(this, stoneCodeList);
            // by default, all 'providers' work in background
            // but if you want give to your user a feedback, pass false in this method
            activeApplicationProvider.setWorkInBackground(false);
            activeApplicationProvider.setDialogTitle("Ativando");
            activeApplicationProvider.setDialogMessage("Ativando Stone SDK, por favor, aguarde..");
            activeApplicationProvider.setConnectionCallback(new StoneCallbackInterface() {

                public void onSuccess() {
                    // your SDK now has a key to pass transactions and your
                    Toast.makeText(getApplicationContext(),"SDK ativado com sucesso",Toast.LENGTH_LONG).show();
                }

                public void onError() {
                    // occurred a error, check the logcat
                    Toast.makeText(getApplicationContext(),"Erro na ativação, derifique o erro na documentação: " + activeApplicationProvider.getListOfErrors(),Toast.LENGTH_LONG).show();
                }
            });
            activeApplicationProvider.execute(); // call this method to run the provider
        }
    }

    //    public void OnButtonClick(View v) {
//
//        BootstrapEditText bootstrapEditText = (BootstrapEditText)findViewById(R.id.txtUserID);
//        if (v.getId() == R.id.btnLogin)
//        {
//            EditText txtUserName = (EditText)findViewById(R.id.txtUserID);
//            String userName = txtUserName.getText().toString();
//            if (userName.equals("101")){
//                Toast.makeText(getApplicationContext(),"Efetuando login...",Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(MainActivity.this,TelaMultipagos.class);
//                i.putExtra("username",userName);
//                startActivity(i);
//            }
//            else
//            {
//                Toast.makeText(getApplicationContext(),"Login invalido",Toast.LENGTH_LONG).show();
//                txtUserName.setVisibility(View.VISIBLE);
//
//                bootstrapEditText.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
//
//            }
//
//
//        }
//    }
}
