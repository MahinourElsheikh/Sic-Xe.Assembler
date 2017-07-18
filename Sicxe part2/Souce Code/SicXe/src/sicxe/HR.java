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
public class HR {
     private final String ProgramName;
    private final int StartAddress;
    private final int ProgramLength;
    
    public HR(String name, int startAddr, int length) {
        ProgramName = name;
        StartAddress = startAddr;
        ProgramLength = length;
          //_startAddress=Integer.toHexString(startAddr);
          ////_programLength=Integer.toHexString();
    }
    
    public String toObjectProgram() {
        return String.format("H%1$-5s^%2$06X^%3$06X", ProgramName, StartAddress, ProgramLength);
    }
}
