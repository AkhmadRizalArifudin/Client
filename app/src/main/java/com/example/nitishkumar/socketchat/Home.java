package com.example.nitishkumar.socketchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;

public class Home extends AppCompatActivity {

    private FloatingActionButton chooseButton;
    public static Socket mSocket;
    private ChatApp app;
    private RecyclerView userView;
    private UserAdapter uAdapter;
    private ArrayList<User> userList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent i = getIntent();
        userList = (ArrayList<User>) i.getSerializableExtra("users");
        System.out.println(userList);
        userView = findViewById(R.id.user_view);
        userView.setHasFixedSize(true);

        userView.setLayoutManager(new LinearLayoutManager(this));
        UserAdapter listUserAdapter = new UserAdapter(userList);
        userView.setAdapter(listUserAdapter);

        setActionBarTitle("Online Users");
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}