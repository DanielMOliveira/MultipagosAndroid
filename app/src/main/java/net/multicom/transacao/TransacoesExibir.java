package net.multicom.transacao;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import net.multicom.multipagospagamentos.R;

public class TransacoesExibir extends Activity {
    private Menu menu;
    private boolean isListView;


    private RecyclerView mRecyclerView;
    //REferencia ao laypout manager TODO: entender melhor
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private TransacaoListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacoes_exibir);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        isListView = true;
        mRecyclerView = (RecyclerView)findViewById(R.id.listTransacao);
        //TODO: Olhar as opções
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        mAdapter = new TransacaoListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(onItemClickListener);


    }

    @Override
    protected void onResume(){

        super.onResume();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu adiciona itens a action bar caso exista
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_toggle){
            toggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle()
    {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (isListView) {
            item.setIcon(R.drawable.ic_action_list);
            item.setTitle("Show as list");
            mStaggeredGridLayoutManager.setSpanCount(2);
            isListView = false;
        } else {
            item.setIcon(R.drawable.ic_action_grid);
            item.setTitle("Show as grid");
            mStaggeredGridLayoutManager.setSpanCount(1);
            isListView = true;
        }
    }

    TransacaoListAdapter.OnItemClickListener onItemClickListener = new TransacaoListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(TransacoesExibir.this, transacao_detalhe.class);
            intent.putExtra(transacao_detalhe.EXTRA_PARAM_ID, position);
            startActivity(intent);
        }
    };

    //TODO: Adicionar o long Click para imprimir




}
