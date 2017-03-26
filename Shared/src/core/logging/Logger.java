package core.logging;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final String INFO = "INFO: ";
    private static final String WARNING = "WARNING: ";
    private static final String ERROR = "ERROR: ";

    private static final Logger INSTANCE = new Logger();

    private final JFrame frame;
    private final JTextArea logArea;

    public Logger() {
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
        System.out.println("\u001B[30m" + INFO + message + "\u001B[0m");
    }

    public static void warn(String message) {
        print(WARNING, message);
        System.out.println("\u001B[33m" + WARNING + message + "\u001B[0m");
    }

    public static void err(String message) {
        print(ERROR, message);
        System.out.println("\u001B[31m" + ERROR + message + "\u001B[0m");
    }

    private static void print(String type, String message) {
        String fullMessage = getTime() + "|" + type + message;
        INSTANCE.logArea.append(fullMessage + "\n");
    }

    private static String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public static void closeWindow() {
        INSTANCE.frame.dispose();
    }
}
