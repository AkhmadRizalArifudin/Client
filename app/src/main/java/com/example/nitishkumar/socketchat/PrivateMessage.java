package com.example.nitishkumar.socketchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PrivateMessage extends AppCompatActivity {
    private EditText editPMessage;

    private Button sendPButton;
//    private FloatingActionButton reqButton;
    public static Socket mSocket;
    private ChatApp app;
    private boolean mTyping;
    private String mUsername;
//    private String self;
    TextView typingView;
    TextView targetName;
    private Boolean isConnected;
    private String TAG="-->>";
    private RecyclerView privateView;
    private MessageAdapter mAdapter;
    private UserAdapter uAdapter;
    private List<Message>messageList;
//    private List<User>userList = new ArrayList<>();
    private List<User>target = new ArrayList<>();
    private static final int TIMER=500;
    private Handler typingHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);

//        String vhost;
//
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//        getSupportActionBar().setTitle(null);
        isConnected=false;
        mTyping=false;
        Intent i = getIntent();
        target = (ArrayList<User>) i.getSerializableExtra("target");
        initializeSocket();
        setUpUI();
    }

    public void initializeSocket(){
        mSocket=ChatApp.getSocket();
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR,onConnectError);
//        mSocket.on("user update",onUpdateUser);
        mSocket.on("user joined",onUserJoined);
        mSocket.on("user left",onUserLeft);
        mSocket.on("private message",onNewMessage);
        mSocket.on("typing",onTyping);
        mSocket.on("stop typing",onStopTyping);
    }


    //result login diproses

    public void setUpUI(){
        messageList=new ArrayList<>();
        privateView=(RecyclerView)findViewById(R.id.private_view);
        mAdapter=new MessageAdapter(messageList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        privateView.setLayoutManager(layoutManager);
        privateView.setItemAnimator(new DefaultItemAnimator());
        privateView.setAdapter(mAdapter);
        editPMessage=(EditText)findViewById(R.id.editPMessage);
        typingView=findViewById(R.id.status);
        sendPButton=(Button)findViewById(R.id.sendPButton);
//        reqButton=(FloatingActionButton)findViewById(R.id.btnrequest);
        targetName=(TextView)findViewById(R.id.target_name);
        targetName.setText(target.get(0).getUser());
//
        editPMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mUsername==null)
                    return;
                if(!mSocket.connected())
                    return;
                if(TextUtils.isEmpty(editPMessage.getText()))
                    return;
                if(mTyping==false) {
                    mTyping = true;
                    mSocket.emit("typing");
                }
                typingHandler.removeCallbacks(onTypingTimeout);
                typingHandler.postDelayed(onTypingTimeout,TIMER);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        sendPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSend();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isConnected) {
            mSocket.disconnect();
            isConnected = false;
        }


        mSocket.off(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR,onConnectError);
        mSocket.off("user joined",onUserJoined);
        mSocket.off("user left",onUserLeft);
        mSocket.off("private message",onNewMessage);
        mSocket.off("typing",onTyping);
        mSocket.off("stop typing",onStopTyping);
//        mSocket.off("user update",onUpdateUser);

        mTyping=false;

    }


    private Emitter.Listener onDisconnect=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected=false;
                    Log.w(TAG, "onDisconnect");
                }
            });
        }
    };

    private Emitter.Listener onConnectError=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG,"error connecting");
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.w(TAG,"onNewMesage");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data= (JSONObject)args[0];
                    String username=null;
                    String message=null;
                    try {
                        username=data.getString("from");
                        message=data.getString("message");
                    } catch (JSONException e) {
                        Log.e(TAG,e.getMessage());
                        e.printStackTrace();
                    }

                    addMessage(username,message,Message.TYPE_MESSAGE_RECEIVED);
                }
            });
        }
    };

