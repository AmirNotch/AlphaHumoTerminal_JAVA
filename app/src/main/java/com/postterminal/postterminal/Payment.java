package com.postterminal.postterminal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.postterminal.postterminal.JsonObjects.PaymentData;
import com.postterminal.postterminal.PostRequest.APIInterface;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.Printer;
import com.zcs.sdk.SdkData;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.card.CardInfoEntity;
import com.zcs.sdk.card.CardReaderManager;
import com.zcs.sdk.card.CardReaderTypeEnum;
import com.zcs.sdk.card.MagCard;
import com.zcs.sdk.listener.OnSearchCardListener;
import com.zcs.sdk.print.PrnStrFormat;
import com.zcs.sdk.print.PrnTextFont;
import com.zcs.sdk.print.PrnTextStyle;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.postterminal.postterminal.LoginActivity.Access_token;
import static com.postterminal.postterminal.PayFragment.Sum;
import static com.postterminal.postterminal.PhoneActivity.number1;
import static com.zcs.sdk.card.CardReaderTypeEnum.MAG_CARD;

public class Payment extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText amount;

    APIInterface apiInterface;

    public int copy = 0;

    private static final int READ_TIMEOUT = 60 * 1000;

    private MagCard mMagCard;

    private Handler mHandler;

    private CardReaderManager mCardReadManager;

    private ProgressDialog mProgressDialog;

    private Dialog mCardInfoDialog;

    CardReaderTypeEnum mCardType = CardReaderTypeEnum.MAG_IC_RF_CARD;

    private static final int MSG_CARD_OK = 2001;
    private static final int MSG_CARD_ERROR = 2002;
    private static final int MSG_CARD_APDU = 2003;
    private static final int MSG_RF_CARD_APDU = 2007;
    private static final int MSG_CARD_M1 = 2004;
    private static final int MSG_CARD_MF_PLUS = 2005; 


    public static final byte[] APDU_SEND_IC = {0x00, (byte) 0xA4, 0x04, 0x00, 0x0E, 0x31, 0x50, 0x41, 0x59, 0x2E, 0x53, 0x59, 0x53, 0x2E, 0x44, 0x44, 0x46, 0x30, 0x31, 0X00};
    public static final byte[] APDU_SEND_RF = {0x00, (byte) 0xA4, 0x04, 0x00, 0x0E, 0x32, 0x50, 0x41, 0x59, 0x2E, 0x53, 0x59, 0x53, 0x2E, 0x44, 0x44, 0x46, 0x30, 0x31, 0x00};
    public static final byte[] APDU_SEND_RANDOM = {0x00, (byte) 0x84, 0x00, 0x00, 0x08};
    // 10 06 01 2E 45 76 BA C5 45 2B 01 09 00 01 80 00
    public static final byte[] APDU_SEND_FELICA = {0x10, 0x06, 0x01, 0x2E, 0x45, 0x76, (byte) 0xBA, (byte) 0xC5, 0x45, 0x2B, 0x01, 0x09, 0x00, 0x01, (byte) 0x80, 0x00};
    private static final String KEY_APDU = "APDU";
    private static final String KEY_RF_CARD_TYPE = "RF_CARD_TYPE";
    private static final byte SLOT_USERCARD = 0x00;
    private static final byte SLOT_PSAM1 = 0x01;
    private static final byte SLOT_PSAM2 = 0x02;

    public String Pan;
    public String EXPDATE;


    private MutableLiveData<String> CardNumber;
    private MutableLiveData<String> ExpdateCard;

    private String userPINCODE = "";

    private List<View> codePoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        View imgPin1 = findViewById (R.id.imgPin1);
        View imgPin2 = findViewById (R.id.imgPin2);
        View imgPin3 = findViewById (R.id.imgPin3);
        View imgPin4 = findViewById (R.id.imgPin4);

        codePoint = List.of (imgPin1,imgPin2,imgPin3,imgPin4);

        CardNumber = new MutableLiveData<>();
        ExpdateCard = new MutableLiveData<>();

        /*card_number = findViewById(R.id.Card_Number);
        card_expdate = findViewById(R.id.Expdate);*/

        amount = findViewById(R.id.summa);

        amount.setText (Sum);

        /*card_number.setText(Pan);
        card_expdate.setText(EXPDATE);*/

        /*CardNumber.postValue(Pan);
        ExpdateCard.postValue(EXPDATE);*/

        /*CardNumber.observe(this, new Observer< String >() {
            @Override
            public void onChanged(String s) {
                card_number.setText(s);
            }
        });

        ExpdateCard.observe(this, new Observer< String >() {
            @Override
            public void onChanged(String s) {
                card_expdate.setText(s);
            }
        });*/

        View rootView = findViewById(android.R.id.content);


        //===================================================================

        Listener();



        //===================================================================================================

        final List< TextInputLayout > textInputLayouts = Utils.findViewsWithType(
                rootView, TextInputLayout.class);

        Button button = findViewById(R.id.pay_transaction);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*@Override
            public void onClick(View view) {
                boolean noErrors = true;
                for (TextInputLayout textInputLayout : textInputLayouts) {
                    String editTextString = textInputLayout.getEditText().getText().toString();
                    if (editTextString.isEmpty()) {
                        textInputLayout.setError(getResources().getString(R.string.error_string));
                        noErrors = false;
                    } else {
                        textInputLayout.setError(null);
                    }
                }

                if (noErrors) {
                    // All fields are valid!*/

                String ID = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request originalRequest = chain.request();

                                Request newRequest = originalRequest.newBuilder()
                                        //.headers()
                                        //.header("Authorization", main_auth)
                                        //.header("uuid_device", ID)*/
                                        .addHeader("Authorization", "Bearer " + Access_token)
                                        .addHeader("uuid", ID)
                                        .build();

                                return chain.proceed(newRequest);
                            }
                        })
                        .addInterceptor(loggingInterceptor)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.43.26:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();

                apiInterface = retrofit.create(APIInterface.class);

                AlertDialog alertDialog = new AlertDialog.Builder(Payment.this)
