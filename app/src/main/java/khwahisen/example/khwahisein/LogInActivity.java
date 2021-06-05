package khwahisen.example.khwahisein;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";

    private EditText edPassword;
    private Button btnCreateUser;
    private SharedPreferences credentials;
    private TextView signBtn;

    private EditText email;
    private FirebaseFirestore firebaseFirestore;

    ProgressDialog RgDialog;
    private String currentUID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();
        edPassword = findViewById(R.id.signin_password);
        email = findViewById(R.id.sign_in_email);
        btnCreateUser = findViewById(R.id.sign_in_button);
        RgDialog = new ProgressDialog(this);
        RgDialog.setTitle("Please wait!");
        signBtn =  findViewById(R.id.login_signup_btn);
        firebaseFirestore = FirebaseFirestore.getInstance();
        credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, MODE_PRIVATE);
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RgDialog.show();
                login();
            }

        });
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }


    private void login() {
        if (!email.getText().toString().equals("")) {
            if (!edPassword.getText().toString().equals("")) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(),edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull  Task<AuthResult> task) {
                        sharedPref();
                        currentUID =  mAuth.getUid();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });


            } else {
                Toast.makeText(LogInActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
                RgDialog.dismiss();
                edPassword.requestFocus();
                edPassword.setError("Please enter email");
            }
        } else {
            Toast.makeText(LogInActivity.this, "Please enter email", Toast.LENGTH_LONG).show();
            RgDialog.dismiss();
            email.requestFocus();
            email.setError("Please enter email");
        }
    }


    private void goHome() {

        Intent intent = new Intent(LogInActivity.this, MainHomeActivity.class);
        startActivity(intent);
        Toast.makeText(LogInActivity.this, "Login successful...", Toast.LENGTH_LONG).show();

        finish();

    }

//    public void moveToLogin(View view) {
//        Intent intent = new Intent(SignUpActivity.this, LogInactivity.class);
//        startActivity(intent);
//    }


    private void sharedPref() {
        String strPassword = edPassword.getText().toString();


        SharedPreferences.Editor editor = credentials.edit();

        editor.putString("Password", strPassword);

        editor.commit();
        goHome();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (credentials.contains("Password")) {
            goHome();
        }
    }

}