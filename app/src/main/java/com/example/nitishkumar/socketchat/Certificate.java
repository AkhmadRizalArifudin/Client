package com.example.nitishkumar.socketchat;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;

class Certificate {
    private static Random r = new SecureRandom();
    private static Hashtable<String, String> cert = new Hashtable();
    private static int max = 4096;

    Certificate() {
    }

    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);

        for(int i = 0; i < hash.length; ++i) {
            String hex = Integer.toHexString(255 & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static void paymaker() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Country Name (2 letter code) []:");
        cert.put("C", sc.nextLine());
        System.out.print("State or Province Name (full name) []:");
        cert.put("ST", sc.nextLine());
        System.out.print("Organization Name (eg, company) []:");
        cert.put("O", sc.nextLine());
        System.out.print("Organization Unit Name (eg, section) []:");
        cert.put("OU", sc.nextLine());
        System.out.print("Common Name (eg, server FQDN or YOUR name) []:");
        cert.put("CN", sc.nextLine());
        System.out.print("Email Address []:");
        cert.put("E", sc.nextLine());
        String sn = new String("");

        String rs;
        for(int i = 1; i <= 12; ++i) {
            int temp = r.nextInt(256);
            rs = new String(Integer.toHexString(temp));
            if (rs.length() == 1) {
                rs = new String("0") + rs;
            }

            if (i != 12) {
                rs = rs + new String(":");
            }

            sn = sn + rs;
        }

        cert.put("SN", sn);
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException var18) {
            var18.printStackTrace();
        }

        String usr;
        try {
            usr = "root";
            rs = "";
            String url = "jdbc:MySQL://localhost/ara_structure";
            conn = DriverManager.getConnection(url, usr, rs);
            System.out.println("\nDatabase Connection Established...");
        } catch (Exception var17) {
            System.err.println("Cannot connect to database server");
            var17.printStackTrace();
        }

        usr = null;
        rs = null;
        BigInteger[] key = com.example.nitishkumar.socketchat.Cryptography.generateKey(2048);
        String namefile = new String("E:\\IKP\\ta\\" + (String)cert.get("CN") + ".key");

        try {
            File myObj = new File(namefile);
            myObj.createNewFile();
        } catch (IOException var16) {
            var16.printStackTrace();
        }

        try {
            FileWriter ww = new FileWriter(namefile);
            ww.write(key[1].toString());
            ww.write("\n");
            ww.write(key[2].toString());
            ww.close();
        } catch (IOException var15) {
            var15.printStackTrace();
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime nows = LocalDateTime.now();
        String y = new String(dtf.format(nows));
        int ty = Integer.parseInt(y.substring(0, 4));
        ++ty;
        String tyear = String.valueOf(ty);
        String x = new String(tyear + y.substring(4, y.length()));

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO user(SN, O, E, C, CN, OU, ST) VALUES('" + (String)cert.get("SN") + "', '" + (String)cert.get("O") + "', '" + (String)cert.get("E") + "', '" + (String)cert.get("C") + "', '" + (String)cert.get("CN") + "', '" + (String)cert.get("OU") + "', '" + (String)cert.get("ST") + "');");
            stmt.executeUpdate("INSERT INTO repository(email, modulus,valid_from,valid_until) VALUES('" + (String)cert.get("E") + "', '" + key[0].toString() + "', '" + y + "', '" + x + "');");
            conn.close();
        } catch (SQLException var14) {
            System.out.println("SQLException: " + var14.getMessage());
            System.out.println("SQLState: " + var14.getSQLState());
            System.out.println("VendorError: " + var14.getErrorCode());
        }

        crtmaker((String)cert.get("E"));
    }

    public static void crtmaker(String s) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException var54) {
            var54.printStackTrace();
        }

        Connection conn = null;

        String userName;
        String password;
        try {
            userName = "root";
            password = "";
            String url = "jdbc:MySQL://localhost/ara_structure?allowMultiQueries=true";
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("\nDatabase Connection Established...");
        } catch (Exception var53) {
            System.err.println("Cannot connect to database server");
            var53.printStackTrace();
        }

        Statement stmt = null;
        ResultSet rs = null;
        new Scanner(System.in);

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user INNER JOIN repository on user.E=repository.email WHERE E LIKE '" + s + "'");
            String SN = null;
            String ST = null;
            String scrt = null;
            String C = null;
            String OU = null;
            String O = null;
            String CN = null;
            String E = null;
            String datee = null;
            String datec = null;
            String sg_cert = null;
            String hcert = null;
            String modulus = null;
            String modulusc = null;
            String SNc = null;
            String Cc = null;
            String Oc = null;
            String CNc = null;
            String Ec = null;

            String scrtr;
            for(scrtr = null; rs.next(); modulusc = rs.getString("modulus")) {
                SN = rs.getString("SN");
                C = rs.getString("C");
                ST = rs.getString("ST");
                OU = rs.getString("OU");
                CN = rs.getString("CN");
                O = rs.getString("O");
                E = rs.getString("E");
                datee = rs.getString("Valid_until");
                datec = rs.getString("Valid_from");
            }

            for(rs = stmt.executeQuery("SELECT * FROM ca  WHERE ST='" + ST + "'"); rs.next(); scrtr = rs.getString("scrt")) {
                SNc = rs.getString("SN");
                Cc = rs.getString("C");
                CNc = rs.getString("CN");
                Oc = rs.getString("O");
                Ec = rs.getString("E");
            }

            String cert = null;

            try {
                FileReader fr = new FileReader("E:\\IKP\\ta\\" + CNc + ".crt");
                BufferedReader br = new BufferedReader(fr);

                String line;
                while((line = br.readLine()) != null) {
                    if (line.contains("-----BEGIN CERTIFICATE-----")) {
                        line = br.readLine();
                        cert = line.replaceAll("\r\n", "");
                        break;
                    }
                }
            } catch (Exception var57) {
                var57.printStackTrace();
            }

            String[] priv = new String[3];
            int i = 0;

            try {
                Scanner scanner;
                if (ST.equals("Jawa Timur")) {
                    scanner = new Scanner(new File("E:\\\\IKP\\\\ta\\\\cajatim.key"));
                } else if (ST.equals("Jawa Tengah")) {
                    scanner = new Scanner(new File("E:\\\\IKP\\\\ta\\\\cajateng.key"));
                } else {
                    scanner = new Scanner(new File("E:\\\\IKP\\\\ta\\\\cajabar.key"));
                }

                while(scanner.hasNextLine()) {
                    priv[i] = scanner.nextLine();
                    ++i;
                }

                modulus = priv[0];
            } catch (Exception var56) {
                var56.printStackTrace();
            }

            sg_cert = "Certificate:\n\tData:\n\t\tVersion: 1\n\t\tSerial Number: " + SN + "\n\t\tSignature Algorithm: sha256WithRabinEncryption\n\t\tIssuer: C=" + Cc + ", O=" + Oc + ", CN=" + CNc + "/emailAddress=" + Ec + "\n\t\tValidity\n\t\t\tNot Before: " + datec + "\n\t\t\tNot After : " + datee + "\n\t\tSubject: C=" + C + ", ST=" + ST + ", O=" + O + ", OU=" + OU + ", CN=" + CN + "/emailAddress=" + E + "\n\t\tSubject Public Key Info:\n\t\t\tPublic Key Algorithm: rabinEncryption\n\t\t\t\tRabin Public-Key: (2048 bit)\n\t\t\t\tModulus:\n\t\t\t\t\t" + modulusc + "\n\t\t\t\tExponent: 2\n\t\tA124v1 extensions:\n\t\t\tA124v1 Basic Constraints: \n\t\t\t\tCA:FALSE\n\t\t\tNetscape Comment: \n\t\t\t\tARA Generated Certificate" + "\n" + cert;
            String[] en = RabinCryptosystem.enc(sg_cert, scrtr, modulus);
            sg_cert = en[0];

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] messageDigest = md.digest(sg_cert.getBytes());
                BigInteger no = new BigInteger(1, messageDigest);

                for(hcert = no.toString(16); hcert.length() < 32; hcert = "0" + hcert) {
                }
            } catch (NoSuchAlgorithmException var55) {
                throw new RuntimeException(var55);
            }

            String p = new String("Certificate:\n\tData:\n\t\tVersion: 1\n\t\tSerial Number: " + SN + "\n\t\tSignature Algorithm: sha256WithRabinEncryption\n\t\tIssuer: C=" + Cc + ", O=" + Oc + ", CN=" + CNc + "/emailAddress=" + Ec + "\n\t\tValidity\n\t\t\tNot Before: " + datec + "\n\t\t\tNot After : " + datee + "\n\t\tSubject: C=" + C + ", ST=" + ST + ", O=" + O + ", OU=" + OU + ", CN=" + CN + "/emailAddress=" + E + "\n\t\tSubject Public Key Info:\n\t\t\tPublic Key Algorithm: rabinEncryption\n\t\t\t\tRabin Public-Key: (2048 bit)\n\t\t\t\tModulus:\n\t\t\t\t\t" + modulusc + "\n\t\t\t\tExponent: 2\n\t\tA124v1 extensions:\n\t\t\tA124v1 Basic Constraints: \n\t\t\t\tCA:FALSE\n\t\t\tNetscape Comment: \n\t\t\t\tARA Generated Certificate\n\n\tEncryption Algorithm: ARAEncryption\n\t\t" + hcert);
            String q = p;
            if (p.length() % 2 == 1) {
                p = p + " ";
            }

            p = p.replaceAll("\n", Character.toString((char) 131));
            p = p.replaceAll("\t", Character.toString((char) 142));
            p = p.replaceAll(" ", Character.toString((char) 224));
            String c = String.valueOf(com.example.nitishkumar.socketchat.Cryptography.enc4(com.example.nitishkumar.socketchat.Cryptography.enc3(com.example.nitishkumar.socketchat.Cryptography.enc2(com.example.nitishkumar.socketchat.Cryptography.enc1(p))), SN));
            String d = String.valueOf(com.example.nitishkumar.socketchat.Cryptography.dec1(com.example.nitishkumar.socketchat.Cryptography.dec2(com.example.nitishkumar.socketchat.Cryptography.dec3(com.example.nitishkumar.socketchat.Cryptography.dec4(c, SN)))));
            d = d.replaceAll(Character.toString((char) 131), "\n");
            d = d.replaceAll(Character.toString((char) 142), "\t");
            d = d.replaceAll(Character.toString((char) 224), " ");
            String w = Base64.getEncoder().encodeToString(c.getBytes(StandardCharsets.UTF_8));
            stmt.executeUpdate("UPDATE `repository` SET `sg_cert`='" + w + "' WHERE email='" + E + "'");
            String payload = q + hcert + "\n-----BEGIN CERTIFICATE-----\n" + w + "\n-----END CERTIFICATE-----\n";
            exporter(payload, CN);
        } catch (SQLException var58) {
            System.out.println("SQLException: " + var58.getMessage());
            System.out.println("SQLState: " + var58.getSQLState());
            System.out.println("VendorError: " + var58.getErrorCode());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException var52) {
                }

                password = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException var51) {
                }

                userName = null;
            }

        }

    }

    public static String genag(String s) {
        String hcert = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException var51) {
            var51.printStackTrace();
        }

        Connection conn = null;

        String userName;
        String password;
        try {
            userName = "root";
            password = "";
            String url = "jdbc:MySQL://localhost/ara_structure?allowMultiQueries=true";
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception var50) {
            System.err.println("Cannot connect to database server");
            var50.printStackTrace();
        }

        Statement stmt = null;
        ResultSet rs = null;
        new Scanner(System.in);

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user INNER JOIN repository on user.E=repository.email WHERE CN='" + s + "'");
            String SN = null;
            String ST = null;
            String scrt = null;
            String C = null;
            String OU = null;
            String O = null;
            String CN = null;
            String E = null;
            String datee = null;
            String datec = null;
            String sg_cert = null;
            String modulus = null;
            String modulusc = null;
            String SNc = null;
            String Cc = null;
            String Oc = null;
            String CNc = null;
            String Ec = null;

            String scrtr;
            for(scrtr = null; rs.next(); modulusc = rs.getString("modulus")) {
                SN = rs.getString("SN");
                C = rs.getString("C");
                ST = rs.getString("ST");
                OU = rs.getString("OU");
                CN = rs.getString("CN");
                O = rs.getString("O");
                E = rs.getString("E");
                datee = rs.getString("Valid_until");
                datec = rs.getString("Valid_from");
            }

            for(rs = stmt.executeQuery("SELECT * FROM ca  WHERE ST='" + ST + "'"); rs.next(); scrtr = rs.getString("scrt")) {
                SNc = rs.getString("SN");
                Cc = rs.getString("C");
                CNc = rs.getString("CN");
                Oc = rs.getString("O");
                Ec = rs.getString("E");
            }

            String cert = null;

            try {
                FileReader fr = new FileReader("E:\\IKP\\ta\\" + CNc + ".crt");
                BufferedReader br = new BufferedReader(fr);

                String line;
                while((line = br.readLine()) != null) {
                    if (line.contains("-----BEGIN CERTIFICATE-----")) {
                        line = br.readLine();
                        cert = line.replaceAll("\r\n", "");
                        break;
                    }
                }
            } catch (Exception var54) {
                var54.printStackTrace();
            }

            String[] priv = new String[3];
            int i = 0;

            try {
                Scanner scanner;
                if (ST.equals("Jawa Timur")) {
                    scanner = new Scanner(new File("E:\\\\IKP\\\\ta\\\\cajatim.key"));
                } else if (ST.equals("Jawa Tengah")) {
                    scanner = new Scanner(new File("E:\\\\IKP\\\\ta\\\\cajateng.key"));
                } else {
                    scanner = new Scanner(new File("E:\\\\IKP\\\\ta\\\\cajabar.key"));
                }

                while(scanner.hasNextLine()) {
                    priv[i] = scanner.nextLine();
                    ++i;
                }

                modulus = priv[0];
            } catch (Exception var53) {
                var53.printStackTrace();
            }

            sg_cert = "Certificate:\n\tData:\n\t\tVersion: 1\n\t\tSerial Number: " + SN + "\n\t\tSignature Algorithm: sha256WithRabinEncryption\n\t\tIssuer: C=" + Cc + ", O=" + Oc + ", CN=" + CNc + "/emailAddress=" + Ec + "\n\t\tValidity\n\t\t\tNot Before: " + datec + "\n\t\t\tNot After : " + datee + "\n\t\tSubject: C=" + C + ", ST=" + ST + ", O=" + O + ", OU=" + OU + ", CN=" + CN + "/emailAddress=" + E + "\n\t\tSubject Public Key Info:\n\t\t\tPublic Key Algorithm: rabinEncryption\n\t\t\t\tRabin Public-Key: (2048 bit)\n\t\t\t\tModulus:\n\t\t\t\t\t" + modulusc + "\n\t\t\t\tExponent: 2\n\t\tA124v1 extensions:\n\t\t\tA124v1 Basic Constraints: \n\t\t\t\tCA:FALSE\n\t\t\tNetscape Comment: \n\t\t\t\tARA Generated Certificate" + "\n" + cert;
            String[] en = RabinCryptosystem.enc(sg_cert, scrtr, modulus);
            sg_cert = en[0];

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] messageDigest = md.digest(sg_cert.getBytes());
                BigInteger no = new BigInteger(1, messageDigest);

                for(hcert = no.toString(16); hcert.length() < 32; hcert = "0" + hcert) {
                }
            } catch (NoSuchAlgorithmException var52) {
                throw new RuntimeException(var52);
            }
        } catch (SQLException var55) {
            System.out.println("SQLException: " + var55.getMessage());
            System.out.println("SQLState: " + var55.getSQLState());
            System.out.println("VendorError: " + var55.getErrorCode());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException var49) {
                }

                password = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException var48) {
                }

                userName = null;
            }

        }

        return hcert;
    }

    public static Boolean verify(String cert, String CN) {
        Boolean cek = false;

        try {
            cert = new String(Base64.getDecoder().decode(cert), "UTF-8");
        } catch (Exception var27) {
            var27.printStackTrace();
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException var26) {
            var26.printStackTrace();
        }

        Connection conn = null;

        String userName;
        String pass;
        String SN;
        try {
            userName = "root";
            pass = "";
            SN = "jdbc:MySQL://localhost/ara_structure";
            conn = DriverManager.getConnection(SN, userName, pass);
        } catch (Exception var25) {
            System.err.println("Cannot connect to database server");
            var25.printStackTrace();
        }

        Statement stmt = null;
        ResultSet rs = null;
        SN = null;

        try {
            stmt = conn.createStatement();

            for(rs = stmt.executeQuery("SELECT SN FROM `user` WHERE CN='" + CN + "'"); rs.next(); SN = rs.getString("SN")) {
            }

            String d = String.valueOf(com.example.nitishkumar.socketchat.Cryptography.dec1(com.example.nitishkumar.socketchat.Cryptography.dec2(com.example.nitishkumar.socketchat.Cryptography.dec3(com.example.nitishkumar.socketchat.Cryptography.dec4(cert, SN)))));
            d = d.replaceAll(Character.toString((char) 131), "\n");
            d = d.replaceAll(Character.toString((char) 142), "\t");
            d = d.replaceAll(Character.toString((char) 224), " ");
            String[] pack = d.split("\n");
            String cacert = pack[pack.length - 1].replaceAll("\t", "");
            cacert = cacert.replaceAll(" ", "");
            if (cacert.equals(genag(CN))) {
                cek = true;
            }
        } catch (SQLException var28) {
            System.out.println("SQLException: " + var28.getMessage());
            System.out.println("SQLState: " + var28.getSQLState());
            System.out.println("VendorError: " + var28.getErrorCode());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException var24) {
                }

                pass = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException var23) {
                }

                userName = null;
            }

        }

        return cek;
    }

    public static void exporter(String payload, String CN) {
        String namefile = new String("E:\\IKP\\ta\\" + CN + ".crt");

        try {
            File myObj = new File(namefile);
            myObj.createNewFile();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        try {
            FileWriter ww = new FileWriter(namefile);
            ww.write(payload);
            ww.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }
}

