/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework;

import java.util.Stack;

/**
 * Реализация алгоритма вычисления выражений
 * 
 */
public class Calculator {
    
    /**
     *
     * @param lexems - таблица лексем, которые будут заменены на константу
     * @return константа - результат вычисления выражения
     * @throws Exception - если что-то пошло не так (или ошибка в выражении)
     */
        public static Lex equ(Lex[] lexems) throws Exception {
        Stack<Lex> data = new Stack<Lex>();
        Stack<Lex> op   = new Stack<Lex>();
        
        //добавим скобки к исходному выражению,
        //чтобы упростить последний шаг
        Lex[] lx = new Lex[lexems.length + 2];
        lx[0] = new Lex("(", Lex.Type.single);
        int j = 1;
        for (Lex l : lexems) {
            lx[j++] = l;
        }
        lx[lx.length - 1] = new Lex(")", Lex.Type.single);
        
        //начнем разбор по лексемам
         for (int i = 0; i < lx.length; i++ ) {
            Lex l = lx[i];
            
            //если встретилась константа
            if (l.isValueType()) {
                data.push(l);
            } else
                
            //если встретилась операция
            if (l.getType() == Lex.Type.single) {
                if ("(".equals(l.getText())) { // (
                    op.push(l);
                } else
                
                if (")".equals(l.getText())) { // )
                    while (!op.peek().getText().equals("(")) {
                        data.push(operate(op, data));
                    }
                    op.pop(); // "("
                } else
                
                    //другая операция (+ - * /)
                if (getPriority(op.peek()) <= getPriority(l) && 
                        !op.peek().getText().equals("(")) {
                    while(getPriority(op.peek()) <= getPriority(l) && 
                        !op.peek().getText().equals("(")) {
                        data.push(operate(op, data));
                    }
                    op.push(l);
                } else {
                    //обработаем унарный минус, сделав его бинарным
                    if (l.getText().equals("-") && lx[i-1].getText().equals("("))
                        data.push(new Lex("0", Lex.Type.constDec, 0));
                    op.push(l); //
                } 
            } else throw new Exception("WAT?"); //если какая-то дичь в выражении
        }
        
        //результат - элемент, который останется
        return data.pop();
    } 
    
    //выполнить операцию
    private static Lex operate(Stack<Lex> ops, Stack<Lex> data) throws Exception {
        Lex op = ops.pop();
        int res;
        switch (op.getText()) {
            case "+":
                res = data.pop().getValue() + data.pop().getValue();
                return new Lex("" + res, Lex.Type.constDec, res);
            case "-":
                res = -( data.pop().getValue() - data.pop().getValue());
                return new Lex("" + res, Lex.Type.constDec, res);
            case "*":
                res = data.pop().getValue() * data.pop().getValue();
                return new Lex("" + res, Lex.Type.constDec, res);
            case "/":
                int b = data.pop().getValue();
                int a = data.pop().getValue();
                res = a / b;
                return new Lex("" + res, Lex.Type.constDec, res);
            default:
                throw new Exception("ничоси! " + op.getText() + "!");
        }
    }
    
    //определить приоритет операции
    public static int getPriority(Lex l) throws Exception {
        switch (l.getText()) {
            case "(":
                return -1;
            case "+": case "-":
                return 2;
            case "/": case "*":
                return 1;
            default:
                throw new Exception("ничоси");
        }
    }
}
