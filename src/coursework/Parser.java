/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author A. Prodan
 */
public class Parser {
    ArrayList<LexTable> lexTables;
    
    public Parser() {
        lexTables = new ArrayList<>();
    }
            
    public void parse(List<String> list) {
        for(String s: list) {
            LexTable newlex = new LexTable(s);
            lexTables.add(newlex);
        }
        
        for(LexTable lt: lexTables) {
            for(String str: lt.toText()) {
                System.out.print(str + "   ");
            }
            System.out.println();
        }
        
        ShowLexTables();
    }
    
    public void parse(String s) {
        
            LexTable newlex = new LexTable(s);
            lexTables.add(newlex);
    }
    
    public void ShowLexTables() {
         for(LexTable lt: lexTables) {
            for(String str: lt.toText()) {
                System.out.print(str + "   ");
            }
            System.out.println();
        }
        try (PrintWriter writer = new PrintWriter("lexTable.txt", "UTF-8")) {
            writer.println("Таблиця лексем");
            for(LexTable lt: lexTables) {
                for(String str: lt.toText()) {
                    writer.print(str + "   ");
                }
            writer.println();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found");
        } catch (UnsupportedEncodingException ex) {
            System.out.println("Error: Unsupported Encoding");
        }
    }
}
