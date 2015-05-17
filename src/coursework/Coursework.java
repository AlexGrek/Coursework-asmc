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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

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
        Reader p = new Reader();
        try {
            System.out.print("\t Note: you better place your file here: ");
            System.out.println(new File(".").getCanonicalPath());
            
            File file = new File(str);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
		p.parse(line);
                System.out.printf("%d\t%s\n", i++, line);
            }
            p.ShowLexTables();
            p.doStuff();
            fr = new FileReader(file);
            reader = new BufferedReader(fr); //для чтения файла сначала
            //для записи в новый файл
            PrintWriter writer = new PrintWriter("out.lst", "UTF-8");
            writer.println("A. Prodan's assembler\n");
            i = 0;
            while ((line = reader.readLine()) != null) {
		String s = p.showLine(i, line);
                System.out.println(s);
                writer.println(s);
                i++;
            }
            
            String[] segs = p.showSegments();
            writer.println("\nSegments:");
            for (String s : segs) {
                if (s == null)
                    break;
                System.out.println(s);
                writer.println(s);
            }
            
            String[] symbols = p.showSymbols();
            writer.println("\nSymbols:");
            for (String s : symbols) {
                if (s == null)
                    break;
                System.out.println(s);
                writer.println(s);
            }
            
            writer.close();
        } catch (IOException ex) {
            System.out.println("Sorry, cannot open this file :(\n" + ex.toString());
            System.exit(0);
        }
    }

}
