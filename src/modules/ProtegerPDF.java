/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

/**
 *
 * @author Cruz Roja
 */
public class ProtegerPDF {
    public void ProtegerPDF(File rutaArchivo,String passAdmin,String passuser){
      final int KEY_LENGTH = 256;
        
        //Instancia a AccessPermission
        AccessPermission ap = new AccessPermission();        
        
        //Se limitan acciones que el usuario puede realizar
        ap.setCanAssembleDocument(false);
        ap.setCanExtractContent(false);
        ap.setCanExtractForAccessibility(false);
        ap.setCanFillInForm(false);
        ap.setCanModify(false);
        ap.setCanModifyAnnotations(false); 
        ap.setCanPrint(false);
        ap.setCanPrintDegraded(false);
        
        try ( PDDocument document = PDDocument.load(rutaArchivo)) {            
            // 123456: Contrasena de propietario para abrir el archivo 
            // con todos los permisos
            // abcdef: Contrasena de usuario para abrir el archivo pero con
            // restricciones
            StandardProtectionPolicy spp = 
                    new StandardProtectionPolicy(passAdmin, passuser, ap);
            //longitud de la clave de cifrado
            spp.setEncryptionKeyLength(KEY_LENGTH);
            //Se aplica la proteccion
            document.protect(spp);
            //se actualiza el archivo y se cierra
            document.save(new File(rutaArchivo.getAbsolutePath()));
            document.close();
            System.out.println("Fin");
        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
    }
}
