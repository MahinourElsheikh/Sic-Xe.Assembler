/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicxe;

public class Literal {
    private final String name;
    private final String value;
    private int address;
    
    public Literal(String name, String value, int address) {
        this.name = name;
        this.value = value;
        this.address = address;
    }
    
    
    public static Literal parse(String literal) {
        String name = literal;
        String value = name.substring(1);
        int address = 0;
        
        return new Literal(name, value, address);
    }
    public String name() {
        return name;
    }
    
    public void updateAddress(int locctr) {
        address = locctr;
    }
    
    public int address() {
        return address;
    }
    public int length() {
        switch (value.charAt(0)) {
            case 'C':
                return (value.length() - 3); // dah character f kol character b byte
            case 'X':
                return (value.length() - 3) / 2; // dah hexa f kol 2 hexa b 1 byte
            default:
                return 0;
        }
    }
    
    public LineDivident toStatement() {
        return new LineDivident("*", name, false, true, null);
    }
    
    
    
}
