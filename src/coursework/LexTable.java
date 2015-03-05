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
        analyse(inp);
    }
    
    public List<String> toText() {
        List<String> lex = new ArrayList<String>();
        for(Lex l: lexems)
            lex.add(l.getText() + " " + l.getType().name() + " : " + l.getLength());
        return lex; 
    }
    
    /**
     * @return true if char c is one-symbol lexeme
     */
    private static boolean isOneSymbol(char c) {
        return c == ':' || c == '[' || c == ']' || 
                c == ',' || c == '+';
    }
    
    private static Lex parseOneSymbol(char c) {
        String lexText = "" + c;
        return new Lex(lexText, Lex.Type.single);
    }
    
    //lexical analyser
    private void analyse(String s) {
        String lx = ""; //lexeme
        
        for(int i = 0; i < s.length (); i++) {
            if (s.charAt(i) == ' ') {
                if (!lx.isEmpty()) {
                    lexems.add(parseLex(lx));
                    lx = "";
                }
            } else
            if (isOneSymbol(s.charAt(i))) {
                if (!lx.isEmpty()) {
                    lexems.add(parseLex(lx));
                    lx = "";
                }
                lexems.add(parseOneSymbol(s.charAt(i)));
            } else
            lx += s.charAt(i);
        }
        if (!lx.isEmpty())
            lexems.add(parseLex(lx));
    }
    
    /**
     * @return the lexeme from string
     */
    private static Lex parseLex(String lex) {
        String pvalue = lex.toLowerCase(); //parse value
        Lex.Type t; //identified lexeme type
        
        //try to compare value with known constants
        switch (pvalue) {
            case "db":
            case "dw":
            case "dd":
                t = Lex.Type.dataDir;
                break;
            case "al":
            case "ah":
            case "bl":
            case "bh":
            case "cl":
            case "ch":
            case "dl":
            case "dh":
                t = Lex.Type.reg8;
                break;
            case "eax":
            case "ebx":
            case "ecx":
            case "edx":  
                t = Lex.Type.reg32;
                break;
            case "ebp":
            case "esp":
            case "esi":
            case "edi":
                t = Lex.Type.reg32addr;
                break;
            case "cs":
            case "ds":
            case "es":
            case "ss":
            case "gs":
            case "fs":
                t = Lex.Type.seg;
                break;
            case "cli":
            case "inc":
            case "dec":
            case "div":
            case "adc":
            case "cmp":
            case "and":
            case "mov":
            case "xor":
            case "jb":
                t = Lex.Type.instruct;
                break;
            default:
                // if lexeme is not identified - maybe it's user defined?
                t = Lex.Type.userId;
                break;
        }
        
        //if lexeme identified - let's return it now!
        if (t != Lex.Type.userId) {
            return new Lex(pvalue, t);
        }
        
        //try another way - maybe it's a text constant?
        if (lex.startsWith("\"")) {
            //yes, it is
            if (lex.endsWith("\"") || lex.length() > 2) {
                //return only text, without the " symbols
                return new Lex(lex.substring(1, lex.length() - 2), Lex.Type.text);
            }
            //string doesen't end with " or is empty - return undefined
            return new Lex(pvalue, Lex.Type.undefined);
        }
        
        //maybe it's numeric constant?
        if (lex.charAt(0) == '-' || Character.isDigit(lex.charAt(0))) {
            //starts with '-' or digit - it's definetely a numeric
            
            //maybe it's binary?
            if (pvalue.endsWith("b")) {
                try {
                    Integer.parseInt(lex.substring(0, lex.length() - 2), 2);
                    t = Lex.Type.constBin;
                } catch (NumberFormatException ex) {
                    //no, it's not a binary constant
                    return new Lex(pvalue, Lex.Type.undefined);
                }
            } else
            
            //maybe it's hex constant?
            if (pvalue.endsWith("h")) {
                try {
                    Integer.parseInt(lex.substring(0, lex.length() - 2), 16);
                    t = Lex.Type.constHex;
                } catch (NumberFormatException ex) {
                    //no, it's not a hexadecimal constant
                    return new Lex(pvalue, Lex.Type.undefined);
                }
            } else
            
            //maybe it's decimal const - last character must be a digit
            if (Character.isDigit(lex.charAt(lex.length()-1))) {
                try {
                    Integer.parseInt(lex, 10);
                    t = Lex.Type.constDec;
                } catch (NumberFormatException ex) {
                    //no, it's not a decimal constant
                    return new Lex(pvalue, Lex.Type.undefined);
                }
            } else 
                
            //or last character is 'd'
            if (pvalue.endsWith("d")) {
                try {
                    Integer.parseInt(lex.substring(0, lex.length() - 2), 10);
                    t = Lex.Type.constDec;
                } catch (NumberFormatException ex) {
                    //no, it's not a decimal constant at all
                    return new Lex(pvalue, Lex.Type.undefined);
                }
            } else 
            
            //no. So it's an error
                return new Lex(pvalue, Lex.Type.undefined);
        }
        
        //so it's a user-defined variable
        return new Lex(pvalue, t);
    }
}
