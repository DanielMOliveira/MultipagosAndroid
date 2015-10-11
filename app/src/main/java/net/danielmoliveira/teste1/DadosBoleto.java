package net.danielmoliveira.teste1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DadosBoleto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_boleto);
    }

    public void btnRealizarPagamento_Click(View v){
        Intent i = new Intent(this,IniciarProcessoTEF.class);
        startActivity(i);
    }

    public void btnCancelarPagamento_Click(View v){
        onBackPressed();
    }
}
