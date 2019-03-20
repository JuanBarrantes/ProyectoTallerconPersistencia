/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Extras;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JhanxD
 */
public class claves {
    public static void main(String[] args) {
        String clave, re, usuario, contra;
        String key = "92AE31A79FEEB2A3"; //llave-parametro 1 
        String iv = "0123456789ABCDEF"; // vector de inicialización parametro 2
        re="/p13lEyRrBO/m0asqHo3jA==";
        usuario="llaque";
        try {
            clave=StringEncrypt.decrypt(key, iv, re);
            System.out.println(clave);
            contra=StringEncrypt.encrypt(key, iv, usuario);
            System.out.println("nueva contraseña " + contra);
        } catch (Exception ex) {
            Logger.getLogger(claves.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
