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
public class ER {
    private final int StartAddress;
    
    public ER(int startAddr) {
        StartAddress = startAddr;
    }
    
    public String toObjectProgram() {
        return String.format("E^%1$06X", StartAddress);
    }
    
}
