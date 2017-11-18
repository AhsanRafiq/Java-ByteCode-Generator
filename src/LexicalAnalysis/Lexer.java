

package LexicalAnalysis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;


public class Lexer {
  
    // To Store the location of the class to parse
   private String sourceCodeLocationString;
    
   // To store Source Code as characters
   private ArrayList<Character>  sourceCodeList;
    
    // To Store Tokens of program
   private ArrayList<Token> tokensList;
   
   // To Control the loop to reach end of program
   private int counter ;
   
   // To store reserve Words
   private  ArrayList<String> keywordsArrayList = new ArrayList<>();
   
   // To Store the line  no
   private int lineNo  = 1;
   
   //To Store Operators
   ArrayList<String> operatorsArrayList ;
   // flag bit to stop
   private boolean  flagBit;
   
   
   public Lexer (String location)
   {
       sourceCodeLocationString = location;
       sourceCodeList = new ArrayList<>();
       tokensList = new ArrayList<>();
       operatorsArrayList  = new ArrayList<>();
       flagBit = true;
       counter = 0 ;
       addKeywords();
        CreateOperatorArray();
         fileReader();
   }
   
   public String startLex() throws IOException
   {
       
   
      
        
         while (counter< sourceCodeList.size() && flagBit) {
         
             
             Token lexem = scanner();
            
             if(lexem!=null)
             {
        
         
                 tokensList.add(lexem);
           
                  System.out.println("Token Sent : "+" < "+lexem.getID()+"  , "+lexem.getValue()+" > ");
                 return lexem.getValue().trim();
           
             }
             
             
           
       }
   
   return  null;
   }
//**********************************//
   
   private void fileReader()
   {
    FileInputStream reader =null;
       
         try{
           
            reader = new FileInputStream(sourceCodeLocationString);
        
         }	
       
        
         catch(FileNotFoundException e)
         {
        
            e.getStackTrace();
       
        }
        
        int chRead =0;
        
        char ch=' ';
       
         try{	
        
             
            while ((chRead = reader .read()) != -1) {
           
             
                ch=(char)chRead;	
           
                
                sourceCodeList.add(ch);
            
           
            }
            
        
        
        }
       
            
         catch(IOException e)
        
        {
        
            e.getStackTrace();
        
        }
       
   
   
   }
   
//*********************************//
   
   
//*******************************************//
   
    private Token scanner() throws IOException {
       

      
        char next = read();

    
        
        if(idChar(next) && !(isDigit(next)))
        {
            return readID(next);
        
        }
        
        if(number(next))
        {
            return readNumber(next);
        
        }
        
        if(next == '"' )
        {
            return readString(next);
        }
        
        if(isOperator(next)) 
        {
            return readOperator(next);
        
        }
        
        if(isSymbol(next))
        {
            return readSymbol(next);
        
        
        }
        if(next=='.')
        {
        
        
            return new Token("Dereference", ".");
        }
          
        
        
        if((int)next == 13){}
      
        if((int)next==10 ){lineNo++;}
        
        if(next ==' '){while((read()==' ')); goBackword(); }
        
        
        
       return null;
       
     
        
    }
    
    //****************************************//



    private char read() {
       
       
        return sourceCodeList.get(counter++);
    }

    private boolean idChar(char c) {

        
        
        if(isAplha(c)){ return  true;}
        
        if(isDigit(c)){ return true;}

        if(c == '_'){ return true;}
        
     //   if(c=='.'){return true;}
        return  false;
    }

    private boolean isAplha(char c) {
       
        return ((int)c >= 65 && (int)c <= 90 || (int)c >= 97 && (int)c <= 122);   
    }

    private boolean isDigit(char c) {

         return ((int)c >= 48 && (int)c <= 57 );
    }

    private Token readID(char next) {
       
        
        String id = ""+next;
        
       
        
        
        while(true)
        {
            char c = read();
            
            if(idChar(c) == false)
            {
                goBackword();
                
                if (isKeyword(id.trim())) {
                    
                 return readKeyword(id);
                } 
                if(startWith(id))
                {
                    System.out.println("Identifier did not start with any number\n Error :  Line # "+lineNo);
                    flagBit = false;
                    return null;
                
                }
                
                return new Token("ID", id.trim());
            }
            
            id = id + ""+c;
        
        }
    }

