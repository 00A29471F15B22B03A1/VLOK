package core;

import core.logging.Console;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String getDownloadPath() {
        return System.getProperty("user.home") + "/Downloads/";
    }

    public static void selectFile(String path) {
        try {
            Runtime.getRuntime().exec("explorer.exe /select," + path);
        } catch (IOException e) {
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

    public static void appendFile(String path, String text) {
        try {
            Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to append to file " + path);
        }
    }

    public static String readFileWithIS(String path) {
        StringBuilder fullFile = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(path)));

            String line;
            while ((line = reader.readLine()) != null)
                fullFile.append(line).append("\n");

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to read file " + path);
        }

        return fullFile.toString();
    }

    public static String readFile(String path) {
        StringBuilder fullFile = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));

            String line;
            while ((line = reader.readLine()) != null)
                fullFile.append(line).append("\n");

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to read file " + path);
        }

        return fullFile.toString();
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
