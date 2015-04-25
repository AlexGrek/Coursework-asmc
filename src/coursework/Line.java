/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework;

/**
 *
 * @author A. Prodan
 */
public class Line {
    
    private Type type;
    
    
    public enum Type {
        DATA, //val db 0a23h
        SEGMENT_START, //data ends
        SEGMENT_END, //data segment
        ASSUME, //assume cs : code,...
        COMMAND, //mov, div, xor...
        END, //end
        EMPTY, //
        UNKNOWN //definetly an error
    }
}
