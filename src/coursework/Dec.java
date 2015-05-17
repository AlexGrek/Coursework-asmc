
package coursework;

import java.util.Map;

/**
 * Представляет инструкцию DEC mem
 * 
 */
public class Dec extends Instruct {
    
    Data mem;
    String reg1, reg2;
    String actualSeg, defaultSeg;
    Data.Type dataType;
    
    public Dec(Lex[] lx, Map<String, Data> vars) {
        int i = 1; //индекс начала аргумента в таблице лексем
        
        //проверим длину (минимум 7: dec var [ reg32 + reg32 ])
        if (lx.length < 7) {
            error = "something is wrong with this instruction";
            return;
        }
        
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
            actualSeg = "ds";
        }
        dataType = mem.getType();
        
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
            
            //префикс замены сегмента, если он нужен
            String out = segmentOverride(actualSeg, defaultSeg);
            
            //код операции
            int opcode = 0xFE; //для 8-разрядных данных
            if (dataType == Data.Type.DD)
                opcode++; //FF для 32-разрядных
            
            int modrm = 0x8C; //10 001 100
            
            String sib = getSib(reg1, reg2);
            
            //это смещение переменной
            String memOffset = String.format("%08X", mem.offset);
            
            out += String.format("%02X %02X%s%s", opcode, modrm, sib, memOffset);
            return out;
        }
}
