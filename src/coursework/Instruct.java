package coursework;

import java.util.Map;

/**
 * Представляет инструкцию процессора
 * 
 */
public abstract class Instruct implements Errorable {
    
    String error;

    /**
     * Считает длину команды на первом проходе
     * @return длина команды
     */
    public abstract int getSize(); 
    
    @Override
    public String getError() {
        return error;
    }
    
    /**
     * Фабрика инструкций. Определяет тип инструкции
     * и создает соответствующий объект
     * @param lx это таблица лексем команды
     * @param vars таблица переменных
     * @param offset смещение в сегменте
     * @param labels таблица меток
     * @param constants таблица констант
     * @return инструкция
     */
    public static Instruct create(Lex[] lx, Map<String, Data> vars, 
            int offset, Map<String, Label> labels, Map<String, Lex> constants) {
        if (lx[0].getType() != Lex.Type.instruct) {
            return null;    //а это не команда - вернем ничего
        }
            
        if (lx[0].textEquals("cli")) {
            return new Cli();
        }
        if (lx[0].textEquals("inc")) {
            return new Inc(lx);
        }
        if (lx[0].textEquals("div")) {
            return new Div(lx);
        }
        if (lx[0].textEquals("adc")) {
            return new Adc(lx, vars);
        }
        if (lx[0].textEquals("dec")) {
            return new Dec(lx, vars);
        }
        if (lx[0].textEquals("cmp")) {
            return new Cmp(lx);
        }
        if (lx[0].textEquals("and")) {
            return new And(lx, vars);
        }
        if (lx[0].textEquals("mov")) {
            return new Mov(lx, constants);
        }
        if (lx[0].textEquals("xor")) {
            return new Xor(lx, constants, vars);
        }
        if (lx[0].textEquals("jb")) {
            return new Jb(lx, labels, offset);
        }
        return null;
    }
    
    /**
     * Определяет код 8-разрядного регистра
     * @param reg8 название регистра
     * @return код регистра
     */
    public static int getReg8(String reg8) {
        switch (reg8) {
            case "al":
                return 0b000;
            case "ah":
                return 0b100;
            case "cl":
                return 0b001;
            case "ch":
                return 0b101;
            case "dl":
                return 0b010;
            case "dh":
                return 0b110;
            case "bl":
                return 0b011;
            case "bh":
                return 0b111;
        }
        return 4; //this should never happen
    }
    
    /**
     * Определяет код 32-разрядного регистра
     * @param reg32 название регистра
     * @return код регистра
     */
    public static int getReg32(String reg32) {
        switch (reg32) {
            case "eax":
                return 0b000;
            case "esp":
                return 0b100;
            case "ecx":
                return 0b001;
            case "ebp":
                return 0b101;
            case "edx":
                return 0b010;
            case "esi":
                return 0b110;
            case "ebx":
                return 0b011;
            case "edi":
                return 0b111;
        }
        return 4; //this should never happen. Never ever.
        }
    
    /**
     * Возвращает байт modR/M для команд без адресации (только с регистром)
     * @param reg   название регистра
     * @param reg32 32-разрядный (true) или 8-разрядный (false) регистр
     * @param opcodeExtension расширение кода операции в поле reg
     * @return строку - байт modR/M в 16ричном виде с ПРОБЕЛОМ в конце
     */
    public String getModRMreg(String reg, boolean reg32, int opcodeExtension) {
        int modrm = 0b11000000; //11 000 000
        modrm += reg32 ? getReg32(reg) : getReg8(reg);  //код регистра в поле rm
        if (opcodeExtension != 0)   //если есть расширение кода операции
            modrm += opcodeExtension << 3; //запишем его в поле reg
        return String.format("%02X ", modrm);
    }
    
    /**
     * Генерирует префикс замены сегмента в том случае, если он нужен
     * возвращает пустую строку, если он не нужен
     * @param actSeg целевой сегментный регистр
     * @param defSeg сегментный регистр по умолчанию
     * @return префикс замены сегмента в 16ричном виде с двоеточием и пробелом
     */
    public String segmentOverride(String actSeg, String defSeg) {
        if (actSeg.equals(defSeg)) {
            return ""; //если замена сегментов не нужна
        }
        int prefix = 0; //префикс замены сегмента
        switch (actSeg) {
            case "es":
                prefix = 0x26;
                break;
            case "cs":
                prefix = 0x2E;
                break;
            case "ss":
                prefix = 0x36;
                break;
            case "ds":
                prefix = 0x3E;
                break;
            case "fs":
                prefix = 0x64;
                break;
            case "gs":
                prefix = 0x65;
                break;
        }
        
        return String.format("%02X: ", prefix);
    }
    
    public String getSib(String base, String index) {
        int sib = 0; //00 000 000
        sib += getReg32(index) << 3;
        sib += getReg32(base);
        return String.format("%02X ", sib);
    }
}

