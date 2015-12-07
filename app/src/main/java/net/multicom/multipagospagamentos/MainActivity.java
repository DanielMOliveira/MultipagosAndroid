package net.multicom.multipagospagamentos;

import android.app.Activity;

/*
* referencia principal http://www.raywenderlich.com/103367/material-design
* */
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.multicom.contasemfatura.ConsultarContaSemFatura;
import net.multicom.transacao.TransacoesExibir;

import java.util.ArrayList;
import java.util.List;

import stone.application.StoneStart;
import stone.application.enums.InstalmentTransactionEnum;
import stone.application.enums.TransactionStatusEnum;
import stone.application.enums.TypeOfTransactionEnum;
import stone.application.interfaces.StoneCallbackInterface;
import stone.providers.ActiveApplicationProvider;
import stone.providers.DisplayMessageProvider;
import stone.providers.TransactionProvider;
import stone.user.Partner;
import stone.user.UserModel;
import stone.utils.GlobalInformations;
import stone.utils.StoneTransaction;

public class MainActivity extends Activity implements View.OnClickListener {

    private Menu menu;
    private boolean isListView;
    private Button btnTransactionsButton;
    private Button btnPinpadsButton;
    private Button btnContasemFatura;
    private Button btnPagarArrecadacao;

    private final String STONE_CODE = "505986781"; // your Stone Code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setSubtitle("Elektro");

        instanceViews();
    }


    //Trata objetos da tela
    public void instanceViews(){
        // create listener to respond when user with
        btnContasemFatura = (Button)findViewById(R.id.btnContaSemFatura);
        btnPinpadsButton = (Button)findViewById(R.id.btnPinpadsButton);
        btnTransactionsButton = (Button)findViewById(R.id.btnTransactionsButton);
        btnPagarArrecadacao = (Button)findViewById(R.id.btnPagamentoBoleto);

        btnPinpadsButton.setOnClickListener(this);
        btnTransactionsButton.setOnClickListener(this);
        btnContasemFatura.setOnClickListener(this);
        btnPagarArrecadacao.setOnClickListener(this);
        ((Button)findViewById(R.id.button2)).setOnClickListener(this);
    }

    private void setUpActionBar(){

    }

    /*REFERENTES A NAVEGAÇÃO SUPERIOR*/
    /*Codigo para a Action BAR TODO: Descobrir como não copiar*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu adiciona itens a action bar caso exista
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_toggle){
            toggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle()
    {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (isListView) {
            item.setIcon(R.drawable.ic_action_list);
            item.setTitle("Show as list");

            isListView = false;
        } else {
            item.setIcon(R.drawable.ic_action_grid);
            item.setTitle("Show as grid");

            isListView = true;
        }
    }

    private void toggleLAN(boolean isActive){
        if (menu != null) {
       MenuItem item = menu.findItem(R.id.action_toggle1);

            if (isActive)
                item.setIcon(R.drawable.ic_lan_connect_white_24dp);
            else
                item.setIcon(R.drawable.ic_lan_disconnect_white_24dp);
        }
    }
    private void toggleBluetooth(boolean isActive){
        if (menu != null) {
        MenuItem item = menu.findItem(R.id.action_toggle2);

            if (isActive)
                item.setIcon(R.drawable.ic_bluetooth_connect_white_24dp);
            else
                item.setIcon(R.drawable.ic_bluetooth_off_white_24dp);
        }
    }

    /*FIM NAVEGAÇÃO SUPERIOR*/

    //ASSOCIA UMA AÇÃO A CADA CLICK
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnPinpadsButton:
                Intent i = new Intent(this, BluetoothActivity.class);
                startActivity(i);
                break;

            case R.id.btnTransactionsButton:
                Intent intent = new Intent(this,TransacoesExibir.class);
                startActivity(intent);
                break;

            case R.id.button2:
                this.testarPagamento();

                break;