    private boolean number(char next) {
       
         return ((int)next >= 48 && (int)next <= 57 );
    }
      
    private Token readNumber(char next) {
        
            String id = ""+next;
        
                 while(true)
                  {
         
                      char c = read();
            
            
                      if((num(c) == false && c != '.'&& Alpha(c)==false))
            
                      {
               
                          
                          goBackword();
                
                          if(StringUtils.countMatches(id, ".")>1){ System.out.println("Only one decimal Point is allowed \nError : Line # "+lineNo); flagBit=false;return  new Token("er", "error"); }
                
                       if(CheckAnyAlpha(id)){System.out.println("Only Constant is allowed && Variable did't start with number\nError: Line # "+lineNo); flagBit=false; return  new Token("er", "error");}
               
                          return new Token("Num", id);
            
                      }
            
            id = id + ""+c;
    }
    
} 

    private boolean num(char c) {
    
      if(isDec(c)){return true;}
      if(number(c)) return  true;
      
       return  false;
    }

    private boolean isDec(char c) {
     
        if(c =='.') return true;
        
        return  false;
    }

    private Token readString(char next) {
       
          String id = "";
        
                 while(true)
                  {
            char c = read();
            
            if( c=='"')
            { 
                read();
               goBackword();
               
             int occurance = StringUtils.countMatches(id, "\\");
                
             if(occurance%2!=0){ 
                   
                   System.out.println("Error : line # "+lineNo);
                   flagBit = false;
                   
                   return null;
               
               }
                return new Token("String", id);
            }
            
            id = id + ""+c;
    }
       
        
    }

    private Token readOperator(char next) {
     
      
                                  
                                      String id = ""+next;
        
                                          while(true)
                                           {
                                                     char c = read();
            
                                      if(!(isOperator(c)))
                                          {
                                              goBackword();
                                             
                                              if(opeartorExits(id.trim()))
                                              {    
                                                  return new Token("OP", id);
                                              
                                              }
                                              else{
                                                  System.out.println("Operator Found : "+id);
                                                
                                                  if(id.equals("//"))
                                                  {
                                                      char next2=read();
                                                      while(next2!='\n')
                                                      {
                                                      
                                                      next2 = read();
                                                          
                                                          
                                                      
                                                      }
                                                  
                                                  }
                                                  
                                                  
                                                  System.out.println("Operator Not Found");
                                                  return null ;
                                              
                                              }
                                          }
            
                                                 id = id + ""+c;
                                      }
       
    }

    private boolean isOperator(char next) {
       
        ArrayList<Character> simpleOperator = new ArrayList<>();

        simpleOperator.add('+');		

        simpleOperator.add('-');

        simpleOperator.add('/');

        simpleOperator.add('%');		

        simpleOperator.add('!');

        simpleOperator.add('>');

        simpleOperator.add('<');
        
        simpleOperator.add('=');
        
        simpleOperator.add('|');
       
      
        
        simpleOperator.add('*');
        
        
      
      if(simpleOperator.contains(next))
      { return  true;}
      
                                    
        return  false;
    }

    private boolean isKeyword(String id) {
     
       
       if(keywordsArrayList.contains(id)) return true;
        
        return false;
    }

    private Token readKeyword(String id)
    {
    
            
        
        return new Token("Keyword", id);
    
    
    }

