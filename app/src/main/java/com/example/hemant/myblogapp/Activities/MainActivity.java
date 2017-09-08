package com.example.hemant.myblogapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hemant.myblogapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public FirebaseAuth auth;
    public FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private Button createButton,login;
    private EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth= FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.emailet);
        password=(EditText)findViewById(R.id.password);
        createButton=(Button) findViewById(R.id.create_account);
        login=(Button) findViewById(R.id.login_button);
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser=firebaseAuth.getCurrentUser();
                if(firebaseUser!=null){
                    //Toast.makeText(MainActivity.this,"Signed In",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this,PostListActivity.class));
                    finish();
                }
                else {
                   // Toast.makeText(MainActivity.this,"Not Signed In",Toast.LENGTH_LONG).show();
                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(email.getText().toString())&&
                        !TextUtils.isEmpty(password.getText().toString())){
                    String emailstring=email.getText().toString();
                    String pwd=password.getText().toString();
                    Login(emailstring,pwd);
                }
                else
                {


                }
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Create_account.class));
            }
        });
    }

    private void Login(String emailstring, String pwd) {
        auth.signInWithEmailAndPassword(emailstring,pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           // Toast.makeText(MainActivity.this,"Signed In",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this,PostListActivity.class));
                            finish();
                        }
                        else
                        {
                           // Toast.makeText(MainActivity.this,"not Signed In",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }



    @Override
    protected void onStart() {

        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
