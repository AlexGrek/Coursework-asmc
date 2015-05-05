
package coursework;

/**
 *
 * Представляет команду CMP reg, reg
 */
public class Cmp extends Instruct {
        private String reg1, reg2;
        private boolean reg32; //32-разрядный регистр = true, 
                               //8-разрядный = false
       
        public Cmp(Lex[] lx) {
            //проверим количество аргментов
            if (lx.length != 4) { //cmp reg , reg
                error = "incorrect arguments";
                return;
            }
            
            //проверим тип аргумента reg1
            switch (lx[1].getType()) {
                case reg8:
                    reg32 = false; //8-разрядный
                    reg1 = lx[1].getText();
                    break;
                case reg32:
                    reg32 = true; //32-разрядный
                    reg1 = lx[1].getText();
                    break;
                default:
                    error = "invalid argument 1";
            }
            
            //проверим разделитель ","
            if (!lx[2].textEquals(",")) {
                error = "invalid separator between arguments";
                return;
            }
            
            boolean reg2is32 = false;
            //проверим тип аргумента reg2
            switch (lx[3].getType()) {
                case reg8:
                    reg2is32 = false; //8-разрядный
                    reg2 = lx[3].getText();
                    break;
                case reg32:
                    reg2is32 = true; //32-разрядный
                    reg2 = lx[3].getText();
                    break;
                default:
                    error = "invalid argument 2";
            }
            
            //сверим типы регистров
            if (reg32 != reg2is32) {
                error = "different register types";
                return;
            }
        }

        @Override
        public int getSize() {
            //если ошибка - длина будет 0
            if (error != null) {
                return 0;
            }
            //2 байта для любых регистров
            return 2;
        }
        
        @Override
        public String toString() {
            if (error != null) {
                return error;
            }
            
            int opcode = 0x3A; //для 8-разрядных регистров
            int reg1code;      //код первого регистра будет в поле reg
            if (reg32){
                opcode++;      //для 32-разрядных регистров
                reg1code = getReg32(reg1);
            } else 
                reg1code = getReg8(reg1);
            
            
            return String.format("%02X ", opcode) + 
                getModRMreg(reg2, reg32, reg1code);
        }
}
