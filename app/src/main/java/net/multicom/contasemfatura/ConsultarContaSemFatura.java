package net.multicom.contasemfatura;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import net.multicom.multipagospagamentos.R;
import net.multicom.transacao.TransacaoListAdapter;
import net.multicom.transacao.transacao_detalhe;

public class ConsultarContaSemFatura extends Activity implements View.OnClickListener{

    private Menu menu;
    private boolean isListView;


    private RecyclerView mRecyclerView;
    //REferencia ao laypout manager TODO: entender melhor
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private ContaSemFaturaListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setSubtitle("Elektro");



        setContentView(R.layout.activity_consultar_conta_sem_fatura);
        isListView = true;
        mRecyclerView = (RecyclerView)findViewById(R.id.listTransacaoContaSemFatura);
        //TODO: Olhar as opções
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);






        ((Button)findViewById(R.id.btnConsultarCSF)).setOnClickListener(this);
    }

    @Override
    protected void onResume(){

        super.onResume();


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnConsultarCSF:
                CarregarContaSemFatura();
                break;


        }
    }

    public void CarregarContaSemFatura(){
        mAdapter = new ContaSemFaturaListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(onItemClickListener);
        if (mAdapter.getItemCount() == 0)
            ((TextView)findViewById(R.id.lblContaSemFatura)).setVisibility(View.VISIBLE);
        else{
            ((TableLayout)findViewById(R.id.TableLayoutContaSemFatura)).setVisibility(View.VISIBLE);
        }


    }

    ContaSemFaturaListAdapter.OnItemClickListener onItemClickListener = new ContaSemFaturaListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(ConsultarContaSemFatura.this, contasemfaturadetalhe.class);
            intent.putExtra(contasemfaturadetalhe.EXTRA_PARAM_ID, position);
            startActivity(intent);
        }
    };


}
