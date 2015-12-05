package net.multicom.contasemfatura;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


/**
 * Created by Daniel on 04/12/2015.
 */
public class ContaSemFaturaData {


    public static ArrayList<ContaSemFatura> ContaSemFaturaList() {
        ArrayList<ContaSemFatura> list = new ArrayList<ContaSemFatura>();
        for (int i = 0; i < 25; i++) {
            Random rnd = new Random();
            int numero = rnd.nextInt(30);
            Date d = new Date();
            SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");

            ContaSemFatura csf = new ContaSemFatura();
            csf.CodigoConta = "0000" + numero;
            csf.UnidadeConsumidora = numero + "0000"  + numero*2;
            csf.Cliente = "Cliente Jose das couves";
            csf.DataVencimento = data.format(d);
            csf.DataPagamento = "";
            csf.CodigodeBarras = "00000.00000.00000.000000.00000";
            csf.Valor = String.valueOf((numero * numero) * 30 /60 * 15 / 100);
            list.add(csf);
        }
        return (list);
    }

}