//set icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                        .setTitle("Оплата")
//set message
                        .setMessage("Вы действительно хотите оплатить")
//set positive button
                        .setPositiveButton("Подтверждаю", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked

                                double amountEnd = Integer.parseInt(amount.getText().toString());

                                String HashPinCode = getMd5("humo2020" + userPINCODE);

                                Log.d ("Payment",number1 + " " + Pan + " " +  EXPDATE + " " +  HashPinCode + " " +  amountEnd+ " ");

                                PaymentData Payment = new PaymentData(number1,/*card_number.getText().toString()*/Pan, EXPDATE/*card_expdate.getText().toString()*/, HashPinCode, amountEnd);

                                Call< PaymentData > call = apiInterface.doPaymentData(Payment);

                                call.enqueue(new Callback< PaymentData >() {
                                    @Override
                                    public void onResponse(Call< PaymentData > call, Response< PaymentData > response) {

                                        PaymentData resource = response.body();

                                        String result_code = resource.result_code;
                                        String result_desc = resource.result_desc;

                                        if (result_code.equals("1")) {
                                            Toast.makeText(getApplicationContext(), "Поздравляем вы совершили оплату", Toast.LENGTH_SHORT).show();
                                        }
                                        if (result_code.equals("28")) {
                                            Toast.makeText(getApplicationContext(), "Поздравляем Вы Совершили Оплату", Toast.LENGTH_SHORT).show();
                                            PrintCheck();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call< PaymentData > call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "Произошла ошибка на стороне инета", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })
//set negative button
                        .setNegativeButton("Отказаться", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                Toast.makeText(getApplicationContext(), "Вы отказались от оплаты", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });


        ImageView refresh = findViewById(R.id.refreshing);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*card_number.setText(Pan);
                card_expdate.setText(EXPDATE);*/

                View rootView = findViewById(android.R.id.content);

                //===================================================================

                Listener();

                /*DriverManager mDriverManager = DriverManager.getInstance();

                mCardReadManager = mDriverManager.getCardReadManager();

                mMagCard = mCardReadManager.getMAGCard();

                mCardReadManager = mDriverManager.getCardReadManager();

                CardNumber.setValue(Pan);
                ExpdateCard.setValue(EXPDATE);

                mCardReadManager.cancelSearchCard();
                mCardReadManager.searchCard(MAG_CARD, READ_TIMEOUT, mListener);
                showSearchCardDialog(R.string.title_waiting, R.string.msg_mag_card);
                boolean isM1 = false;
                boolean isMfPlus = false;
                OnSearchCardListener mListener = new OnSearchCardListener() {
                    @Override
                    public void onCardInfo(CardInfoEntity cardInfoEntity) {
                        CardReaderTypeEnum cardType = cardInfoEntity.getCardExistslot();
                        readMagCard();
                        Toast.makeText(getApplicationContext(),"dawdaw",Toast.LENGTH_LONG).show();

                        switch (cardType) {
                            case MAG_CARD:
                                readMagCard();
                                Toast.makeText(getApplicationContext(),"dawdaw",Toast.LENGTH_LONG).show();
                            case PSIM2:
                                //readICCard(CardSlotNoEnum.SDK_ICC_SAM2);
                                break;
                        }
                    }
                    @Override
                    public void onError(int i) {
                        *//*isM1 = false;
                        isMfPlus = false;*//*
                        mHandler.sendEmptyMessage(i);
                    }
                    @Override
                    public void onNoCard(CardReaderTypeEnum cardReaderTypeEnum, boolean b) {
                    }
                };*/
            }
        });

    }


   @Override
    public void onClick(View view){
           switch (view.getId ()) {
               case R.id.ll1:
                   userPINCODE += "1";
                   break;
               case R.id.ll2:
                   userPINCODE += "2";
                   break;
               case R.id.ll3:
                   userPINCODE += "3";
                   break;
               case R.id.ll4:
                   userPINCODE += "4";
                   break;
               case R.id.ll5:
                   userPINCODE += "5";
                   break;
               case R.id.ll6:
                   userPINCODE += "6";
                   break;
               case R.id.ll7:
                   userPINCODE += "7";
                   break;
               case R.id.ll8:
                   userPINCODE += "8";
                   break;
               case R.id.ll9:
                   userPINCODE += "9";
                   break;
               case R.id.ll0:
                   userPINCODE += "0";
                   break;
               default:
                   if (!userPINCODE.isEmpty ())
                       userPINCODE = userPINCODE.substring (0, userPINCODE.length () - 1);
                   break;
           }

           userPINCODE = userPINCODE.substring (0, Math.min (4, userPINCODE.length ()));
           Log.d("Warr",codePoint.size ()+ "  " +  userPINCODE.length());
           onChangePinField ();
    }




    @SuppressLint("ResourceAsColor")
    public void onChangePinField(){
        Log.d("Warr",codePoint.size ()+ "  " +  userPINCODE.length());
        for (int i = 0; i < codePoint.size();i++){
            if (i < userPINCODE.length()){
                codePoint.get(i).setBackground (getDrawable (R.drawable.ic_circle_orange));
                //codePoint.get(i).setBackgroundTintList(ColorStateList.valueOf (Color.BLUE));
            }
            if (i >= userPINCODE.length()){
                //codePoint.get(i).setBackgroundTintList(ColorStateList.valueOf(R.color.black));
                codePoint.get(i).setBackground (getDrawable (R.drawable.ic_circle_grey));

            }
        }
    }


    public void Listener() {

        DriverManager mDriverManager = DriverManager.getInstance();

        mCardReadManager = mDriverManager.getCardReadManager();

        mMagCard = mCardReadManager.getMAGCard();

        mCardReadManager = mDriverManager.getCardReadManager();

        mHandler = new CardHandler(this);

        mCardReadManager.cancelSearchCard();
        mCardReadManager.searchCard(MAG_CARD, READ_TIMEOUT, mListener);

        boolean isM1 = false;
        boolean isMfPlus = false;
        OnSearchCardListener mListener = new OnSearchCardListener() {
            @Override
            public void onCardInfo(CardInfoEntity cardInfoEntity) {
                CardReaderTypeEnum cardType = cardInfoEntity.getCardExistslot();
                readMagCard();
                Toast.makeText(getApplicationContext(),"dawdaw",Toast.LENGTH_LONG).show();

                switch (cardType) {
                            /*case RF_CARD:
// only can get SdkData.RF_TYPE_A / SdkData.RF_TYPE_B /
                                SdkData.RF_TYPE_FELICA / SdkData.RF_TYPE_MEMORY_A / SdkData.RF_TYPE_MEMORY_B
                                byte rfCardType = cardInfoEntity.getRfCardType();
                                Log.e(TAG, "rfCardType: " + rfCardType);
                                if (isM1) {
                                    readM1Card();
                                } else if (isMfPlus) {
                                    readMFPlusCard();
                                } else {
                                    if (rfCardType == SdkData.RF_TYPE_FELICA) { // felica card
                                        readFelica();
                                    } else if (rfCardType == SdkData.RF_TYPE_A || rfCardType ==
                                            SdkData.RF_TYPE_B) {
                                        readCpuCard();
                                    }
                                }
                                break;*/
                    case MAG_CARD:
                        readMagCard();
                        Toast.makeText(getApplicationContext(),"dawdaw",Toast.LENGTH_LONG).show();
                            /*case IC_CARD:
                                readICCard(CardSlotNoEnum.SDK_ICC_USERCARD);
                                break;
                            case PSIM1:
                                readICCard(CardSlotNoEnum.SDK_ICC_SAM1);
                                break;*/
                    case PSIM2:
                        //readICCard(CardSlotNoEnum.SDK_ICC_SAM2);
                        break;
                }
            }
            @Override
            public void onError(int i) {
                        /*isM1 = false;
                        isMfPlus = false;*/
                mHandler.sendEmptyMessage(i);
            }
            @Override
            public void onNoCard(CardReaderTypeEnum cardReaderTypeEnum, boolean b) {
            }
        };

    }
    @SuppressLint("SetTextI18n")
    public void readMagCard() {
        CardInfoEntity cardInfo = mMagCard.getMagTrackData();
        Log.d("TAG", "cardInfo.getResultcode():" + cardInfo.getResultcode());
        if (cardInfo.getResultcode() == SdkResult.SDK_OK) {
            String exp = cardInfo.getExpiredDate();
            String cardNo = cardInfo.getCardNo();
            String tk1 = cardInfo.getTk1();
            String tk2 = cardInfo.getTk2();
            String tk3 = cardInfo.getTk3();

            String cardexpdate = "5058270301095005=2307121193364380";
            String[] s = tk2.split("=");
            String pan = s[0];
            String EXPDATEE = s[1];

            StringBuilder count = new StringBuilder();
            StringBuilder count1 = new StringBuilder();
            StringBuilder count2 = new StringBuilder();
            for (int i = 0; i < 4; i++){
                count.append(EXPDATEE.charAt(i));
            }
            for (int a = 0;a < 2; a++){
                count1.append(count.charAt(a));
            }
            for (int d = 2;d < 4; d++){
                count2.append(count.charAt(d));
            }

            Pan = pan;
            EXPDATE = count2.toString() + count1.toString();

            /* System.out.println(count);
            System.out.println(count1);
            System.out.println(count2);
            System.out.println(count2.toString() + count1.toString());
            System.out.println(EXPDATE);
            System.out.println(pan);*/

            Message msg = Message.obtain();
            msg.what = MSG_CARD_OK;
            msg.arg1 = cardInfo.getResultcode();
            msg.obj = cardInfo;
            Log.d("Warning",tk1 + " " + tk2  + " " + tk3  + " " + exp  + " " + cardNo  + " " + msg.what  + " " + msg.arg1  + " " + msg.obj);
            mHandler.sendMessage(msg);

        } else {
            mHandler.sendEmptyMessage(cardInfo.getResultcode());
        }
        mMagCard.magCardClose();
        // search again
        mCardReadManager.searchCard(mCardType, READ_TIMEOUT, mListener);


    }


    OnSearchCardListener mListener = new OnSearchCardListener() {
        @Override
        public void onCardInfo(CardInfoEntity cardInfoEntity) {
            CardReaderTypeEnum cardType = cardInfoEntity.getCardExistslot();
            switch (cardType) {
                case MAG_CARD:
                    // onCardDetect()
                    readMagCard();
                    break;
            }
        }

        @Override
        public void onError(int i) {
            /*isM1 = false;
            isMfPlus = false;
            isNtag = false;*/
            mHandler.sendEmptyMessage(i);
        }

        @Override
        public void onNoCard(CardReaderTypeEnum cardReaderTypeEnum, boolean b) {

        }
    };


    private void showSearchCardDialog(@StringRes int title, @StringRes int msg) {
        mProgressDialog = (ProgressDialog) DialogUtils.showProgress(Payment.this, getString(title), getString(msg), new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mCardReadManager.cancelSearchCard();
            }
        });
    }
    public void PrintCheck(){
        DriverManager mDriverManager = DriverManager.getInstance();
        Printer mPrinter = mDriverManager.getPrinter();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();

        int printStatus = mPrinter.getPrinterStatus();
        if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
        } else {
            PrnStrFormat format = new PrnStrFormat();
            format.setTextSize(30);
            format.setAli(Layout.Alignment.ALIGN_CENTER);
            format.setStyle(PrnTextStyle.BOLD);
            format.setFont(PrnTextFont.CUSTOM);
            mPrinter.setPrintAppendString(getResources().getString(R.string.pos_sales_slip), format);
            format.setTextSize(25);
            format.setStyle(PrnTextStyle.NORMAL);
            format.setAli(Layout.Alignment.ALIGN_NORMAL);
            mPrinter.setPrintAppendString(" ", format);
            mPrinter.setPrintAppendString(getResources().getString(R.string.merchant_name), format);
            mPrinter.setPrintAppendString(getResources().getString(R.string.merchant_no)+ " " + number1, format);
            mPrinter.setPrintAppendString(getResources().getString(R.string.terminal_name), format);
            mPrinter.setPrintAppendString(getResources().getString(R.string.operator_no) + " Ашан ", format);
            mPrinter.setPrintAppendString(getResources().getString(R.string.card_no) + " 0200511645 ", format);
            mPrinter.setPrintAppendString(getResources().getString(R.string.exe_date) + "  " + formatter.format(date), format);
            mPrinter.setPrintAppendString(getResources().getString(R.string.acq_institute) + " 888800080", format);
            mPrinter.setPrintAppendString(getResources().getString(R.string.iss) + " ", format);
            mPrinter.setPrintAppendString(getResources().getString(R.string.sale), format);
            mPrinter.setPrintAppendString(getResources().getString(R.string.batch_no) + " 0 ", format);
            mPrinter.setPrintAppendString(getResources().getString(R.string.trans_type), format);
            mPrinter.setPrintAppendString(" ---------------------------------", format);
            format.setAli(Layout.Alignment.ALIGN_NORMAL);
            format.setTextSize(32);
            format.setStyle(PrnTextStyle.BOLD);
            mPrinter.setPrintAppendString("Сумма: TJS   " + amount.getText().toString(), format);
            format.setAli(Layout.Alignment.ALIGN_NORMAL);
            format.setStyle(PrnTextStyle.NORMAL);
            format.setTextSize(25);
            mPrinter.setPrintAppendString(" ---------------------------------", format);
            mPrinter.setPrintAppendString("Терминал: ОАО " +  "Humo Банк", format);
            mPrinter.setPrintAppendString("Тел (+992) 88 777 55 44", format);
            mPrinter.setPrintAppendString(" Копия " + ++copy, format);
            mPrinter.setPrintAppendString(" ", format);
            mPrinter.setPrintAppendString(" ", format);
            mPrinter.setPrintAppendString(" ", format);
            printStatus = mPrinter.setPrintStart();
        }
    }
    public static String getMd5(String input)
    {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    void closeSearch() {
        Log.i("TAG", "closeSearch");
        mCardReadManager.cancelSearchCard();
    }

    class CardHandler extends Handler implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
        WeakReference<AppCompatActivity> mFragment;


        CardHandler(AppCompatActivity fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            Payment fragment = (Payment) mFragment.get();
            if (mCardInfoDialog != null) {
                mCardInfoDialog.dismiss();
            }
            if (fragment.mProgressDialog != null) {
                fragment.mProgressDialog.dismiss();
            }
            switch (msg.what) {
                case MSG_CARD_OK:
                    CardInfoEntity cardInfoEntity = (CardInfoEntity) msg.obj;
                    MyApp.cardInfoEntity = cardInfoEntity;
                    mCardInfoDialog = DialogUtils.show(Payment.this,
                            fragment.getString(R.string.title_card), SDK_Result.obtainCardInfo(Payment.this, cardInfoEntity),
                            "OK", this, this);
                    break;
                case MSG_CARD_APDU:
                    mCardInfoDialog = DialogUtils.show(Payment.this,
                            fragment.getString(R.string.title_apdu),
                            SDK_Result.appendMsg("Code", msg.arg1 + "", "APDU send", msg.getData().getString(Payment.KEY_APDU), "APDU response", (String) msg.obj),
                            "OK", this, this);
                    break;
                case MSG_RF_CARD_APDU:
                    byte rfCardType = msg.getData().getByte(KEY_RF_CARD_TYPE);
                    String type = handleRfCardType(rfCardType);
                    mCardInfoDialog = DialogUtils.show(Payment.this, type,
                            SDK_Result.appendMsg("Code", msg.arg1 + "", "Send", msg.getData().getString(Payment.KEY_APDU), "Response", (String) msg.obj),
                            "OK", this, this);
                    break;
                case MSG_CARD_M1:
                    mCardInfoDialog = DialogUtils.show(Payment.this,
                            fragment.getString(R.string.title_card), (String) msg.obj,
                            "OK", this, this);
                    break;

                case MSG_CARD_MF_PLUS:
                    mCardInfoDialog = DialogUtils.show(Payment.this,
                            fragment.getString(R.string.title_card), (String) msg.obj,
                            "OK", this, this);
                    break;
                default:
                    mCardInfoDialog = DialogUtils.show(Payment.this,
                            fragment.getString(R.string.title_error),
                            SDK_Result.obtainMsg(getApplicationContext(), msg.what),
                            "OK", this, this);
                    break;
            }
        }

        private String handleRfCardType(byte rfCardType) {
            String type = "";
            switch (rfCardType) {
                case SdkData.RF_TYPE_A:
                    type = "RF_TYPE_A";
                    break;
                case SdkData.RF_TYPE_B:
                    type = "RF_TYPE_B";
                    break;
                case SdkData.RF_TYPE_MEMORY_A:
                    type = "RF_TYPE_MEMORY_A";
                    break;
                case SdkData.RF_TYPE_FELICA:
                    type = "RF_TYPE_FELICA";
                    break;
                case SdkData.RF_TYPE_MEMORY_B:
                    type = "RF_TYPE_MEMORY_B";
                    break;
            }
            return type;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            Payment fragment = (Payment) mFragment.get();
            closeSearch();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Payment fragment = (Payment) mFragment.get();
            closeSearch();
        }
    }
}
