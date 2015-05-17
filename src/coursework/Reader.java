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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * класс, представляющий компилятор
 * @author A. Prodan
 */
public class Reader {
    private ArrayList<LexTable> lexTables;  //список таблиц лексем
    private Line[] lines;   //строки

    /**
     *  таблица констант
     */
    public Map<String, Lex> constants;

    /**
     *  таблица переменных
     */
    public Map<String, Data> variables;
    
    private int offset; //текущее смещение

    /**
     * текущий сегмент
     */
    public Segment openSegment;

    
    /**
     * флаг конца файла
     */
    public boolean end = false;


    /**
     * таблица меток
     */
    public Map<String, Label> labels;
    
    /**
     * Сегмент данных
     */
    public Segment dataSegment;

    /**
     * Сегмент кодов
     */
    public Segment codeSegment;
    
    public Reader() {
        lexTables = new ArrayList<>();
    }
    
    /**
     * Возвращает смещение в текущем сегменте
     * @return текущее смещение
     */
    public int getOffset() {
        return offset;
    }
    
    /**
     * Сбросить смещение в 0
     */
    public void resetOffset() {
        offset = 0;
    }
    
    /**
     * увеличить смещение
     * @param i длина команды/данных
     * @return смещение
     */
    public int incOffset(int i) {
        offset += i;
        return offset;
    }
            
    /**
     * формирует таблицу лексем из данного списка строк
     * @param list список строк
     */
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
    
    /**
     * считать очередную строку и создать таблицу лексем
     * @param s строка, которую надо разобрать
     */
    public void parse(String s) {
        
            LexTable newlex = new LexTable(s);
            lexTables.add(newlex);
    }
    
    /**
     * первый проход
     */
    public void doStuff() {
        lines = new Line[lexTables.size()];
        constants = new HashMap<>();
        labels = new HashMap<>();
        variables = new HashMap<>();
        int i = 0;
        for(LexTable lt: lexTables) {
            Line line = new Line(lt, this);
            lines[i] = line;
            i++;
        }
        
        i = 1;
        for(Line line: lines) {
            System.out.println(String.valueOf(i) + "\t" + line.print());  
            i++;
        }
        
    }

    /**
     * отобразить таблицу лексем для каждой строки и записать в файл
     */
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
