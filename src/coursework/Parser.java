/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author A. Prodan
 */
public class Parser {
    ArrayList<LexTable> lexTables;
            
    public void parse(List<String> list) {
        lexTables = new ArrayList<>();
        for(String s: list) {
            LexTable newlex = new LexTable(s);
            lexTables.add(newlex);
        }
        
        for(LexTable lt: lexTables) {
            for(String str: lt.toText()) {
                System.out.print(str + '\t');
            }
            System.out.println();
        }
    }
}
