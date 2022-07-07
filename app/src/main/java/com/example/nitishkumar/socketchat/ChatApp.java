package com.example.nitishkumar.socketchat;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Nitish Kumar on 12-12-2017.
 */

public class ChatApp extends Application {
    private String CHAT_URL = "";//="http://192.168.94.54:3000/";

//    public ChatApp (String host){
//        this.CHAT_URL = "http://"+host+":3000/";
//    }

    public void setUrl(String host){
        this.CHAT_URL += "http://"+host+":3000/";
    }

    private Socket mSocket;
    {
        try {
            System.out.println(this.CHAT_URL);
            mSocket= IO.socket(this.CHAT_URL);
        } catch (URISyntaxException e) {
            System.out.println("gggg");
            e.printStackTrace();
        }
    }

    public Socket getSocket(){
        return mSocket;
    }
}
