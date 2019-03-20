/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Extras;

import Beans.Constancia;
import Beans.Jurado;
import Beans.Observacion;
import Beans.Tesis;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author JhanxD
 */
public class VistaModel implements Serializable {
    
    public VistaModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public List<Tesis> ordenar(String nombre) {
        EntityManager em = getEntityManager();
        List<Tesis> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT f FROM Tesis f JOIN f.codigoC co WHERE co.codigoTesis LIKE CONCAT('%',?1,'%')";
            Query q = em.createQuery(jpql);
            q.setParameter(1, nombre);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }
    
    public List<Constancia> datos0(int codigo) {
        EntityManager em = getEntityManager();
        List<Constancia> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT f FROM Constancia f WHERE f.idConstancia = ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, codigo);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }
    
    public List<Jurado> datos1(int codigo) {
        EntityManager em = getEntityManager();
        List<Jurado> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT f FROM Jurado f JOIN f.idTesis tt JOIN tt.codigoC cc WHERE cc.idConstancia = ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, codigo);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }

    public Date datos2(int codigo) {
        EntityManager em = getEntityManager();
        List<Observacion> lista=new ArrayList<>();
        Date fecha=null;
        try {
            em = getEntityManager();
            String jpql="SELECT f FROM Observacion f JOIN f.idTesis tt JOIN tt.codigoC cc WHERE cc.idConstancia = ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, codigo);
            lista = q.setMaxResults(1).getResultList();
            fecha = lista.get(0).getFechaFin();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return fecha;
    }
    
    
    
}
