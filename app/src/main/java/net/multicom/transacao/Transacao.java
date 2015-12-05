package net.multicom.transacao;

import java.util.Date;

import stone.application.enums.CardBrandEnum;
import stone.application.enums.InstalmentTransactionEnum;
import stone.application.enums.TransactionStatusEnum;
import stone.application.enums.TypeOfTransactionEnum;
import stone.application.xml.enums.InstalmentTypeEnum;

/**
 * Created by Daniel on 02/12/2015.
 */
public class Transacao {
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
    public String RecipientTransactionIdentification;

}
