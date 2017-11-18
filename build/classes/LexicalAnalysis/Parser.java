/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LexicalAnalysis;


import Parsing.ClassWrite;
import java.io.IOException;
import javafx.beans.binding.StringExpression;
import org.objectweb.asm.ClassWriter;



/**
 *
 * @author Ahsan Rafiq
 */
public class Parser {
    
    private static boolean flag = true;
    
    String lookahead ="";
  
    private int counter = 0;
    
    private int checkCount =20;
   

    private boolean againCheckField = false;
   
   
    private Lexer lexer=null;
    
    private char digits[];
    
        private int index=1;
            
    public static void main(String args[]) throws IOException
    {
        Parser demoParser = new Parser();
        demoParser.lexer =  new Lexer("src\\Parsing\\SourceProgram.java");
        
        demoParser.Compilation();
        
        if(Parser.flag)
        {
        
            System.out.println("Programe Cannot Parsed");
            
        }
        
        
    }

    
    
    
     public void Compilation()
    {
        lookahead=  getToken();
        
        optionalPackageDeclaration();  optionalImportDeclaration();  optionalTypeDeclaration();
        
    
    }

    private void optionalPackageDeclaration()  {

      
         if (lookahead == null) {
            return;
        }
        
        
      if(lookahead.equals("package"))
        {
            matcher("package"); identifier(); matcher(";");
        }
        return;
    }

    private void optionalImportDeclaration() {

     
            singleTypeImportDeclaration();
        
        
       
    
    }

    private void optionalTypeDeclaration() {
        
        classDeclaration();
        
        
    }

    private void matcher(String lexem)  {
     
     
        if (lookahead == null) {
            
            return;
        }
       
        
        if(lookahead.equals(lexem))
        {
             System.out.println("Matched : "+lookahead);
        
             lookahead = getToken();
        
             try {
                   if(lookahead.equals("error"))
   
                   {
                       System.out.println("Program cannot Parsed\nThere is an error in source code");
                       System.exit(0);
        
         
                   }
            } catch (Exception e) {
        
            }
      
         
            if(lookahead==null)
           {
               
               
               System.out.println("\n\n\nProgram Parsed\n");
               Parser.flag = false;
               
               System.out.println("Byte Code is : \n");
               ClassWrite classWrite = new ClassWrite();
               System.exit(0);
               
           
           }
         else   if(lookahead.matches("[0-9.]+")&& !(lookahead.equals(".")))
         {
             index =1;
             digits = lookahead.toCharArray();
           
             lookahead=""+digits[0];
           
             matchDigit("");
         }
       
        }
    }

    private void identifier()  {

        if(reserveWords(lookahead))
        {
            return;
        
        }
        
        if(lookahead.matches("[0-9.]+"))
        {
            return;
            
        
        }
        if(lookahead.matches("[a-zA-Z_0-9]+"))
        {
            System.out.println("Matched : "+lookahead);
            
            next();
        
        }

    }

    private void next() {

          lookahead=  getToken();
         
          if(lookahead==null)
           {
           
               System.out.println("Program Parsed");
           
           }
    }
    private String getToken() 
    { 
        String token ="";
        
        try{
           token = lexer.startLex(); 
          }
          catch(Exception exc)
          {
              System.out.println("No token Left");
          
          }
        
  
        return token;
    
    
    }

    private void singleTypeImportDeclaration() {

        if (lookahead == null) {
            return;
        }
    
        if(lookahead.equals("import"))
        {
            matcher("import"); identifier(); optionalPoint(); optionalSterik(); matcher(";");
        
        
        
        }
        
        try
        {
              if (lookahead == null) {
            return;
        }
        
            if(lookahead.equals("import")){singleTypeImportDeclaration();}
      
        }
        catch(NullPointerException exc)
        {
        
        
        }
    
    
    }

    private void optionalPoint() {

        if(lookahead.equals("."))
        {
            matcher("."); identifier(); optionalPoint();
        }
    
    }

    private void optionalSterik() {

    
       if(lookahead.equals("*"))
       {
           matcher("*");
       
       }
    
    }

    private void classDeclaration() {
   
        normalClassDeclaration();
        
    
    }

    private void normalClassDeclaration() {

        optionalModifier(); matcher("class"); identifier(); optionalTypeParameters(); optionalSuperClass(); optionalSuperInterface(); classBody();
    
    }

