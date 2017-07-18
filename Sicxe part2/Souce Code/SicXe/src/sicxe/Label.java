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
public class Label {
     String name;
     Object value;
    boolean D;
    char type;

    Label(String n, Object val) {
       this.name =  n;
       this.value= val;
       this.D= false;
       this.type='R';
    }
     Label(String n) {
       this.name =  n;
       this.value= 0;
       this.D= false;
        this.type='R';
    }
     Label(String n,Object val, boolean f) {
       this.name =  n;
       this.value= val;
       this.D= f;
        this.type='R';
    }
     Label(String n,Object val,char f) {
       this.name =  n;
       this.value= val;
       this.D= false;
        this.type=f;
    }
}

