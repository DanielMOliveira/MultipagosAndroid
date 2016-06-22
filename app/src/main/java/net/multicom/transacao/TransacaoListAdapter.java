package net.multicom.transacao;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.multicom.multipagospagamentos.R;

import java.util.ArrayList;
import java.util.List;

import stone.database.transaction.TransactionDAO;

/**
 * Created by Daniel on 02/12/2015.
 */
public class TransacaoListAdapter extends RecyclerView.Adapter<TransacaoListAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;
    TransactionDAO transactionDAO;
    List<Transacao> transacaoList = new ArrayList<Transacao>();

    // 2
    public TransacaoListAdapter(Context context) {
        this.mContext = context;
        transactionDAO = new TransactionDAO(this.mContext);
        Cursor cursor = new GerenciadorTransacao(context).GetTransacao();
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    Transacao t = new Transacao();
                    /*
                    * values.put("Tipo",Tipo);
        values.put("Data",Data);
        values.put("Hora",Hora);
        values.put("Cartao",Cartao);
        values.put("Status",Status);
        values.put("Valor",Valor);
        values.put("timeToPassTransaction",timeToPassTransaction);
        values.put("cardHolderNumber",cardHolderNumber);
        values.put("cardHolderName",cardHolderName);
        values.put("authorizationCode",authorizationCode);
        values.put("pinpadUsed",pinpadUsed);
        values.put("cardBrand",cardBrand);
        values.put("cardBrand",cardBrand);
        values.put("RecipientTransactionIdentification",RecipientTransactionIdentification);
        values.put("CodigoBarras",CodigoBarras);
        values.put("UnidadeConsumidora",UnidadeConsumidora);
        values.put("Comprovante",Comprovante);*/
                    t.Tipo = cursor.getString(1);
                    t.Data = cursor.getString(2);
                    t.Hora = cursor.getString(3);
                    t.Cartao = cursor.getString(4);
                    t.Status = cursor.getString(5);
                    t.Valor = cursor.getString(6);
                    t.timeToPassTransaction = cursor.getString(7);
                    t.cardHolderNumber = cursor.getString(8);
                    t.cardHolderName = cursor.getString(9);
                    t.authorizationCode = cursor.getString(10);
                    t.pinpadUsed = cursor.getString(11);
                    t.CardBrandName = cursor.getString(12);
                    t.RecipientTransactionIdentification = cursor.getString(13);
                    t.CodigoBarras = cursor.getString(14);
                    t.UnidadeConsumidora = cursor.getString(15);




                    transacaoList.add(t);
                }while (cursor.moveToNext());
            }
        }

        //transacaoList = new GerenciadorTransacao(context).GetTransacao();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transacao_multipagos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //TODO: Caso esteja zerada como fazer?

        final Transacao transacao;
        if (!Build.FINGERPRINT.startsWith("generic") ) {

            transacao = transacaoList.get(position);
        }
       else
            transacao = new TransacaoData().transacaoList().get(position);

        holder.Data.setText(transacao.Data);
        holder.Hora.setText(transacao.Hora);
        try {
            holder.Valor.setText("R$ " + String.format("%.2f", Double.parseDouble(transacao.Valor) / 100));
        }
        catch (Exception ex){
            holder.Valor.setText("R$ " +transacao.Valor);
        }
        holder.Tipo.setText("Debito a vista");
        holder.Status.setText(transacao.Status);

    }

    @Override
    public int getItemCount()
    {

        int total = 0;
        if (!Build.FINGERPRINT.startsWith("generic") ) {
            total = transacaoList.size();
        }
        else
            total = new TransacaoData().transacaoList().size();

        return total;

    }

    public Transacao get(int position){
        return transacaoList.get(position);
    }


    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        public TextView Tipo;
        public TextView Status;
        public TextView Cartao;
        public TextView Data;
        public TextView Hora;
        public TextView Valor;

        public ViewHolder(View itemView) {
            super(itemView);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            Tipo = (TextView) itemView.findViewById(R.id.txt_TipoTransacao);
            Status = (TextView) itemView.findViewById(R.id.txt_StatusTransacao);
            Data = (TextView) itemView.findViewById(R.id.txt_DataTransacao);
            Hora = (TextView) itemView.findViewById(R.id.txt_HoraTransacao);
            Valor = (TextView) itemView.findViewById(R.id.txt_ValorTransacao);
            placeHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}