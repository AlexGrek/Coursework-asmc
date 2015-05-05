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
    private final int value;
    private final boolean valueType;
    
    /**
     * проверяет текст лексемы на совпадение со строкой
     * @param s строка
     * @return true если совпадает
     */
    public boolean textEquals(String s) {
        return s.equals(text);
    }
    
    public Lex(String str, Type tp) {
        text = str;
        type = tp;
        value = 0;
        valueType = false;
    }
    
    public Lex(String str, Type tp, int val) {
        text = str;
        type = tp;
        value = val;
        valueType = true;
    }
    
    /**
     * проверяет наличие числового значения у лексемы
     * @return константа ли
     */
    public boolean isValueType() {
        return this.valueType;
    }

    /**
     * @return текст лексемы
     */
    public String getText() {
        return text;
    }
    
    /**
     * @return длину текста
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
    
    /**
     * число
     * @return значение константы
     */
    public int getValue() {
        return value;
    }
    
    public enum Type {
        userId, //ідентифікатор користувача
        single, //односимвольна
        instruct, //інструкція процесора
        seg, //сегментний регістр
        reg8, //8-розрядний регістр ЗП
        reg32, //32-розрядний регістр ЗП
        constDec, constHex, constBin, //константи
        text,  //текстова константа
        undefined, //помилка - лексема невизначена
        dataDir, // что будет значить data directives
        dir // директива
    }
}
