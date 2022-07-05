package com.example.nitishkumar.socketchat;

import java.math.BigInteger;
import java.nio.charset.Charset;

public class RabinCryptosystem {
    public RabinCryptosystem() {
    }

    public static String[] enc(String plain, String scrt, String modulus) {
        BigInteger n = new BigInteger(modulus);
        BigInteger m = new BigInteger((scrt + plain).getBytes(Charset.forName("ascii")));
        BigInteger c = com.example.nitishkumar.socketchat.Cryptography.encrypt(m, n);
        String[] ans = new String[]{c.toString(), scrt};
        return ans;
    }

    public static String dec(String[] cipher, String p, String q) {
        String finalMessage = null;
        BigInteger c = new BigInteger(cipher[0]);
        BigInteger pp = new BigInteger(p);
        BigInteger qq = new BigInteger(q);
        BigInteger[] m2 = com.example.nitishkumar.socketchat.Cryptography.decrypt(c, pp, qq);
        BigInteger[] var11 = m2;
        int var10 = m2.length;

        for(int var9 = 0; var9 < var10; ++var9) {
            BigInteger b = var11[var9];
            String dec = new String(b.toByteArray(), Charset.forName("ascii"));
            if (dec.substring(0, 32).equals(cipher[1])) {
                finalMessage = dec.substring(32, dec.length());
            }
        }

        return finalMessage;
    }
}

