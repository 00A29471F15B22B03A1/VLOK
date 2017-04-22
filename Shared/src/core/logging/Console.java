package core.logging;

import core.prefs.Prefs;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {

    private static final String INFO = "INFO: ";
    private static final String WARNING = "WARNING: ";
    private static final String ERROR = "ERROR: ";

    private static final Console INSTANCE = new Console();

    private final JFrame frame;
    private final JTextArea logArea;

//    private PrintWriter outputStream;

    public Console() {
//        try {
//            File file = new File("VLOKData/log.txt");
//            if(!file.exists()) {
//                file.getParentFile().mkdirs();
//                file.createNewFile();
//            }
//            outputStream = new PrintWriter(new FileOutputStream(file));
//        } catch (IOException e) {
//            e.printStackTrace();
//            Console.err("Failed to create log file");
//        }

        frame = new JFrame("Log");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLocation(200, 100);
        frame.setResizable(true);
        frame.setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        frame.add(scrollPane, BorderLayout.CENTER);

        JTextField inputField = new JTextField();
        inputField.addActionListener(e -> {
            CommandManager.handleCommand(inputField.getText());
            inputField.setText("");
        });
        frame.add(inputField, BorderLayout.SOUTH);

        //if (Prefs.SETTINGS.getBoolean("debug"))
        //    frame.setVisible(true);
    }

    public static void info(String message) {
        print(INFO, message);
    }

    public static void warn(String message) {
        print(WARNING, message);
    }

    public static void err(String message) {
        print(ERROR, message);
    }

    private static void print(String type, String message) {
        String fullMessage = getTime() + " | " + type + message;
        System.out.println(fullMessage);
//        INSTANCE.logArea.append(fullMessage + "\n");
//        INSTANCE.logArea.setCaretPosition(INSTANCE.logArea.getDocument().getLength());
//        INSTANCE.outputStream.println(fullMessage);
//        INSTANCE.outputStream.flush();
    }

    public static void openWindow() {
        INSTANCE.frame.setVisible(true);
    }

    public static void close() {
//        INSTANCE.outputStream.close();
        INSTANCE.frame.dispose();
    }

    private static String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public static void clear() {
        INSTANCE.logArea.setText("");
    }
}
