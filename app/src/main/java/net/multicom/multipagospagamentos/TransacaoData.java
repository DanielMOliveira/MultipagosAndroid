package net.multicom.multipagospagamentos;

import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
        

        if (transactionDAO == null)
            return null;

        // get all transactions in base by id DESC
        // if there's no transaction, returns a empty List
        allTransactions = transactionDAO.getAllTransactionsOrderByIdDesc();

        for (TransactionObject currentTransaction : allTransactions) {
            Transacao t = new Transacao();
            t.Cartao = currentTransaction.getCardHolderNumber();
            t.Valor = currentTransaction.getAmount();
            t.Hora = currentTransaction.getTime();
            t.Data = currentTransaction.getDate();
            t.Status = "";
            if (currentTransaction.getTransactionStatus() == TransactionStatusEnum.APPROVED)
                t.Status = "Aprovada";
            else
                t.Status = "Cancelada";

            t.Tipo = currentTransaction.getUserModelSale();
            //TODO: Adicionar os outros tipos para o objeto transacao

            list.add(t);

        }

        return (list);
    }


}
