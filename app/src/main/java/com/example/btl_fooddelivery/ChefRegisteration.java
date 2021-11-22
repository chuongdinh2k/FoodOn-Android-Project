package com.example.btl_fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.btl_fooddelivery.ReusableCodeForAll.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ChefRegisteration extends AppCompatActivity {


    TextInputLayout Fname, Lname, Email, Pass, cfpass, mobileno, addressno, area, postcode;
    Spinner Rolespin, Cityspin;
    Button signup, Emaill, Phone;
    CountryCodePicker Cpp;
    private FirebaseAuth mAuth;


    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname;
    String lname;
    String emailid;
    String password;
    String confirmpassword;
    String mobile;
    String address;
    String Area;
    String Postcode;
    String role;
    String cityy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_registeration);
        Fname = (TextInputLayout) findViewById(R.id.Firstname);
        Lname = (TextInputLayout) findViewById(R.id.Lastname);
        Email = (TextInputLayout) findViewById(R.id.Email);
        Pass = (TextInputLayout) findViewById(R.id.Pwd);
        cfpass = (TextInputLayout) findViewById(R.id.Cpass);
        mobileno = (TextInputLayout) findViewById(R.id.Mobileno);
        addressno = (TextInputLayout) findViewById(R.id.houseNo);
        postcode = (TextInputLayout) findViewById(R.id.Postcode);
        Rolespin = (Spinner) findViewById(R.id.Role);
        Cityspin = (Spinner) findViewById(R.id.Citys);
        signup = (Button) findViewById(R.id.Signup);
        Emaill = (Button) findViewById(R.id.emaill);
//        Phone = (Button) findViewById(R.id.phone);



//            get role
        Rolespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                role = value.toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                cityy = value.toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference("User");

        Emaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToLogin = new Intent(ChefRegisteration.this,ChefLogin.class);
                startActivity(moveToLogin);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = Objects.requireNonNull(Fname.getEditText()).getText().toString().trim();
                lname = Objects.requireNonNull(Lname.getEditText()).getText().toString().trim();
                emailid = Objects.requireNonNull(Email.getEditText()).getText().toString().trim();
                mobile = Objects.requireNonNull(mobileno.getEditText()).getText().toString().trim();
                password = Objects.requireNonNull(Pass.getEditText()).getText().toString().trim();
                confirmpassword = Objects.requireNonNull(cfpass.getEditText()).getText().toString().trim();
//                Area = Objects.requireNonNull(area.getEditText()).getText().toString().trim();
                address = Objects.requireNonNull(addressno.getEditText()).getText().toString().trim();
                Postcode = Objects.requireNonNull(postcode.getEditText()).getText().toString().trim();


                if(isValid()){
                    final ProgressDialog mDialog = new ProgressDialog(ChefRegisteration.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Vui lòng đợi chút...");
                    mDialog.show();

                    mAuth.createUserWithEmailAndPassword(emailid, password)
                            .addOnCompleteListener(ChefRegisteration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
                                        final HashMap<String,String> hashMap = new HashMap<>();
//                                        hashMap.put("Area", Area);
                                        hashMap.put("City", cityy);
                                        hashMap.put("EmailID", emailid);
                                        hashMap.put("Fname", fname);
                                        hashMap.put("Address", address);
                                        hashMap.put("Lname", lname);
                                        hashMap.put("Role", role);
                                        hashMap.put("Mobile", mobile);
                                        hashMap.put("Postcode", Postcode);
                                        hashMap.put("City",cityy);
                                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mDialog.dismiss();
                                                Toast.makeText(ChefRegisteration.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        mDialog.dismiss();
                                        ReusableCodeForAll.ShowAlert(ChefRegisteration.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                                    }
                                }
                            });
                }

            }
        });
        }
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public boolean isValid() {
        Email.setErrorEnabled(false);
        Email.setError("");
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Lname.setErrorEnabled(false);
        Lname.setError("");
        Pass.setErrorEnabled(false);
        Pass.setError("");
        mobileno.setErrorEnabled(false);
        mobileno.setError("");
        cfpass.setErrorEnabled(false);
        cfpass.setError("");
//        area.setErrorEnabled(false);
//        area.setError("");
        addressno.setErrorEnabled(false);
        addressno.setError("");
        postcode.setErrorEnabled(false);
        postcode.setError("");

        boolean isValidname = false, isValidemail = false, isvalidpassword = false, isvalidconfirmpassword = false, isvalid = false, isvalidmobileno = false, isvalidlname = false, isvalidhousestreetno = false, isvalidarea = false, isvalidpostcode = false;
        if (TextUtils.isEmpty(fname)) {
            Fname.setErrorEnabled(true);
            Fname.setError("Firstname is required");
        } else {
            isValidname = true;
        }
        if (TextUtils.isEmpty(lname)) {
            Lname.setErrorEnabled(true);
            Lname.setError("Lastname is required");
        } else {
            isvalidlname = true;
        }
        if (TextUtils.isEmpty(emailid)) {
            Email.setErrorEnabled(true);
            Email.setError("Email is required");
        } else {
            if (emailid.matches(emailpattern)) {
                isValidemail = true;
            } else {
                Email.setErrorEnabled(true);
                Email.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(password)) {
            Pass.setErrorEnabled(true);
            Pass.setError("Password is required");
        } else {
            if (password.length() < 6) {
                Pass.setErrorEnabled(true);
                Pass.setError("password too weak");
            } else {
                isvalidpassword = true;
            }
        }
        if (TextUtils.isEmpty(confirmpassword)) {
            cfpass.setErrorEnabled(true);
            cfpass.setError("Confirm Password is required");
        } else {
            if (!password.equals(confirmpassword)) {
                Pass.setErrorEnabled(true);
                Pass.setError("Password doesn't match");
            } else {
                isvalidconfirmpassword = true;
            }
        }
        if (TextUtils.isEmpty(mobile)) {
            mobileno.setErrorEnabled(true);
            mobileno.setError("Mobile number is required");
        } else {
            if (mobile.length() < 10) {
                mobileno.setErrorEnabled(true);
                mobileno.setError("Invalid mobile number");
            } else {
                isvalidmobileno = true;
            }
        }
        if (TextUtils.isEmpty(address)) {
            addressno.setErrorEnabled(true);
            addressno.setError("Field cannot be empty");
        } else {
            isvalidhousestreetno = true;
        }
//        if (TextUtils.isEmpty(Area)) {
//            area.setErrorEnabled(true);
//            area.setError("Field cannot be empty");
//        } else {
//            isvalidarea = true;
//        }
        if (TextUtils.isEmpty(Postcode)) {
            postcode.setErrorEnabled(true);
            postcode.setError("Field cannot be empty");
        } else {
            isvalidpostcode = true;
        }

        isvalid = (isValidname && isvalidpostcode && isvalidlname && isValidemail && isvalidconfirmpassword && isvalidpassword && isvalidmobileno  && isvalidhousestreetno) ? true : false;
        return isvalid;
    }


}
