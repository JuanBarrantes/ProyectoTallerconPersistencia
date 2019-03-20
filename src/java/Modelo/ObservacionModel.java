/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Beans.Tesis;
import Beans.Jurado;
import Beans.Observacion;
import Modelo.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JhanxD
 */
public class ObservacionModel implements Serializable {

    public ObservacionModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Observacion observacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tesis idTesis = observacion.getIdTesis();
            if (idTesis != null) {
                idTesis = em.getReference(idTesis.getClass(), idTesis.getIdTesis());
                observacion.setIdTesis(idTesis);
            }
            Jurado idJurado = observacion.getIdJurado();
            if (idJurado != null) {
                idJurado = em.getReference(idJurado.getClass(), idJurado.getRelacion());
                observacion.setIdJurado(idJurado);
            }
            em.persist(observacion);
            if (idTesis != null) {
                idTesis.getObservacionList().add(observacion);
                idTesis = em.merge(idTesis);
            }
            if (idJurado != null) {
                idJurado.getObservacionList().add(observacion);
                idJurado = em.merge(idJurado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Observacion observacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Observacion persistentObservacion = em.find(Observacion.class, observacion.getIdObservacion());
            Tesis idTesisOld = persistentObservacion.getIdTesis();
            Tesis idTesisNew = observacion.getIdTesis();
            Jurado idJuradoOld = persistentObservacion.getIdJurado();
            Jurado idJuradoNew = observacion.getIdJurado();
            if (idTesisNew != null) {
                idTesisNew = em.getReference(idTesisNew.getClass(), idTesisNew.getIdTesis());
                observacion.setIdTesis(idTesisNew);
            }
            if (idJuradoNew != null) {
                idJuradoNew = em.getReference(idJuradoNew.getClass(), idJuradoNew.getRelacion());
                observacion.setIdJurado(idJuradoNew);
            }
            observacion = em.merge(observacion);
            if (idTesisOld != null && !idTesisOld.equals(idTesisNew)) {
                idTesisOld.getObservacionList().remove(observacion);
                idTesisOld = em.merge(idTesisOld);
            }
            if (idTesisNew != null && !idTesisNew.equals(idTesisOld)) {
                idTesisNew.getObservacionList().add(observacion);
                idTesisNew = em.merge(idTesisNew);
            }
            if (idJuradoOld != null && !idJuradoOld.equals(idJuradoNew)) {
                idJuradoOld.getObservacionList().remove(observacion);
                idJuradoOld = em.merge(idJuradoOld);
            }
            if (idJuradoNew != null && !idJuradoNew.equals(idJuradoOld)) {
                idJuradoNew.getObservacionList().add(observacion);
                idJuradoNew = em.merge(idJuradoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = observacion.getIdObservacion();
                if (findObservacion(id) == null) {
                    throw new NonexistentEntityException("The observacion with id " + id + " no longer exists.");
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
            Observacion observacion;
            try {
                observacion = em.getReference(Observacion.class, id);
                observacion.getIdObservacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The observacion with id " + id + " no longer exists.", enfe);
            }
            Tesis idTesis = observacion.getIdTesis();
            if (idTesis != null) {
                idTesis.getObservacionList().remove(observacion);
                idTesis = em.merge(idTesis);
            }
            Jurado idJurado = observacion.getIdJurado();
            if (idJurado != null) {
                idJurado.getObservacionList().remove(observacion);
                idJurado = em.merge(idJurado);
            }
            em.remove(observacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Observacion> findObservacionEntities() {
        return findObservacionEntities(true, -1, -1);
    }

    public List<Observacion> findObservacionEntities(int maxResults, int firstResult) {
        return findObservacionEntities(false, maxResults, firstResult);
    }

    private List<Observacion> findObservacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Observacion.class));
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

    public Observacion findObservacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Observacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getObservacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Observacion> rt = cq.from(Observacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
