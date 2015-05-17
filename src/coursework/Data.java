
package coursework;

import java.util.Map;

/**
 * Представляет директиву данных и переменную
 * @author A. Prodan
 */
public class Data implements Errorable  {
    int[] data;
    int offset;
    String error;
    String name;
    private Type type; //тип данных
    
    public Data(Lex[] lx, Map<String, Data> varTable, Segment seg, int o) {
        
        //проверим, нет ли переменной с таким же именем
        if (varTable.containsKey(lx[0].getText())) {
            error = "a variable with same name already exists";
            return;
        }
        
        //проверим, в каком сегменте указана переменная
        if (seg.getType() != Segment.Type.DATA) {
            error = "not in data segment";
            return;
        }
        
        //добавим переменную в таблицу переменных
        varTable.put(lx[0].getText(), this);
        
        //сохраним имя переменной
        name = lx[0].getText();
        
        //сохраним смещение в сегменте
        offset = o;
        
        
        //определим тип данных
        switch (lx[1].getText()) {
            case "db":
                type = Type.DB;
                break;
            case "dw":
                type = Type.DW;
                break;
            default: // "dd"
                type = Type.DD;
        }
        
        //если это просто константа
        if (lx[2].isValueType()) {
            //проверим размер
            if (!inRange(lx[2].getValue(), type))
                error = "value is not in range";
            else {
                data = new int[1];
                data[0] = lx[2].getValue();
            }
        } else
        
        //если это строковая константа
        if (lx[2].getType() == Lex.Type.text) {
            String s = lx[2].getText(); //текст константы
            data = new int[s.length()]; //массив значений
            //добавим в массив все символы строки
            for(int i = 0; i < s.length(); i++) {
                data[i] = (int)s.charAt(i);
            }
        } else 
            //если это непонятно шо
            error = "invalid data directive";
    }
    
    public Type getType() {
        return type;
    }
    
    /**
     * проверяет, входит ли число в диапазон допустимых значений для типа t
     * @param i число
     * @param t тип
     * @return true если входит, false если нет
     */
    public static boolean inRange(long i, Type t) {
        int max, min; //верхняя и нижняя граница
        switch (t) {
            case DB:
                min = -128;
                max = 127;
                break;
            case DW:
                max = 32767;
                min = -32768;
                break;
            case DD:
                max = Integer.MAX_VALUE;
                min = Integer.MIN_VALUE;
                break;
            default:
                //this will never happen
                max = min = 0;
        }
        
        return i <= max && i >= min;
    }
    
    @Override
    public String toString() {
        if (error != null)
            return error;
        String s = "";
        for(int b : data) {
            switch (type) {
                case DB:
                    s += String.format("%02X ", b);
                    break;
                case DW:
                    s += String.format("%04X ", b);
                    break;
                case DD:
                    s += String.format("%08X ", b);
            }
        }
        return s;
    }

    public int getSize() {
        if (error != null)
            return 0;
        
        switch (type) {
                case DB:
                    return data.length;
                case DW:
                    return 2;
                case DD:
                    return 4;
            }
        return 0;
    }

    @Override
    public String getError() {
        return error;
    }
    
    public enum Type {
        DB,
        DW,
        DD
    }
}
