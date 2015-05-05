
package coursework;



public class Div extends Instruct {
    
    private String reg;
    private boolean reg32; //32-разрядный регистр = true, 
                               //8-разрядный = false
    
    public Div(Lex[] lx) {
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
            return 2; //длина всегда 2
        }
        
        @Override
        public String toString() {
            if (error != null) {
                return error;
            }
            
            int opcode = 0xF6; //для 8-разрядных регистров
            if (reg32)
                opcode++;      //для 32-разрядных регистров
            return String.format("%02X ", opcode) + 
                    getModRMreg(reg, reg32, 0b110);
        }
}