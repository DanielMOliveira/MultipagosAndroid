package net.danielmoliveira.teste1;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Context;
import android.os.Bundle;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void OnButtonClick(View v){

        BootstrapEditText bootstrapEditText = (BootstrapEditText)findViewById(R.id.txtUserID);
        if (v.getId() == R.id.btnLogin)
        {
            EditText txtUserName = (EditText)findViewById(R.id.txtUserID);
            String userName = txtUserName.getText().toString();
            if (userName.equals("101")){
                Toast.makeText(getApplicationContext(),"Efetuando login...",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,TelaMultipagos.class);
                i.putExtra("username",userName);
                startActivity(i);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Login invalido",Toast.LENGTH_LONG).show();
                txtUserName.setVisibility(View.VISIBLE);

                bootstrapEditText.setBootstrapBrand(DefaultBootstrapBrand.DANGER);

            }


        }
    }
}
