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
import Beans.LineaDocente;
import Beans.LineaInvestigacion;
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
public class LineaInvestigacionModel implements Serializable {

    public LineaInvestigacionModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LineaInvestigacion lineaInvestigacion) {
        if (lineaInvestigacion.getLineaDocenteList() == null) {
            lineaInvestigacion.setLineaDocenteList(new ArrayList<LineaDocente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Escuela escuela = lineaInvestigacion.getEscuela();
            if (escuela != null) {
                escuela = em.getReference(escuela.getClass(), escuela.getIdEscuela());
                lineaInvestigacion.setEscuela(escuela);
            }
            List<LineaDocente> attachedLineaDocenteList = new ArrayList<LineaDocente>();
            for (LineaDocente lineaDocenteListLineaDocenteToAttach : lineaInvestigacion.getLineaDocenteList()) {
                lineaDocenteListLineaDocenteToAttach = em.getReference(lineaDocenteListLineaDocenteToAttach.getClass(), lineaDocenteListLineaDocenteToAttach.getIdRelacion());
                attachedLineaDocenteList.add(lineaDocenteListLineaDocenteToAttach);
            }
            lineaInvestigacion.setLineaDocenteList(attachedLineaDocenteList);
            em.persist(lineaInvestigacion);
            if (escuela != null) {
                escuela.getLineaInvestigacionList().add(lineaInvestigacion);
                escuela = em.merge(escuela);
            }
            for (LineaDocente lineaDocenteListLineaDocente : lineaInvestigacion.getLineaDocenteList()) {
                LineaInvestigacion oldLineaIdOfLineaDocenteListLineaDocente = lineaDocenteListLineaDocente.getLineaId();
                lineaDocenteListLineaDocente.setLineaId(lineaInvestigacion);
                lineaDocenteListLineaDocente = em.merge(lineaDocenteListLineaDocente);
                if (oldLineaIdOfLineaDocenteListLineaDocente != null) {
                    oldLineaIdOfLineaDocenteListLineaDocente.getLineaDocenteList().remove(lineaDocenteListLineaDocente);
                    oldLineaIdOfLineaDocenteListLineaDocente = em.merge(oldLineaIdOfLineaDocenteListLineaDocente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LineaInvestigacion lineaInvestigacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LineaInvestigacion persistentLineaInvestigacion = em.find(LineaInvestigacion.class, lineaInvestigacion.getIdLinea());
            Escuela escuelaOld = persistentLineaInvestigacion.getEscuela();
            Escuela escuelaNew = lineaInvestigacion.getEscuela();
            List<LineaDocente> lineaDocenteListOld = persistentLineaInvestigacion.getLineaDocenteList();
            List<LineaDocente> lineaDocenteListNew = lineaInvestigacion.getLineaDocenteList();
            List<String> illegalOrphanMessages = null;
            lineaDocenteListOld = (lineaDocenteListOld==null)? new LinkedList<LineaDocente>(): lineaDocenteListOld;
            lineaDocenteListNew = (lineaDocenteListNew==null)? new LinkedList<LineaDocente>(): lineaDocenteListNew;
            for (LineaDocente lineaDocenteListOldLineaDocente : lineaDocenteListOld) {
                if (!lineaDocenteListNew.contains(lineaDocenteListOldLineaDocente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LineaDocente " + lineaDocenteListOldLineaDocente + " since its lineaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (escuelaNew != null) {
                escuelaNew = em.getReference(escuelaNew.getClass(), escuelaNew.getIdEscuela());
                lineaInvestigacion.setEscuela(escuelaNew);
            }
            List<LineaDocente> attachedLineaDocenteListNew = new ArrayList<LineaDocente>();
            for (LineaDocente lineaDocenteListNewLineaDocenteToAttach : lineaDocenteListNew) {
                lineaDocenteListNewLineaDocenteToAttach = em.getReference(lineaDocenteListNewLineaDocenteToAttach.getClass(), lineaDocenteListNewLineaDocenteToAttach.getIdRelacion());
                attachedLineaDocenteListNew.add(lineaDocenteListNewLineaDocenteToAttach);
            }
            lineaDocenteListNew = attachedLineaDocenteListNew;
            lineaInvestigacion.setLineaDocenteList(lineaDocenteListNew);
            lineaInvestigacion = em.merge(lineaInvestigacion);
            if (escuelaOld != null && !escuelaOld.equals(escuelaNew)) {
                escuelaOld.getLineaInvestigacionList().remove(lineaInvestigacion);
                escuelaOld = em.merge(escuelaOld);
            }
            if (escuelaNew != null && !escuelaNew.equals(escuelaOld)) {
                escuelaNew.getLineaInvestigacionList().add(lineaInvestigacion);
                escuelaNew = em.merge(escuelaNew);
            }
            for (LineaDocente lineaDocenteListNewLineaDocente : lineaDocenteListNew) {
                if (!lineaDocenteListOld.contains(lineaDocenteListNewLineaDocente)) {
                    LineaInvestigacion oldLineaIdOfLineaDocenteListNewLineaDocente = lineaDocenteListNewLineaDocente.getLineaId();
                    lineaDocenteListNewLineaDocente.setLineaId(lineaInvestigacion);
                    lineaDocenteListNewLineaDocente = em.merge(lineaDocenteListNewLineaDocente);
                    if (oldLineaIdOfLineaDocenteListNewLineaDocente != null && !oldLineaIdOfLineaDocenteListNewLineaDocente.equals(lineaInvestigacion)) {
                        oldLineaIdOfLineaDocenteListNewLineaDocente.getLineaDocenteList().remove(lineaDocenteListNewLineaDocente);
                        oldLineaIdOfLineaDocenteListNewLineaDocente = em.merge(oldLineaIdOfLineaDocenteListNewLineaDocente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = lineaInvestigacion.getIdLinea();
                if (findLineaInvestigacion(id) == null) {
                    throw new NonexistentEntityException("The lineaInvestigacion with id " + id + " no longer exists.");
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
            LineaInvestigacion lineaInvestigacion;
            try {
                lineaInvestigacion = em.getReference(LineaInvestigacion.class, id);
                lineaInvestigacion.getIdLinea();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lineaInvestigacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<LineaDocente> lineaDocenteListOrphanCheck = lineaInvestigacion.getLineaDocenteList();
            for (LineaDocente lineaDocenteListOrphanCheckLineaDocente : lineaDocenteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This LineaInvestigacion (" + lineaInvestigacion + ") cannot be destroyed since the LineaDocente " + lineaDocenteListOrphanCheckLineaDocente + " in its lineaDocenteList field has a non-nullable lineaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Escuela escuela = lineaInvestigacion.getEscuela();
            if (escuela != null) {
                escuela.getLineaInvestigacionList().remove(lineaInvestigacion);
                escuela = em.merge(escuela);
            }
            em.remove(lineaInvestigacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LineaInvestigacion> findLineaInvestigacionEntities() {
        return findLineaInvestigacionEntities(true, -1, -1);
    }

    public List<LineaInvestigacion> findLineaInvestigacionEntities(int maxResults, int firstResult) {
        return findLineaInvestigacionEntities(false, maxResults, firstResult);
    }

    private List<LineaInvestigacion> findLineaInvestigacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LineaInvestigacion.class));
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

    public LineaInvestigacion findLineaInvestigacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LineaInvestigacion.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<LineaInvestigacion> findLineas2(String nombre) {
        EntityManager em = getEntityManager();
        List<LineaInvestigacion> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT l FROM LineaInvestigacion l WHERE l.nombre LIKE CONCAT('%',?1,'%')";
            Query q = em.createQuery(jpql);
            q.setParameter(1, nombre);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }

    public int getLineaInvestigacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LineaInvestigacion> rt = cq.from(LineaInvestigacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<LineaDocente> findLineaInvestigacionEntities3(Escuela codigo) {
        EntityManager em = getEntityManager();
        List<LineaDocente> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT l FROM LineaInvestigacion l WHERE l.escuela = ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, codigo);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }

    public List<LineaDocente> findLineas3(Docente findDocente) {
        EntityManager em = getEntityManager();
        List<LineaDocente> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT d FROM LineaDocente d WHERE d.docenteId= ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, findDocente);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }
    
}