    private void optionalModifier() {

        if(lookahead.equals("public")){
    
            matcher("public");
        
        }
     
        else if(lookahead.equals("protected")){
        
            matcher("protected");
        }
        else if(lookahead.equals("private"))    {
        
            matcher("private");
        }
         
        else if (lookahead.equals("abstract")){
        
            matcher("abstract");
        }
               
        else if (lookahead.equals("static")){
         
            matcher("static");
        }
        else if (lookahead.equals("final")){
        
        matcher("final");
        }
        
        else if (lookahead.equals("stricfp")){
        
            matcher("strictfp");
        }
    }

    private void optionalTypeParameters() {

        if(lookahead.equals("<")){
        matcher("<"); typeParametersList(); matcher(">");
        }
    }

    private void typeParametersList() {

    
        typeParameters(); optionalComma(); OptionalTypeParameters();
    
    }

    private void typeParameters() {

        if(lookahead.equals("E"))
        {
            matcher("E");
        
        }
        
        else if(lookahead.equals("K"))
        {
            matcher("K");
        
        }
         
        else if(lookahead.equals("N"))
        {
            matcher("N");
        
        }
          
        else if(lookahead.equals("T"))
        {
            matcher("T");
        
        }
           
         else if(lookahead.equals("V"))
        {
            matcher("V");
        
        }
            
        
              
        
        
    
        
    
    }

    private void optionalComma() {
       
        if(lookahead.equals(","))
        {
        
            matcher(",");
        }
        
    }

    private void OptionalTypeParameters() {

    
        typeParameters();
    
    }

    private void optionalSuperClass() {

        if(lookahead.equals("extends"))
        {
            matcher("extends"); identifier(); optionalTypeParameters();
        
        }
    
    
    
    }

    private void optionalSuperInterface() {

    
        if(lookahead.equals("implements"))
        {
            matcher("implements"); identifier(); optionalMultiInterfaces();
        
        }
    
    }

    private void optionalMultiInterfaces() {

        if(lookahead.equals(","))
        {
            matcher(",");identifier(); optionalMultiInterfaces();
        
        }
    
    
    }

    private void classBody() {

        
        matcher("{"); optionalClassBodyDeclaration(); matcher("}");
        
        
    }

    private void optionalClassBodyDeclaration() {

         constructorDeclaration();
        classMemberDeclaration();
 
     
    
    }

    private void classMemberDeclaration() {

        
        fieldDeclaration();
         
       MethodDeclaration();
   
        
        
    }

   

    private void constructorDeclaration() {

        optionalConstModifier(); constHeader(); constBody();
        
    }

    private void fieldDeclaration() {

     
    
        optionalFieldModifier(); optionalStaticOrFinal();optionalStaticOrFinal(); fieldType(); if(lookahead.equals("(")) {return;}   variableDeclaratorList(); if(lookahead.equals(";")){againCheckField=true;}matcher(";");
    
        if(againCheckField){                                                                                                                                                                                                                                                                                                        
          
            againCheckField = false;
      
            fieldDeclaration();
        
        }
        
    }

    private void optionalFieldModifier() {

     
          if(lookahead.equals("public")){
    
            matcher("public");
        
        }
     
        else if(lookahead.equals("protected")){
        
            matcher("protected");
        }
        else if(lookahead.equals("private"))    {
        
            matcher("private");
        }
         
        else if (lookahead.equals("static")){
        
            matcher("static");
        }
               
        else if (lookahead.equals("transient")){
         
            matcher("transient");
        }
        else if (lookahead.equals("final")){
        
        matcher("final");
        }
        
        else if (lookahead.equals("volatile")){
        
            matcher("volatile");
        }
    
    }

    private void optionalStaticOrFinal() {

    
     if(lookahead.equals("static")){
    
            matcher("static");
        
        }
     
        else if(lookahead.equals("final")){
        
            matcher("final");
        }
    
    }

    private void fieldType() {

      
         if(lookahead.equals("long")){
    
            matcher("long");
        
        }
     
        else if(lookahead.equals("int")){
        
            matcher("int");
        }
        else if(lookahead.equals("short"))    {
        
            matcher("short");
        }
         
        else if (lookahead.equals("byte")){
        
            matcher("byte");
        }
               
        else if (lookahead.equals("double")){
         
            matcher("double");
        }
        else if (lookahead.equals("float")){
        
        matcher("float");
        }
        
        else if (lookahead.equals("char")){
        
            matcher("char");
        }
         
          
        else if (lookahead.equals("boolean")){
        
            matcher("boolean");
        }
         
         else if (lookahead.equals("String")){
        
            matcher("String");
        }
    
    }

