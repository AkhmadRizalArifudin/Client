package com.example.nitishkumar.socketchat;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

class Cryptography {
    private static Random r = new SecureRandom();
    private static BigInteger TWO = BigInteger.valueOf(2L);
    private static BigInteger THREE = BigInteger.valueOf(3L);
    private static BigInteger FOUR = BigInteger.valueOf(4L);

    Cryptography() {
    }

    public static BigInteger[] generateKey(int bitLength) {
        BigInteger p = blumPrime(bitLength / 2);
        BigInteger q = blumPrime(bitLength / 2);
        BigInteger N = p.multiply(q);
        return new BigInteger[]{N, p, q};
    }

    public static BigInteger encrypt(BigInteger m, BigInteger N) {
        return m.modPow(TWO, N);
    }

    public static BigInteger[] decrypt(BigInteger c, BigInteger p, BigInteger q) {
        BigInteger N = p.multiply(q);
        BigInteger p1 = c.modPow(p.add(BigInteger.ONE).divide(FOUR), p);
        BigInteger p2 = p.subtract(p1);
        BigInteger q1 = c.modPow(q.add(BigInteger.ONE).divide(FOUR), q);
        BigInteger q2 = q.subtract(q1);
        BigInteger[] ext = Gcd(p, q);
        BigInteger y_p = ext[1];
        BigInteger y_q = ext[2];
        BigInteger d1 = y_p.multiply(p).multiply(q1).add(y_q.multiply(q).multiply(p1)).mod(N);
        BigInteger d2 = y_p.multiply(p).multiply(q2).add(y_q.multiply(q).multiply(p1)).mod(N);
        BigInteger d3 = y_p.multiply(p).multiply(q1).add(y_q.multiply(q).multiply(p2)).mod(N);
        BigInteger d4 = y_p.multiply(p).multiply(q2).add(y_q.multiply(q).multiply(p2)).mod(N);
        return new BigInteger[]{d1, d2, d3, d4};
    }

    public static BigInteger[] Gcd(BigInteger a, BigInteger b) {
        BigInteger s = BigInteger.ZERO;
        BigInteger old_s = BigInteger.ONE;
        BigInteger t = BigInteger.ONE;
        BigInteger old_t = BigInteger.ZERO;
        BigInteger r = b;

        BigInteger old_r;
        BigInteger tt;
        for(old_r = a; !r.equals(BigInteger.ZERO); old_t = tt) {
            BigInteger q = old_r.divide(r);
            BigInteger tr = r;
            r = old_r.subtract(q.multiply(r));
            old_r = tr;
            BigInteger ts = s;
            s = old_s.subtract(q.multiply(s));
            old_s = ts;
            tt = t;
            t = old_t.subtract(q.multiply(t));
        }

        return new BigInteger[]{old_r, old_s, old_t};
    }

    public static BigInteger blumPrime(int bitLength) {
        BigInteger p;
        do {
            p = BigInteger.probablePrime(bitLength, r);
        } while(!p.mod(FOUR).equals(THREE));

        return p;
    }

    public static char[] dec4(String cipher, String sn) {
        char[] in = cipher.toCharArray();
        char[] k = new char[17];
        int j = 0;
        int l = 2;

        int i;
        for(i = 0; i < 16; ++i) {
            k[i] = (char)Integer.parseInt(sn.substring(j, l), 16);
            j = l + 1;
            l = j + 2;
            if (j > sn.length()) {
                j = 0;
                l = 2;
            }
        }

        char[] c = new char[in.length];
        j = 0;

        for(i = 0; i < in.length; ++j) {
            c[i] = (char)(in[i] >> k[j] % 8 | c[i] << 8 - k[j] % 8);
            c[i] ^= k[j];
            if (j + 1 == 16) {
                j = 0;
            }

            ++i;
        }

        return c;
    }

