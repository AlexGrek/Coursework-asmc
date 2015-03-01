/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package coursework;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author A. Prodan
 */
public class LexTable {
    ArrayList<Lex> lexems;
    
    public LexTable(String inp) {
        lexems = new ArrayList<Lex>();
       //TODO: add lexical analysis here 
        lexems.add(new Lex("eax", Lex.Type.reg32));
    }
    
    public List<String> toText() {
        List<String> lex = new ArrayList<String>();
        for(Lex l: lexems)
            lex.add(l.getText() + " " + l.getType().name() + " : " + l.getLength());
        return lex; 
    }
    
    
}
