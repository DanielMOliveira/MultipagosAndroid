package net.multicom.contasemfatura;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.multicom.multipagospagamentos.R;

import java.util.List;

/**
 * Created by Daniel on 29/01/2016.
 */
public class ContaSemFaturaAdapter extends RecyclerView.Adapter<ContaSemFaturaAdapter.MyViewHolder> {
    private List<ContaSemFatura> contaSemFaturasList;

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_contasemfatura,parent,false);

        return new MyViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ContaSemFatura contaSemFatura = this.contaSemFaturasList.get(position);
        holder.codigoConta.setText(contaSemFatura.getCodigoConta());
        holder.valorPagamento.setText(contaSemFatura.getValor());
        holder.dataVencimento.setText(contaSemFatura.getDataVencimento());
        holder.checkBoxSelected.setTag(contaSemFatura);
        holder.checkBoxSelected.setChecked(contaSemFatura.getIsSelected());

        //TODO: Adicionar longClick para exibir detalhes da fatura
        holder.checkBoxSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                ContaSemFatura contaSemFaturaSelected = (ContaSemFatura)checkBox.getTag();
                contaSemFatura.setSelected(checkBox.isChecked());

            }
        });

        /*
        holder.placeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBoxSelected.setChecked(v.isSelected());
            }
        });
        */
    }



    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (contaSemFaturasList == null)
            return 0;

        return contaSemFaturasList.size();
    }

    public ContaSemFaturaAdapter(List<ContaSemFatura> contaSemFaturas){
        this.contaSemFaturasList = contaSemFaturas;
    }

    public List<ContaSemFatura> getList(){
        return this.contaSemFaturasList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView dataVencimento, valorPagamento, codigoConta;
        public CheckBox checkBoxSelected;
        public LinearLayout placeHolder;
        public MyViewHolder(View view){
            super(view);
            dataVencimento = (TextView)view.findViewById(R.id.txt_DataTransacao);
            valorPagamento = (TextView)view.findViewById(R.id.txt_ValorTransacao);
            codigoConta = (TextView)view.findViewById(R.id.txt_CodigoConta);
            checkBoxSelected = (CheckBox)view.findViewById(R.id.chkSelected);
            placeHolder = (LinearLayout)view.findViewById(R.id.placeNameHolder);
        }
    }
}