    public static String enc4(char[] buffera, String sn) {
        char[] c = new char[buffera.length];
        char[] k = new char[17];
        int j = 0;
        int l = 2;

        for(int i = 0; i < 16; ++i) {
            k[i] = (char)Integer.parseInt(sn.substring(j, l), 16);
            j = l + 1;
            l = j + 2;
            if (j > sn.length()) {
                j = 0;
                l = 2;
            }
        }

        l = 0;

        for(j = 0; j < buffera.length; ++l) {
            c[j] = (char)(k[l] ^ buffera[j]);
            c[j] = (char)(c[j] << k[l] % 8 | c[j] >> 8 - k[l] % 8);
            if (l + 1 == 16) {
                l = 0;
            }

            ++j;
        }

        return String.valueOf(c);
    }

    public static char[] dec2(char[] in) {
        char[] hasil = new char[in.length];
        int a = 0;

        int i;
        for(i = in.length - 1; i >= 0; i -= 2) {
            hasil[a] = in[i];
            ++a;
        }

        for(i = 0; i < in.length; i += 2) {
            hasil[a] = in[i];
            ++a;
        }

        return hasil;
    }

    public static char[] enc2(char[] in) {
        char[] hasil = new char[in.length];
        int a = 0;

        int i;
        for(i = in.length / 2; i < in.length; ++i) {
            hasil[a] = in[i];
            a += 2;
        }

        --a;

        for(i = 0; i < in.length / 2; ++i) {
            hasil[a] = in[i];
            a -= 2;
        }

        return hasil;
    }

    public static char[] dec3(char[] in) {
        int myheart = 32;
        int heart = 231;
        char[] hasil = new char[in.length];
        int[] table = new int[]{244, 243, 45, 158, 190, 200, 111, 121, 202, 159, 206, 8, 180, 184, 112, 65, 161, 9, 215, 232, 6, 13, 142, 22, 50, 77, 194, 73, 5, 119, 178, 252, 123, 211, 91, 70, 40, 247, 57, 162, 140, 104, 248, 125, 165, 172, 176, 128, 208, 137, 97, 228, 46, 107, 80, 118, 94, 1, 88, 166, 10, 52, 92, 7, 82, 196, 18, 255, 68, 44, 240, 75, 98, 144, 78, 150, 169, 224, 179, 149, 47, 233, 79, 217, 171, 155, 25, 36, 139, 153, 237, 64, 122, 130, 69, 129, 126, 114, 31, 174, 2, 62, 67, 105, 152, 201, 24, 207, 100, 72, 183, 117, 16, 66, 102, 205, 12, 14, 34, 89, 93, 116, 109, 239, 26, 181, 157, 148, 173, 250, 209, 227, 27, 33, 186, 108, 29, 113, 195, 43, 187, 110, 136, 54, 167, 219, 74, 218, 189, 37, 188, 182, 4, 223, 127, 51, 145, 241, 229, 151, 163, 143, 19, 156, 235, 11, 106, 245, 53, 134, 251, 199, 131, 147, 193, 59, 135, 49, 3, 210, 138, 236, 133, 28, 85, 238, 242, 222, 132, 90, 198, 197, 212, 220, 58, 20, 246, 96, 63, 0, 99, 249, 154, 76, 101, 124, 42, 55, 81, 230, 83, 164, 146, 71, 32, 17, 38, 170, 160, 48, 231, 177, 103, 175, 60, 61, 214, 221, 30, 120, 203, 23, 35, 185, 254, 226, 56, 41, 84, 39, 141, 213, 115, 192, 87, 168, 86, 253, 225, 95, 191, 204, 216, 234, 21, 15};

        for(int i = 0; i < in.length; ++i) {
            for(int j = 0; j < 256; ++j) {
                if (in[i] == table[Math.floorMod(myheart * j + heart, 255)]) {
                    hasil[i] = (char)table[j];
                }
            }
        }

        return hasil;
    }

