/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicxe;

/**
 *
 * @author mahin
 */
public class Operation {
    private String name;
    private String opcode;
    private String format;
    
    public Operation(String name, String format, String opcode) {
        this.name = name;
        this.opcode = opcode;
        this.format = format;
    }
    
    public String mnemonic() {
        return name;
    }
    
    public String opcode() {
        return opcode;
    }
    
    public String format() {
        return format;
    }
}
