package net.danielmoliveira.teste1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DadosBoleto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_boleto);

        String codBarras = getIntent().getStringExtra("codBarras");
        String dataVencimento = getIntent().getStringExtra("dataVencimento");
        String valor = getIntent().getStringExtra("valor");

        TextView tvCodBarras = (TextView)findViewById(R.id.txtCodigoBarras_Conf);
        tvCodBarras.setText(codBarras);

        TextView tvDataVencimento = (TextView)findViewById(R.id.txtDataVencimento_Conf);
        tvDataVencimento.setText(dataVencimento);

        TextView tvValor = (TextView)findViewById(R.id.txtValor_conf);
        tvValor.setText(valor);




    }

    public void btnRealizarPagamento_Click(View v){
        Intent i = new Intent(this,IniciarProcessoTEF.class);
        startActivity(i);
    }

    public void btnCancelarPagamento_Click(View v){
        onBackPressed();
    }
}
