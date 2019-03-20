/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Extras;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author JhanxD
 */
public class fechas {
    public static void main(String[] args) {
        String[] meses= {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        Calendar c1 = Calendar.getInstance();
        String dia=Integer.toString(c1.get(Calendar.DATE));
        String mes=meses[c1.get(Calendar.MONTH)];
        String mes1=Integer.toString(c1.get(Calendar.MONTH)+1);
        String año =Integer.toString(c1.get(Calendar.YEAR));
        String Formato1=dia+" de "+mes+" de "+año;
        System.out.println(dia+" de "+mes+" de "+año);
        String dia2="", mes2="";
        if (dia.length()==1) {
           dia2="0"+dia;
        }
        if (mes1.length()==1) {
            mes2="0"+mes1;
        }
        String Formato2=dia2+"/"+mes2+"/"+año;
        System.out.println(dia2+"/"+mes2+"/"+año);
    }
}
