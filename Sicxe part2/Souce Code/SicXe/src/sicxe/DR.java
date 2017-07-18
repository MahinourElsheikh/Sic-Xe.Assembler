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
public class DR {
    
    
    private List<String> Name;
    private List<Object> addresses;
    
    public DR() {
        
        Name = new ArrayList<>();
        addresses= new ArrayList<>();
        
    }
    public void addn(String objectCode) {
        
        if(!"".equals(objectCode))
        {
          Name.add(objectCode);
        }
    }
    
    public void adda(int objectCode) {
       addresses.add(objectCode);
    }
    
    public String toObjectProgram() {
        String text = String.format("D");
         for (int i=0 ; i<Name.size();i++) {
             String s= Name.get(i);
             Object add=   addresses.get(i);
            text += String.format("^%s^%06X",s,add);
            
        }
        
        return text;
    }
    
}
