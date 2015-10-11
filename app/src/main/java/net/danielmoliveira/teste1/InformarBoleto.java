package net.danielmoliveira.teste1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InformarBoleto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informar_boleto);
    }

    public void BtnValidar_Click(){
        Intent i = new Intent(this,DadosBoleto.class);
        startActivity(i);
    }
}
