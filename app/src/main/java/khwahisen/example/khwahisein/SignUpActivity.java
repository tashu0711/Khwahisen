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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";
    private EditText edUsername;
    private EditText edPassword;
    private Button btnCreateUser;
    private SharedPreferences credentials;

    private EditText email;
    private FirebaseDatabase database;
    private FirebaseFirestore firebaseFirestore;

    ProgressDialog RgDialog;
    private String currentUID = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        edUsername = findViewById(R.id.sign_up_personname);
        edPassword = findViewById(R.id.signup_password);
        email = findViewById(R.id.sign_up_email);
        btnCreateUser = findViewById(R.id.sign_up_button);
         database = FirebaseDatabase.getInstance();
         RgDialog = new ProgressDialog(this);
         RgDialog.setTitle("Please wait!");
         firebaseFirestore = FirebaseFirestore.getInstance();
        credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, MODE_PRIVATE);
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RgDialog.show();
                RegisterWithEmail();
            }

        });
    }

    private void RegisterWithEmail() {
        if (!edUsername.getText().toString().equals("")) {

            if (!edPassword.getText().toString().equals("")) {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), edPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<Object, String> userdata = new HashMap<>();
                                    userdata.put("User Name", edUsername.getText().toString());

                                    firebaseFirestore.collection("USERS").document(mAuth.getUid()).set(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull  Task<Void> task) {
                                            sharedPref();
                                            SIGNUPGO();
                                            RgDialog.dismiss();
                                            currentUID = mAuth.getUid();
                                            finish();
                                            Toast.makeText(SignUpActivity.this,"Successful...",Toast.LENGTH_LONG).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            RgDialog.dismiss();

                                            Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    });


                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_LONG).show();
                                    RgDialog.dismiss();

                                }
                            }
                        });

            } else {
                Toast.makeText(SignUpActivity.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                edPassword.requestFocus();
                edPassword.setError("Enter Password!");
                RgDialog.dismiss();
            }
;



        } else {
            Toast.makeText(SignUpActivity.this, "Please enter Email!", Toast.LENGTH_SHORT).show();
            edUsername.requestFocus();
            RgDialog.dismiss();
            edUsername.setError("Enter Valid Email!");
        }


    }

    private void SIGNUPGO() {

        Intent intent = new Intent(SignUpActivity.this, MainHomeActivity.class);
        startActivity(intent);
    }

//    public void moveToLogin(View view) {
//        Intent intent = new Intent(SignUpActivity.this, LogInactivity.class);
//        startActivity(intent);
//    }



    private void sharedPref() {
        String strPassword = edPassword.getText().toString();

        String strUsername = edUsername.getText().toString();

        SharedPreferences.Editor editor = credentials.edit();
        editor.putString("Password", strPassword);
        editor.putString("name",strUsername);
        editor.commit();
        SignUpActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (credentials.contains("Password") && credentials.contains("name"))
        {
            SIGNUPGO();
        }
    }
}