//    private Emitter.Listener onUpdateUser = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            Log.w(TAG,"onUpdateUser");
//            runOnUiThread(new Runnable() {
//                @SuppressLint("StringFormatInvalid")
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    JSONArray datas;
//                    userList = new ArrayList<>();
//                    try {
//                        datas = data.getJSONArray("user");
//                        try{
//                            self = data.getString("self");
//                            for (int i=0;i<datas.length();i++){
//                                JSONObject temp = (JSONObject) datas.get(i);
//                                String id = temp.getString("userID");
//                                String username = temp.getString("username");
////                            System.out.println(self);
////                            System.out.println(username);
////                            System.out.println(username.trim());
//                                if(!username.trim().equals(self.trim())){
//                                    addUser(id,username);
//                                }
//                            }
//                        }catch (Exception e){
//                            for (int i=0;i<datas.length();i++){
//                                JSONObject temp = (JSONObject) datas.get(i);
//                                String id = temp.getString("userID");
//                                String username = temp.getString("username");
//
//                                if(!username.trim().equals(self.trim())){
//                                    addUser(id,username);
//                                }
//
//                            }
//                        }
//
////                        System.out.println(self);
//                        System.out.println(userList);
//                    } catch (JSONException e) {
//                        Log.e(TAG, e.getMessage());
//                        return;
//                    }
////
//                }
//            });
//        }
//    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.w(TAG,"onUserJoined");
            runOnUiThread(new Runnable() {
                @SuppressLint("StringFormatInvalid")
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username=null;
//                    JSONArray datas;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
//                        datas = data.getJSONArray("users");
//                        System.out.println(datas);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    addLog(username+" has joined");
                    addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.w(TAG,"onUserLeft");
            runOnUiThread(new Runnable() {
                @SuppressLint("StringFormatInvalid")
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    addLog(username+" left");
                    addParticipantsLog(numUsers);
                    removeTyping();
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.w(TAG,"onTyping");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.w(TAG,"onStopTyping");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    removeTyping();
                }
            });
        }
    };

    private void addLog(String message){
        messageList.add(new Message(Message.TYPE_LOG,message));
        mAdapter.notifyItemInserted(messageList.size()-1);
        scrollUp();
    }

    private void addMessage(String username,String message, int messageType){
        messageList.add(new Message(messageType,username,message));
        mAdapter.notifyItemInserted(messageList.size()-1);
        scrollUp();
    }

//    private void addUser(String id, String username){
////        userList = new ArrayList<>();
//        userList.add(new User(id, username));
//        System.out.println(userList);
////        uAdapter.notifyItemInserted(userList.size()-1);
//        scrollUp();
//    }

    private void addParticipantsLog(int numUsers) {
        addLog("There are "+numUsers+" users in the chat room");
        scrollUp();
    }

    private void addTyping(String username){
        typingView.setText(username.trim()+" is typing");
    }

    private void removeTyping() {
        typingView.setText(null);
    }

    private void attemptSend() {
        if (mUsername==null) return;
        if (!mSocket.connected()) return;
        if(mTyping) {
            mTyping = false;
            mSocket.emit("stop typing");
        }
        String message = editPMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            editPMessage.requestFocus();
            return;
        }

        editPMessage.setText("");
        addMessage(mUsername, message,Message.TYPE_MESSAGE_SENT);

//        mSocket.emit("get modulus", message);

//        mSocket.on("send modulus", new Emitter.Listener() {
//            @Override
//            public void call(final Object... args) {
//                Log.w(TAG,"onSendMod");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Scanner scanner;
//                        for(scanner = new Scanner(new File("E:\\IKP\\ta\\SID\\" + userName + "_" + t + ".txt")); scanner.hasNextLine(); scrt = scanner.nextLine()) {
//                        }
//
//                        String text = message.substring(message.indexOf(")") + 1, clientMessage.length());
//                        String[] Message = RabinCryptosystem.enc(text, scrt, n);
//                        this.server.privatebr("(secured)" + Message[0], t);
//                    }
//                });
//            }
//        });
        // perform the sending message attempt.
        JSONObject messageDetails = new JSONObject();
        try {
            messageDetails.put("from", mSocket.id());
            messageDetails.put("message", message);
            messageDetails.put("to", target.get(0).getID());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mSocket.emit("private message", messageDetails);
    }

    private Runnable onTypingTimeout=new Runnable() {
        @Override
        public void run() {
            if(mTyping==false)
                return;

            mTyping=false;
            mSocket.emit("stop typing");
        }
    };
    private void scrollUp(){
        privateView.scrollToPosition(mAdapter.getItemCount()-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mSocket.disconnect();
        isConnected=false;
        mTyping=false;
        messageList.clear();
        this.recreate();
    }
}
