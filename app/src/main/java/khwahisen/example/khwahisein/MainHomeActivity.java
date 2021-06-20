`package khwahisen.example.khwahisein;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainHomeActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private TextView totalDonation, totalContributor,totalHelped,totalNeedy;
    private Button donateBtn;
    private ImageSlider bannerSlider;
    private BottomSheetDialog paymentDialogue;
    private TextView upi_lencho,gpay,ppay,Apay;
    private Button OtherUpi;
    final int UPI_PAYMENT = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        firebaseFirestore = FirebaseFirestore.getInstance();
        totalDonation=findViewById(R.id.homeactivity_td);
        totalContributor=findViewById(R.id.homeactivity_tc);
        totalHelped=findViewById(R.id.hoeactivity_th);
        totalNeedy=findViewById(R.id.homeactivity_tn);
        donateBtn=findViewById(R.id.bottom_donate_btn);
        paymentDialogue = new BottomSheetDialog(this);
        paymentDialogue.setContentView(R.layout.payment_dialogue);
        upi_lencho=paymentDialogue.findViewById(R.id.Mainhomeupitext);
        gpay=paymentDialogue.findViewById(R.id.Mainhomegpay);
        ppay=paymentDialogue.findViewById(R.id.Mainhomeppay);
        Apay=paymentDialogue.findViewById(R.id.MainhomeApay);
        OtherUpi=paymentDialogue.findViewById(R.id.Mainhomepaymentbutton);

        bannerSlider=findViewById(R.id.homeactivity_banner_slider);
        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentDialogue.show();
            }
        });



        OtherUpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payUsingUpi();
            }
        });


    }




    void payUsingUpi() {

        Uri uri = new Uri.Builder().scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", "8081532834@okbizaxis")
                .appendQueryParameter("pn", "Sumit Sharma")
                .appendQueryParameter("mc", "BCR2DN6T562YZ63B")
                .appendQueryParameter("tn", "khwahisen")
                .appendQueryParameter("am", String.valueOf(1))
                .appendQueryParameter("tid", "")
                .appendQueryParameter("tr", "")
                .appendQueryParameter("cu", "INR")
                .build();





        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(MainHomeActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(MainHomeActivity.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(MainHomeActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(MainHomeActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainHomeActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainHomeActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

}