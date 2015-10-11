package net.danielmoliveira.teste1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TelaMultipagos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_multipagos);
    }

    public void onClickContaSemFatura(View v){
        Intent i = new Intent(TelaMultipagos.this,CodigodaInstalacao.class);
        startActivity(i);
    }
    public void onClickArrecadacao(View v){
        Intent i = new Intent(TelaMultipagos.this,InformarBoleto.class);
        startActivity(i);
    }
}
