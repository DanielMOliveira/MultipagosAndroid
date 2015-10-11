package br.ContaSemFatura;

/**
 * Created by Daniel on 11/10/2015.
 */
public class ContaSemFatura {
    private int _id ;
    private String _codBarras;
    private String _dataVencimento;
    private String _valor;
    public ContaSemFatura(){}
    public ContaSemFatura(int pID,String pCodBarras, String pData, String pValor)
    {
        _id = pID;
        _codBarras =pCodBarras;
        _dataVencimento = pData;
        _valor = pValor;

    }

    public int get_id(){return _id;}
    public String get_codBarras(){return _codBarras;}
    public String get_dataVencimento(){return _dataVencimento;}
    public String get_valor(){return _valor;}




}
