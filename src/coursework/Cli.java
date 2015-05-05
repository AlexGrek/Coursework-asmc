
package coursework;



 /** 
     *  Инструкция CLI, без операндов
     */
    public class Cli extends Instruct {
        //Cli всегда cli
        public Cli() {
        }

        @Override
        public int getSize() {
            //длина всегда 1
            return 1;
        }
        
        @Override
        public String toString() {
            //код операции тоже не меняется
            return "FA ";
        }
    }        
