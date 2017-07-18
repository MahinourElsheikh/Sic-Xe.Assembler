/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicxe;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
public class SicXe {
    
    
   
   
    public int  flag=0,flag1=0;
    private int baseAddress=0;
    private int sflag=0, loctrflag=0;
    List<Integer> Programlengths = new ArrayList<>();
    List<Integer> locctrs = new ArrayList<>();
    List<Integer> startAddresses = new ArrayList<>();
    private final Map<String, Operation>opTable;
    private List<Map<String, Literal>> literalTables;
    public  final Map<String, Integer> RegiterTable=new HashMap<>();
    public  ArrayList <LineDivident> Lines= new ArrayList<LineDivident>();
    private List<Map<String,Label>> SymbolTables = new ArrayList();
    
     
    public SicXe(File validOp) throws FileNotFoundException {
         opTable = new HashMap<>();
         
         //ana 3amalt hashtable 3ashan 27ot fiha el optables w el machine code bt3hm w el format (hash table gowaha operations
        try (Scanner scanner = new Scanner(validOp)) {
            while (scanner.hasNext()) {
                String[] buf = scanner.nextLine().split(" ");
                
                opTable.put(buf[0], new Operation(buf[0], buf[1], buf[2])); 
            }
        }
        
        literalTables= new ArrayList<>();
    }
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, ScriptException{
        
        
        SicXe Sicxe = new SicXe(new File("Validoperations.txt"));
        Sicxe.Passe(new File("equ.asm"), new File("HTE-Record.txt"));
    }
    public void Passe (File input, File output) throws IOException, ClassNotFoundException, ScriptException{
         SymbolTables = passOne(input,Lines);// ba5azn awel pass f list of lines
         if(flag==0){
        passTwo(SymbolTables,Lines, output);// batalla3 f file gedid
         }
         else { 
             System.out.println("DID not Complete Pass2!!!!!!!!");
             System.exit(0);}
    }
    
     
     private List<Map<String,Label>> passOne(File input, ArrayList Lines) throws IOException, ScriptException{
     

        try (Scanner scanner = new Scanner(input);)
            
        {
         
                       
            Map<String, Literal> literalPool = new HashMap<>();
            LineDivident Line = LineDivident.parse(scanner.nextLine());
           
            Lines.add(Line);
            
            if (Line.compareTo("START") == 0) {
                Map<String,Label> sym =new HashMap<>();
               // sym.put("ha",new Label("ha",false));
                SymbolTables.add(sym);
                startAddresses.add(Integer.parseInt(Line.operand1()));
                
            } else {
                System.out.println(" START Not Found");
                          flag=1;
                            
            }
            
            locctrs.add(startAddresses.get(sflag)); 
            
            while (scanner.hasNext()) {
                Line = LineDivident.parse(scanner.nextLine());
                
                Line.setLocation(locctrs.get(loctrflag));
//                Uncomment the next line can show the Loc and Source statements
                  //System.out.println(Line);
            
                {
                    if (Line.compareTo("END") != 0) {
                        if (Line.isComment()) {
                            continue;
                        }
                        
                        if (Line.label() != null) {
                            if (SymbolTables.get(sflag).containsKey(Line.label()) && SymbolTables.get(sflag).get(Line.label()).D==false && !"CSECT".equals(Line.operation())) {
                              System.out.println("Duplicate label"+ Line);
                              flag=1;
                            } else if(SymbolTables.get(sflag).containsKey(Line.label()) &&SymbolTables.get(sflag).get(Line.label()).D== true) {
                                SymbolTables.get(sflag).replace(Line.label(),new Label(Line.label(),locctrs.get(loctrflag),true));
                               
                            } 
                            else{ SymbolTables.get(sflag).put(Line.label(), new Label(Line.label(),locctrs.get(loctrflag)));
                            }
                        }

                        if (opTable.containsKey(Line.operation())) {
                            switch (opTable.get(Line.operation()).format()) {
                                case "1":
                                { locctrs.set(loctrflag,(locctrs.get(loctrflag)+1));
                                    
                                    break;}
                                case "2":
                                    locctrs.set(loctrflag,(locctrs.get(loctrflag)+2));
                                    break;
                                case "3/4":
                                    locctrs.set(loctrflag,(locctrs.get(loctrflag)+3 + (Line.isFormat4() ? 1 : 0)));
                                    break;
                            }
                        } else if (Line.compareTo("WORD") == 0) {
                            locctrs.set(loctrflag,(locctrs.get(loctrflag)+3));
                        } else if (Line.compareTo("RESW") == 0) {
                            locctrs.set(loctrflag,(locctrs.get(loctrflag)+3 * Integer.parseInt(Line.operand1())));
                        } else if (Line.compareTo("RESB") == 0) {
                            locctrs.set(loctrflag,(locctrs.get(loctrflag)+Integer.parseInt(Line.operand1())));
                            
                        } else if (Line.compareTo("BYTE") == 0) {
                            String s = Line.operand1();

                            switch (s.charAt(0)) {
                                case 'C':
                                    locctrs.set(loctrflag,(locctrs.get(loctrflag)+(s.length() - 3))); // C'EOF' -> EOF -> 3 bytes
                                    break;
                                case 'X':
                                    locctrs.set(loctrflag,(locctrs.get(loctrflag)+(s.length() - 3) / 2)); // X'05' -> 05 -> 2 half bytes
                                    break;
                            }
                        } else if (Line.compareTo("BASE") == 0) {
                            // pass one do nothing to directive 'BASE'
                        }else if (Line.compareTo("NOBASE") == 0) {
                            // pass one do nothing to directive 'BASE'
                        } else if (Line.compareTo("EQU") == 0) {
                            if("*".equals(Line.operand1()))
                            {    //System.out.println("hoi");
                                   SymbolTables.get(sflag).put(Line.label() , new Label(Line.label(),locctrs.get(loctrflag)));
                                   
                                  // System.out.println(symTab.get(Line.label()));
                            }
                            else
                            {    
                              //  System.out.println(Line.operand1());
                                ScriptEngineManager mgr = new ScriptEngineManager();
                                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                String foo = parsech(Line.operand1(), SymbolTables.get(sflag));
                                Object m = engine.eval(foo);
                                SymbolTables.get(sflag).put(Line.label(), new Label(Line.label(),m,'A'));
                            
                                
                            } 
                           
                        }else if (Line.compareTo("ORG") == 0) {
                           // System.out.println(Line.operand1());
                                ScriptEngineManager mgr = new ScriptEngineManager();
                                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                String foo = parsech(Line.operand1(), SymbolTables.get(sflag));
                                Object m = engine.eval(foo);
                               
                                locctrs.set(loctrflag, (Integer) m);
                                
                                
                        }else if (Line.compareTo("CSECT") == 0) {
                            Programlengths.add(locctrs.get(loctrflag) - startAddresses.get(sflag));
                            sflag++;
                            loctrflag++;
                           // System.out.println("hi");
                            Map<String,Label> sym =new HashMap<>();
                           //sym.put(Line.label(),new Label("ha",false));
                           startAddresses.add(0);
                           locctrs.add(0);
                           SymbolTables.add(sym);
                         if(Line.label()!=null){
                              // System.out.println(Line.label()+"ya raaaab");
                               SymbolTables.get(sflag).put(Line.label(),new Label(Line.label(),0));
                           }
                           
                           
               
                            
                          
                        } else if (Line.compareTo("LTORG") == 0) {
                            // LTORG
                        }
                        else if (Line.compareTo("EXTDEF") == 0) {
                            SymbolTables.get(sflag).put(Line.operand1(), new Label(Line.operand1(), 0, true));
                            if(Line.operand2()!=null){
                            String[] pieces = Line.operand2().split(",");
                            for (String piece : pieces) {
                             
                                SymbolTables.get(sflag).put(piece, new Label(piece, 0, true));
                            }
                                
                            }
               
                          
                       
                        }else if (Line.compareTo("EXTREF") == 0) {
                            
                             SymbolTables.get(sflag).put(Line.operand1(), new Label(Line.operand1(), 0, false));
                           
                             if(Line.operand2()!=null){
                             String[] pieces = Line.operand2().split(",");
                            for (String piece : pieces) {
                              
                                SymbolTables.get(sflag).put(piece, new Label(piece, 0, false));
                            }}
                        } else {
                            System.out.println(" Invalid Operation"+ Line.operation());
                            flag=1;
                        }

                        if (Line.hasLiteral()) {
                            Literal literal = Literal.parse(Line.operand1());
                        
                            literalPool.put(literal.name(), literal);
                        }
                    }
                  
                
                }
                
               
               Lines.add(Line);
                
                if (Line.compareTo("LTORG") == 0 || Line.compareTo("END") == 0) {
                    for (Literal literal : literalPool.values()) {
                        Line = literal.toStatement();
                    
                        
                        literal.updateAddress(locctrs.get(loctrflag));
                        Line.setLocation(locctrs.get(loctrflag));
                       
                         locctrs.set(loctrflag,(locctrs.get(loctrflag)+literal.length()));
                       // objOutputStream.writeObject(Line);
                       Lines.add(Line);
                    }
                    
                    literalTables.add(literalPool);
                    literalPool = new HashMap<>();
                }
            }
            
            Programlengths.add(locctrs.get(loctrflag) - startAddresses.get(sflag));
            
         
                  for(int h =0 ; h<SymbolTables.size();h++){
                      System.out.println("Control Section number: "+ h );
                for (String name: SymbolTables.get(h).keySet()){

                  String key = name;
               Object value =  SymbolTables.get(h).get(name).value;  
               char type=SymbolTables.get(h).get(name).type;
               System.out.println(key + " " + value + " "+ type);  


                                } }
            return SymbolTables;
        }
    }
     private void passTwo( List<Map<String, Label>> SymTables,ArrayList Lines , File output) throws IOException, ClassNotFoundException//, ExpectedDirectiveNotFoundException 
    {   
         
                           sflag=0;
                           loctrflag=0;
                            
   ;
            Map<String, Integer> RegisterTable = new HashMap<>();
            
            // put REGISTERs into register table
            RegisterTable .put(null, 0);
            RegisterTable .put("A", 0);
            RegisterTable .put("X", 1);
            RegisterTable .put("L", 2);
            RegisterTable .put("B", 3);
            RegisterTable .put("S", 4);
            RegisterTable .put("T", 5);
            RegisterTable .put("F", 6);
            RegisterTable .put("SW", 9);
        try (PrintWriter writer = new PrintWriter(output, "UTF-8");) 
                {
            LineDivident Line =   (LineDivident) Lines.get(0);
            
            for(int x =0; x< locctrs.size();x++){
               
              
            if (Line.compareTo("START") == 0) {
                writer.println(new HR(Line.label(), startAddresses.get(sflag), Programlengths.get(sflag)).toObjectProgram()); //bndah 3ala class HR w ba5od baktbo fl object code
                
            } else if(x!=0 && Line.operation()!=null ) { // bedayaaa mn awl extref
                // System.out.println(startAddresses.get(sflag)+ " staaart" + Programlengths.get(sflag) + "eeend");
                // writer.println(new HR(Line.label(), startAddresses.get(sflag),Programlengths.get(0)).toObjectProgram()) ; //Programlengths.get(sflag-1)).toObjectProgram()
                
            }
            else{
                System.out.println(" START Not Found");
                          flag=1;
            }
            
            List<MR> ModificationRecords = new ArrayList<>();
           
            TR textRecord = new TR(startAddresses.get(sflag));
            DR DRecord = new DR();
            EXR EXRecord = new EXR();
            int i=0;
             ArrayList <LineDivident> Lines2= new ArrayList<>(Lines.subList(Lines.indexOf(Line), Lines.size()));
          

            if("CSECT".equals(Line.operation())){
           
             Lines2= new ArrayList<>(Lines.subList((Lines.indexOf(Line)+1), Lines.size()-1));
              
            }
             
            while (Lines2.isEmpty()==false) {
                
                
                
                    
                    
                
                 
                 if(( !"CSECT".equals(Lines2.get(0).operation()) ) && ("CSECT".equals(Line.operation()) ) ){
                     
                   
                          sflag++;
                          loctrflag++;
                         
                           
                         
                            break;
                        
                 
                 }
                 
                if(i==Lines2.size()){ 
                    
                break;
                }
                Line = (LineDivident) Lines2.get(i++); //b cast el object l LineDividrnt lw mawgoud
                if(Line.compareTo("EXTDEF") == 0 ){
                   Map<String, Label> SymTable = SymTables.get(sflag);
                          
                        for ( Label l : SymTable.values()){
                
                    if(l.D == true &&!"ha".equals(l.name)){
                    
                    DRecord.addn(l.name);
                    DRecord.adda((int) l.value);
                    }
                        }
               
                    writer.println(DRecord.toObjectProgram() +'\n');
                        }
                if(Line.compareTo("EXTREF") == 0 ){
                   EXRecord.addn(Line.operand1());
                           if(Line.operand2()!=null){
                             String[] pieces = Line.operand2().split(",");
                            for (String piece : pieces) {
                                EXRecord.addn(piece);
                            }}
                      
                    writer.println(EXRecord.toObjectProgram() +'\n');
                        }
                        
                
                if (Line.isComment() == false) {
                    String objectCode = SicXeInstruction(Line, SymTables.get(sflag),RegisterTable);//hna b2a ana b3ml el objectcode
                    //dlw2ty ana m3aya el objectcode bta3 el instruction
                    // If it is format 4 and not immediate value
                    if (Line.isFormat4() && SymTables.get(sflag).containsKey(Line.operand1())) {
                     
                        ModificationRecords.add(new MR(Line.location() + 1,5,Line.operand1())); //n7taha f list el modification recors 3ashan fl 25r27thm mara w7da
                    }
                    

//                    to see the  corresponding object code below
                       System.out.println(Line+"\t \t"+objectCode);
//                     
                    if ( (/*"".equals(objectCode) ||*/ Line.operation().equalsIgnoreCase("RESB")== true || Line.operation().equalsIgnoreCase("RESW")== true)  && Line.operation().equalsIgnoreCase("BASE")== false )
                    {    
                      
                        if(textRecord.size()!=0){
                    
                       writer.println(textRecord.toObjectProgram() + '\n');
                         textRecord.MakeListEmpty();
                     }
                    else {
                        
                        }   
                        // textRecord.MakeListEmpty();
                       //textRecord = new TR(Line.location());
                    }
                    if( !"".equals(objectCode) && (textRecord.size() + objectCode.length()/2)<=30 && Line.operation().equalsIgnoreCase("RESB")== false && Line.operation().equalsIgnoreCase("RESW")== false )
                    { 
                      if( Line.operation().equalsIgnoreCase("BASE")== false  || Line.operation().equalsIgnoreCase("LETORG")== false){
                    
                      if(textRecord.size()==0){
                      textRecord = new TR(Line.location());
                    }
                      // System.out.println("meean  c" + objectCode);
                       textRecord.add(objectCode);
                       
                    }
                    }
                    else if ((textRecord.size() + objectCode.length())>30 && Line.operation().equalsIgnoreCase("BASE")== false) {
                       
                    writer.println(textRecord.toObjectProgram() + '\n');
                    textRecord.MakeListEmpty();
                    textRecord = new TR(Line.location());
                    textRecord.add(objectCode);
                    }

                     
                }
            }
            if(textRecord.length!=0){ 
                 
             writer.println(textRecord.toObjectProgram() + '\n');}
            
            for (MR r : ModificationRecords) {
               writer.println(r.toObjectProgram() + '\n');
            }
            
            if(sflag!=0){
            writer.println(new ER(startAddresses.get(sflag-1)).toObjectProgram() + '\n');
             if(Lines2.indexOf(Line)!= (Lines2.size()-2)){
             
             if(i!=Lines2.size()){
           writer.println(new HR(Line.label(), startAddresses.get(sflag),Programlengths.get(sflag)).toObjectProgram()) ;
             Line = (LineDivident) Lines2.get(i++);}
             }
            }
            else if(i==Lines2.size()){
            writer.println(new ER(startAddresses.get(sflag)).toObjectProgram() + '\n');
            
            }
              
             
        }
                }
        
    }
    
