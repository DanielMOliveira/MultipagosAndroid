package net.multicom.transacao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import stone.application.enums.CardBrandEnum;
import stone.application.enums.TransactionStatusEnum;
import stone.database.transaction.TransactionDAO;
import stone.database.transaction.TransactionObject;

/**
 * Created by Daniel on 02/12/2015.
 */
public class TransacaoData {

    List<TransactionObject> allTransactions;


    public static ArrayList<Transacao> transacaoList() {
        ArrayList<Transacao> list = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            Random rnd = new Random();
            int numero = rnd.nextInt(30);
            Date d = new Date();
            SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");

            Transacao t = new Transacao();
            t.Cartao = "XXXX";
            t.Valor = String.valueOf(numero);
            t.Hora = hora.format(d);
            t.Data = data.format(d);

            t.authorizationCode = "Authorization code";
            t.cardBrand = CardBrandEnum.VISA;
            t.cardHolderName = "NOME CARTAO";
            t.cardHolderNumber = "XXXXXXX0991";
            t.pinpadUsed = "PINPAD FANTASMA";
            t.timeToPassTransaction = "0.001ms";
            t.RecipientTransactionIdentification = "NAO FACO IDEIA";

            t.Status = "";

            if (i % 2 == 0)
                t.Status = "Aprovada";
            else
                t.Status = "Negada";

            if (i % 3 == 0)
                t.Tipo = "Debito";
            else
                t.Tipo = "Credito";


            list.add(t);
        }
        return (list);
    }

    public static ArrayList<Transacao> transacaoList(TransactionDAO transactionDAO) {
        ArrayList<Transacao> list = new ArrayList<>();
        List<TransactionObject> allTransactions;

        SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");

        if (transactionDAO == null)
            return null;

        // get all transactions in base by id DESC
        // if there's no transaction, returns a empty List
        allTransactions = transactionDAO.getAllTransactionsOrderByIdDesc();

        for (TransactionObject currentTransaction : allTransactions) {
            Transacao t = new Transacao();
            t.Cartao = currentTransaction.getCardHolderNumber();
            t.Valor = currentTransaction.getAmount();
            t.Hora = currentTransaction.getTime().substring(0, 5);
            t.Data = currentTransaction.getDate();
            //t.Data = t.Data.substring(9,2) +  "/" + t.Data.substring(6,2) +  "/" + t.Data.substring(0,4);

            t.authorizationCode = currentTransaction.getAuthorizationCode();
            t.cardBrand = currentTransaction.getCardBrand();
            t.cardHolderName = currentTransaction.getCardHolderName();
            t.cardHolderNumber = currentTransaction.getCardHolderNumber();
            t.pinpadUsed = currentTransaction.getPinpadUsed();
            t.timeToPassTransaction = currentTransaction.getTimeToPassTransaction();
            t.RecipientTransactionIdentification = currentTransaction.getRecipientTransactionIdentification();

            t.Status = "";
            if (currentTransaction.getTransactionStatus() == TransactionStatusEnum.APPROVED)
                t.Status = "Aprovada";
            else
                t.Status = "Cancelada";

            t.Tipo = "DEBITO A VISTA";
            //TODO: Adicionar os outros tipos para o objeto transacao

            list.add(t);

        }

        return (list);
    }


}
