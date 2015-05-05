package coursework;

import java.util.Map;

/**
 * Инструкция условного перехода JB
 * @author A. Prodan
 */
public class Jb extends Instruct {
    
    private Label to;
    private int offset;
    private boolean shortJump; // однобайтное смещение
    Map<String, Label> labels;
    private String labelName;
    
    public Jb(Lex[] lx, Map<String, Label> labs, int offs) {
        if (lx.length != 2 && lx[1].getType() == Lex.Type.userId) {
            error = "incorrect arguments";
            return;
        }
        
        labels = labs;
        
        offset = offs;
        shortJump = false;
        
        //если метка найдена
        if (labels.containsKey(lx[1].getText())) {
            to = labels.get(lx[1].getText()); //запомним её
            if (Data.inRange(to.getOffset() - (offset + 2), Data.Type.DB)) {
                shortJump = true; //если расстояние до метки влазит в байт
            }
        } else 
            labelName = lx[1].getText();
    }

    @Override
    public int getSize() {
        if (shortJump) {
            //1 байт - код операции, 1 байт - относительное смещение
            return 2;
        } else {
            //2 байта - код операции, 4 байта - смещение
            return 6;
        }
    }
    
    @Override
    public String toString() {
        if (error != null)
            return error;
        
        //определим метку, если еще не определена
        if (to == null) {
            if (!labels.containsKey(labelName)) {
                error = "label not found: " + labelName;
                return error;
            }
            to = labels.get(labelName);
        }
        
        if (shortJump) {
            int opcode = 0x72;
            int diff = to.getOffset() - (offset + 2);
            return String.format("%02X %02X ", opcode, (byte)diff);
        }
        
        String result = "0F 82 ";
        result += String.format("%08X ", to.getOffset());
        return result;
    }
}
