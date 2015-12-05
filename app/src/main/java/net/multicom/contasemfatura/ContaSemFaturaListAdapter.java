package net.multicom.contasemfatura;


import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.multicom.multipagospagamentos.R;
import net.multicom.transacao.Transacao;
import net.multicom.transacao.TransacaoData;

import stone.database.transaction.TransactionDAO;

/**
 * Created by Daniel on 02/12/2015.
 */
public class ContaSemFaturaListAdapter extends RecyclerView.Adapter<ContaSemFaturaListAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;
    TransactionDAO transactionDAO;

    // 2
    public ContaSemFaturaListAdapter(Context context) {
        this.mContext = context;
        //transactionDAO = new TransactionDAO(this.mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contasemfatura, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //TODO: Caso esteja zerada como fazer?

        final ContaSemFatura csf;
        if (!Build.FINGERPRINT.startsWith("generic") ) {

            //TODO: Configurar para buscar do ws
            csf = new ContaSemFaturaData().ContaSemFaturaList().get(position);
        }
       else
            csf = new ContaSemFaturaData().ContaSemFaturaList().get(position);


        holder.CodigoConta.setText(csf.CodigoConta);
        holder.DataVencimento.setText(csf.DataVencimento);
        holder.Valor.setText("R$ " + csf.Valor);

    }

    @Override
    public int getItemCount()
    {

        int total = 0;
        if (!Build.FINGERPRINT.startsWith("generic") ) {
            total = new ContaSemFaturaData().ContaSemFaturaList().size();
        }
        else
            total = new ContaSemFaturaData().ContaSemFaturaList().size();

        return total;

    }


    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        public TextView CodigoConta;
        public TextView DataVencimento;
        public TextView Valor;


        public ViewHolder(View itemView) {
            super(itemView);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            CodigoConta = (TextView) itemView.findViewById(R.id.txt_Codico_CSF);
            DataVencimento = (TextView) itemView.findViewById(R.id.txt_DataVencimento_CSF);
            Valor = (TextView) itemView.findViewById(R.id.txt_Valor_CSF);

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