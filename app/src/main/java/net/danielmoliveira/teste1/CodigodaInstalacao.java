package net.danielmoliveira.teste1;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CodigodaInstalacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigoda_instalacao);
    }

    public void OnClickConsultarCodigo(View v){

        if (v.getId() == R.id.btConsultarSemFatura)
        {
            EditText txtCodigoInstalacao = (EditText)findViewById(R.id.txtCodigoInstalacao);
            String codigoInstalacao = txtCodigoInstalacao.getText().toString();
            if (!codigoInstalacao.isEmpty()){
                Toast.makeText(getApplicationContext(), "Consultando webservice...", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this,ListarContasEmAberto.class);
                i.putExtra("codigo",codigoInstalacao);
                startActivity(i);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Digite um valor valido",Toast.LENGTH_LONG).show();
                txtCodigoInstalacao.setVisibility(View.VISIBLE);
                txtCodigoInstalacao.setBackgroundColor(Color.RED);

            }


        }

    }
}
