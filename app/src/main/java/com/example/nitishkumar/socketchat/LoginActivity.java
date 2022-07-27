package com.example.nitishkumar.socketchat;

import android.content.Intent;
import android.content.res.AssetManager;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.json.JSONException;
import org.json.JSONObject;


import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.nitishkumar.socketchat.MainActivity.mSocket;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity {
    private EditText mUsernameEditText, mPasswordEditText;
    private Button mLogin;
    private String mUsername, mPassword;
    private Boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsernameEditText=(EditText)findViewById(R.id.username_editText);
        mPasswordEditText=(EditText)findViewById(R.id.password_editText);
        mLogin=(Button)findViewById(R.id.login);
        isConnected=false;
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on("login",onLogin);
        mSocket.on("cred",onCred);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("login",onLogin);
        mSocket.off("cred",onCred);
        mSocket.off(Socket.EVENT_CONNECT,onConnect);
    }

    private void login(View v) {
        String username=mUsernameEditText.getText().toString().trim();
        String password=mPasswordEditText.getText().toString().trim();
        AssetManager assetManager = getAssets();

//        System.out.println("brr");
        if(TextUtils.isEmpty(username)){
            Snackbar.make(v,"Enter username",Snackbar.LENGTH_SHORT).show();
            mUsernameEditText.requestFocus();
            return;
        }else if(TextUtils.isEmpty(password)){
            Snackbar.make(v,"Enter password",Snackbar.LENGTH_SHORT).show();
            mPasswordEditText.requestFocus();
            return;
        }


        mUsername=username;
        mPassword=password;
        mSocket.connect();

//        System.out.println("drr");
    }

    private Emitter.Listener onConnect=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(!isConnected){
                isConnected=true;
//                System.out.println("tesarar");
                JSONObject loginDetails = new JSONObject();
                String hashtext;
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-512");

                    byte[] messageDigest = md.digest(mPassword.getBytes());

                    BigInteger no = new BigInteger(1, messageDigest);

                    hashtext = no.toString(16);

                    while (hashtext.length() < 32) {
                        hashtext = "0" + hashtext;
                    }
                }catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }

                try {
                    loginDetails.put("user", mUsername);
                    loginDetails.put("pass", hashtext);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//                Map<String, String> loginDetails = new HashMap<String, String>();
//                loginDetails.put("pass", mPassword);
                mSocket.emit("add user",loginDetails);
            }
            else{
                Log.w("-->>","onConnect Failure");
            }
        }
    };

    private Emitter.Listener onCred = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data= (JSONObject) args[0];
            String CN=null;
            String modulus=null;

            try {
                System.out.println(data.toString(4));
                CN=data.getString("CN");
                System.out.println(CN);
                modulus=data.getString("mod");
            }catch (JSONException e){
                e.printStackTrace();
            }

            InputStream input;
            Boolean cek = false;
            try {
                input = getAssets().open(CN+".key");

                int size = input.available();
                byte[] buffer = new byte[size];

                //int count = 0;
                if(input.read(buffer)>0) {
                    // ...
                }
                input.close();

                // byte buffer into a string
                //String [] name = input.getText().toString();
                String[] priv = new String(buffer).split("\n");
                System.out.println(priv[0]);
                if (modulus.equals((new BigInteger(priv[0])).multiply(new BigInteger(priv[1])).toString())) {
                    cek = true;
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            Intent i=new Intent();
//            i.putExtra("CN",mUsername);
//            i.putExtra("numUsers",numUsers);
//            setResult(RESULT_OK,i);
            System.out.println("crr");
            mSocket.emit("validate",cek);
//            finish();
        }
    };

    private Emitter.Listener onLogin=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data= (JSONObject) args[0];
            int numUsers=0;
            try {
                numUsers=data.getInt("numUsers");
            }catch (JSONException e){
                e.printStackTrace();
            }
//            System.out.println("arh");
            Intent i=new Intent();
            i.putExtra("username",mUsername);
            i.putExtra("numUsers",numUsers);
//            i.putExtra("id",mSo);
            setResult(RESULT_OK,i);
            finish();
        }
    };
}