    private void addKeywords()
    {
       keywordsArrayList.add(0,"abstract");

        keywordsArrayList.add(1,"boolean");

        keywordsArrayList.add(2,"break");

        keywordsArrayList.add(3,"byte");

        keywordsArrayList.add(4,"case");

        
        
        keywordsArrayList.add(5,"catch");

        keywordsArrayList.add(6,"char");

        keywordsArrayList.add(7,"class");

        keywordsArrayList.add(8,"continue");

        

        keywordsArrayList.add(9,"default");

        keywordsArrayList.add(10,"do");

        keywordsArrayList.add(11,"double");

        keywordsArrayList.add(12,"else");

        keywordsArrayList.add(13,"extends");

        keywordsArrayList.add(14,"final");

        keywordsArrayList.add(15,"finally");

        keywordsArrayList.add(16,"float");

        keywordsArrayList.add(17,"for");

        

        keywordsArrayList.add(18,"if");

        keywordsArrayList.add(19,"implements");

        keywordsArrayList.add(20,"import");

        keywordsArrayList.add(21,"instanceof");

        keywordsArrayList.add(22,"int");

        keywordsArrayList.add(23,"interface");

        keywordsArrayList.add(24,"long");

        keywordsArrayList.add(25,"native");

        keywordsArrayList.add(26,"new");

        

        keywordsArrayList.add(27,"package");

        keywordsArrayList.add(28,"private");

        keywordsArrayList.add(29,"protected");

        keywordsArrayList.add(30,"volatile");

        keywordsArrayList.add(31,"public");

        keywordsArrayList.add(32,"return");
        
        keywordsArrayList.add(33,"short");
    
        keywordsArrayList.add(34,"static");
         
        keywordsArrayList.add(34,"super");
        
        keywordsArrayList.add(35,"switch");
     
        keywordsArrayList.add(36,"this");
        
        keywordsArrayList.add(37,"void");
        keywordsArrayList.add(38,"while");
    
    }

    private void goBackword() {
      
        counter--;
    }

    private boolean isSymbol(char next) {
        
        if((int)next == 59 || (int)next == 123 || (int)next == 125 || (int)next == 40 ||(int)next == 41||(int)next == 91||(int)next == 93||(int)next ==44){return  true;}
        return  false;
    }

    private Token readSymbol(char next) {
        
        return new Token("Symbol", ""+next);
    }

    private boolean opeartorExits(String id) {
        
        if(operatorsArrayList.contains(id)) return  true;
        else return false;
        
        
        
        
    }
    
    
    private void CreateOperatorArray()
    {
        
        operatorsArrayList.add("=");
        operatorsArrayList.add("+");
        operatorsArrayList.add("-");
        operatorsArrayList.add("*");
        operatorsArrayList.add("/");
        operatorsArrayList.add("%");
        operatorsArrayList.add("++");
        operatorsArrayList.add("--");
        operatorsArrayList.add("!");
        operatorsArrayList.add("==");
        operatorsArrayList.add("!=");
        operatorsArrayList.add(">=");
        operatorsArrayList.add(">");
        operatorsArrayList.add("<");
        operatorsArrayList.add("<=");
        operatorsArrayList.add("instanceof");
        operatorsArrayList.add("~");
        operatorsArrayList.add(">>");
        operatorsArrayList.add("<<");
        operatorsArrayList.add(">>>");
        operatorsArrayList.add("&");
        operatorsArrayList.add("^");
        operatorsArrayList.add("|");
        operatorsArrayList.add("&&");
        operatorsArrayList.add("||");
         operatorsArrayList.add(".");
        
    
    
    
    }
 
    public boolean containsIllegals(String toExamine) {
    
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\_^]");

   
        Matcher matcher = pattern.matcher(toExamine);
    
        return matcher.find();
}

    private boolean startWith(String id) {
      
        char firstCh=id.charAt(0);
        
        if((""+firstCh).matches("[0-9]")) {return true;}
       
        return  false;
    }

    private boolean CheckAnyAlpha(String id) {
       
        
        
        if(id.matches("[0-9.]+")){return false;}
       
        return  true;
    }

    private Token readLibraryFile() {
      
        String id = ""+read();
        while(true)
        {
            char c = read();
        
            if(c==';')
            {
                goBackword();
                
                
                return new Token("Library", id);
            
            }
        id = id + ""+c;
        
        }
    }

    private boolean Alpha(char c) {

        if((int)c >=97 &&  (int)c <=122 || (int)c >=65 &&  (int)c <=90 ) 
        {
            return true;
        
        }
    
        return  false;
    
    }
    
    
    
    

}// Class End


    
    

