/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework;

/**
 * Представляет строку программы
 * @author A. Prodan
 */
public class Line {
    
    private Type type;
    int offset = -1;
    String error;
    Errorable content;
    
    
    public Line(LexTable lext, Reader current) {
        //если таблица лексем пустая - то это пустая строка
        if (lext.size() == 0) {
            type = type.EMPTY;
            return;
        }
        
        //если не пустая строка после директивы END
        if (current.end) {
            error = "file has already ended";
            return;
        }
        
        //если это директива end - конец файла
        if (lext.size() == 1 && lext.get(0).textEquals("end")) {
            type = type.END;
            if (current.openSegment != null) {
                error = "segment " + current.openSegment.getName() + 
                        " is not closed";
            }
            current.end = true; //флаг конца файла
            return;
        }
        
        //если таблица лексем содержит неопределенный идентификатор
        //  - значит в ней ошибка
        if (lext.containsType(Lex.Type.undefined)) {
            type = Type.UNKNOWN;
            error = "undefined symbols in line";
            return;
        }
        
        //если это директива equ
        if (lext.size() >= 3 && lext.get(0).getType() == Lex.Type.userId &&
                lext.get(1).textEquals("equ")) {
            type = Type.EQU;
            try {
                //попытаться вычислить выражение
                Lex result = Calculator.equ(lext.getPart(2));
                //в случае удачи - добавить его в таблицу констант
                current.constants.put(lext.get(0).getText(), result);
            } catch (Exception e) { //в случае неудачи записать ошибку
                error = "cannot calculate expression";
            }
            return;
        }
        
        //если это директива открытия/закрытия сегмента
        if (lext.size() == 2 && lext.get(1).getType() == Lex.Type.dir &&
                lext.get(0).getType() == Lex.Type.userId) {
            //директива открытия сегмента
            if (lext.get(1).textEquals("segment")) {
                if (current.openSegment != null) {
                    error = "cannot create new segment inside segment " +
                            current.openSegment.getName();
                    return;
                }
                //создадим новый сегмент
                Segment ns = new Segment(lext.get(0).getText());
                current.segs.put(ns.getName(), ns); //добавим его в таблицу
                current.openSegment = ns;   //сделаем его текущим
                offset = 0; //смещение текущей строки = 0
                current.resetOffset();  //смещение в новом сегменте = 0
                type = Type.SEGMENT_START;
                return;
            } else
                //директива закрытия сегмента
            if (lext.get(1).textEquals("ends") && 
                    lext.get(0).textEquals(current.openSegment.getName())) {
                type = Type.SEGMENT_END;
                if (current.openSegment == null) {
                    error = "cannot close segment " +
                            lext.get(0).getText();
                    return;
                }
                //закроем сегмент
                current.openSegment.size = current.getOffset();//определим длину
                current.openSegment = null;   //удалим текущий сегмент
                offset = current.getOffset();   //смещение = размер сегмента
                current.resetOffset();
                return;
            }
        }
        
        if (current.openSegment == null) {
            error = "not in segment";
            type = Type.UNKNOWN;
            return;
        }
        
        //если это директива assume
        if (lext.size() > 2 && lext.get(0).textEquals("assume")) {
            type = Type.ASSUME;
            //если директива assume уже была
            if (current.segTable != null) {
                error = "second assume directive";
                return;
            }
            //создаем новую таблицу сегментов
            SegmentTable segt = new SegmentTable(lext.getPart(1) ,current.segs);
            if (segt.valid) { //если прочитать удалось
                current.segTable = segt;
            } else { //если прочитать не удалось
                error = "invalid assume directive";
            }
            return;
        }
        
        //проверим на директиву данных
        if (lext.size() >= 3 && lext.get(0).getType() == Lex.Type.userId &&
                lext.get(1).getType() == Lex.Type.dataDir) {
            this.type = Type.DATA;
            this.offset = current.getOffset();
            
            Lex[] lexArray; //массив лексем, который описывает данные
            //если размер больше трех - то это выражение, считаем его
            if (lext.size() > 3) {
                try {
                //попытаться вычислить выражение
                Lex result = Calculator.equ(lext.getPart(2));
                lexArray = new Lex[3];
                lexArray[0] = lext.get(0);
                lexArray[1] = lext.get(1);
                lexArray[2] = result;
                current.constants.put(lext.get(0).getText(), result); //зачем??
                } catch (Exception e) { //в случае неудачи записать ошибку
                    error = "cannot calculate expression";
                    return;
                }
            }
            else {
            //заменим константу ее числовым значением, если она есть
                lexArray = lext.getArray();
                if (lext.get(2).getType() == Lex.Type.userId) {
                    if (current.constants.containsKey(lext.get(2).getText())) {
                        lexArray[3] = current.constants.get(lext.get(2).getText());
                    }
                    else {
                        error = "constant not found";
                        return;
                    }
                    
                }
            }
            //создадим переменную
            Data dat = new Data(lexArray, current.variables, 
                    current.openSegment, current.getOffset());
            content = dat;
            current.incOffset(dat.getSize());
            return;
        }
        
        //проверим на инструкцию процессора с меткой
        if (    lext.size() >= 2 &&
                lext.get(0).getType() == Lex.Type.userId && //имя метки
                lext.get(1).textEquals(":")) {          // :
            //если метка найдена - обработаем метку
            type = Type.LABEL;
            //проверим, не встречалась ли такая метка ранее
            if (current.labels.containsKey(lext.get(0).getText())) {
                error = "this label name is already in use";
                return;
            }
            //создадим метку
            Label lab = new Label(lext.get(0).getText(), 
                    current.getOffset(), current.openSegment);
            current.labels.put(lab.getName(), lab); //запишем ее в таблицу
            
            //если на строке только метка - то на этом закончим
            if (lext.size() == 2)
                return;
            //если же нет - то дальше, вероятно, команда
            //создадим команду с помощью фабрики команд
            Instruct ins = Instruct.create(lext.getPart(2), current.variables,
                    current.getOffset(), current.labels, current.constants);
            if (ins == null) {  //если непонятно шо, а не команда
                error = "what is it? " + lext.get(0).getText();
                return;
            }
            type = Type.COMMAND;    //да, это таки команда
            offset = current.getOffset();
            content = ins;
            current.incOffset(ins.getSize());
            return;
        }
        
        //если это команда без метки
        if (lext.get(0).getType() == Lex.Type.instruct) {
            type = Type.COMMAND;
            Instruct ins = Instruct.create(lext.getArray(), current.variables,
                    current.getOffset(), current.labels, current.constants);
            if (ins == null) {  //если не команда
                error = "what is it? " + lext.get(0).getText();
                return;
            }
            offset = current.getOffset();
            current.incOffset(ins.getSize());
            content = ins;
            return;
        }
        
        //в принципе, все, больше вариантов нет
        
        type = Type.UNKNOWN; //ашоита?
    }
    
    public String print() {
        String s = "";
        if (offset > -1) {
            s += String.format("%04X ", offset);
        }
        if (error != null) {
            s += " error: " + error;
        }
        if (content != null) {
            s += content.getClass().toString() + " " + content.toString();
        }
        return s;
    }
    
    public enum Type {
        DATA, //val db 0a23h
        SEGMENT_START, //data segment
        SEGMENT_END, //data ends
        ASSUME, //assume cs : code,...
        COMMAND, //mov, div, xor...
        END, //end
        EQU,    //equ
        EMPTY, //
        LABEL, //label:
        UNKNOWN //definetly an error
    }
}
