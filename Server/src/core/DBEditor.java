package core;

import core.filesystem.StoredFile;

import java.util.Scanner;

public class DBEditor {

    public static void main(String[] args) {
        //create entry
        //delete entry
        //list entries
        //rename entries
        //set description
        //edit description
        //help

        String currentInput;
        Scanner scanner = new Scanner(System.in);

        while (!(currentInput = scanner.nextLine()).equals("exit")) {

            String[] splitInput = currentInput.split(" ");

            if (currentInput.startsWith("create")) {
                StoredFile file = new StoredFile(splitInput[1], splitInput[2]);
                FileDatabase.addFile(file);
            }

        }
    }

}
