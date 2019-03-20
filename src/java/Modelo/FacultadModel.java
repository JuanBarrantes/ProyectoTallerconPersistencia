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
import Beans.Escuela;
import Beans.Facultad;
import Modelo.exceptions.IllegalOrphanException;
import Modelo.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JhanxD
 */
public class FacultadModel implements Serializable {

    public FacultadModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facultad facultad) {
        if (facultad.getEscuelaList() == null) {
            facultad.setEscuelaList(new ArrayList<Escuela>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Escuela> attachedEscuelaList = new ArrayList<Escuela>();
            for (Escuela escuelaListEscuelaToAttach : facultad.getEscuelaList()) {
                escuelaListEscuelaToAttach = em.getReference(escuelaListEscuelaToAttach.getClass(), escuelaListEscuelaToAttach.getIdEscuela());
                attachedEscuelaList.add(escuelaListEscuelaToAttach);
            }
            facultad.setEscuelaList(attachedEscuelaList);
            em.persist(facultad);
            for (Escuela escuelaListEscuela : facultad.getEscuelaList()) {
                Facultad oldFacultadOfEscuelaListEscuela = escuelaListEscuela.getFacultad();
                escuelaListEscuela.setFacultad(facultad);
                escuelaListEscuela = em.merge(escuelaListEscuela);
                if (oldFacultadOfEscuelaListEscuela != null) {
                    oldFacultadOfEscuelaListEscuela.getEscuelaList().remove(escuelaListEscuela);
                    oldFacultadOfEscuelaListEscuela = em.merge(oldFacultadOfEscuelaListEscuela);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Facultad facultad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facultad persistentFacultad = em.find(Facultad.class, facultad.getIdFacultad());
            List<Escuela> escuelaListOld = persistentFacultad.getEscuelaList();
            List<Escuela> escuelaListNew = facultad.getEscuelaList();
            List<String> illegalOrphanMessages = null;
            escuelaListOld = (escuelaListOld==null)? new LinkedList<Escuela>(): escuelaListOld;
            escuelaListNew = (escuelaListNew==null)? new LinkedList<Escuela>(): escuelaListNew;
            for (Escuela escuelaListOldEscuela : escuelaListOld) {
                if (!escuelaListNew.contains(escuelaListOldEscuela)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Escuela " + escuelaListOldEscuela + " since its facultad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Escuela> attachedEscuelaListNew = new ArrayList<Escuela>();
            for (Escuela escuelaListNewEscuelaToAttach : escuelaListNew) {
                escuelaListNewEscuelaToAttach = em.getReference(escuelaListNewEscuelaToAttach.getClass(), escuelaListNewEscuelaToAttach.getIdEscuela());
                attachedEscuelaListNew.add(escuelaListNewEscuelaToAttach);
            }
            escuelaListNew = attachedEscuelaListNew;
            facultad.setEscuelaList(escuelaListNew);
            facultad = em.merge(facultad);
            for (Escuela escuelaListNewEscuela : escuelaListNew) {
                if (!escuelaListOld.contains(escuelaListNewEscuela)) {
                    Facultad oldFacultadOfEscuelaListNewEscuela = escuelaListNewEscuela.getFacultad();
                    escuelaListNewEscuela.setFacultad(facultad);
                    escuelaListNewEscuela = em.merge(escuelaListNewEscuela);
                    if (oldFacultadOfEscuelaListNewEscuela != null && !oldFacultadOfEscuelaListNewEscuela.equals(facultad)) {
                        oldFacultadOfEscuelaListNewEscuela.getEscuelaList().remove(escuelaListNewEscuela);
                        oldFacultadOfEscuelaListNewEscuela = em.merge(oldFacultadOfEscuelaListNewEscuela);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facultad.getIdFacultad();
                if (findFacultad(id) == null) {
                    throw new NonexistentEntityException("The facultad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facultad facultad;
            try {
                facultad = em.getReference(Facultad.class, id);
                facultad.getIdFacultad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facultad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Escuela> escuelaListOrphanCheck = facultad.getEscuelaList();
            for (Escuela escuelaListOrphanCheckEscuela : escuelaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Facultad (" + facultad + ") cannot be destroyed since the Escuela " + escuelaListOrphanCheckEscuela + " in its escuelaList field has a non-nullable facultad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(facultad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Facultad> findFacultadEntities() {
        return findFacultadEntities(true, -1, -1);
    }

    public List<Facultad> findFacultadEntities(int maxResults, int firstResult) {
        return findFacultadEntities(false, maxResults, firstResult);
    }

    private List<Facultad> findFacultadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Facultad.class));
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

    public Facultad findFacultad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facultad.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Facultad> findFacultad2(String nombre) {
        EntityManager em = getEntityManager();
        List<Facultad> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT f FROM Facultad f WHERE f.nombre LIKE CONCAT('%',?1,'%')";
            Query q = em.createQuery(jpql);
            q.setParameter(1, nombre);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }

    public int getFacultadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Facultad> rt = cq.from(Facultad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
