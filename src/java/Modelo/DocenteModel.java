/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Beans.Docente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Beans.Escuela;
import Beans.Jurado;
import java.util.ArrayList;
import java.util.List;
import Beans.LineaDocente;
import Beans.LineaInvestigacion;
import Modelo.exceptions.IllegalOrphanException;
import Modelo.exceptions.NonexistentEntityException;
import java.util.LinkedList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JhanxD
 */
public class DocenteModel implements Serializable {

    public DocenteModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Docente docente) {
        if (docente.getJuradoList() == null) {
            docente.setJuradoList(new ArrayList<Jurado>());
        }
        if (docente.getLineaDocenteList() == null) {
            docente.setLineaDocenteList(new ArrayList<LineaDocente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Escuela escuela = docente.getEscuela();
            if (escuela != null) {
                escuela = em.getReference(escuela.getClass(), escuela.getIdEscuela());
                docente.setEscuela(escuela);
            }
            List<Jurado> attachedJuradoList = new ArrayList<Jurado>();
            for (Jurado juradoListJuradoToAttach : docente.getJuradoList()) {
                juradoListJuradoToAttach = em.getReference(juradoListJuradoToAttach.getClass(), juradoListJuradoToAttach.getRelacion());
                attachedJuradoList.add(juradoListJuradoToAttach);
            }
            docente.setJuradoList(attachedJuradoList);
            List<LineaDocente> attachedLineaDocenteList = new ArrayList<LineaDocente>();
            for (LineaDocente lineaDocenteListLineaDocenteToAttach : docente.getLineaDocenteList()) {
                lineaDocenteListLineaDocenteToAttach = em.getReference(lineaDocenteListLineaDocenteToAttach.getClass(), lineaDocenteListLineaDocenteToAttach.getIdRelacion());
                attachedLineaDocenteList.add(lineaDocenteListLineaDocenteToAttach);
            }
            docente.setLineaDocenteList(attachedLineaDocenteList);
            em.persist(docente);
            if (escuela != null) {
                escuela.getDocenteList().add(docente);
                escuela = em.merge(escuela);
            }
            for (Jurado juradoListJurado : docente.getJuradoList()) {
                Docente oldIdDocenteOfJuradoListJurado = juradoListJurado.getIdDocente();
                juradoListJurado.setIdDocente(docente);
                juradoListJurado = em.merge(juradoListJurado);
                if (oldIdDocenteOfJuradoListJurado != null) {
                    oldIdDocenteOfJuradoListJurado.getJuradoList().remove(juradoListJurado);
                    oldIdDocenteOfJuradoListJurado = em.merge(oldIdDocenteOfJuradoListJurado);
                }
            }
            for (LineaDocente lineaDocenteListLineaDocente : docente.getLineaDocenteList()) {
                Docente oldDocenteIdOfLineaDocenteListLineaDocente = lineaDocenteListLineaDocente.getDocenteId();
                lineaDocenteListLineaDocente.setDocenteId(docente);
                lineaDocenteListLineaDocente = em.merge(lineaDocenteListLineaDocente);
                if (oldDocenteIdOfLineaDocenteListLineaDocente != null) {
                    oldDocenteIdOfLineaDocenteListLineaDocente.getLineaDocenteList().remove(lineaDocenteListLineaDocente);
                    oldDocenteIdOfLineaDocenteListLineaDocente = em.merge(oldDocenteIdOfLineaDocenteListLineaDocente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Docente docente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Docente persistentDocente = em.find(Docente.class, docente.getIdDocente());
            Escuela escuelaOld = persistentDocente.getEscuela();
            Escuela escuelaNew = docente.getEscuela();
            List<Jurado> juradoListOld = persistentDocente.getJuradoList();
            List<Jurado> juradoListNew = docente.getJuradoList();
            List<LineaDocente> lineaDocenteListOld = persistentDocente.getLineaDocenteList();
            List<LineaDocente> lineaDocenteListNew = docente.getLineaDocenteList();
            lineaDocenteListOld = (lineaDocenteListOld==null)? new LinkedList<LineaDocente>(): lineaDocenteListOld;
            lineaDocenteListNew = (lineaDocenteListNew==null)? new LinkedList<LineaDocente>(): lineaDocenteListNew;
            juradoListOld = (juradoListOld==null)? new LinkedList<Jurado>(): juradoListOld;
            juradoListNew = (juradoListNew==null)? new LinkedList<Jurado>(): juradoListNew;
            List<String> illegalOrphanMessages = null;
            for (Jurado juradoListOldJurado : juradoListOld) {
                if (!juradoListNew.contains(juradoListOldJurado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jurado " + juradoListOldJurado + " since its idDocente field is not nullable.");
                }
            }
            for (LineaDocente lineaDocenteListOldLineaDocente : lineaDocenteListOld) {
                if (!lineaDocenteListNew.contains(lineaDocenteListOldLineaDocente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LineaDocente " + lineaDocenteListOldLineaDocente + " since its docenteId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (escuelaNew != null) {
                escuelaNew = em.getReference(escuelaNew.getClass(), escuelaNew.getIdEscuela());
                docente.setEscuela(escuelaNew);
            }
            List<Jurado> attachedJuradoListNew = new ArrayList<Jurado>();
            for (Jurado juradoListNewJuradoToAttach : juradoListNew) {
                juradoListNewJuradoToAttach = em.getReference(juradoListNewJuradoToAttach.getClass(), juradoListNewJuradoToAttach.getRelacion());
                attachedJuradoListNew.add(juradoListNewJuradoToAttach);
            }
            juradoListNew = attachedJuradoListNew;
            docente.setJuradoList(juradoListNew);
            List<LineaDocente> attachedLineaDocenteListNew = new ArrayList<LineaDocente>();
            for (LineaDocente lineaDocenteListNewLineaDocenteToAttach : lineaDocenteListNew) {
                lineaDocenteListNewLineaDocenteToAttach = em.getReference(lineaDocenteListNewLineaDocenteToAttach.getClass(), lineaDocenteListNewLineaDocenteToAttach.getIdRelacion());
                attachedLineaDocenteListNew.add(lineaDocenteListNewLineaDocenteToAttach);
            }
            lineaDocenteListNew = attachedLineaDocenteListNew;
            docente.setLineaDocenteList(lineaDocenteListNew);
            docente = em.merge(docente);
            if (escuelaOld != null && !escuelaOld.equals(escuelaNew)) {
                escuelaOld.getDocenteList().remove(docente);
                escuelaOld = em.merge(escuelaOld);
            }
            if (escuelaNew != null && !escuelaNew.equals(escuelaOld)) {
                escuelaNew.getDocenteList().add(docente);
                escuelaNew = em.merge(escuelaNew);
            }
            for (Jurado juradoListNewJurado : juradoListNew) {
                if (!juradoListOld.contains(juradoListNewJurado)) {
                    Docente oldIdDocenteOfJuradoListNewJurado = juradoListNewJurado.getIdDocente();
                    juradoListNewJurado.setIdDocente(docente);
                    juradoListNewJurado = em.merge(juradoListNewJurado);
                    if (oldIdDocenteOfJuradoListNewJurado != null && !oldIdDocenteOfJuradoListNewJurado.equals(docente)) {
                        oldIdDocenteOfJuradoListNewJurado.getJuradoList().remove(juradoListNewJurado);
                        oldIdDocenteOfJuradoListNewJurado = em.merge(oldIdDocenteOfJuradoListNewJurado);
                    }
                }
            }
            for (LineaDocente lineaDocenteListNewLineaDocente : lineaDocenteListNew) {
                if (!lineaDocenteListOld.contains(lineaDocenteListNewLineaDocente)) {
                    Docente oldDocenteIdOfLineaDocenteListNewLineaDocente = lineaDocenteListNewLineaDocente.getDocenteId();
                    lineaDocenteListNewLineaDocente.setDocenteId(docente);
                    lineaDocenteListNewLineaDocente = em.merge(lineaDocenteListNewLineaDocente);
                    if (oldDocenteIdOfLineaDocenteListNewLineaDocente != null && !oldDocenteIdOfLineaDocenteListNewLineaDocente.equals(docente)) {
                        oldDocenteIdOfLineaDocenteListNewLineaDocente.getLineaDocenteList().remove(lineaDocenteListNewLineaDocente);
                        oldDocenteIdOfLineaDocenteListNewLineaDocente = em.merge(oldDocenteIdOfLineaDocenteListNewLineaDocente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = docente.getIdDocente();
                if (findDocente(id) == null) {
                    throw new NonexistentEntityException("The docente with id " + id + " no longer exists.");
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
            Docente docente;
            try {
                docente = em.getReference(Docente.class, id);
                docente.getIdDocente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Jurado> juradoListOrphanCheck = docente.getJuradoList();
            for (Jurado juradoListOrphanCheckJurado : juradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Docente (" + docente + ") cannot be destroyed since the Jurado " + juradoListOrphanCheckJurado + " in its juradoList field has a non-nullable idDocente field.");
            }
            List<LineaDocente> lineaDocenteListOrphanCheck = docente.getLineaDocenteList();
            for (LineaDocente lineaDocenteListOrphanCheckLineaDocente : lineaDocenteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Docente (" + docente + ") cannot be destroyed since the LineaDocente " + lineaDocenteListOrphanCheckLineaDocente + " in its lineaDocenteList field has a non-nullable docenteId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Escuela escuela = docente.getEscuela();
            if (escuela != null) {
                escuela.getDocenteList().remove(docente);
                escuela = em.merge(escuela);
            }
            em.remove(docente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Docente> findDocenteEntities() {
        return findDocenteEntities(true, -1, -1);
    }

    public List<Docente> findDocenteEntities(int maxResults, int firstResult) {
        return findDocenteEntities(false, maxResults, firstResult);
    }

    private List<Docente> findDocenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Docente.class));
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

    public Docente findDocente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            System.out.println("asa "+em.find(Docente.class, id));
            return em.find(Docente.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Docente> rt = cq.from(Docente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Docente> findDocente2(String nombre) {
        EntityManager em = getEntityManager();
        List<Docente> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT d FROM Docente d WHERE (d.nombre LIKE CONCAT('%',?1,'%')) OR (d.aPaterno LIKE CONCAT('%',?2,'%')) or (d.aMaterno LIKE CONCAT('%',?3,'%'))";
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

    public List<Docente> findDocente3(Escuela escul) {
        EntityManager em = getEntityManager();
        List<Docente> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT d FROM Docente d WHERE d.escuela= ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, escul);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }
    
}