    public static char[] enc3(char[] in) {
        int[] table = new int[]{244, 243, 45, 158, 190, 200, 111, 121, 202, 159, 206, 8, 180, 184, 112, 65, 161, 9, 215, 232, 6, 13, 142, 22, 50, 77, 194, 73, 5, 119, 178, 252, 123, 211, 91, 70, 40, 247, 57, 162, 140, 104, 248, 125, 165, 172, 176, 128, 208, 137, 97, 228, 46, 107, 80, 118, 94, 1, 88, 166, 10, 52, 92, 7, 82, 196, 18, 255, 68, 44, 240, 75, 98, 144, 78, 150, 169, 224, 179, 149, 47, 233, 79, 217, 171, 155, 25, 36, 139, 153, 237, 64, 122, 130, 69, 129, 126, 114, 31, 174, 2, 62, 67, 105, 152, 201, 24, 207, 100, 72, 183, 117, 16, 66, 102, 205, 12, 14, 34, 89, 93, 116, 109, 239, 26, 181, 157, 148, 173, 250, 209, 227, 27, 33, 186, 108, 29, 113, 195, 43, 187, 110, 136, 54, 167, 219, 74, 218, 189, 37, 188, 182, 4, 223, 127, 51, 145, 241, 229, 151, 163, 143, 19, 156, 235, 11, 106, 245, 53, 134, 251, 199, 131, 147, 193, 59, 135, 49, 3, 210, 138, 236, 133, 28, 85, 238, 242, 222, 132, 90, 198, 197, 212, 220, 58, 20, 246, 96, 63, 0, 99, 249, 154, 76, 101, 124, 42, 55, 81, 230, 83, 164, 146, 71, 32, 17, 38, 170, 160, 48, 231, 177, 103, 175, 60, 61, 214, 221, 30, 120, 203, 23, 35, 185, 254, 226, 56, 41, 84, 39, 141, 213, 115, 192, 87, 168, 86, 253, 225, 95, 191, 204, 216, 234, 21, 15};
        //int myheart = false;
        char[] hasil = new char[in.length];
        int myheart = 32;
        int heart = 231;

        for(int i = 0; i < in.length; ++i) {
            for(int j = 0; j < 256; ++j) {
                if (in[i] == table[j]) {
                    int x = (myheart * j + heart) % 255;
                    hasil[i] = (char)table[x];
                    break;
                }
            }
        }

        return hasil;
    }

    public static String dec1(char[] in) {
        int a = 25;
        int b = 7;
        int c = 21;
        char[] hasil = new char[in.length];

        for(int i = 0; i < in.length; ++i) {
            int j;
            if (in[i] >= 'a' && in[i] <= 'z') {
                for(j = 0; j < 26; ++j) {
                    if (in[i] - 97 == (j + a) % 26) {
                        hasil[i] = (char)(j + 97);
                        break;
                    }
                }
            } else if (in[i] >= 'A' && in[i] <= 'Z') {
                for(j = 0; j < 26; ++j) {
                    if (in[i] - 65 == (j + b) % 26) {
                        hasil[i] = (char)(j + 65);
                        break;
                    }
                }
            } else if (in[i] >= '0' && in[i] <= '9') {
                for(j = 0; j <= 9; ++j) {
                    if (in[i] - 48 == (j + c) % 10) {
                        hasil[i] = (char)(j + 48);
                        break;
                    }
                }
            } else {
                hasil[i] = in[i];
            }
        }

        return String.valueOf(hasil);
    }

    public static char[] enc1(String plain) {
        char[] in = plain.toCharArray();
        char[] hasil = new char[in.length];
        int a = 25;
        int b = 7;
        int c = 21;

        for(int i = 0; i < in.length; ++i) {
            if (in[i] >= 'a' && in[i] <= 'z') {
                hasil[i] = (char)((in[i] - 97 + a) % 26 + 97);
            } else if (in[i] >= 'A' && in[i] <= 'Z') {
                hasil[i] = (char)((in[i] - 65 + b) % 26 + 65);
            } else if (in[i] >= '0' && in[i] <= '9') {
                hasil[i] = (char)((in[i] - 48 + c) % 10 + 48);
            } else {
                hasil[i] = in[i];
            }
        }

        return hasil;
    }
}
