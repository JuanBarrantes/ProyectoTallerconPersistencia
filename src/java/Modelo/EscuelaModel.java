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
import Beans.Facultad;
import Beans.LineaInvestigacion;
import java.util.ArrayList;
import java.util.List;
import Beans.Docente;
import Beans.Alumno;
import Beans.Escuela;
import Modelo.exceptions.IllegalOrphanException;
import Modelo.exceptions.NonexistentEntityException;
import java.util.LinkedList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JhanxD
 */
public class EscuelaModel implements Serializable {

    public EscuelaModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Escuela escuela) {
        if (escuela.getLineaInvestigacionList() == null) {
            escuela.setLineaInvestigacionList(new ArrayList<LineaInvestigacion>());
        }
        if (escuela.getDocenteList() == null) {
            escuela.setDocenteList(new ArrayList<Docente>());
        }
        if (escuela.getAlumnoList() == null) {
            escuela.setAlumnoList(new ArrayList<Alumno>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facultad facultad = escuela.getFacultad();
            if (facultad != null) {
                facultad = em.getReference(facultad.getClass(), facultad.getIdFacultad());
                escuela.setFacultad(facultad);
            }
            List<LineaInvestigacion> attachedLineaInvestigacionList = new ArrayList<LineaInvestigacion>();
            for (LineaInvestigacion lineaInvestigacionListLineaInvestigacionToAttach : escuela.getLineaInvestigacionList()) {
                lineaInvestigacionListLineaInvestigacionToAttach = em.getReference(lineaInvestigacionListLineaInvestigacionToAttach.getClass(), lineaInvestigacionListLineaInvestigacionToAttach.getIdLinea());
                attachedLineaInvestigacionList.add(lineaInvestigacionListLineaInvestigacionToAttach);
            }
            escuela.setLineaInvestigacionList(attachedLineaInvestigacionList);
            List<Docente> attachedDocenteList = new ArrayList<Docente>();
            for (Docente docenteListDocenteToAttach : escuela.getDocenteList()) {
                docenteListDocenteToAttach = em.getReference(docenteListDocenteToAttach.getClass(), docenteListDocenteToAttach.getIdDocente());
                attachedDocenteList.add(docenteListDocenteToAttach);
            }
            escuela.setDocenteList(attachedDocenteList);
            List<Alumno> attachedAlumnoList = new ArrayList<Alumno>();
            for (Alumno alumnoListAlumnoToAttach : escuela.getAlumnoList()) {
                alumnoListAlumnoToAttach = em.getReference(alumnoListAlumnoToAttach.getClass(), alumnoListAlumnoToAttach.getIdAlumno());
                attachedAlumnoList.add(alumnoListAlumnoToAttach);
            }
            escuela.setAlumnoList(attachedAlumnoList);
            em.persist(escuela);
            if (facultad != null) {
                facultad.getEscuelaList().add(escuela);
                facultad = em.merge(facultad);
            }
            for (LineaInvestigacion lineaInvestigacionListLineaInvestigacion : escuela.getLineaInvestigacionList()) {
                Escuela oldEscuelaOfLineaInvestigacionListLineaInvestigacion = lineaInvestigacionListLineaInvestigacion.getEscuela();
                lineaInvestigacionListLineaInvestigacion.setEscuela(escuela);
                lineaInvestigacionListLineaInvestigacion = em.merge(lineaInvestigacionListLineaInvestigacion);
                if (oldEscuelaOfLineaInvestigacionListLineaInvestigacion != null) {
                    oldEscuelaOfLineaInvestigacionListLineaInvestigacion.getLineaInvestigacionList().remove(lineaInvestigacionListLineaInvestigacion);
                    oldEscuelaOfLineaInvestigacionListLineaInvestigacion = em.merge(oldEscuelaOfLineaInvestigacionListLineaInvestigacion);
                }
            }
            for (Docente docenteListDocente : escuela.getDocenteList()) {
                Escuela oldEscuelaOfDocenteListDocente = docenteListDocente.getEscuela();
                docenteListDocente.setEscuela(escuela);
                docenteListDocente = em.merge(docenteListDocente);
                if (oldEscuelaOfDocenteListDocente != null) {
                    oldEscuelaOfDocenteListDocente.getDocenteList().remove(docenteListDocente);
                    oldEscuelaOfDocenteListDocente = em.merge(oldEscuelaOfDocenteListDocente);
                }
            }
            for (Alumno alumnoListAlumno : escuela.getAlumnoList()) {
                Escuela oldEscuelaOfAlumnoListAlumno = alumnoListAlumno.getEscuela();
                alumnoListAlumno.setEscuela(escuela);
                alumnoListAlumno = em.merge(alumnoListAlumno);
                if (oldEscuelaOfAlumnoListAlumno != null) {
                    oldEscuelaOfAlumnoListAlumno.getAlumnoList().remove(alumnoListAlumno);
                    oldEscuelaOfAlumnoListAlumno = em.merge(oldEscuelaOfAlumnoListAlumno);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Escuela escuela) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Escuela persistentEscuela = em.find(Escuela.class, escuela.getIdEscuela());
            Facultad facultadOld = persistentEscuela.getFacultad();
            Facultad facultadNew = escuela.getFacultad();
            List<LineaInvestigacion> lineaInvestigacionListOld = persistentEscuela.getLineaInvestigacionList();
            List<LineaInvestigacion> lineaInvestigacionListNew = escuela.getLineaInvestigacionList();
            List<Docente> docenteListOld = persistentEscuela.getDocenteList();
            List<Docente> docenteListNew = escuela.getDocenteList();
            List<Alumno> alumnoListOld = persistentEscuela.getAlumnoList();
            List<Alumno> alumnoListNew = escuela.getAlumnoList();
            lineaInvestigacionListOld = (lineaInvestigacionListOld==null)? new LinkedList<LineaInvestigacion>(): lineaInvestigacionListOld;
            lineaInvestigacionListNew = (lineaInvestigacionListNew==null)? new LinkedList<LineaInvestigacion>(): lineaInvestigacionListNew;
            docenteListOld = (docenteListOld==null)? new LinkedList<Docente>(): docenteListOld;
            docenteListNew = (docenteListNew==null)? new LinkedList<Docente>(): docenteListNew;
            alumnoListOld = (alumnoListOld==null)? new LinkedList<Alumno>(): alumnoListOld;
            alumnoListNew = (alumnoListNew==null)? new LinkedList<Alumno>(): alumnoListNew;
            List<String> illegalOrphanMessages = null;
            for (LineaInvestigacion lineaInvestigacionListOldLineaInvestigacion : lineaInvestigacionListOld) {
                if (!lineaInvestigacionListNew.contains(lineaInvestigacionListOldLineaInvestigacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LineaInvestigacion " + lineaInvestigacionListOldLineaInvestigacion + " since its escuela field is not nullable.");
                }
            }
            for (Docente docenteListOldDocente : docenteListOld) {
                if (!docenteListNew.contains(docenteListOldDocente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Docente " + docenteListOldDocente + " since its escuela field is not nullable.");
                }
            }
            for (Alumno alumnoListOldAlumno : alumnoListOld) {
                if (!alumnoListNew.contains(alumnoListOldAlumno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alumno " + alumnoListOldAlumno + " since its escuela field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (facultadNew != null) {
                facultadNew = em.getReference(facultadNew.getClass(), facultadNew.getIdFacultad());
                escuela.setFacultad(facultadNew);
            }
            List<LineaInvestigacion> attachedLineaInvestigacionListNew = new ArrayList<LineaInvestigacion>();
            for (LineaInvestigacion lineaInvestigacionListNewLineaInvestigacionToAttach : lineaInvestigacionListNew) {
                lineaInvestigacionListNewLineaInvestigacionToAttach = em.getReference(lineaInvestigacionListNewLineaInvestigacionToAttach.getClass(), lineaInvestigacionListNewLineaInvestigacionToAttach.getIdLinea());
                attachedLineaInvestigacionListNew.add(lineaInvestigacionListNewLineaInvestigacionToAttach);
            }
            lineaInvestigacionListNew = attachedLineaInvestigacionListNew;
            escuela.setLineaInvestigacionList(lineaInvestigacionListNew);
            List<Docente> attachedDocenteListNew = new ArrayList<Docente>();
            for (Docente docenteListNewDocenteToAttach : docenteListNew) {
                docenteListNewDocenteToAttach = em.getReference(docenteListNewDocenteToAttach.getClass(), docenteListNewDocenteToAttach.getIdDocente());
                attachedDocenteListNew.add(docenteListNewDocenteToAttach);
            }
            docenteListNew = attachedDocenteListNew;
            escuela.setDocenteList(docenteListNew);
            List<Alumno> attachedAlumnoListNew = new ArrayList<Alumno>();
            for (Alumno alumnoListNewAlumnoToAttach : alumnoListNew) {
                alumnoListNewAlumnoToAttach = em.getReference(alumnoListNewAlumnoToAttach.getClass(), alumnoListNewAlumnoToAttach.getIdAlumno());
                attachedAlumnoListNew.add(alumnoListNewAlumnoToAttach);
            }
            alumnoListNew = attachedAlumnoListNew;
            escuela.setAlumnoList(alumnoListNew);
            escuela = em.merge(escuela);
            if (facultadOld != null && !facultadOld.equals(facultadNew)) {
                facultadOld.getEscuelaList().remove(escuela);
                facultadOld = em.merge(facultadOld);
            }
            if (facultadNew != null && !facultadNew.equals(facultadOld)) {
                facultadNew.getEscuelaList().add(escuela);
                facultadNew = em.merge(facultadNew);
            }
            for (LineaInvestigacion lineaInvestigacionListNewLineaInvestigacion : lineaInvestigacionListNew) {
                if (!lineaInvestigacionListOld.contains(lineaInvestigacionListNewLineaInvestigacion)) {
                    Escuela oldEscuelaOfLineaInvestigacionListNewLineaInvestigacion = lineaInvestigacionListNewLineaInvestigacion.getEscuela();
                    lineaInvestigacionListNewLineaInvestigacion.setEscuela(escuela);
                    lineaInvestigacionListNewLineaInvestigacion = em.merge(lineaInvestigacionListNewLineaInvestigacion);
                    if (oldEscuelaOfLineaInvestigacionListNewLineaInvestigacion != null && !oldEscuelaOfLineaInvestigacionListNewLineaInvestigacion.equals(escuela)) {
                        oldEscuelaOfLineaInvestigacionListNewLineaInvestigacion.getLineaInvestigacionList().remove(lineaInvestigacionListNewLineaInvestigacion);
                        oldEscuelaOfLineaInvestigacionListNewLineaInvestigacion = em.merge(oldEscuelaOfLineaInvestigacionListNewLineaInvestigacion);
                    }
                }
            }
            for (Docente docenteListNewDocente : docenteListNew) {
                if (!docenteListOld.contains(docenteListNewDocente)) {
                    Escuela oldEscuelaOfDocenteListNewDocente = docenteListNewDocente.getEscuela();
                    docenteListNewDocente.setEscuela(escuela);
                    docenteListNewDocente = em.merge(docenteListNewDocente);
                    if (oldEscuelaOfDocenteListNewDocente != null && !oldEscuelaOfDocenteListNewDocente.equals(escuela)) {
                        oldEscuelaOfDocenteListNewDocente.getDocenteList().remove(docenteListNewDocente);
                        oldEscuelaOfDocenteListNewDocente = em.merge(oldEscuelaOfDocenteListNewDocente);
                    }
                }
            }
            for (Alumno alumnoListNewAlumno : alumnoListNew) {
                if (!alumnoListOld.contains(alumnoListNewAlumno)) {
                    Escuela oldEscuelaOfAlumnoListNewAlumno = alumnoListNewAlumno.getEscuela();
                    alumnoListNewAlumno.setEscuela(escuela);
                    alumnoListNewAlumno = em.merge(alumnoListNewAlumno);
                    if (oldEscuelaOfAlumnoListNewAlumno != null && !oldEscuelaOfAlumnoListNewAlumno.equals(escuela)) {
                        oldEscuelaOfAlumnoListNewAlumno.getAlumnoList().remove(alumnoListNewAlumno);
                        oldEscuelaOfAlumnoListNewAlumno = em.merge(oldEscuelaOfAlumnoListNewAlumno);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = escuela.getIdEscuela();
                if (findEscuela(id) == null) {
                    throw new NonexistentEntityException("The escuela with id " + id + " no longer exists.");
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
            Escuela escuela;
            try {
                escuela = em.getReference(Escuela.class, id);
                escuela.getIdEscuela();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The escuela with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<LineaInvestigacion> lineaInvestigacionListOrphanCheck = escuela.getLineaInvestigacionList();
            for (LineaInvestigacion lineaInvestigacionListOrphanCheckLineaInvestigacion : lineaInvestigacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Escuela (" + escuela + ") cannot be destroyed since the LineaInvestigacion " + lineaInvestigacionListOrphanCheckLineaInvestigacion + " in its lineaInvestigacionList field has a non-nullable escuela field.");
            }
            List<Docente> docenteListOrphanCheck = escuela.getDocenteList();
            for (Docente docenteListOrphanCheckDocente : docenteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Escuela (" + escuela + ") cannot be destroyed since the Docente " + docenteListOrphanCheckDocente + " in its docenteList field has a non-nullable escuela field.");
            }
            List<Alumno> alumnoListOrphanCheck = escuela.getAlumnoList();
            for (Alumno alumnoListOrphanCheckAlumno : alumnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Escuela (" + escuela + ") cannot be destroyed since the Alumno " + alumnoListOrphanCheckAlumno + " in its alumnoList field has a non-nullable escuela field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Facultad facultad = escuela.getFacultad();
            if (facultad != null) {
                facultad.getEscuelaList().remove(escuela);
                facultad = em.merge(facultad);
            }
            em.remove(escuela);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Escuela> findEscuelaEntities() {
        return findEscuelaEntities(true, -1, -1);
    }
    
    public List<Escuela> findEscuelaEntities2(Facultad dato) {
        EntityManager em = getEntityManager();
        List<Escuela> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT l FROM Escuela l WHERE l.facultad = ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, dato);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }

    public List<Escuela> findEscuelaEntities(int maxResults, int firstResult) {
        return findEscuelaEntities(false, maxResults, firstResult);
    }

    private List<Escuela> findEscuelaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Escuela.class));
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
    
    public List<Escuela> findEscuela2(String nombre) {
        EntityManager em = getEntityManager();
        List<Escuela> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT e FROM Escuela e WHERE e.nombre LIKE CONCAT('%',?1,'%')";
            Query q = em.createQuery(jpql);
            q.setParameter(1, nombre);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }

    public Escuela findEscuela(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Escuela.class, id);
        } finally {
            em.close();
        }
    }

    public int getEscuelaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Escuela> rt = cq.from(Escuela.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
