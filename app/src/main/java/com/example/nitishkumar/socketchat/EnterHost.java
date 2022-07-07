package com.example.nitishkumar.socketchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class EnterHost extends AppCompatActivity {
    private EditText host;
    private Button btnhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_host);

        host=(EditText)findViewById(R.id.host);
        btnhost=(Button)findViewById(R.id.btnhost);

        btnhost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ihost = host.getText().toString().trim();

                if(TextUtils.isEmpty(ihost)) {
                    Snackbar.make(view, "Enter host", Snackbar.LENGTH_SHORT).show();
                    host.requestFocus();
                    return;
                }else{
                    Intent i=new Intent(EnterHost.this, MainActivity.class);
                    i.putExtra("host",ihost);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}