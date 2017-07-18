/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicxe;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mahin
 */
public class TR {
     private int StartAddress;
    private int Length;
    private List<String> ObjectCodes;
    public int length;
    
    //public static final int MAXLENGTH = 32;
    
    public TR(int startAddr) {
        StartAddress = startAddr;
        Length = 0;
        ObjectCodes = new ArrayList<>();
    }
   
      public void add(String objectCode) {
       ObjectCodes.add(objectCode);
    }
    public int size() {
        length=0;
         for (String s : ObjectCodes) {
             length = length + s.length()/2;
        }
         //System.out.println(length);
        return length ;
    }
    public void MakeListEmpty() {
        ObjectCodes.clear();
        length=0;
    }
    
    public String toObjectProgram() {
        Length = size();
        
        String text = String.format("T^%02X^%06X", Length , StartAddress);
         for (String s : ObjectCodes) {
            text += String.format("^%s",s);
        }
        
        return text;
    }
}
