/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package coursework;

/**
 *
 * @author A. Prodan
 */
public class Lex {
    private final String text;
    private final Type type;
    
    public Lex(String str, Type tp) {
        text = str;
        type = tp;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }
    
    /**
     * @return the length of text
     */
    public int getLength() {
        return text.length();
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }
    
    public enum Type {
        userId, //ідентифікатор користувача
        single, //односимвольна
        instruct, //інструкція процесора
        seg, //сегментний регістр
        reg8, //8-розрядний регістр ЗП
        reg32, //32-розрядний регістр ЗП
        reg32addr, //32-розрядний регістр адрес
        constDec, constHex, constBin, //константи
        data, //Ідентифікатор директиви даних 
        text,  //текстова константа
        undefined, //помилка - лексема невизначена
        dataDir // что будет значить data directives
    }
}
