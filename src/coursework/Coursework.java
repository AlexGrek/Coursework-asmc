/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework;

import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author A. Prodan
 */
public class Coursework {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanIn = new Scanner(System.in);
        System.out.println("What should I compile?");
        String str = scanIn.nextLine();
        Path file = Paths.get(str);
        try {
        List<String> strs = Files.readAllLines(file);
        } catch(IOException ex) {
            System.out.println("Sorry, cannot open this file :(\n" + ex.toString());
            System.exit(0);
        }
    }
    
}