    private void variableDeclaratorList() {

       
        variableDeclarator(); optionalVariableList();
        
    }

    private void variableDeclarator() {

      
        variableDeclaratorId(); optionalInitilization();
    
    }

    private void optionalVariableList() {

    if(lookahead.equals(","))
    {
        optionalComma(); variableDeclarator();
    
    }
        
    
    }

    private void variableDeclaratorId() {

 
        identifier(); optionalBrackets();
        
    
    }

    private void optionalInitilization() {

        if(lookahead.equals("="))
        {
            matcher("="); variableInitilization();
        
        }
    
    
    }

    private void optionalBrackets() {

        if(lookahead.equals("["))
        {
            
            matcher("["); matcher("]");
        
        
        }
    
    
    }

    private void digits() {

       if(lookahead.matches("[0-9]+"))
       {
           next();
       
       }
    
    }

    private void variableInitilization() {

        if(lookahead.equals("new"))
        {
           
            matcher("new");arrayInitilization(); optionalObjectInialization();
        
        
        }
        else{
        
        expression();
    
        }    
    }

    private void expression() {

     
        StringExpression();
        Expr();
 
    
    }

    private void arrayInitilization() {

    
        fieldType(); matcher("["); digits(); matcher("]");
        
    
    
    }
    
    void Expr()
    {
     
        
        Term(); ExprRest();
    
    }

    private void Term() {
        
        Factor(); TermRest();
        
    }

    private void ExprRest() {
      
        
        if(lookahead.equals("+") )
        {   
        
                matcher("+"); Term();ExprRest();//Syntax
        }
        
         else if(lookahead.equals("-"))
        {  
    
                 matcher("-"); Term(); ExprRest();
        }
        else
        {
       
        
        }
        
    }
    
    
    

    
   

    private void TermRest() {
      
        if(lookahead.equals("*"))
        {  
      
           
            
             matcher("*");
      
            Factor(); ;TermRest();
      
             
        }
        
        
        if(lookahead.equals("/"))
      
        {   
            
         matcher("/");
      
          Factor(); TermRest();
      
          
  
        }
        
  
        
        else
        {  
        
        }
    }
    
    private void Factor()
    {  
       Base(); FactorRest();
    
    }
    
    
    
    private void FactorRest()
    {
    
        if(lookahead.equals("^")){
         
        

             
             matcher("^"); Base(); FactorRest();
             
        }
   
        
    
    }
    


    private void Base() {
       
     
       if(lookahead.matches("[0-9]+") )
         {
              
        Number();
        
         }
    
       else if(!(reserveWords(lookahead)) && lookahead.matches("[A-Za-z_0-9]+"))
       {
          
           identifier();
       
       }
        
      else   if(lookahead.equals("("))
        {
           
           matcher("("); Expr();  matcher(")"); 
        
        }
         
  
    }

    private void Number() {
    {
      
        if(lookahead.contains("[+-]"))
        {
        
          Opt_Sign();
        
        }
          
      else  if(lookahead.matches("[0-9]+")){   matchDigit(lookahead);} 
      else if(!(reserveWords(lookahead)) && lookahead.matches("[A-Za-z_0-9]+"))
       {
          
           identifier();
       
       }
    NumberRest();
  
      
    }

 
    }

    private void Opt_Sign() {

        if(lookahead.equals("+"))
        {
         
        
             matcher("+");
        
        }
        
        else if(lookahead.equals("-"))
        {
       
            matcher("-");
        
        }
        
    }

    private void NumberRest()
    {

        

      Opt_Point(); 
      if(lookahead.matches("[0-9]+"))
      {  
          
      
      }
        else if(!(reserveWords(lookahead)) && lookahead.matches("[A-Za-z_0-9]+"))
       {
        
           identifier();
       
       }
     
     if(lookahead.matches("[0-9]+")){NumberRest();}
     else if(!(reserveWords(lookahead)) &&lookahead.matches("[A-Za-z_0-9]+"))
       {
                
           identifier();
        
       
           NumberRest();
       }
    }

    private void Opt_Point() {

    
        if(lookahead.equals("."))
        {
            
            
             matchDigit(".");
        
        }
    
    
    }

