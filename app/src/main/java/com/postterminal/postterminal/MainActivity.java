package com.postterminal.postterminal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.postterminal.postterminal.JsonObjects.LogOut;
import com.postterminal.postterminal.PostRequest.APIInterface;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.Printer;
import com.zcs.sdk.SdkData;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.Sys;
import com.zcs.sdk.bluetooth.BluetoothManager;
import com.zcs.sdk.bluetooth.emv.BluetoothHandler;
import com.zcs.sdk.bluetooth.emv.CardDetectedEnum;
import com.zcs.sdk.bluetooth.emv.EmvStatusEnum;
import com.zcs.sdk.bluetooth.emv.OnBluetoothEmvListener;
import com.zcs.sdk.card.CardInfoEntity;
import com.zcs.sdk.card.CardReaderManager;
import com.zcs.sdk.card.CardReaderTypeEnum;
import com.zcs.sdk.card.ICCard;
import com.zcs.sdk.card.MagCard;
import com.zcs.sdk.card.RfCard;
import com.zcs.sdk.listener.OnSearchCardListener;

import java.io.IOException;
import java.lang.ref.WeakReference;

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

public class MainActivity extends AppCompatActivity {


    APIInterface apiInterface;

    MaterialCardView pay, changepin;

   /* Button printout, printpaper, printimage, scanCard,secanCard,sicanCard;*/

    private Handler mHandler;

    private static final int READ_TIMEOUT = 60 * 1000;

    private ProgressDialog mProgressDialog;

    private CardReaderManager mCardReadManager;
    private ICCard mICCard;
    private RfCard mRfCard;
    private MagCard mMagCard;

    CardReaderTypeEnum mCardType = CardReaderTypeEnum.MAG_IC_RF_CARD;

    byte mRfCardType = 0;


