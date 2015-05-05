
package coursework;

import java.util.Map;


public class Adc extends Instruct {
    
    private String reg;
    private boolean reg32; //32-разрядный регистр = true, 
                               //8-разрядный = false
    Data mem;
    String reg1, reg2;
    String actualSeg, defaultSeg;
    Data.Type dataType;
    
    public Adc(Lex[] lx, Map<String, Data> vars) {
        //проверим количество аргментов
            if (lx.length < 9) { //adc reg , var [ reg + reg ] 
                error = "incorrect arguments";
                return;
            }
            
            //проверим тип аргумента reg
            switch (lx[1].getType()) {
                case reg8:
                    reg32 = false; //8-разрядный
                    reg = lx[1].getText();
                    break;
                case reg32:
                    reg32 = true; //32-разрядный
                    reg = lx[1].getText();
                    break;
                default:
                    error = "invalid argument";
                    return;
            }
            
            //проверим оператор "запятая"
            if (!lx[2].textEquals(",")) {
                error = "invalid separator between arguments";
                return;
            }
            
        int i = 3; //индекс начала аргумента в таблице лексем
        
        
        //отследим возможную замену сегмента
        if (lx[i].getType() == Lex.Type.seg &&
                lx[i+1].textEquals(":")) {
            actualSeg = lx[i].getText();
            i += 2;
        }
        try {
        //проверим структуру команды
            if (lx[i].getType() == Lex.Type.userId && // var
                lx[i+1].textEquals("[") &&            // [
                lx[i+2].getType() == Lex.Type.reg32 &&// reg32
                lx[i+3].textEquals("+") &&            // +
                lx[i+4].getType() == Lex.Type.reg32 &&// reg32
                lx[i+5].textEquals("]")               // ]
                ) {
                //команда правильная, можно работать
                
                //поищем переменную в таблице переменных
                if (!vars.containsKey(lx[i].getText())) {
                    error = "variable not found";
                    return;
                }
                //если нашли
                mem = vars.get(lx[i].getText());
            } else {
                //структура команды неправильная
                error = "invalid command structure";
                return;
            }
        } catch (Exception e) {
            error = "something wrong with arguments";
            return;
        }
        
        //установим целевой сегмент, если не указан явно
        if (actualSeg == null) {
            actualSeg = mem.segment.usedIn;
        }
        
        //сверим размер данных и регистра
        dataType = mem.getType();
        if ((reg32 && dataType != Data.Type.DD) || 
                (!reg32 && dataType != Data.Type.DB)) {
            error = "different data types";
            return;
        }
        
        //определим регистры адресации
        reg1 = lx[i+2].getText();
        reg2 = lx[i+4].getText();
        
        //проверим регистры адресации
        if (reg1.equals("ebp") && reg2.equals("ebp")) {
            error = "ebp+ebp is not allowed";
            return;
        }
        
        //установим сегмент данных по умолчанию
        if (reg1.equals("ebp") || reg1.equals("esp") || reg2.equals("esp")) 
            defaultSeg = "ss";
        else defaultSeg = "ds";
    }

    @Override
        public int getSize() {
            if (error != null) 
                return 0;

            int len = 7; //длина команды = 7
            //если сегмент по умолчанию не совпадает с целевым
            if (!defaultSeg.equals(actualSeg))
                len++; //префикс замены сегмента: +1 байт
            return len;
        }
        
        @Override
        public String toString() {
            if (error != null) {
                return error;
            }
            
            return "yellow unicorn";
        }
}
