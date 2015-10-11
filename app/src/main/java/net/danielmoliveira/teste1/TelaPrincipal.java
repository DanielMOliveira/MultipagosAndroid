package net.danielmoliveira.teste1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TelaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        String userName = getIntent().getStringExtra("username");



        TextView textView = (TextView)findViewById(R.id.txtLoginName);
        textView.append(userName);
    }
}
