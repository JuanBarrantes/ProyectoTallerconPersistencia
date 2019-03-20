/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Beans.Alumno;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Beans.Escuela;
import Modelo.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JhanxD
 */
public class AlumnoModel implements Serializable {

    public AlumnoModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(Alumno alumno) {
        String res;
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Escuela escuela = alumno.getEscuela();
            if (escuela != null) {
                escuela = em.getReference(escuela.getClass(), escuela.getIdEscuela());
                alumno.setEscuela(escuela);
            }
            em.persist(alumno);
            if (escuela != null) {
                escuela.getAlumnoList().add(alumno);
                escuela = em.merge(escuela);
            }
            em.getTransaction().commit();
            res="Registrado";
            return res;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        
    }

    public void edit(Alumno alumno) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno persistentAlumno = em.find(Alumno.class, alumno.getIdAlumno());
            Escuela escuelaOld = persistentAlumno.getEscuela();
            Escuela escuelaNew = alumno.getEscuela();
            if (escuelaNew != null) {
                escuelaNew = em.getReference(escuelaNew.getClass(), escuelaNew.getIdEscuela());
                alumno.setEscuela(escuelaNew);
            }
            alumno = em.merge(alumno);
            if (escuelaOld != null && !escuelaOld.equals(escuelaNew)) {
                escuelaOld.getAlumnoList().remove(alumno);
                escuelaOld = em.merge(escuelaOld);
            }
            if (escuelaNew != null && !escuelaNew.equals(escuelaOld)) {
                escuelaNew.getAlumnoList().add(alumno);
                escuelaNew = em.merge(escuelaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alumno.getIdAlumno();
                if (findAlumno(id) == null) {
                    throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno alumno;
            try {
                alumno = em.getReference(Alumno.class, id);
                alumno.getIdAlumno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.", enfe);
            }
            Escuela escuela = alumno.getEscuela();
            if (escuela != null) {
                escuela.getAlumnoList().remove(alumno);
                escuela = em.merge(escuela);
            }
            em.remove(alumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alumno> findAlumnoEntities() {
        return findAlumnoEntities(true, -1, -1);
    }

    public List<Alumno> findAlumnoEntities(int maxResults, int firstResult) {
        return findAlumnoEntities(false, maxResults, firstResult);
    }

    private List<Alumno> findAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumno.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Alumno findAlumno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumno> rt = cq.from(Alumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Alumno> findAlumnoEntities2(String nombre, String paterno, String materno) {
        EntityManager em = getEntityManager();
        List<Alumno> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT l FROM Alumno l WHERE (l.nombre LIKE CONCAT('%',?1,'%')) "+
                    "AND (l.aPaterno LIKE CONCAT('%',?2,'%')) AND (l.aMaterno LIKE CONCAT('%',?3,'%'))";
            Query q = em.createQuery(jpql);
            q.setParameter(1, nombre);
            q.setParameter(2, paterno);
            q.setParameter(3, materno);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;

    }
    
    public List<Alumno> findAlumno2(String nombre) {
        EntityManager em = getEntityManager();
        List<Alumno> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT f FROM Alumno f WHERE (f.nombre LIKE CONCAT('%',?1,'%'))"+
                    "OR (f.aPaterno LIKE CONCAT('%',?2,'%')) OR (f.aMaterno LIKE CONCAT('%',?3,'%'))";
            Query q = em.createQuery(jpql);
            q.setParameter(1, nombre);
            q.setParameter(2, nombre);
            q.setParameter(3, nombre);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }
    
}
