package net.multicom.multipagospagamentos;

import net.multicom.transacao.Transacao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import stone.utils.PrintObject;

/**
 * Created by Daniel on 03/12/2015.
 */
public class ComprovantePagamento {
    private Transacao transactionObject;

    public ComprovantePagamento(Transacao transacao) {
        this.transactionObject = transacao;
    }

    public List<PrintObject> ComprovantePadrao() {

        List<PrintObject> listToPrint = new ArrayList<PrintObject>();
        if (transactionObject != null) {
            String[] date = transactionObject.Data.split("-");
            String[] hour = transactionObject.Hora.split(":");
            String dateHour = String.format("%s/%s/%s %s:%s", new Object[]{date[2], date[1], date[0], hour[0], hour[1]});
            String dividerLine = "________________________";
            Double amountAsDouble = Double.valueOf(Double.parseDouble(transactionObject.Valor) / 100.0D);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
            listToPrint.add(new PrintObject("Credenciadora Banco PAN", PrintObject.SMALL, PrintObject.CENTER));
            listToPrint.add(new PrintObject("MULTIPAGOS ARRECADAÇÃO DIGITAL", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("Multilink Tecnologia", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("01.104.875/0001-04", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));

            listToPrint.add(new PrintObject("V1.3.11.0 TERM: 101", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject(" ", PrintObject.BIG, PrintObject.CENTER));

            listToPrint.add(new PrintObject("ELEKTRO", PrintObject.BIG, PrintObject.LEFT));
            listToPrint.add(new PrintObject(" ", PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("Codigo de Barras:", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("86680000002-2 54091557856-6", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("90057519408-0 10061000000-0", PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject(" ", PrintObject.MEDIUM, PrintObject.CENTER));
            if (decimalFormat.format(amountAsDouble).length() == 3) {
                listToPrint.add(new PrintObject("Valor Pago: R$ 0" + decimalFormat.format(amountAsDouble), PrintObject.MEDIUM, PrintObject.LEFT));
            } else {
                listToPrint.add(new PrintObject("Valor Pago: R$ " + decimalFormat.format(amountAsDouble), PrintObject.MEDIUM, PrintObject.LEFT));
            }
            listToPrint.add(new PrintObject("Pagamento  :" + transactionObject.Data.toString(), PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("Autorização:" + transactionObject.authorizationCode, PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject("NSU        :000X", PrintObject.MEDIUM, PrintObject.LEFT));

            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));
            listToPrint.add(new PrintObject("** VIA CLIENTE **", PrintObject.SMALL, PrintObject.CENTER));
            listToPrint.add(new PrintObject("STONE - VIA CLIENTE", PrintObject.SMALL, PrintObject.CENTER));
            listToPrint.add(new PrintObject(String.format("%s - %s", new Object[]{transactionObject.cardBrand.toString(), "DEBITO A VISTA"}), PrintObject.MEDIUM, PrintObject.CENTER));
            listToPrint.add(new PrintObject(String.format("%s %s ", new Object[]{transactionObject.cardHolderNumber, dateHour}), PrintObject.SMALL, PrintObject.LEFT));
            listToPrint.add(new PrintObject("STONE ID: " + transactionObject.RecipientTransactionIdentification, PrintObject.MEDIUM, PrintObject.LEFT));
            listToPrint.add(new PrintObject(dividerLine, PrintObject.BIG, PrintObject.CENTER));

            listToPrint.add(new PrintObject(" ", PrintObject.MEDIUM, PrintObject.CENTER));
            listToPrint.add(new PrintObject(" ", PrintObject.MEDIUM, PrintObject.CENTER));

        }
        return listToPrint;
    }
}