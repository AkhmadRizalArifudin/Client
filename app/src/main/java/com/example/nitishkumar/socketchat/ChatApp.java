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

    private Socket mSocket;
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
//    public static String CHAT_URL;//="http://192.168.94.54:3000/";
//
//    public ChatApp (String host){
//        this.CHAT_URL = "http://"+host+":3000/";
//        CHAT_URL = "http://"+host+":3000/";
//        System.out.println(this.CHAT_URL);
//        System.out.println(CHAT_URL);
//    }
//
//
//    public String getHost()
//    {
//        return CHAT_URL;
//    }
//
////    public void setHost(String i)
////    {
////        this.CHAT_URL = "http://"+i+":3000/";
////    }
//
//    private Socket mSocket;
//    {
//        try {
//            System.out.println(this.CHAT_URL);
//            System.out.println(CHAT_URL);
//            mSocket= IO.socket(CHAT_URL);
//        } catch (URISyntaxException e) {
//            System.out.println("gggg");
//            e.printStackTrace();
//        }
//    }

    Socket getSocket(){
        System.out.println(this.mSocket);
        return this.mSocket;
    }
}
