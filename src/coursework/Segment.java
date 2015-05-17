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
    
    /**
     *
     * @return Имя сегмента
     */
    public String getName() {
        return name;
    }
    
    
    public Segment(String n, Type t) {
        name = n;
        //при создании сегмента его тип известен
        type = t;
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
