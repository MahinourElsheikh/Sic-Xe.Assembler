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
public class EXR {
  
    
    
    private List<String> Names;
  
    
    public EXR() {
        
        Names = new ArrayList<>();
       
        
    }
    public void addn(String objectCode) {
       
        if(!"".equals(objectCode))
        {
          Names.add(objectCode);
        }
    }
    
    
    
    public String toObjectProgram() {
        String text = String.format("R^");
       
          for ( String s : Names)
          {
              
            text += String.format("%s^",s);
        }
        
        return text;
    }
    
}

    

