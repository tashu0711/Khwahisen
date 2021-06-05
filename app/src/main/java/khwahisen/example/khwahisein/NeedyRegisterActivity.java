package khwahisen.example.khwahisein;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class NeedyRegisterActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private TextView Registration;
    private EditText name,father_name,father_occupation,help,contact_num,teacher,college,Class,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needy_register);
        name=findViewById(R.id.needy_name);
        father_name=findViewById(R.id.needyactivity_fn_);
        father_occupation=findViewById(R.id.needyactivity_foc);
        help=findViewById(R.id.needyactivity_help);
        contact_num=findViewById(R.id.needyactivity_con);
        teacher=findViewById(R.id.needyactivity_teacher);
        college=findViewById(R.id.needyactivity_con);
        Class= findViewById(R.id.needyactivity_ron);
        address=findViewById(R.id.needyactivity_adress);
    }
}