    private void MethodDeclaration() {

     
         optionalMethodModifier(); optionalStaticOrFinal();optionalStaticOrFinal(); methodHeader();  optionalSemiColon();methodBody();
         if(checkMethodAgain())
         {
          MethodDeclaration();
         
         }
    
    }

    private void optionalMethodModifier() {

        if(lookahead.equals("public")){
    
            matcher("public");
        
        }
     
        else if(lookahead.equals("protected")){
        
            matcher("protected");
        }
        else if(lookahead.equals("private"))    {
        
            matcher("private");
        }
         
        else if (lookahead.equals("static")){
        
            matcher("static");
        }
               
        else if (lookahead.equals("synchronized")){
         
            matcher("synchronized");
        }
        else if (lookahead.equals("native")){
        
        matcher("native");
        }
        
        else if (lookahead.equals("strictfp")){
        
            matcher("strictfp");
        }
        
         else if (lookahead.equals("abstract")){
        
            matcher("abstract");
        }
    

    
    
    
    }

    private void methodReturnType() {
      
        if(lookahead.equals("long")){
    
            matcher("long");
        
        }
     
        else if(lookahead.equals("int")){
        
            matcher("int");
        }
        else if(lookahead.equals("short"))    {
        
            matcher("short");
        }
         
        else if (lookahead.equals("byte")){
        
            matcher("byte");
        }
               
        else if (lookahead.equals("double")){
         
            matcher("double");
        }
        else if (lookahead.equals("float")){
        
        matcher("float");
        }
        
        else if (lookahead.equals("char")){
        
            matcher("char");
        }
         
          
        else if (lookahead.equals("boolean")){
        
            matcher("boolean");
        }
         
         else if (lookahead.equals("String")){
        
            matcher("String");
         }        
        
          else if (lookahead.equals("void")){
        
            matcher("void");
         }        
    }

    private void optionalFormalParameter() {
    
        fieldType(); identifier(); optionalBrackets(); if(lookahead.equals(",")){ matcher(","); optionalFormalParameter();}
    }

    private void methodHeader() {
       methodReturnType();  methodDeclarator();   
       
       
       
        

    }

    private void methodDeclarator() {
       
        identifier(); matcher("("); optionalFormalParameter(); matcher(")");
        
    }

    private void methodBody() {
 
        matcher("{");
        
        blockStatements();
        
        
        
        
        matcher("}");
    
    
    }

    private void optionalSemiColon() {

    
        if(lookahead.equals(";"))
    
            matcher(";");
    }

    private void blockStatements() {

      
     
                    printStatement();
        
      

        localVariableDeclaration();
        objectCreationDeclaration();
        
        objectDereference();
        statements();
       
           if(!(lookahead.equals("}")) && checkCount >0 )
    {
        checkCount--;
     
       classMemberDeclaration();
    }
    
    
    
    
    }

    private void localVariableDeclaration() {

    optionalStaticOrFinal();optionalStaticOrFinal(); fieldType(); if(reserveWords(lookahead)) return;  variableDeclaratorList(); if(lookahead.equals(";")){againCheckField=true;}matcher(";");
    
        if(againCheckField){                                                                                                                                                                                                                                                                                                        
          
            againCheckField = false;
      
             localVariableDeclaration() ;
        
        }
        
    
    }

    private void statements() {

     
      

        IfThenStatement();
        IfThenElseStatment();
        doWhileStatement();
        whileStatement();
        forStatement();
        switchStatement();
        returnStatemnet();
    
    
    }



    private void IfThenStatement() {

    
           if(lookahead.equals("if")){
         
               matcher("if"); matcher("(");Expr();comparison();Expr();matcher(")"); optionalStartingBracket(); blockStatements(); optionalClosingBracket();
           }
      
    
    
    }

    private void comparison() {

        if(lookahead.equals("<"))
        {
            matcher("<");
        
        }
       else if(lookahead.equals("!"))
        {
            matcher("!");
        
        }
       else if(lookahead.equals(">"))
        {
            matcher(">");
        
            
        }
        
           else if(lookahead.equals(">"))
        {
            matcher(">");
        
            
        }
        
           else if (lookahead.equals(">="))
           {
           
               matcher(">=");
           
           }
        
           else if (lookahead.equals("<="))
           {
           
               matcher("<=");
           
           }
           else if (lookahead.equals("!="))
           {
           
               matcher("!=");
           
           }
    
    
    }

    private void equality() {

        if(lookahead.equals("="))
        {
            matcher("=");
        }    
    }

