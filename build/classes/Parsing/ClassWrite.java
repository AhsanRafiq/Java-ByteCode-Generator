/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Parsing;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V1_7;
import static org.objectweb.asm.Opcodes.V1_8;

/**
 *
 * @author Ahsan Rafiq
 */
public class ClassWrite {
    
    
    public ClassWrite(){
        
        ClassWriter cw = new ClassWriter(1);

        cw.visit(V1_8, ACC_PUBLIC , "Parsing/SourceProgram", null, "java/lang/Object",null);

        cw.visitField(ACC_PUBLIC , "number", "I",null, null).visitEnd();

     
        cw.visitMethod(ACC_PUBLIC , "secondMethod","([java/lang/String;)B", null, null).visitEnd();
      

       cw.visitMethod(ACC_PUBLIC+Opcodes.ACC_STATIC , "main","([java/lang/String;)V", null, null).visitEnd();
     
        cw.visitMethod(ACC_PUBLIC , "firstMethod","(I)V", null, null).visitEnd();

        cw.visitEnd();

        byte[] b = cw.toByteArray();
        
        for (byte c : b) {
            
            System.out.print((char)c);
        }
        System.out.println();
   
    }
    
    
}