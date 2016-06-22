package net.multicom.transacao;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import stone.application.enums.CardBrandEnum;
import stone.utils.PrintObject;

/**
 * Created by Daniel on 02/12/2015.
 */
public class Transacao implements Serializable {
    public String Tipo;
    public String Data;
    public String Hora;
    public String Cartao;
    public String Status;
    public String Valor; //Amount

    public String timeToPassTransaction;
    public String cardHolderNumber;
    public String cardHolderName;
    public String authorizationCode;
    public String pinpadUsed;
    public CardBrandEnum cardBrand;
    public String CardBrandName;
    public String RecipientTransactionIdentification;

    public String CodigoBarras;
    public String UnidadeConsumidora;

    public String Comprovante;

    public List<PrintObject> listToPrint = new ArrayList<PrintObject>();

    public String GetCodigoBarras(){
        if (this.CodigoBarras == null)
            return "";
        return this.CodigoBarras;
    }

    public String GetUnidadeConsumidora(){
        if (this.UnidadeConsumidora == null)
            return "";
        return this.UnidadeConsumidora;
    }

    public String GetComprovante(){
        String result = "";
        for (PrintObject print:listToPrint)
        {
            result += print.getMessage()+"\n";
        }

        return result;
    }

    public String GetValor(){
        try {
            Double amountAsDouble = Double.valueOf(Double.parseDouble(Valor) / 100.0D);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
            return decimalFormat.format(amountAsDouble);
        }
        catch (Exception ex){
            return Valor;
        }

    }

}
