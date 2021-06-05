package khwahisen.example.khwahisein;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainHomeActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private TextView totalDonation, totalContributor,totalHelped,totalNeedy;
    private Button donateBtn;
    private ImageSlider bannerSlider;
    private BottomSheetDialog paymentDialogue;

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

        bannerSlider=findViewById(R.id.homeactivity_banner_slider);
        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentDialogue.show();
            }
        });




    }
}