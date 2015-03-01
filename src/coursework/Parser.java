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
    ArrayList<LexTable> lexTable;
            
    public void parse(List<String> list) {
        lexTable = new ArrayList<LexTable>();
        for(String s: list) {
            lexTable.add(new LexTable(s));
            //TODO: remove that
            for(String z: lexTable.get(0).toText()) {
                System.out.println(z);
            }
        }
        
    }
}