    private boolean reserveWords(String lookahead) {

        if(lookahead.equals("if"))
        {
        
            return true;
        
        }
        
        if(lookahead.equals("void"))
        {
        
            return  true;
        }
        
        if(lookahead.equals("for"))
        {
         return  true;
        
        }
        
          if(lookahead.equals("auto")){
         return  true;
        
        }

       if(lookahead.equals("break")){
         return  true;
        
        }

        if(lookahead.equals("case")){
         return  true;
        
        }

       if(lookahead.equals("char")){
         return  true;
        
        }

       if(lookahead.equals("const")){
         return  true;
        
        }

        
        
       if(lookahead.equals("continue")){
         return  true;
        
        }

       if(lookahead.equals("default")){
         return  true;
        
        }

       if(lookahead.equals("do")){
         return  true;
        
        }

      if(lookahead.equals("double")){
         return  true;
        
        }

        

      if(lookahead.equals("else")){
         return  true;
        
        }

      if(lookahead.equals("enum")){
         return  true;
        
        }

      if(lookahead.equals("extern")){
         return  true;
        
        }

        if(lookahead.equals("float")){
         return  true;
        
        }

      if(lookahead.equals("for")){
         return  true;
        
        }

        if(lookahead.equals("goto")){
         return  true;
        
        }

      if(lookahead.equals("if")){
         return  true;
        
        }

       if(lookahead.equals("int")){
         return  true;
        
        }

        if(lookahead.equals("long")){
         return  true;
        
        }

        

        if(lookahead.equals("register")){
         return  true;
        
        }

        if(lookahead.equals("return")){
         return  true;
        
        }

        if(lookahead.equals("short")){
         return  true;
        
        }

        if(lookahead.equals("signed")){
         return  true;
        
        }

        if(lookahead.equals("sizeof")){
         return  true;
        
        }

        if(lookahead.equals("sizeof")){
         return  true;
        
        }

        if(lookahead.equals("struct")){
         return  true;
        
        }

        if(lookahead.equals("switch")){
         return  true;
        
        }

        if(lookahead.equals("typedef")){
         return  true;
        
        }

        

        if(lookahead.equals("union")){
         return  true;
        
        }

        if(lookahead.equals("unsigned")){
         return  true;
        
        }

        if(lookahead.equals("void")){
         return  true;
        
        }

        if(lookahead.equals("volatile")){
         return  true;
        
        }

        if(lookahead.equals("while")){
         return  true;
        
        }

        if(lookahead.equals("_Packed")){
         return  true;
        
        }
        
        if(lookahead.equals("package")){
         return  true;
        
        }
    
        if(lookahead.equals("public")){
         return  true;
        
        }
         
        if(lookahead.equals("class")){
         return  true;
        
        }
        
        if(lookahead.equals("String")){
         return  true;
        
        }
     
        if(lookahead.equals("static")){
         return  true;
        
        }
        
        if(lookahead.equals("import")){
         return  true;
        
        }
        
        if(lookahead.equals("System"))
        {
            return true;
        
        }
        
       
        
      return false;
    
    }

    private void IfThenElseStatment() {

    
        if(lookahead.equals("else"))
        {
         
            matcher("else"); optionalStartingBracket(); blockStatements(); optionalClosingBracket();
            
        
        }
    
    
    }


    private void optionalStartingBracket() {

    
        if(lookahead.equals("{"))
        {
        
            matcher("{");
        }
    
    }

    private void optionalClosingBracket() {
      
           if(lookahead.equals("}"))
        {
        
            matcher("}");
        }
    
        
    }

    private void whileStatement() {

    
        if(lookahead.equals("while"))
        {
        
            matcher("while"); matcher("(");Expr();comparison();Expr(); optionalTrueOrFalse();matcher(")"); optionalStartingBracket(); blockStatements(); optionalClosingBracket();
        
        }
    
    
    }

    private void forStatement() {

        if(lookahead.equals("for"))
        {
        
            matcher("for"); matcher("("); optionalForInt();matcher(";"); optionalExpr();  matcher(";");optionalIncrement();matcher(")");optionalStartingBracket(); blockStatements(); optionalClosingBracket();
        
        }
    
    
    }

    private void optionalForInt() {

    
        if(lookahead.equals("int"))
        {
            
            matcher("int"); identifier(); matcher("="); expression();
        
        }
        else
        {
        
            identifier(); matcher("="); expression();
        }
    
    }

