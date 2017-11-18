/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LexicalAnalysis;

/**
 *
 * @author Ahsan Rafiq
 */
class Token {
    
    private String idString ;
    private String valueString;
    
    public  Token(String idString, String valueString)
    {
    
        this.idString = idString;
        
        this .valueString = valueString;
    
    }
    
    public  String getID()
    {
        return idString;
    
    }
    
    
    public  String getValue ()
    {
        return  valueString;
    
    }
    
    
    
    
}
