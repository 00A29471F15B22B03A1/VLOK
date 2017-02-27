package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.filesystem.SupportedFileTypes;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class ClientMain {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        NetworkClient client = new NetworkClient();
        client.connect("vlok.dynu.com");

        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("Connected " + connection.getRemoteAddressTCP());
            }
        });

        System.out.println("Successfully connected to server");

        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);

        String[] supportedFiles = new String[SupportedFileTypes.values().length];

        int i = 0;
        for (SupportedFileTypes file : SupportedFileTypes.values())
            supportedFiles[i++] = file.toString().toLowerCase();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Upload Files", supportedFiles);
        chooser.setFileFilter(filter);

        while (true) {
            int returnValue = chooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File[] files = chooser.getSelectedFiles();

                String path = JOptionPane.showInputDialog("Please gimme path:");

                FileUploader.sendFiles(path, files, client);

            }
        }
    }

}