    private void optionalExpr() {
     
        
        identifier(); comparison(); expression();
        
    }

    private void optionalIncrement() {

        
        if(lookahead.equals("++") || lookahead.equals("--"))
        {
        
            identifier();
        
        }
    
        else
        {
        
            identifier();matcher("++"); matcher("--");
        
        }
    
    }

    private void matchDigit(String chr) {

          
        
        if((lookahead.equals( chr)) && index <digits.length)
          {
             
          
              lookahead=""+digits[index++];
           
          }
        else if (index == digits.length){
        
            lookahead =getToken();
             if(lookahead.matches("[0-9.]+"))
             {
                 index=1;
                 digits = lookahead.toCharArray();
         
                 
             
                 lookahead=""+digits[0];
             
                
            
                 matchDigit("");
             
             }
             
        
            
        }
        
    
    }

    private void optionalTrueOrFalse() {

    
        if(lookahead.equals("true"))
        {
            matcher("true");
        
        }
        
        if(lookahead.equals("false"))
        {
            matcher("false");
        
        }
    
    
    }

    private void optionalConstModifier() {

    
        if(lookahead.equals("public"))
        {
            matcher("public");
        
        }
    
    }

    private void constHeader() {
     
            identifier(); matcher("("); optionalFormalParameter(); matcher(")");
        

            
    
    }

    private void constBody() {

    
        matcher("{"); blockStatements(); matcher("}");
    
    }

    private void objectCreationDeclaration() {

  
        if(!(reserveWords(lookahead)))
       {
             
           identifier(); identifier(); matcher("="); matcher("new");optionalObjectInialization();matcher(";");
       
       }
    
    }

    private void optionalObjectInialization() {

    
       identifier();  matcher("(");  identifier(); if(lookahead.equals(",")){ matcher(",");identifier();} matcher(")"); 
    
    }

    private void doWhileStatement() {

       
        if(lookahead.equals("do"))
        {
        
        
            matcher("do");matcher("{"); blockStatements(); matcher("}"); matcher("while");matcher("(");expression();comparison();expression(); optionalTrueOrFalse();matcher(")"); matcher(";");
        
        }
    
    
    }

    private void switchStatement() {

    
        if(lookahead.equals("switch"))
        {
            matcher("switch"); matcher("("); identifier();matcher(")"); matcher("{"); switctBody(); matcher("}");
        
        }
    
    }

    private void switctBody() {
       

        matcher("case"); identifier(); matcher(":");matcher("{");blockStatements();  matcher("break");matcher(";");matcher("}");

        if(lookahead.equals("case"))
        {
        switctBody();
        }
        
        if(lookahead.equals("default"))
        {
            matcher("default"); matcher(":");matcher("{");blockStatements();  matcher("break");matcher(";");matcher("}");
        
        }
        
    }

    private void StringExpression() {

      
        
        if(!(reserveWords(lookahead)) && lookahead.matches("[A-Za-z_0-9 ]+"))
       {
           
          next();
       
       }
    
    
    }

    private void printStatement() {

      
        matcher("System"); matcher(".");matcher("out"); matcher(".");matcher("println");matcher("("); oprionalParametrs();matcher(")"); matcher(";");
    
    
    }

    private void oprionalParametrs() {

    
 
           expression();
    
    }

    private void objectDereference() {

     
    
        if(!reserveWords(lookahead))
        {
            identifier();matcher("."); identifier(); matcher("(");identifier(); if(lookahead.equals(",")){ matcher(",");identifier();} matcher("="); expression(); matcher(")");
        
        }
    
    
    }

    private void returnStatemnet() {
      
         if(lookahead==null){return;}
        
        if(lookahead.equals("return"))
        {
        
            matcher("return"); expression(); optionalTrueOrFalse(); matcher(";");
        }
        

    }

    private boolean checkMethodAgain() {
       
         if(lookahead.equals("public")){
    
            return true;
        
        }
     
        else if(lookahead.equals("protected")){
        
             return true;
        }
        else if(lookahead.equals("private"))    {
        
             return true;
        }
         
        else if (lookahead.equals("static")){
        
             return true;
        }
               
        else if (lookahead.equals("synchronized")){
         
            return true;
        }
        else if (lookahead.equals("native")){
        
         return true; 
        }
        
        else if (lookahead.equals("strictfp")){
        
             return true;
        }
        
         else if (lookahead.equals("abstract")){
        
            return true;
        }
        
        
        
         return false;
    
    }
}
