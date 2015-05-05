
package coursework;

import static coursework.Instruct.getReg8;
import java.util.Map;



public class Mov extends Instruct {
        private String reg;
        private boolean reg32; //32-разрядный регистр = true, 
                               //8-разрядный = false
        private int imm;
       
        public Mov(Lex[] lx, Map<String, Lex> constants) {
            //проверим количество аргментов
            if (lx.length < 4) { //mov reg , imm
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
                    error = "invalid argument 1";
            }
            
            //исследуем объект "З.А.П.Я.Т.А.Я."
            if (!lx[2].textEquals(",")) {
                error = "invalid separator between arguments, dude";
                return;
            }
            
            //приступим к разбору операнда IMM
            //4 операнда - значит выражения нет
            if (lx.length == 4) {
                if (lx[3].getType() == Lex.Type.userId) {
                    if (constants.containsKey(lx[3].getText())) {
                        imm = constants.get(lx[3].getText()).getValue();
                    }
                    else {
                        error = "constant not found";
                        return;
                    }
                } else if (lx[3].isValueType()) {
                    imm = lx[3].getValue();
                }
            } else {
                //выражение есть
                //выделим его в отдельный массив
                int start = 3;
                Lex[] im = new Lex[lx.length - start];
                for (int i = 3; i < lx.length; i++) {
                    im[i - start] = lx[i];
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
            
            if (!reg32 && !Data.inRange(imm, Data.Type.DB)) {
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
            //2 байта для 8-разрядных регистров,
            //5 байт для 32-разрядных
            return reg32? 5 : 2;
        }
        
        @Override
        public String toString() {
            if (error != null) {
                return error;
            }
            
            return String.valueOf(imm);
        }
}