    // private Printer mPrinter;


    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HIIstory ()).commit();
        }

        mHandler = new CardHandler(this);

        /*DriverManager mDriverManager = DriverManager.getInstance();
        Sys mSys = mDriverManager.getBaseSysDevice();

        Printer mPrinter = mDriverManager.getPrinter();*/

        pay = findViewById(R.id.pay);
        changepin = findViewById(R.id.ChangePin);


       /* mCardReadManager = mDriverManager.getCardReadManager();
        mICCard = mCardReadManager.getICCard();
        mRfCard = mCardReadManager.getRFCard();
        mMagCard = mCardReadManager.getMAGCard();


        CardReaderManager mCardReadManager = mDriverManager.getCardReadManager();



        BluetoothManager mBluetoothManager = BluetoothManager.getInstance();
        BluetoothHandler mBluetoothHandler = mDriverManager.getBluetoothHandler();
*/
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


       /* printout.setOnClickListener(v -> {
            *//*int statue = mSys.getFirmwareVer(new String[1]);
            if (statue != SdkResult.SDK_OK) {
                int sysPowerOn = mSys.sysPowerOn();
                Log.d("TAG_TEST", "sysPowerOn: " + sysPowerOn);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int i = mSys.sdkInit();
            if (i == SdkResult.SDK_OK) {
                Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_LONG).show();
            }*//*

            int printStatus = mPrinter.getPrinterStatus();
            if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
            } else {
                PrnStrFormat format = new PrnStrFormat();
                format.setTextSize(30);
                format.setAli(Layout.Alignment.ALIGN_CENTER);
                format.setStyle(PrnTextStyle.BOLD);
                format.setFont(PrnTextFont.CUSTOM);

                format.setTextSize(25);
                format.setStyle(PrnTextStyle.NORMAL);
                format.setAli(Layout.Alignment.ALIGN_NORMAL);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                printStatus = mPrinter.setPrintStart();

            }


        });


        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        printpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int printStatus = mPrinter.getPrinterStatus();
                if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                } else {
                    PrnStrFormat format = new PrnStrFormat();
                    format.setTextSize(30);
                    format.setAli(Layout.Alignment.ALIGN_CENTER);
                    format.setStyle(PrnTextStyle.BOLD);
                    format.setFont(PrnTextFont.CUSTOM);

                   *//* mPrinter.setPrintAppendString("POS SALES SLIP", format);
                    format.setTextSize(25);
                    format.setStyle(PrnTextStyle.NORMAL);
                    format.setAli(Layout.Alignment.ALIGN_NORMAL);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString("MERCHANGT NAME:" + " Test ", format);
                    mPrinter.setPrintAppendString("MERCHANT NO:" + " 123456789012345 ", format);
                    mPrinter.setPrintAppendString("TERMINAL NAME:" + " 12345678 ", format);
                    mPrinter.setPrintAppendString("OPERATOR NO:" + " 01 ", format);
                    mPrinter.setPrintAppendString("CARD NO: ", format);
                    format.setAli(Layout.Alignment.ALIGN_CENTER);
                    format.setTextSize(30);
                    format.setStyle(PrnTextStyle.BOLD);
                    mPrinter.setPrintAppendString("6214 44** **** **** 7816", format);
                    format.setAli(Layout.Alignment.ALIGN_NORMAL);
                    format.setStyle(PrnTextStyle.NORMAL);
                    format.setTextSize(25);
                    mPrinter.setPrintAppendString(" -----------------------------", format);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(" ", format);
                    printStatus = mPrinter.setPrintStart();*//*

                    mPrinter.setPrintAppendString(getResources().getString(R.string.pos_sales_slip), format);
                    format.setTextSize(25);
                    format.setStyle(PrnTextStyle.NORMAL);
                    format.setAli(Layout.Alignment.ALIGN_NORMAL);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(getResources().getString(R.string.merchant_name), format);
                    mPrinter.setPrintAppendString(getResources().getString(R.string.merchant_no) + " 900728078 ", format);
                    mPrinter.setPrintAppendString(getResources().getString(R.string.terminal_name), format);
                    mPrinter.setPrintAppendString(getResources().getString(R.string.operator_no) + " Пойтахт Тичорат ", format);
                    mPrinter.setPrintAppendString(getResources().getString(R.string.card_no) + " 0200511645 ", format);
                    mPrinter.setPrintAppendString(getResources().getString(R.string.exe_date) + " " + formatter.format(date), format);
                    mPrinter.setPrintAppendString(getResources().getString(R.string.acq_institute) + " 888800080", format);
                    mPrinter.setPrintAppendString(getResources().getString(R.string.iss) + " ", format);
                    mPrinter.setPrintAppendString(getResources().getString(R.string.sale), format);
                    mPrinter.setPrintAppendString("" + " 0200511645 ", format);
                    mPrinter.setPrintAppendString(getResources().getString(R.string.batch_no) + " 0 ", format);
                    mPrinter.setPrintAppendString(getResources().getString(R.string.trans_type), format);
                    mPrinter.setPrintAppendString(" -----------------------------", format);
                    format.setAli(Layout.Alignment.ALIGN_NORMAL);
                    format.setTextSize(32);
                    format.setStyle(PrnTextStyle.BOLD);
                    mPrinter.setPrintAppendString("Сумма: TJS " + " ", format);
                    format.setAli(Layout.Alignment.ALIGN_NORMAL);
                    format.setStyle(PrnTextStyle.NORMAL);
                    format.setTextSize(25);
                    mPrinter.setPrintAppendString(" Терминал: ОАО " +  "{Humo Банк}", format);
                    mPrinter.setPrintAppendString("Тел (+992) 88 777 55 44", format);
                    mPrinter.setPrintAppendString(" Копия ", format);
                    printStatus = mPrinter.setPrintStart();
                }
            }
        });*/

        /*printimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap mBitmapDef = null;
                int printStatus = mPrinter.getPrinterStatus();
                if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                } else {
                    if (mBitmapDef == null) {
                        try {
                            InputStream inputStream =
                                    getAssets().open("print_demo.bmp");
                            Drawable drawable = Drawable.createFromStream(inputStream, null);
                            mBitmapDef = ((BitmapDrawable) drawable).getBitmap();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    PrnStrFormat format = new PrnStrFormat();
                    mPrinter.setPrintAppendBitmap(mBitmapDef, Layout.Alignment.ALIGN_CENTER);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(" ", format);
                    printStatus = mPrinter.setPrintStart();
                    if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                    }
                }

            }
        });*/



        //==============================================

       /* mBluetoothHandler.addEmvListener(new OnBluetoothEmvListener() {
            @Override
            public void onKeyEnter() {

            }

            @Override
            public void onKeyCancel() {

            }

            @Override
            public void onCardDetect(CardDetectedEnum cardDetectedEnum) {

                if (cardDetectedEnum == CardDetectedEnum.SWIPED) {
                    CardInfoEntity magReadData = mBluetoothHandler.getMagData();

                    if (magReadData.getResultcode() == SdkResult.SDK_OK) {
                        String tk1 = magReadData.getTk1();
                        String tk2 = magReadData.getTk2();
                        String tk3 = magReadData.getTk3();
                        String expiredDate = magReadData.getExpiredDate();
                        String cardNo = magReadData.getCardNo();
                        Log.d("card_swipe_test", "tk1: " + tk1);
                        Log.d("card_swipe_test", "tk2: " + tk2);
                        Log.d("card_swipe_test", "tk3: " + tk3);
                        Log.d("card_swipe_test", "expiredDate: " + expiredDate);
                        Log.d("card_swipe_test", "cardNo: " + cardNo);
                        Log.d("card_swipe_test", "isICCard: " + mBluetoothHandler.isICChip());

                    } else {
                        Log.d("card_swipe_test", "Mag card read error: " + magReadData.getResultcode());
                    }
                }

            }

            @Override
            public void onEmvTimeout() {
            }

            @Override
            public void onEnterPasswordTimeout() {
            }

            @Override
            public void onEmvStatus(EmvStatusEnum emvStatusEnum) {
            }
        });*/
        //==============================================




        /*scanCard.setOnClickListener(v -> {
            mCardReadManager.cancelSearchCard();
            mCardReadManager.searchCard(MAG_CARD, 60 * 1000, mListener);
            showSearchCardDialog(R.string.title_waiting, R.string.msg_mag_card);
            searchBankCard(CardReaderTypeEnum.IC_CARD);
        });
        secanCard.setOnClickListener(v -> {
            //mCardReadManager.cancelSearchCard();
            //mCardReadManager.searchCard(MAG_CARD, 60 * 1000, mListener);
            //searchBankCard(CardReaderTypeEnum.IC_CARD);
        });

        sicanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*BluetoothManager mBluetoothManager = BluetoothManager.getInstance();
                BluetoothHandler mBluetoothHandler = mDriverManager.getBluetoothHandler();
                mBluetoothHandler.addEmvListener(new OnBluetoothEmvListener() {
                    @Override
                    public void onKeyEnter() {
                        Log.e("TAG", "onKeyEnter: ");
                    }
                    @Override
                    public void onKeyCancel() {
                        Log.e("TAG", "onKeyCancel: ");
                    }
                    @Override
                    public void onCardDetect(CardDetectedEnum cardDetectedEnum) {
                        Log.e("TAG", "onCardDetect: " + cardDetectedEnum.name());
                        switch (cardDetectedEnum) {
                            case INSERTED:
                                Log.d("RRR","IC card insert");
                                emvRet = mBluetoothHandler.emv(CardReaderTypeEnum.IC_CARD, 100,
                                        "20180811121212", 10);
                                ("Start emv ret: " + emvRet);
                                break;
                            case SWIPED:
                                getMagData();
                                break;
                            case CONTACTLESS_FR:
                                Log.d("Tag","Rf card");
                                emvRet = mBluetoothHandler.emv(CardReaderTypeEnum.RF_CARD, 100,
                                        10);
                                Log.d("Tag","Start emv ret: " + emvRet);
                                break;
                            case REMOVED:
                                Log.d("Tag","IC card remove");
                                break;
                        }
                    }
                    @Override
                    public void onEmvTimeout() {
                        Log.e("TAG", "onEmvTimeout: ");
                        Log.d("Tag","onEmvTimeout");
                    }
                    @Override
                    public void onEnterPasswordTimeout() {
                        Log.e("TAG", "onEnterPasswordTimeout: ");
                    }
                    @Override
                    public void onEmvStatus(EmvStatusEnum emvStatusEnum) {
                        RequiresPermission.Read PBOC card
                        RequiresPermission.Read magnetic stripe card
                        Log.e("TAG", "onEmvStatus: " + emvStatusEnum.name());
                        Log.d("Tag","onEmvStatus: " + emvStatusEnum.name());
                        if (emvStatusEnum == EmvStatusEnum.PBOC_OK || emvStatusEnum ==
                                EmvStatusEnum.QPBOC_OK) {
                            getEmvData();
                        }
                    }
                });*//*

                mCardReadManager.cancelSearchCard();
                mCardReadManager.searchCard(MAG_CARD, 10000, mListener);
                showSearchCardDialog(R.string.title_waiting, R.string.msg_mag_card);
                boolean isM1 = false;
                boolean isMfPlus = false;
                OnSearchCardListener mListener = new OnSearchCardListener() {
                    @Override
                    public void onCardInfo(CardInfoEntity cardInfoEntity) {
                        CardReaderTypeEnum cardType = cardInfoEntity.getCardExistslot();
                     //   readMagCard();
                        Toast.makeText(getApplicationContext(),"dawdaw",Toast.LENGTH_LONG).show();

                        switch (cardType) {
                            *//*case RF_CARD:
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
                                break;*//*
                            case MAG_CARD:
                                readMagCard();
                                Toast.makeText(getApplicationContext(),"dawdaw",Toast.LENGTH_LONG).show();
                            *//*case IC_CARD:
                                readICCard(CardSlotNoEnum.SDK_ICC_USERCARD);
                                break;
                            case PSIM1:
                                readICCard(CardSlotNoEnum.SDK_ICC_SAM1);
                                break;*//*
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
                };
                *//*readMagCard();*//*


            }
        });*/
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.history:
                            selectedFragment = new HistoryFragment ();
                            break;
                        case R.id.Pay_Fragment:
                            selectedFragment = new PayFragment ();
                            String Summ = "";
                            Sum = Summ;
                            break;
                        case R.id.Main_menu:
                            selectedFragment = new Main_MenuFragment ();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
    };
    private void readMagCard() {
// use `getMagReadData` to get mag track data and parse data
// use `getMagTrackData` to get origin track data
//CardInfoEntity cardInfo = mMagCard.getMagReadData();
        /*CardInfoEntity cardInfo = mMagCard.getMagTrackData();
        Log.d("TAG", "cardInfo.getResultcode():" + cardInfo.getResultcode());
        if (cardInfo.getResultcode() == SdkResult.SDK_OK) {
//String exp = cardInfo.getExpiredDate();
//String cardNo = cardInfo.getCardNo();
            String tk1 = cardInfo.getTk1();
            String tk2 = cardInfo.getTk2();
            String tk3 = cardInfo.getTk3();
            Toast.makeText(getApplicationContext(),tk1,Toast.LENGTH_LONG).show();
        }
        mMagCard.magCardClose();*/

        CardInfoEntity cardInfo = mMagCard.getMagTrackData();
        Log.d(TAG, "cardInfo.getResultcode():" + cardInfo.getResultcode());
        if (cardInfo.getResultcode() == SdkResult.SDK_OK) {
            String exp = cardInfo.getExpiredDate();
            String cardNo = cardInfo.getCardNo();
            String tk1 = cardInfo.getTk1();
            String tk2 = cardInfo.getTk2();
            String tk3 = cardInfo.getTk3();
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
    void searchBankCard(CardReaderTypeEnum cardType) {
        mCardType = cardType;
        mRfCardType = SdkData.RF_TYPE_A | SdkData.RF_TYPE_B;
        switch (cardType) {
            case MAG_CARD:
                showSearchCardDialog(R.string.title_waiting, R.string.msg_mag_card);
                break;
        }
        mCardReadManager.cancelSearchCard();
        mCardReadManager.searchCard(cardType, READ_TIMEOUT, mListener);
    }

    private void showSearchCardDialog(@StringRes int title, @StringRes int msg) {
        mProgressDialog = (ProgressDialog) DialogUtils.showProgress(MainActivity.this, getString(title), getString(msg), new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mCardReadManager.cancelSearchCard();
            }
        });
    }

    /*private void printPaperOut() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int printStatus = mPrinter.getPrinterStatus();
                if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtils.show(MainActivity.this, getString(R.string.printer_out_of_paper));

                        }
                    });
                } else {
                    mPrinter.setPrintLine(30);
                }
            }
        }).start();
    }*/

    /*final public Activity getActivity() {
        return mHost == null ? null : mHost.getActivity();
    }

    FragmentHostCallback mHost;
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.Log_out:

                Call< LogOut > call = apiInterface.doLogOut ();

                call.enqueue (new Callback< LogOut > () {
                    @Override
                    public void onResponse(Call< LogOut > call, Response< LogOut > response) {

                        LogOut resource = response.body ();

                        String result_code = resource.result_code;

                        if (result_code.equals ("1")) {
                            startActivity (new Intent (MainActivity.this, PhoneActivity.class));
                        }
                        if (result_code.equals ("0")) {
                            Toast.makeText (getApplicationContext (), "Попробуйте снова!", Toast.LENGTH_SHORT).show ();
                        }
                    }

                    @Override
                    public void onFailure(Call< LogOut > call, Throwable t) {
                        Toast.makeText (getApplicationContext (), "Проверьте ваше интернет соединение!", Toast.LENGTH_SHORT).show ();
                    }

                });

                return true;
            case R.id.Change_pin_code:
                Intent Change_Pin_code = new Intent (getApplicationContext (), ChangePin.class);
                startActivity (Change_Pin_code);
                break;

            default:
                return super.onOptionsItemSelected (item);
        }
        return false;
    }
    private static final String TAG = "CardFragment";
    void closeSearch() {
        Log.i(TAG, "closeSearch");
        mCardReadManager.cancelSearchCard();
    }


    private Dialog mCardInfoDialog;

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


    class CardHandler extends Handler implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
        WeakReference<AppCompatActivity> mFragment;


        CardHandler(AppCompatActivity fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity fragment = (MainActivity) mFragment.get();
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
                    mCardInfoDialog = DialogUtils.show(MainActivity.this,
                            fragment.getString(R.string.title_card), SDK_Result.obtainCardInfo(MainActivity.this, cardInfoEntity),
                            "OK", this, this);
                    break;
                case MSG_CARD_APDU:
                    mCardInfoDialog = DialogUtils.show(MainActivity.this,
                            fragment.getString(R.string.title_apdu),
                            SDK_Result.appendMsg("Code", msg.arg1 + "", "APDU send", msg.getData().getString(MainActivity.KEY_APDU), "APDU response", (String) msg.obj),
                            "OK", this, this);
                    break;
                case MSG_RF_CARD_APDU:
                    byte rfCardType = msg.getData().getByte(KEY_RF_CARD_TYPE);
                    String type = handleRfCardType(rfCardType);
                    mCardInfoDialog = DialogUtils.show(MainActivity.this, type,
                            SDK_Result.appendMsg("Code", msg.arg1 + "", "Send", msg.getData().getString(MainActivity.KEY_APDU), "Response", (String) msg.obj),
                            "OK", this, this);
                    break;
                case MSG_CARD_M1:
                    mCardInfoDialog = DialogUtils.show(MainActivity.this,
                            fragment.getString(R.string.title_card), (String) msg.obj,
                            "OK", this, this);
                    break;

                case MSG_CARD_MF_PLUS:
                    mCardInfoDialog = DialogUtils.show(MainActivity.this,
                            fragment.getString(R.string.title_card), (String) msg.obj,
                            "OK", this, this);
                    break;
                default:
                    mCardInfoDialog = DialogUtils.show(MainActivity.this,
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
            MainActivity fragment = (MainActivity) mFragment.get();
                closeSearch();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            MainActivity fragment = (MainActivity) mFragment.get();
                closeSearch();
        }
    }
}