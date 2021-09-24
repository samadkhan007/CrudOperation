package edu.uol.mad.crudoperation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText mTitle ,mDesc;
    private Button msavebtn ,mshowallbtn;
    private FirebaseFirestore db;
    private String uTitle, uDesc, uId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = findViewById(R.id.edit_title);
        mDesc = findViewById(R.id.edit_desc);
        msavebtn = findViewById(R.id.save_btn);
        mshowallbtn = findViewById(R.id.showall_btn);

        mshowallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this ,ShowActivity.class));
            }
        });

        db= FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            msavebtn.setText("update");
            uTitle=bundle.getString("uTitle");
            uDesc=bundle.getString("uDesc");
            uId = bundle.getString("uId");
            mTitle.setText(uTitle);
            mDesc.setText(uDesc);
        }
        else
        {
           msavebtn.setText("Save");
        }

        msavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=mTitle.getText().toString();
                String desc=mDesc.getText().toString();

                Bundle bundle1=getIntent().getExtras();
                if (bundle1 != null)
                {
                   String id= uId;
                   updateToFireStore(id , title, desc);
                }
                else
                {
                    String id = UUID.randomUUID().toString();
                    saveToFirebaseStore(id ,title ,desc);

                }

            }
        });

    }

    private void updateToFireStore(String id,String title ,String desc)
    {
        db.collection("Documents").document(id).update("title",title ,"Desc",desc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this, "Data updated!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Error.."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveToFirebaseStore( String id, String title, String desc)
    {
        if (!title.isEmpty() && !desc.isEmpty())
        {
            HashMap<String ,Object> map= new HashMap<>();
            map.put("id" ,id);
            map.put("title" ,title);
            map.put("Desc", desc);

            db.collection("Documents").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful())
                             {
                                 Toast.makeText(MainActivity.this, "Data save!!", Toast.LENGTH_SHORT).show();
                             }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else 
        {
            Toast.makeText(this, "Empty field!", Toast.LENGTH_SHORT).show();
        }
    }
}