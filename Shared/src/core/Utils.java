package core;

import core.logging.Console;

import javax.swing.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static void setNativeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Console.info("Set ui look and feel to system default");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static String hash(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(value.getBytes());
            return bytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Console.err("Failed to get encryption algorithm");
        }

        return null;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String readFileWithIS(String path) {
        String fullFile = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(path)));

            String line;
            while ((line = reader.readLine()) != null)
                fullFile += line + "\n";

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to read file " + path);
        }

        return fullFile;
    }

    public static String readFile(String path) {
        String fullFile = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));

            String line;
            while ((line = reader.readLine()) != null)
                fullFile += line + "\n";

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to read file " + path);
        }

        return fullFile;
    }

    public static void writeToFile(String path, String contents) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
            writer.write(contents);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to write to file " + path);
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static boolean isBoolean(String s) {
        try {
            Boolean.parseBoolean(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

}
