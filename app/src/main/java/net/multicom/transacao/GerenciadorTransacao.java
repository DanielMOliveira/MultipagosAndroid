package net.multicom.transacao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel on 05/02/2016.
 */
public class GerenciadorTransacao extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "multipagos.db";
    private static final Integer VERSAO_SCHEMA = 1;
    public GerenciadorTransacao(Context context){
        super(context,NOME_BANCO,null,VERSAO_SCHEMA);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE transacao ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                +  ", Tipo TEXT "
                +  ", Data TEXT "
                +  ", Hora TEXT "
                +  ", Cartao TEXT "
                +  ", Status TEXT "
                +  ", Valor TEXT "
                +  ", timeToPassTransaction TEXT "
                +  ", cardHolderNumber TEXT "
                +  ", CardHolderName TEXT "
                +  ", authorizationCode TEXT "
                +  ", pinpadUsed TEXT "
                +  ", cardBrand TEXT "
                +  ", RecipientTransactionIdentification TEXT "
                +  ", CodigoBarras TEXT "
                +  ", UnidadeConsumidora TEXT "
                +  ", Comprovante TEXT "
                +");";

        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void InsertTransacao( String Tipo,
             String Data,
             String Hora,
             String Cartao,
             String Status,
             String Valor, //Amount
             String timeToPassTransaction,
             String cardHolderNumber,
             String cardHolderName,
             String authorizationCode,
             String pinpadUsed,
             String cardBrand,
             String RecipientTransactionIdentification,
             String CodigoBarras,
             String UnidadeConsumidora,
             String Comprovante){
        ContentValues values = new ContentValues();
        values.put("Tipo",Tipo);
        values.put("Data",Data);
        values.put("Hora",Hora);
        values.put("Cartao",Cartao);
        values.put("Status",Status);
        values.put("Valor",Valor);
        values.put("timeToPassTransaction",timeToPassTransaction);
        values.put("cardHolderNumber",cardHolderNumber);
        values.put("cardHolderName",cardHolderName);
        values.put("authorizationCode",authorizationCode);
        values.put("pinpadUsed",pinpadUsed);
        values.put("cardBrand",cardBrand);
        values.put("cardBrand",cardBrand);
        values.put("RecipientTransactionIdentification",RecipientTransactionIdentification);
        values.put("CodigoBarras",CodigoBarras);
        values.put("UnidadeConsumidora",UnidadeConsumidora);
        values.put("Comprovante",Comprovante);


        getWritableDatabase().insert("transacao","Tipo",values);
    }

    public Cursor GetTransacao(){
        return getReadableDatabase().rawQuery("select * from transacao order by _id desc",null);
    }

    public String GetTipo (Cursor c){
        return c.getString(1);
    }
}
