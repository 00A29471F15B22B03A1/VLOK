package core.logging;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final String INFO = "INFO: ";
    private static final String WARNING = "WARNING: ";
    private static final String ERROR = "ERROR: ";

    private static final Logger INSTANCE = new Logger();

    private final JFrame frame;
    private final JTextArea logArea;

    private PrintWriter outputStream;

    public Logger() {
        try {
            outputStream = new PrintWriter(new FileOutputStream(new File("log.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum()));
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener((ae) -> INSTANCE.logArea.setText(""));
        frame.add(clearButton, BorderLayout.SOUTH);

        frame.setVisible(true);
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
        INSTANCE.logArea.append(fullMessage + "\n");
        System.out.println(fullMessage);
        INSTANCE.outputStream.println(fullMessage);
        INSTANCE.outputStream.flush();
    }

    private static String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public static void closeWindow() {
        INSTANCE.frame.dispose();
    }

    public static void close() {
        INSTANCE.outputStream.close();
    }
}
