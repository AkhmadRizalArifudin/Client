package com.example.nitishkumar.socketchat;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Nitish Kumar on 12-12-2017.
 */

public class ChatApp{ // extends Application {
    private String CHAT_URL;

    public ChatApp(String CHAT_URL) {
        this.CHAT_URL = "http://"+CHAT_URL+":3000/";
    }

    private static Socket mSocket;
    public Socket execute()
    {
        try {
//            System.out.println(this.CHAT_URL);
//            System.out.println(CHAT_URL);
            this.mSocket = IO.socket(this.CHAT_URL);
            System.out.println(this.CHAT_URL);
            System.out.println(this.mSocket);

        } catch (URISyntaxException e) {
            System.out.println("gggg");
            e.printStackTrace();
        }
        return this.mSocket;
    }

    void setUrl(String CHAT_URL) {
        this.CHAT_URL = CHAT_URL;
    }

    String getUrl() {
        return this.CHAT_URL;
    }


    public static Socket main(String h) {

        String CHAT_URL = h;

        ChatApp client = new ChatApp(CHAT_URL);
        Socket mSocket = client.execute();
        return mSocket;
    }

    public static synchronized Socket getSocket(){
        System.out.println(mSocket);
        return mSocket;
    }
}
