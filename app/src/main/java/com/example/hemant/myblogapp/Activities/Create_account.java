package com.example.hemant.myblogapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hemant.myblogapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Create_account extends AppCompatActivity {
    private EditText firstname,lastname,email,password;
    private Button createAccount,signin;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private ImageButton profile_pic;
    private ProgressDialog progressDialog;
    private StorageReference firebaseStorage;
    private  final static int GALLERY_CODE=1;
    private  Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firstname=(EditText)findViewById(R.id.firstname);
        lastname=(EditText)findViewById(R.id.lastname);
        email=(EditText)findViewById(R.id.Emailid);
        password=(EditText)findViewById(R.id.password);
        progressDialog=new ProgressDialog(this);
        createAccount=(Button) findViewById(R.id.create_account);
        signin=(Button)findViewById(R.id.sign_in);
        profile_pic=(ImageButton)findViewById(R.id.imageButton);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Create_account.this,MainActivity.class));
            }
        });
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Users");
        auth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance()
                .getReference().child("Blog_Profile_Pics");
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });



    }

    private void createNewAccount() {
        final String fname=firstname.getText().toString().trim();
        final String lname=lastname.getText().toString().trim();
        String emailstring=email.getText().toString().trim();
        String pwd=password.getText().toString().trim();
        if(!TextUtils.isEmpty(fname)&&!TextUtils.isEmpty(lname)
                &&!TextUtils.isEmpty(emailstring)&&!TextUtils.isEmpty(pwd)){
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            auth.createUserWithEmailAndPassword(emailstring,pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if(authResult!=null){
                                StorageReference imagepath=firebaseStorage.child("Blog_Profile_Pics")
                                        .child(resultUri.getLastPathSegment());
                                imagepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(getApplicationContext(),"loololooll",Toast.LENGTH_SHORT).show();
                                        String userId=auth.getCurrentUser().getUid();
                                        DatabaseReference currentuserdb=databaseReference.child(userId);
                                        currentuserdb.child("firstname").setValue(fname);
                                        currentuserdb.child("lastname").setValue(lname);
                                        currentuserdb.child("image").setValue(resultUri.toString());
                                        progressDialog.dismiss();

                                        Intent i=new Intent(Create_account.this,PostListActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();
                                    }
                                });







//                                String userId=auth.getCurrentUser().getUid();
//                                DatabaseReference currentuserdb=databaseReference.child(userId);
//                                currentuserdb.child("firstname").setValue(fname);
//                                currentuserdb.child("lastname").setValue(lname);
//                                currentuserdb.child("image").setValue("none");
//                                progressDialog.dismiss();
//
//                                Intent i=new Intent(Create_account.this,PostListActivity.class);
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(i);
                            }
                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE&&resultCode==RESULT_OK){
            Uri ImageURI=data.getData();
            CropImage.activity(ImageURI)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                profile_pic.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
