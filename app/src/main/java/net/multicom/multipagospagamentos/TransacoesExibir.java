package net.multicom.multipagospagamentos;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import stone.application.enums.TransactionStatusEnum;
import stone.application.interfaces.StoneCallbackInterface;
import stone.database.transaction.TransactionDAO;
import stone.database.transaction.TransactionObject;
import stone.providers.CancellationProvider;
import stone.providers.PrintProvider;
import stone.user.UserModel;
import stone.utils.GlobalInformations;
import stone.utils.PrintObject;

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

    /*METODOS REFERENTE AO CARREGAMENTO DA TRANSAÇAO E A IMPRESSAÕ*/
    public class ComprovantePagamento {
        private TransactionObject transactionObject;

        public ComprovantePagamento(TransactionObject transactionObject) {
            this.transactionObject = transactionObject;
        }

        public List<PrintObject> ComprovantePadrao() {
            //TESTE DE IMPRESSAO
            List<PrintObject> listToPrint = new ArrayList<PrintObject>();

            String[] date = transactionObject.getDate().split("-");
            String[] hour = transactionObject.getTime().split(":");
            String dateHour = String.format("%s/%s/%s %s:%s", new Object[]{date[2], date[1], date[0], hour[0], hour[1]});
            String dividerLine = "________________________";
            Double amountAsDouble = Double.valueOf(Double.parseDouble(transactionObject.getAmount()) / 100.0D);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
            listToPrint.add(new PrintObject("Credenciadora Banco PAN", PrintObject.SMALL, PrintObject.CENTER));
            listToPrint.add(new PrintObject("MULTIPAGOS ARRECADAÇÃO DIGITAL", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("Multilink Tecnologia", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("01.104.875/0001-04", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));

            listToPrint.add(new PrintObject("V1.3.11.0 TERM: 101", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject(" ", PrintObject.BIG, PrintObject.CENTER));

            listToPrint.add(new PrintObject("ELEKTRO", PrintObject.BIG, PrintObject.LEFT));
            listToPrint.add(new PrintObject(" ", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("Codigo de Barras:", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("86680000002-2 54091557856-6", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("90057519408-0 10061000000-0", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject(" ", PrintObject.MEDIUM, PrintObject.CENTER));
            if(decimalFormat.format(amountAsDouble).length() == 3) {
                listToPrint.add(new PrintObject("Valor Pago: R$ 0" + decimalFormat.format(amountAsDouble), PrintObject.MEDIUM, PrintObject.LEFT));
            } else {
                listToPrint.add(new PrintObject("Valor Pago: R$ " + decimalFormat.format(amountAsDouble), PrintObject.MEDIUM, PrintObject.LEFT));
            }
            listToPrint.add(new PrintObject("Pagamento  :" + transactionObject.getDate().toString(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("Autorização:"+transactionObject.getAid(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("NSU        :"+transactionObject.getRecipientTransactionIdentification(), PrintObject.MEDIUM, PrintObject.LEFT));

            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("** VIA CLIENTE **", PrintObject.SMALL, PrintObject.CENTER));
            listToPrint.add(new PrintObject("STONE - VIA CLIENTE", PrintObject.SMALL, PrintObject.CENTER));
            listToPrint.add(new PrintObject(String.format("%s - %s", new Object[]{transactionObject.getCardBrand(), "DEBITO A VISTA"}), PrintObject.MEDIUM, PrintObject.CENTER));
            listToPrint.add(new PrintObject(String.format("%s %s ",new Object[]{transactionObject.getCardHolderNumber(),dateHour}), PrintObject.SMALL, PrintObject.LEFT));
            listToPrint.add(new PrintObject("AID: " + transactionObject.getAid(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("ARCQ: " + transactionObject.getArcq(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("Serial: " + transactionObject.getPinpadUsed(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("STONE ID: " + transactionObject.getRecipientTransactionIdentification(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));

            listToPrint.add(new PrintObject(" ", PrintObject.MEDIUM, PrintObject.CENTER));
            listToPrint.add(new PrintObject(" ", PrintObject.MEDIUM, PrintObject.CENTER));
            return listToPrint;
        }
    }


}
