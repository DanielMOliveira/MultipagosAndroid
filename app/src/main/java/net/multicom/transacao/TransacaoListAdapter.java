package net.multicom.transacao;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import net.multicom.multipagospagamentos.R;

import stone.database.transaction.TransactionDAO;

/**
 * Created by Daniel on 02/12/2015.
 */
public class TransacaoListAdapter extends RecyclerView.Adapter<TransacaoListAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;
    TransactionDAO transactionDAO;

    // 2
    public TransacaoListAdapter(Context context) {
        this.mContext = context;
        transactionDAO = new TransactionDAO(this.mContext);
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

            transacao = new TransacaoData().transacaoList(transactionDAO).get(position);
        }
       else
            transacao = new TransacaoData().transacaoList().get(position);

        holder.Data.setText(transacao.Data);
        holder.Hora.setText(transacao.Hora);
        holder.Valor.setText("R$ " + transacao.Valor);
        holder.Tipo.setText("Debito a vista");
        holder.Status.setText(transacao.Status);

    }

    @Override
    public int getItemCount()
    {

        int total = 0;
        if (!Build.FINGERPRINT.startsWith("generic") ) {
            total = new TransacaoData().transacaoList(transactionDAO).size();
        }
        else
            total = new TransacaoData().transacaoList().size();

        return total;

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