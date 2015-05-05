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
public class Segment {
    
    private Type type;
    private final String name;
    public int size;
    public String usedIn; //segment register
    
    /**
     *
     * @return Имя сегмента
     */
    public String getName() {
        return name;
    }
    
    /**
     *проверить тип сегмента
     * @param t
     * @return соответствует ли тип сегмента
     */
        public boolean checkType(Type t) {
        //если тип сегмента еще не определен - задать его
        if (type == Type.UNKNOWN)
            type = t;
        //сверить тип
        return type == t;
    } 
    
    public Segment(String n) {
        name = n;
        //при создании сегмента его тип неизвестен
        type = Type.UNKNOWN;
    }
    
    /**
     *
     * @return Тип сегмента
     */
    public Type getType() {
        return type;
    }
    
    /**
     * Тип сегмента
     */
    public enum Type {
        DATA,
        CODE,
        UNKNOWN
    }
}
