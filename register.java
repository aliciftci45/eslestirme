package com.aliciftci45.eletirme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText idMail, idPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();

        idMail = findViewById(R.id.idMail);
        idPassword = findViewById(R.id.idPassword);
    }

    public void Register(View view){
        mAuth.createUserWithEmailAndPassword(idMail.getText().toString(), idPassword.getText().toString())
                .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(register.this, "User Created!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), userProfil.class);
                            startActivity(intent);
                        }


                    }
                }).addOnFailureListener(register.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(register.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
