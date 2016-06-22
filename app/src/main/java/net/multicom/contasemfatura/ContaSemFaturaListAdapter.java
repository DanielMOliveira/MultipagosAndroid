package net.multicom.contasemfatura;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.multicom.multipagospagamentos.R;

import java.util.List;

import stone.database.transaction.TransactionDAO;

/**
 * Created by Daniel on 02/12/2015.
 */
public class ContaSemFaturaListAdapter extends RecyclerView.Adapter<ContaSemFaturaListAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;
    TransactionDAO transactionDAO;
    String CodigoCliente;
    String url;
    ContaSemFaturaData csfData;
    List<ContaSemFatura> listContaSemFatura;
    // 2
    public ContaSemFaturaListAdapter(Context context,String CodigoCliente) {
        this.mContext = context;
        this.CodigoCliente = CodigoCliente;

        //transactionDAO = new TransactionDAO(this.mContext);
    }

    public ContaSemFaturaListAdapter(Context context,List<ContaSemFatura> Contas) {
        this.mContext = context;
        this.listContaSemFatura = Contas;

        //transactionDAO = new TransactionDAO(this.mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contasemfatura, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int pos = position;
        //TODO: Caso esteja zerada como fazer?
        ContaSemFatura conta = listContaSemFatura.get(position);

        holder.CodigoConta.setText(conta.getCodigoConta());
        holder.DataVencimento.setText(conta.getDataVencimento());
        holder.Valor.setText("R$ " + conta.getValor());
        holder.chkSelected.setChecked(conta.getIsSelected());

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                listContaSemFatura.get(pos).setSelected(cb.isChecked());

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return (null != listContaSemFatura ? listContaSemFatura.size() : 0);
    }


    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        public TextView CodigoConta;
        public TextView DataVencimento;
        public TextView Valor;
        public CheckBox chkSelected;


        public ViewHolder(View itemView) {
            super(itemView);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            CodigoConta = (TextView) itemView.findViewById(R.id.txt_Codico_CSF);
            DataVencimento = (TextView) itemView.findViewById(R.id.txt_DataVencimento_CSF);
            Valor = (TextView) itemView.findViewById(R.id.txt_Valor_CSF);
            chkSelected = (CheckBox) itemView.findViewById(R.id.chkSelected);

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