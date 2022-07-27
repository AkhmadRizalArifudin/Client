package com.example.nitishkumar.socketchat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

class Session {
    private static Random r = new SecureRandom();

    Session() {
    }

    public static String add(String nows) {
        int thour = Integer.parseInt(nows.substring(0, 2));
        ++thour;
        String ttime = String.valueOf(thour);
        if (ttime.length() == 1) {
            (new StringBuilder("0")).append(ttime).toString();
        }

        String die = new String(thour + ":" + nows.substring(2, 4) + ":" + nows.substring(4, 6));
        return die;
    }

    public static String createid(String pub_a, String pub_b) {
        StringBuffer sb = new StringBuffer();

        while(sb.length() < 26) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmmss");
        LocalTime nows = LocalTime.now();
        new String(add(dtf.format(nows)));
        String sid = new String(sb.toString().substring(0, 26) + dtf.format(nows));
        String namefile = new String("E:\\IKP\\ta\\SID\\" + pub_a + "_" + pub_b + ".txt");

        try {
            File myObj = new File(namefile);

            if (!myObj.createNewFile()) {
                // file delete failed; take appropriate action
            }
        } catch (IOException var10) {
            var10.printStackTrace();
        }

        try {
            FileWriter w = new FileWriter(namefile);
            w.write(sid);
            w.close();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        return sid;
    }

    public static void hapus(String pub_a, String pub_b) {
        File file = new File("E:\\IKP\\ta\\SID\\" + pub_a + "_" + pub_b + ".txt");

        if (!file.delete()) {
            // file delete failed; take appropriate action
        }
    }
}