    private String SicXeInstruction(LineDivident Line, Map<String,Label> symTab , Map<String, Integer>RegisterTable) {
        String objCode = "";
        boolean bs=false;
        boolean lf=false;
          Literal lit=null;
        if (opTable.containsKey(Line.operation())) {
            switch (opTable.get(Line.operation()).format()) {
                case "1":
                    objCode = opTable.get(Line.operation()).opcode();
                    
                    break;
                case "2":
                    objCode = opTable.get(Line.operation()).opcode();
                    
                    objCode += Integer.toHexString(RegisterTable .get(Line.operand1())).toUpperCase();
                    objCode += Integer.toHexString(RegisterTable .get(Line.operand2())).toUpperCase();
                    
                    break;
                case "3/4":  // ana hna baftered en kolo 1s w a2a 23ml or m3a elly ynf3 m3ah
                    final int n = 1 << 5; //hna el nixbpe are 6 bits starting mn e bit number 1 w b3d kda shift
                    final int i = 1 << 4; 
                    final int x = 1 << 3;
                    final int b = 1 << 2;
                    final int p = 1 << 1;
                    final int e = 1;
                    
                    int code = Integer.parseInt(opTable.get(Line.operation()).opcode(), 16) << 4; //shift 4 left y3ny lw rsub 4c el code b3d el
                                                                                                    // el shift b2a 4C0
                          String operand = Line.operand1();
                  
                    if (operand == null) {
                        code = (code | n | i) << 12; // for RSUB //bgm3 align el shift y3ny ba7ot 3 0 hexas fl 25r
                    } else {
                        switch (operand.charAt(0)) {
                            case '#': // immediate addressing
                                code = code | i; 

                                operand = operand.substring(1);
                                break;
                            case '@': // indirect addressing
                                code = code| n;

                                operand = operand.substring(1);
                                break;
                            default: // simple/direct addressing
                                code = code|n | i;

                                if (Line.operand2() != null) {
                                    code = code| x;
                                }
                        }
                        
                        int disp = 0, target;
                        for (Map<String, Literal> literalPool : literalTables){
                          
                        for ( Literal literal : literalPool.values()){
                        
                        if(literal.name() == null ? operand == null : literal.name().equals(operand))
                        {  lf=true;
                            lit = literal;
                           break;
                        }
                        }
                            
                        }
                        
                        if (symTab.get(operand) == null && lf == false) {
                         
                             try{
                            disp = Integer.parseInt(operand);}
                             catch( NumberFormatException xx ){
                             
                             System.out.println("ERRORRRR!!!! Label isnt identifind ");
                             System.exit(0);
                             }
                        } else {
                            if (symTab.get(operand) != null) {
                                disp = target = (int) symTab.get(operand).value;
                            } else {
                                disp = target = lit.address();
                            }
                            if(symTab.get("BASE")== null ){
                             bs=true;
                            }
                            
                            if (Line.isFormat4() == false) {
                                
                                disp -= Line.location() + 3 ;
                                int disp1 = target - baseAddress;
                               
                                if (disp >= -2048 && disp <= 2047) { // hna ana ba2oulo hwa Pc wala base relative 3ala 7asab el
                                    code |= p;
                                } else if ((disp1 >=0 && disp1 <=4096)&& bs==true)  {
                                   
                                    code |= b;
                                    
                                    disp = target - baseAddress;
                                }
                                else{
                                 System.out.println("Addres is bigger than base address or base relative not defined");
                                  System.exit(0);
                                
                                }
                            }
                        }
                        
                        if (Line.isFormat4()) {
                            code |= e;    //ba5aly el e b wa7ed
                            
                            code = (code << 20) | (disp & 0xFFFFF); //ba5alih el code 4 bytes badal 3 bytes 3ashan el disp b 5 hexa
                        } else {
                            code = (code << 12) | (disp & 0xFFF);  //ba5alih el code 3 bytes badal 3 bytes 3ashan el disp b 3 hexa
                        }
                    }
                    
                    objCode = String.format(Line.isFormat4() ? "%08X" : "%06X", code); //hna ana ba7awel el code l hexa 3ala 7asab 3adad el hexa elly fih
                    
                    
                    break;
            }
        }
        else if (Line.compareTo("BYTE") == 0 || Line.hasLiteral()) {
            String s, type;
            
            if (Line.compareTo("BYTE") == 0) {
                s = Line.operand1();
            } else { // literal
                s = Line.operation();
            }
            //ana 7a2asm el string l hwa charcter wala ih (no3o) w elly gowah(s)
            type = s.substring(0, s.indexOf('\'')); 
            s = s.substring(s.indexOf('\'') + 1, s.lastIndexOf('\''));
            
            switch (type) {
                case "C": //lw charcter b7taha zay ma hya fl object code
                case "=C":
                    for (char ch : s.toCharArray()) {
                        objCode += Integer.toHexString(ch).toUpperCase();
                    }
                
                    break;
                case "X":
                case "=X":
                    objCode = s; //ba7ot 3ltoul el rakam
                    
                    break;
            }
        } else if (Line.compareTo("WORD") == 0) {
            int foo = Integer.parseInt(Line.operand1());
            objCode = String.format("%06x",foo);
        } else if (Line.compareTo("BASE") == 0) {
            baseAddress = (int) symTab.get(Line.operand1()).value;
           
        } else if (Line.compareTo("NOBASE") == 0) {
           
        }
        
        
        
        return objCode;
    }
    public String parsech(String x,Map<String, Label> symTab){
        int countx=0;
        int county=0;
        StringBuilder numeval = new StringBuilder();
        for( int i=0 ; i< x.length();i++){
            char c =  x.charAt(i);
           
            if(Character.isLetterOrDigit(c)){
                StringBuilder sb = new StringBuilder();
                while(Character.isLetterOrDigit(x.charAt(i))){
                      
                     
                        sb.append(x.charAt(i));
                            i++;
                      if(i==x.length()) break;      
                }
                i--;
               
                 
                String str =sb.toString();
               
                if(Character.isDigit(str.charAt(0))){
                  
                    numeval.append(str);
                
                }
                else{
                     if(symTab.containsKey(str))//search symbol table
                     { Object m = symTab.get(str).value;// ybdlha ow ydrab flag
                    
                       numeval.append(m); //y7taha fl numval 
                     }
                     else{
                      flag1=1;   
                     }
                    
                }
                    
               
            } 
            else {
                if(c=='*'){
                    
                      System.out.println( x+"found: " +c+ "  error no multiplication in relative expression");
                      
                      System.exit(0);
                      }
                else if(c=='+'){
                countx++;
                }
                else if(c=='-'){
                county++;
                }
                
                numeval.append(c);
                
            }
    
    }
        
    String expression = numeval.toString();
    if(countx-county>=1){
        System.out.println(x+ "  error relative expression");
        
        }
    
    return expression;
    
    } 
   
    
}
