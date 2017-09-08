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

import com.example.hemant.myblogapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    private ImageButton postimage;
    private EditText title,desc;
    private Button Submit;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private StorageReference storageReference;

    private Uri imageuri;
    private ProgressDialog progressDialog;
    private static final int GALLERY_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        progressDialog=new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Blog");
        postimage=(ImageButton)findViewById(R.id.postimgbutton);
        title=(EditText)findViewById(R.id.posttitletext);
        desc=(EditText) findViewById(R.id.postdesctext);
        Submit=(Button)findViewById(R.id.submit_post);
        postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galaryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galaryIntent.setType("image/*");
                startActivityForResult(galaryIntent,GALLERY_CODE);
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE&&resultCode==RESULT_OK){
            imageuri=data.getData();
            postimage.setImageURI(imageuri);
        }
    }

    private void startPosting() {

        progressDialog.setMessage("Posting To Blog...");
        progressDialog.show();
        final String titleVal=title.getText().toString().trim();
        final String descVal=desc.getText().toString().trim();
        if(!TextUtils.isEmpty(titleVal)&&!TextUtils.isEmpty(descVal)&&imageuri!=null){
            StorageReference filePath=storageReference.child("Blog_Images").
                    child(imageuri.getLastPathSegment());
            filePath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadURL=taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost=databaseReference.push();

                    Map<String,String> dataToSave=new HashMap<String, String>();
                    dataToSave.put("title",titleVal);
                    dataToSave.put("desc",descVal);
                    dataToSave.put("image",downloadURL.toString());
                    dataToSave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    dataToSave.put("userId",user.getUid());

                    newPost.setValue(dataToSave);





                    progressDialog.dismiss();
                    startActivity(new Intent(AddPostActivity.this,PostListActivity.class));
                }
            });

        }
    }
}
