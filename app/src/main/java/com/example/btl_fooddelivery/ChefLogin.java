package com.example.btl_fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_fooddelivery.CustomerFoodPanel.CustomerFoodPanel_BottomNavigation;
import com.example.btl_fooddelivery.Model.User;
import com.example.btl_fooddelivery.ReusableCodeForAll.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ChefLogin extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    TextInputLayout email, pass;
    Button Signin, Signinphone;
    TextView Forgotpassword;
    TextView txt;
    FirebaseAuth FAuth;
    String em;
    String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_login);

        try {
            email = (TextInputLayout) findViewById(R.id.Lemail);
            pass = (TextInputLayout) findViewById(R.id.Lpassword);
            Signin = (Button) findViewById(R.id.button4);
            txt = (TextView) findViewById(R.id.textView3);
//            Forgotpassword = (TextView) findViewById(R.id.forgotpass);
//            Signinphone = (Button) findViewById(R.id.btnphone);

            FAuth = FirebaseAuth.getInstance();
            databaseReference = firebaseDatabase.getInstance().getReference("User");
//          Signup
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent Register = new Intent(ChefLogin.this, ChefRegisteration.class);
                    startActivity(Register);
                    finish();

                }
            });
            Signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    em = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
                    pwd = Objects.requireNonNull(pass.getEditText()).getText().toString().trim();
                    if(isValid()){
                        final ProgressDialog mDialog = new ProgressDialog(ChefLogin.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("Logging in...");
                        mDialog.show();

                        FAuth.signInWithEmailAndPassword(em,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mDialog.dismiss();
                                    String useridd = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                 databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User user = snapshot.child(useridd).getValue(User.class);
                                         assert user != null;
//                                         Toast.makeText(ChefLogin.this, user.getEmailID().toString(), Toast.LENGTH_SHORT).show();
                                         if(user.getRole().equals("Chef")){
                                             Intent Chef_home =  new Intent(ChefLogin.this,ChefFoodPanel_BottomNavigation.class);
                                             startActivity(Chef_home);
                                         }
                                         else if(user.getRole().equals("Customer")){
                                             Intent Chef_home =  new Intent(ChefLogin.this, CustomerFoodPanel_BottomNavigation.class);
                                             startActivity(Chef_home);
                                         }
                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError error) {

                                     }
                                 });
                                }
                                else{
                                    mDialog.dismiss();
                                    ReusableCodeForAll.ShowAlert(ChefLogin.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                                }
                            }
                        });
                    }

                }
            });
//            Sign In with phone
//            Signinphone.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent q = new Intent(ChefLogin.this, ChefLoginphone.class);
//                    startActivity(q);
//                    finish();
//                }
//            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

//    check valid form
String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid() {
        email.setErrorEnabled(false);
        email.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");

        boolean isvalidemail = false, isvalidpassword = false, isvalid = false;
        if (TextUtils.isEmpty(em)) {
            email.setErrorEnabled(true);
            email.setError("Email is required");
        } else {
            if (em.matches(emailpattern)) {
                isvalidemail = true;
            } else {
                email.setErrorEnabled(true);
                email.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(pwd)) {
            pass.setErrorEnabled(true);
            pass.setError("Password is required");
        } else {
            isvalidpassword = true;
        }
        isvalid = (isvalidemail && isvalidpassword) ? true : false;
        return isvalid;
    }

}