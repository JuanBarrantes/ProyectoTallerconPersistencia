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
import Beans.LineaInvestigacion;
import Beans.Docente;
import Beans.Constancia;
import Beans.LineaDocente;
import Modelo.exceptions.IllegalOrphanException;
import Modelo.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JhanxD
 */
public class LineaDocenteModel implements Serializable {

    public LineaDocenteModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LineaDocente lineaDocente) {
        if (lineaDocente.getConstanciaList() == null) {
            lineaDocente.setConstanciaList(new ArrayList<Constancia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LineaInvestigacion lineaId = lineaDocente.getLineaId();
            if (lineaId != null) {
                lineaId = em.getReference(lineaId.getClass(), lineaId.getIdLinea());
                lineaDocente.setLineaId(lineaId);
            }
            Docente docenteId = lineaDocente.getDocenteId();
            if (docenteId != null) {
                docenteId = em.getReference(docenteId.getClass(), docenteId.getIdDocente());
                lineaDocente.setDocenteId(docenteId);
            }
            List<Constancia> attachedConstanciaList = new ArrayList<Constancia>();
            for (Constancia constanciaListConstanciaToAttach : lineaDocente.getConstanciaList()) {
                constanciaListConstanciaToAttach = em.getReference(constanciaListConstanciaToAttach.getClass(), constanciaListConstanciaToAttach.getIdConstancia());
                attachedConstanciaList.add(constanciaListConstanciaToAttach);
            }
            lineaDocente.setConstanciaList(attachedConstanciaList);
            em.persist(lineaDocente);
            if (lineaId != null) {
                lineaId.getLineaDocenteList().add(lineaDocente);
                lineaId = em.merge(lineaId);
            }
            if (docenteId != null) {
                docenteId.getLineaDocenteList().add(lineaDocente);
                docenteId = em.merge(docenteId);
            }
            for (Constancia constanciaListConstancia : lineaDocente.getConstanciaList()) {
                LineaDocente oldDocenteLineaOfConstanciaListConstancia = constanciaListConstancia.getDocenteLinea();
                constanciaListConstancia.setDocenteLinea(lineaDocente);
                constanciaListConstancia = em.merge(constanciaListConstancia);
                if (oldDocenteLineaOfConstanciaListConstancia != null) {
                    oldDocenteLineaOfConstanciaListConstancia.getConstanciaList().remove(constanciaListConstancia);
                    oldDocenteLineaOfConstanciaListConstancia = em.merge(oldDocenteLineaOfConstanciaListConstancia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LineaDocente lineaDocente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LineaDocente persistentLineaDocente = em.find(LineaDocente.class, lineaDocente.getIdRelacion());
            LineaInvestigacion lineaIdOld = persistentLineaDocente.getLineaId();
            LineaInvestigacion lineaIdNew = lineaDocente.getLineaId();
            Docente docenteIdOld = persistentLineaDocente.getDocenteId();
            Docente docenteIdNew = lineaDocente.getDocenteId();
            List<Constancia> constanciaListOld = persistentLineaDocente.getConstanciaList();
            List<Constancia> constanciaListNew = lineaDocente.getConstanciaList();
            List<String> illegalOrphanMessages = null;
            for (Constancia constanciaListOldConstancia : constanciaListOld) {
                if (!constanciaListNew.contains(constanciaListOldConstancia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Constancia " + constanciaListOldConstancia + " since its docenteLinea field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (lineaIdNew != null) {
                lineaIdNew = em.getReference(lineaIdNew.getClass(), lineaIdNew.getIdLinea());
                lineaDocente.setLineaId(lineaIdNew);
            }
            if (docenteIdNew != null) {
                docenteIdNew = em.getReference(docenteIdNew.getClass(), docenteIdNew.getIdDocente());
                lineaDocente.setDocenteId(docenteIdNew);
            }
            List<Constancia> attachedConstanciaListNew = new ArrayList<Constancia>();
            for (Constancia constanciaListNewConstanciaToAttach : constanciaListNew) {
                constanciaListNewConstanciaToAttach = em.getReference(constanciaListNewConstanciaToAttach.getClass(), constanciaListNewConstanciaToAttach.getIdConstancia());
                attachedConstanciaListNew.add(constanciaListNewConstanciaToAttach);
            }
            constanciaListNew = attachedConstanciaListNew;
            lineaDocente.setConstanciaList(constanciaListNew);
            lineaDocente = em.merge(lineaDocente);
            if (lineaIdOld != null && !lineaIdOld.equals(lineaIdNew)) {
                lineaIdOld.getLineaDocenteList().remove(lineaDocente);
                lineaIdOld = em.merge(lineaIdOld);
            }
            if (lineaIdNew != null && !lineaIdNew.equals(lineaIdOld)) {
                lineaIdNew.getLineaDocenteList().add(lineaDocente);
                lineaIdNew = em.merge(lineaIdNew);
            }
            if (docenteIdOld != null && !docenteIdOld.equals(docenteIdNew)) {
                docenteIdOld.getLineaDocenteList().remove(lineaDocente);
                docenteIdOld = em.merge(docenteIdOld);
            }
            if (docenteIdNew != null && !docenteIdNew.equals(docenteIdOld)) {
                docenteIdNew.getLineaDocenteList().add(lineaDocente);
                docenteIdNew = em.merge(docenteIdNew);
            }
            for (Constancia constanciaListNewConstancia : constanciaListNew) {
                if (!constanciaListOld.contains(constanciaListNewConstancia)) {
                    LineaDocente oldDocenteLineaOfConstanciaListNewConstancia = constanciaListNewConstancia.getDocenteLinea();
                    constanciaListNewConstancia.setDocenteLinea(lineaDocente);
                    constanciaListNewConstancia = em.merge(constanciaListNewConstancia);
                    if (oldDocenteLineaOfConstanciaListNewConstancia != null && !oldDocenteLineaOfConstanciaListNewConstancia.equals(lineaDocente)) {
                        oldDocenteLineaOfConstanciaListNewConstancia.getConstanciaList().remove(constanciaListNewConstancia);
                        oldDocenteLineaOfConstanciaListNewConstancia = em.merge(oldDocenteLineaOfConstanciaListNewConstancia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = lineaDocente.getIdRelacion();
                if (findLineaDocente(id) == null) {
                    throw new NonexistentEntityException("The lineaDocente with id " + id + " no longer exists.");
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
            LineaDocente lineaDocente;
            try {
                lineaDocente = em.getReference(LineaDocente.class, id);
                lineaDocente.getIdRelacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lineaDocente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Constancia> constanciaListOrphanCheck = lineaDocente.getConstanciaList();
            for (Constancia constanciaListOrphanCheckConstancia : constanciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This LineaDocente (" + lineaDocente + ") cannot be destroyed since the Constancia " + constanciaListOrphanCheckConstancia + " in its constanciaList field has a non-nullable docenteLinea field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            LineaInvestigacion lineaId = lineaDocente.getLineaId();
            if (lineaId != null) {
                lineaId.getLineaDocenteList().remove(lineaDocente);
                lineaId = em.merge(lineaId);
            }
            Docente docenteId = lineaDocente.getDocenteId();
            if (docenteId != null) {
                docenteId.getLineaDocenteList().remove(lineaDocente);
                docenteId = em.merge(docenteId);
            }
            em.remove(lineaDocente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LineaDocente> findLineaDocenteEntities() {
        return findLineaDocenteEntities(true, -1, -1);
    }

    public List<LineaDocente> findLineaDocenteEntities(int maxResults, int firstResult) {
        return findLineaDocenteEntities(false, maxResults, firstResult);
    }

    private List<LineaDocente> findLineaDocenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LineaDocente.class));
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

    public LineaDocente findLineaDocente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LineaDocente.class, id);
        } finally {
            em.close();
        }
    }

    public int getLineaDocenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LineaDocente> rt = cq.from(LineaDocente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<LineaDocente> findLineaDocente2(Docente codigo) {
        EntityManager em = getEntityManager();
        List<LineaDocente> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT l FROM LineaDocente l WHERE l.docenteId = ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, codigo);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;

    }

    public String cotejarCant(Docente facul, LineaInvestigacion facul2) {
        EntityManager em = getEntityManager();
        String lista="";
        try {
            em = getEntityManager();
            String jpql="SELECT COUNT(d.idRelacion) FROM LineaDocente d WHERE (d.docenteId= ?1) AND (d.lineaId= ?2)";
            Query q = em.createQuery(jpql);
            q.setParameter(1, facul);
            q.setParameter(2, facul2);
            lista = String.valueOf((long)q.getSingleResult());
            System.out.println("li"+lista);
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;

    }

    public String cotejarRel(Docente facul, LineaInvestigacion facul2) {
        EntityManager em = getEntityManager();
        List<LineaDocente> lista=new ArrayList<>();
        String res="";
        try {
            em = getEntityManager();
            String jpql="SELECT d FROM LineaDocente d WHERE (d.docenteId= ?1) AND (d.lineaId= ?2)";
            Query q = em.createQuery(jpql);
            q.setParameter(1, facul);
            q.setParameter(2, facul2);
            lista = q.getResultList();
            for (int i = 0; i < lista.size(); i++) {
                res=String.valueOf(lista.get(i).getIdRelacion());
            }
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return res;
    }
    
}
