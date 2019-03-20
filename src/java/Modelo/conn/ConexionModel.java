/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.conn;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author JhanxD
 */
public class ConexionModel {
    private static ConexionModel conexion; 
    private EntityManagerFactory Pool;
    public ConexionModel (){
        Pool = Persistence.createEntityManagerFactory("Proyecto_Taller_PersistenciaPU");
    }
    public static ConexionModel getConexion (){
        if (conexion==null) {
            conexion = new ConexionModel();
        }
        return conexion;
    }

    public EntityManagerFactory getPool() {
        return Pool;
    }
    
    
}
