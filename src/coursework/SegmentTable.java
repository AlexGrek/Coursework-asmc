/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework;

import java.util.Map;

/**
 *  Представляет таблицу сегментных регистров
 * 
 */
public class SegmentTable {
    public Segment cs, ds, ss, es, gs, fs;
    public boolean valid;
    
    public SegmentTable(Lex[] assume, Map<String, Segment> segs) {
        int i = 0;  //итератор
        valid = true;   //флаг корректности
        try {
            while (i < assume.length) {
                if (assume[i].getType() == Lex.Type.seg && 
                        assume[i+1].textEquals(":") &&
                        assume[i+2].getType() == Lex.Type.userId) {
                    switch (assume[i].getText()) {
                        case "cs":
                            cs = segs.get(assume[i+2].getText());
                            cs.usedIn = "cs"; //сегмент должен знать, 
                                              //где он использован
                            break;
                        case "ds":
                            ds = segs.get(assume[i+2].getText());
                            ds.usedIn = "ds";
                            break;
                        case "ss":
                            ss = segs.get(assume[i+2].getText());
                            ss.usedIn = "ss";
                            break;
                        case "gs":
                            gs = segs.get(assume[i+2].getText());
                            gs.usedIn = "gs";
                            break;
                        case "es":
                            es = segs.get(assume[i+2].getText());
                            es.usedIn = "es";
                            break;
                        case "fs":
                            fs = segs.get(assume[i+2].getText());
                            fs.usedIn = "fs";
                            break;
                    }
                } else
                    valid = false;
                i+=4;
            }
        }catch(Exception e) {
            //возможны ошибки выхода за границу массива
            //и отсутствия сегмента в списке сегментов
            //они возможны только при некорректном вводе данных
            valid = false;
        }
    }
}