/*
            case R.id.btnPagamentoBoleto:
                Intent intent1 = new Intent(this, CodigoInstalacao.class);
                startActivity(intent1);
                break;
                 */
            case R.id.btnContaSemFatura:
                Intent intent2 = new Intent(this, ConsultarContaSemFatura.class);
                startActivity(intent2);
                break;


        }
    }

    protected void onResume() {
        super.onResume();

        // The SDK need start, call this to initialize a session
        // the method 'init' will return a List of UserModel.
        // UserModel represent all user in cache, get it just to check if is != null
        List<UserModel> userModels = StoneStart.init(this);



        // if userModels == null, your SDK has not been activated, yet.
        // if userModels != null, you don't need do nothing.5
        if (userModels == null) {

            // the SDK can active multi merchants.
            // But in this case, we'll use only one Stone code
            List<String> stoneCodeList = new ArrayList<>();
            stoneCodeList.add(STONE_CODE);

            final ActiveApplicationProvider activeApplicationProvider = new ActiveApplicationProvider(this, stoneCodeList);

            // by default, all 'providers' work in background
            // but if you want give to your user a feedback, pass false in this method
            activeApplicationProvider.setWorkInBackground(false);
            activeApplicationProvider.setDialogTitle("Ativando o aplicativo....");
            activeApplicationProvider.setDialogMessage("Ativando SDK, por favor, aguarde..");
            activeApplicationProvider.setConnectionCallback(new StoneCallbackInterface() {

                public void onSuccess() {
                    // your SDK now has a key to pass transactions and your
                    Toast.makeText(getApplicationContext(), "Ativado com sucesso, iniciando o aplicativo", Toast.LENGTH_LONG).show();
                }

                public void onError() {
                    // occurred a error, check the logcat
                    Toast.makeText(getApplicationContext(), "Erro na ativação, verifique o erro na documentação: " + activeApplicationProvider.getListOfErrors(), Toast.LENGTH_LONG).show();
                }
            });
            activeApplicationProvider.execute(); // call this method to run the provider
        }
        else{
            Partner partnerDeveloper = new Partner(userModels.get(0));
            userModels.set(0,partnerDeveloper);
            GlobalInformations.sessionApplication.setUserModelList(userModels);
        }

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Boolean possuiinternet = Utils.verificaConexao(manager);
        toggleLAN(possuiinternet);

/*
        ImageView iv_lan_status = (ImageView)findViewById(R.id.iv_network_status);
        if (!possuiinternet)
            iv_lan_status.setImageResource(R.drawable.ic_lan_disconnect_white_24dp);
        else
            iv_lan_status.setImageResource(R.drawable.ic_lan_connect_white_24dp);


        ImageView imageViewBluetoothStatus = (ImageView)findViewById(R.id.iv_bluetooth_status);
        imageViewBluetoothStatus.setImageResource(R.drawable.ic_bluetooth_off_white_24dp);
*/


        if (Utils.verificaConexao(manager))

            // check if there's any pinpad connected
            if (Utils.isConnectedWithPinpad()) {

                DisplayMessageProvider displayMessageProvider = new DisplayMessageProvider(this, "  Multipagos        ELEKTRO          ", GlobalInformations.getPinpadFromListAt(0));
                displayMessageProvider.setDialogTitle("Multipagos");
                displayMessageProvider.setConnectionCallback(new StoneCallbackInterface() {
                    public void onSuccess() {

                    }

                    public void onError() {
                        Toast.makeText(getApplicationContext(), "A conexão com o Pinpad foi perdida", Toast.LENGTH_LONG).show();
                    }
                });
                displayMessageProvider.execute();
                //imageViewBluetoothStatus.setImageResource(R.drawable.ic_bluetooth_connect_white_24dp);
                //Sem pagamento de arrecadacao neste momento
                btnPagarArrecadacao.setEnabled(false);
                btnPagarArrecadacao.setBackgroundColor(Color.GRAY);

                btnContasemFatura.setEnabled(true);
                btnContasemFatura.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                btnTransactionsButton.setEnabled(true);
                btnTransactionsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                toggleBluetooth(true);

            }
            else
            {
                if (!Build.FINGERPRINT.startsWith("generic") ) {
                    btnContasemFatura.setBackgroundColor(Color.GRAY);
                    btnTransactionsButton.setBackgroundColor(Color.GRAY);
                    btnTransactionsButton.setBackgroundColor(Color.GRAY);
                    btnPagarArrecadacao.setBackgroundColor(Color.GRAY);

                    //btnTransactionsButton.setEnabled(false);
                    btnPagarArrecadacao.setEnabled(false);
                    //btnContasemFatura.setEnabled(false);

                }
                toggleBluetooth(true);

            }


    }

    public void testarPagamento() {
//TOOD: Testar timeout de pagamento
            // check is there's a pinpad connected
            if (Utils.isConnectedWithPinpad() == true) {

                final StoneTransaction stoneTransaction = new StoneTransaction(GlobalInformations.getPinpadFromListAt(0));
                stoneTransaction.setAmount("50"); // R$ 0,50
                stoneTransaction.setRequestId("123465789"); // ID in portal
                stoneTransaction.setShortName("MULTIPAGOS"); // name that will appears in stratum client
                stoneTransaction.setInstalmentTransactionEnum(InstalmentTransactionEnum.ONE_INSTALMENT); // transaction "a vista"
                stoneTransaction.setTypeOfTransaction(TypeOfTransactionEnum.DEBIT);
                stoneTransaction.setUserModel(GlobalInformations.getUserModel(0));

                // create and pass transaction
                final TransactionProvider transactionProvider = new TransactionProvider(this, stoneTransaction, GlobalInformations.getPinpadFromListAt(0));
                transactionProvider.setWorkInBackground(false);
                transactionProvider.setConnectionCallback(new StoneCallbackInterface() {

                    public void onSuccess() {
                        if (transactionProvider.getTransactionStatus() == TransactionStatusEnum.APPROVED) {
                            Toast.makeText(getApplicationContext(), "Sua transação foi efetuada com sucesso", Toast.LENGTH_LONG).show();

                            finish();
                        } else if (transactionProvider.getTransactionStatus() == TransactionStatusEnum.REJECTED) {
                            Toast.makeText(getApplicationContext(), "Sua transação foi rejeitada pelo autorizador", Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), transactionProvider.getMessageFromAuthorize().toString(), Toast.LENGTH_SHORT).show();
                        }
                        else if (transactionProvider.getTransactionStatus() == TransactionStatusEnum.TECHNICAL_ERROR)
                            Toast.makeText(getApplicationContext(), "Houve um erro técnico durante o processamento da transação, tente novamente", Toast.LENGTH_LONG).show();
                        else // DECLINED
                            Toast.makeText(getApplicationContext(), "Sua transação foi negada", Toast.LENGTH_LONG).show();
                    }


                    public void onError() {

                    }
                });
                transactionProvider.execute();

            } else
                Toast.makeText(getApplicationContext(), "Você não possui conexão com Pinpad para passar transações", Toast.LENGTH_LONG).show();
        }

}
