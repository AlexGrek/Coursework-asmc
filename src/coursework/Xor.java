
package coursework;

import java.util.Map;



public class Xor extends Instruct{
        private boolean reg32; //32-разрядный регистр = true, 
                               //8-разрядный = false
        private int imm;
        
        Data mem;
        String reg1, reg2;
        String actualSeg, defaultSeg;
        Data.Type dataType;
       
        public Xor(Lex[] lx, Map<String, Lex> constants, Map<String, Data> vars) {
            //проверим количество аргментов
            if (lx.length < 9) { //xor var [ reg32 + reg ] , imm
                error = "incorrect arguments";
                return;
            }
            
            int i = 1; //индекс начала первого аргумента в таблице лексем

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
            i += 7;
            //проверим разделитель "," между аргументами
            if (!lx[i-1].textEquals(",")) {
                error = "invalid separator between arguments";
                return;
            }
            
            //установим целевой сегмент, если не указан явно
        if (actualSeg == null) {
            actualSeg = mem.segment.usedIn;
        }
        
        //определим регистры адресации
        reg1 = lx[i+2-7].getText();
        reg2 = lx[i+4-7].getText();
        
        //проверим регистры адресации
        if (reg1.equals("ebp") && reg2.equals("ebp")) {
            error = "ebp+ebp is not allowed";
            return;
        }
        
        //установим сегмент данных по умолчанию
        if (reg1.equals("ebp") || reg1.equals("esp") || reg2.equals("esp")) 
            defaultSeg = "ss";
        else defaultSeg = "ds";
            
            //приступим к разбору операнда IMM
            //второй операнд 1 лексема - значит выражения нет
            if (lx.length == i+1) {
                if (lx[i].getType() == Lex.Type.userId) {
                    if (constants.containsKey(lx[i].getText())) {
                        imm = constants.get(lx[i].getText()).getValue();
                    }
                    else {
                        error = "constant not found";
                        return;
                    }
                } else if (lx[i].isValueType()) {
                    imm = lx[i].getValue();
                }
            } else {
                //выражение есть
                //выделим его в отдельный массив
                int start = i;
                Lex[] im = new Lex[lx.length - start];
                for (int j = i; j < lx.length; j++) {
                    im[j - start] = lx[j];
                }
                //и попытаемся посчитать
                try {
                    Lex result = Calculator.equ(im);
                    imm = result.getValue();
                } catch (Exception e) {
                    error = "cannot calculate that";
                    return;
                }
            }
            
            //проверим, влезло ли это значение
            dataType = mem.getType();
            if (!Data.inRange(imm, dataType)) {
                error = "value is not in range";
                return;
            }
        }

        @Override
        public int getSize() {
            //если ошибка - длина будет 0
            if (error != null) {
                return 0;
            }
            int len = 8; //длина команды = 8 - джекпот! (1 байт опкод,
                                                        //2 байта модР/М + СИБ
                                                        //4 байта адрес в памяти
                                                        //1 байт данных
            //если сегмент по умолчанию не совпадает с целевым
            if (!defaultSeg.equals(actualSeg))
                len++; //префикс замены сегмента: +1 байт
            if (!Data.inRange(imm, Data.Type.DB))
                len += 3; //данные будут занимать 4 байта
            return len;
        }
        
        @Override
        public String toString() {
            if (error != null) {
                return error;
            }
            
            return String.valueOf(imm);
        }
}
