
package coursework;


 /** 
     *  Инструкция INC reg
     */
    public class Inc extends Instruct {
        private String reg;
        private boolean reg32; //32-разрядный регистр = true, 
                               //8-разрядный = false
       
        public Inc(Lex[] lx) {
            //проверим количество аргментов
            if (lx.length != 2) { //inc reg
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
            }
        }

        @Override
        public int getSize() {
            //если ошибка - длина будет 0
            if (error != null) {
                return 0;
            }
            //2 байта для 8-разрядных регистров,
            //1 байт для 32-разрядных
            return reg32? 1 : 2;
        }
        
        @Override
        public String toString() {
            if (error != null) {
                return error;
            }
            
            if (!reg32) { //8-битный регистр
                String result = "FE "; //код операции
                result += getModRMreg(reg, reg32, 0); //байт modR/M
                return result;
            } else {
                int opcode = 0x40 + getReg32(reg); //код регистра в опкоде
                return String.format("%02X ", opcode);
            }
            
        }
    }        
