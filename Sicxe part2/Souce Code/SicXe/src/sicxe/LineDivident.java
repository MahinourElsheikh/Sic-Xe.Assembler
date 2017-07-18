/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicxe;

public class LineDivident implements  Comparable {
    private final String label;
    private final String operation;
    private final String[] symbols;
    private final String comment;
    private final boolean format4;
    private final boolean literal;
    private int location;
    
    private LineDivident(String label, String operation, boolean extended, boolean literal, String[] symbols, String comment) {
        this.label = label;
        this.operation = operation;
        this.format4 = extended;
        this.literal = literal;
        this.symbols = symbols;
        this.comment = comment;
    }
    
    public LineDivident (String label, String operation, boolean extended, boolean literal, String[] symbols) {
        this(label, operation, extended, literal, symbols, null);
    }
    
    public LineDivident(String comment) {
        this(null, ".", false, false, null, comment);
    }
    
    public String label() {
        return label;
    }
    
    public String operation() {
        return operation;
    }
    
    public String operand1() {
        if (literal && symbols[0].compareTo("=*") == 0) {
            return "#" +location;
        } else {
            return symbols[0];
        }
    }
    
    public String operand2() {
        return symbols[1];
    }
    
    public boolean isComment() {
        return operation.compareTo(".") == 0;
    }
    
    public boolean isFormat4() {
        return format4;
    }
    
    public boolean hasLiteral() {
        return literal;
    }
    
    public void setLocation(int loc) {
        location = loc;
    }
    
    public int location() {
        return location;
    }
    
    public static LineDivident parse(String Line) {
        String[] pieces = Line.trim().split("\t"); // ana hna 2asmt el line bl tabs l array of strings(not the best approach)
        
        if (pieces[0].compareTo(".") == 0) {
            return new LineDivident(Line.substring(Line.indexOf('.') + 1));//dah comment
        } else {
            String label, operation;
            String[] symbols;
            boolean extended = false;
            boolean literal = false;
            int index = 0;

            if (pieces.length == 3) { // lw kan 3ndy 3 strings m3naha 3ndy label w opcode w operand lw akal yb2a mafeesh label
                label = pieces[index++];
            } else if(pieces.length ==2 &&( "CSECT".equals(pieces[1]) || "RSUB".equals(pieces[1]))) {
                label = pieces[index++];
            }
            else{
                label = null;
            }

            operation = pieces[index++]; // 5od el operation w zawed el index
            if (operation.charAt(0) == '+') {
                extended = true;       //flag en dah format 4
                operation = operation.substring(1); // shelt el + 3ashan adwr 3aliha 3ady fl optable
            }

            symbols = new String[2];   
            if (index < pieces.length) {
                int pos = pieces[index].indexOf(','); //bashouf fein el , 3ashan 23raf hwa operand wa7ed wala 2
                if (pos >= 0) {
                    symbols[0] = pieces[index].substring(0, pos);//operand 1 elly able el ,
                    symbols[1] = pieces[index].substring(pos + 1);//operand 2 elly b3d el ,
                } else {
                    symbols[0] = pieces[index];// kda yb2a fi operand wa7ed bas
                    symbols[1] = null;
                }
                
                if (symbols[0].charAt(0) == '=') {  //lw = yb2a dah literal
                    literal = true;
                }
            } else {
                symbols[0] = symbols[1] = null; // ya format 1 ya ema rsub mafehash operand asslan
            }

            return new LineDivident(label, operation, extended, literal, symbols); //barag3 el line mt2sm
        }
      

      
    }
    
    @Override
    public String toString() { // hna ana bazabt el statement atb3ha yb2a shklaha 3aml 2zay
        
        //String s = String.format("%1$04X", _location) + "\t"; knt ba7awl l hexa bas ana m3rftsh 3ashan el start hexa bardo
        String s = location + "\t";
        //System.out.println(s+"dah");
        if (isComment()) {
            s += ".\t" + comment;
        } else {
            if (label != null) {
                s += label;
            }

            s += '\t';
            
            if (format4) {
                s += '+';
            }
            
            s += operation + '\t';

            if (symbols != null) {
                if (symbols[0] != null) {
                    s += symbols[0];
                }

                if (symbols[1] != null) {
                    s +=  ',' + symbols[1];
                }
            }
        }
        
        return s;
    }

    @Override
    public int compareTo(Object obj) {
        return operation.compareTo((String) obj);
    }
}
