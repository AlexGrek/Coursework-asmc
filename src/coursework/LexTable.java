/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package coursework;

import java.util.ArrayList;
import java.util.List;

/**
 * таблица лексем
 * @author A. Prodan
 */
public class LexTable {
    ArrayList<Lex> lexems;
    
    public LexTable(String inp) {
        lexems = new ArrayList<Lex>();
        inp = cleanup(inp);
        analyse(inp);
    }
    
    //убрать комментарии из строки
    private String cleanup(String str) {
        int i = str.indexOf(';');
        if (i >= 0) {
            str = str.substring(0, i);
        }
        return str;
    }
    
    /**
     * заменить лексему
     * @param index номер лексемы
     * @param to лексема, на которую будет заменена лексема по индексу index
     */
    public void Change(int index, Lex to) {
        lexems.remove(index);
        lexems.add(index, to);
    }
    
    /**
     * вернуть лексему по заданному индексу
     * @param n индекс
     * @return лексема по индексу n
     */
    public Lex get(int n) {
        return lexems.get(n);
    }
    
    /**
     * превращает таблицу лексем в массив
     * @return таблица лексем как массив
     */
    public Lex[] getArray() {
        Lex[] array = new Lex[lexems.size()];
        lexems.toArray(array);
        return array;
    }
    
    /**
     * оформляет таблицу лексем
     * @return таблица лексем в виде строк
     */
    public List<String> toText() {
        List<String> lex = new ArrayList<>();
        for(Lex l: lexems)
            lex.add(l.getText() + " [" + l.getType().name() + "(" + l.getLength() + ")]" + (l.isValueType()? String.valueOf(l.getValue()) : ""));
        return lex; 
    }
    
    /**
     * @return true if char c is one-symbol lexeme
     */
    private static boolean isOneSymbol(char c) {
        return c == ':' || c == '[' || c == ']' || 
                c == ',' || c == '+' || c == '(' || c == ')' || c == '/' || 
                c == '*' || c == '-';
    }
    
    private static Lex parseOneSymbol(char c) {
        String lexText = "" + c;
        return new Lex(lexText, Lex.Type.single);
    }
    
    /**
     * возвращает часть таблицы лексем как массив
     * @param startIndex начало массива
     * @return массив от startIndex до конца
     */
    public Lex[] getPart(int startIndex) {
        Lex[] arr = new Lex[this.lexems.size() - startIndex];
        for (int i = startIndex; i < lexems.size(); i++) {
            arr[i - startIndex] = lexems.get(i);
        }
        return arr;
    }
    
    //lexical analyser
    private void analyse(String s) {
        String lx = ""; //lexeme
        boolean stringMode = false; //reading of string constant
        char stringChar = ' '; //character the string begins and ends with
        
        for(int i = 0; i < s.length (); i++) {
            if(stringMode) {
                if (s.charAt(i) == stringChar) {
                    stringMode = false;
                    lx += stringChar;
                    lexems.add(parseLex(lx));
                    lx = "";
                }
                else {
                    lx += s.charAt(i);
                }
            } else
            if (s.charAt(i) == '\'' || s.charAt(i) == '"') {
                stringMode = true;
                stringChar = s.charAt(i);
                lx += stringChar;
            } else
            if (s.charAt(i) == ' ' || s.charAt(i) == '\t') {
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
            case "ebp":
            case "esp":
            case "esi":
            case "edi":
                t = Lex.Type.reg32;
                break;
            case "cs":
            case "ds":
            case "es":
            case "ss":
            case "gs":
            case "fs":
                t = Lex.Type.seg;
                break;
            case "segment":
            case "ends":
            case "end":
            case "equ":
                t = Lex.Type.dir;
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
        if (lex.startsWith("\"") || lex.startsWith("'")) {
            //yes, it is
            if ((lex.endsWith("\"") || lex.endsWith("'")) && lex.length() > 2) {
                //return only text, without the " symbols
                return new Lex(lex.substring(1, lex.length() - 1), Lex.Type.text);
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
                    int i = Integer.parseInt(lex.substring(0, lex.length() - 1), 2);
                    t = Lex.Type.constBin;
                    return new Lex(pvalue, t, i);
                } catch (NumberFormatException ex) {
                    //no, it's not a binary constant
                    
                    return new Lex(pvalue, Lex.Type.undefined);
                }
            } else
            
            //maybe it's hex constant?
            if (pvalue.endsWith("h")) {
                try {
                    int i = Integer.parseInt(lex.substring(0, lex.length() - 1), 16);
                    t = Lex.Type.constHex;
                    return new Lex(pvalue, t, i);
                } catch (NumberFormatException ex) {
                    //no, it's not a hexadecimal constant
                    return new Lex(pvalue, Lex.Type.undefined);
                }
            } else
            
            //maybe it's decimal const - last character must be a digit
            if (Character.isDigit(lex.charAt(lex.length()-1))) {
                try {
                    int i = Integer.parseInt(lex, 10);
                    t = Lex.Type.constDec;
                    return new Lex(pvalue, t, i);
                } catch (NumberFormatException ex) {
                    //no, it's not a decimal constant
                    return new Lex(pvalue, Lex.Type.undefined);
                }
            } else 
                
            //or last character is 'd'
            if (pvalue.endsWith("d")) {
                try {
                    int i = Integer.parseInt(lex.substring(0, lex.length() - 1), 10);
                    t = Lex.Type.constDec;
                    return new Lex(pvalue, t, i);
                } catch (NumberFormatException ex) {
                    //no, it's not a decimal constant at all
                    return new Lex(pvalue, Lex.Type.undefined);
                }
            } else 
            
            //no. So it's an error
                return new Lex(pvalue, Lex.Type.undefined);
        }
        
        //so maybe it's a user-defined variable
        if (pvalue.length() <= 6 && pvalue.matches("^[a-z][0-9a-z]*$")) {
            return new Lex(pvalue, t);
        }
        return new Lex(pvalue, Lex.Type.undefined);
    }

    /**
     * позволяет узнать количество лексем на строке
     * @return размер таблицы лексем
     */
    public int size() {
        return lexems.size();
    }
    
    /**
     * указывает, есть ли в таблице лексемы типа t
     * @param t тип лексемы
     * @return true - если есть, false - если нет
     */
    public boolean containsType(Lex.Type t) {
        for(Lex l: lexems) {
            if (l.getType() == t)
                return true;
        }
        return false;
    }
}
