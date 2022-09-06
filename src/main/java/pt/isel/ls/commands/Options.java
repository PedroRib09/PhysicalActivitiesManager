package pt.isel.ls.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Options {
    public static void listCommands() {
        try {
            File file = new File("Commands.txt");
            try (Scanner reader = new Scanner(file)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file");
        }

    }
}
