package net.multicom.contasemfatura;

import java.io.Serializable;

/**
 * Created by Daniel on 04/12/2015.
 */
@SuppressWarnings("serial")
public class ContaSemFatura implements Serializable{
    private String CodigoConta;
    private String UnidadeConsumidora;
    private String Cliente;
    private String DataVencimento;
    private String DataPagamento;
    private String CodigodeBarras;
    private String Valor;
    private int ID;
    private Boolean isSelected;
    private String comprovante;
    private String Status;

    public ContaSemFatura(){this.isSelected = false;}
    public ContaSemFatura (Integer ID, String CodigoConta, String UnidadeConsumidora,String Cliente,String DataVencimento, String DataPagamento, String CodigodeBarras,String Valor){
        this.CodigoConta = CodigoConta;
        this.UnidadeConsumidora = UnidadeConsumidora;
        this.Cliente = Cliente;
        this.DataVencimento = DataVencimento;
        this.DataPagamento = DataPagamento;
        this.CodigodeBarras = CodigodeBarras;
        this.Valor = Valor;
        this.isSelected = false;
        this.setID(ID);
    }


    public String getCodigoConta() {
        return CodigoConta;
    }

    public void setCodigoConta(String codigoConta) {
        CodigoConta = codigoConta;
    }

    public String getUnidadeConsumidora() {
        return UnidadeConsumidora;
    }

    public void setUnidadeConsumidora(String unidadeConsumidora) {
        UnidadeConsumidora = unidadeConsumidora;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public String getDataVencimento() {
        return DataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        DataVencimento = dataVencimento;
    }

    public String getDataPagamento() {
        return DataPagamento;
    }

    public void setDataPagamento(String dataPagamento) {
        DataPagamento = dataPagamento;
    }

    public String getCodigodeBarras() {
        return CodigodeBarras;
    }

    public void setCodigodeBarras(String codigodeBarras) {
        CodigodeBarras = codigodeBarras;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public void setSelected(boolean isSelected){this.isSelected = isSelected; }
    public Boolean getIsSelected(){return this.isSelected;}


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getComprovante() {
        return comprovante;
    }

    public void setComprovante(String comprovante) {
        this.comprovante = comprovante;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}

