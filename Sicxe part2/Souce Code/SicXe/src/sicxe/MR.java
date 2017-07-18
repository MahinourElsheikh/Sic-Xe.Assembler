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
public class MR {
      private final int Location;
    private final int Length;
     private final String name;
    
    public MR(int modifiedLoc, int modifiedLen,String n) {
       Location =modifiedLoc;
       Length =modifiedLen;
       name=n;
               
    }
    
    public String toObjectProgram() {
        return String.format("M^%02X^%06X+%s", Length,Location,name);
       
    }
    
}
