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
import Beans.Constancia;
import Beans.Facultad;
import Beans.Jurado;
import Beans.LineaDocente;
import java.util.ArrayList;
import java.util.List;
import Beans.Observacion;
import Beans.Tesis;
import Modelo.exceptions.IllegalOrphanException;
import Modelo.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JhanxD
 */
public class TesisModel implements Serializable {

    public TesisModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(Tesis tesis) {
        if (tesis.getJuradoList() == null) {
            tesis.setJuradoList(new ArrayList<Jurado>());
        }
        if (tesis.getObservacionList() == null) {
            tesis.setObservacionList(new ArrayList<Observacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Constancia codigoC = tesis.getCodigoC();
            if (codigoC != null) {
                codigoC = em.getReference(codigoC.getClass(), codigoC.getIdConstancia());
                tesis.setCodigoC(codigoC);
            }
            List<Jurado> attachedJuradoList = new ArrayList<Jurado>();
            for (Jurado juradoListJuradoToAttach : tesis.getJuradoList()) {
                juradoListJuradoToAttach = em.getReference(juradoListJuradoToAttach.getClass(), juradoListJuradoToAttach.getRelacion());
                attachedJuradoList.add(juradoListJuradoToAttach);
            }
            tesis.setJuradoList(attachedJuradoList);
            List<Observacion> attachedObservacionList = new ArrayList<Observacion>();
            for (Observacion observacionListObservacionToAttach : tesis.getObservacionList()) {
                observacionListObservacionToAttach = em.getReference(observacionListObservacionToAttach.getClass(), observacionListObservacionToAttach.getIdObservacion());
                attachedObservacionList.add(observacionListObservacionToAttach);
            }
            tesis.setObservacionList(attachedObservacionList);
            em.persist(tesis);
            if (codigoC != null) {
                codigoC.getTesisList().add(tesis);
                codigoC = em.merge(codigoC);
            }
            for (Jurado juradoListJurado : tesis.getJuradoList()) {
                Tesis oldIdTesisOfJuradoListJurado = juradoListJurado.getIdTesis();
                juradoListJurado.setIdTesis(tesis);
                juradoListJurado = em.merge(juradoListJurado);
                if (oldIdTesisOfJuradoListJurado != null) {
                    oldIdTesisOfJuradoListJurado.getJuradoList().remove(juradoListJurado);
                    oldIdTesisOfJuradoListJurado = em.merge(oldIdTesisOfJuradoListJurado);
                }
            }
            for (Observacion observacionListObservacion : tesis.getObservacionList()) {
                Tesis oldIdTesisOfObservacionListObservacion = observacionListObservacion.getIdTesis();
                observacionListObservacion.setIdTesis(tesis);
                observacionListObservacion = em.merge(observacionListObservacion);
                if (oldIdTesisOfObservacionListObservacion != null) {
                    oldIdTesisOfObservacionListObservacion.getObservacionList().remove(observacionListObservacion);
                    oldIdTesisOfObservacionListObservacion = em.merge(oldIdTesisOfObservacionListObservacion);
                }
            }
            em.getTransaction().commit();
            String res="Registrado";
            return res;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tesis tesis) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tesis persistentTesis = em.find(Tesis.class, tesis.getIdTesis());
            Constancia codigoCOld = persistentTesis.getCodigoC();
            Constancia codigoCNew = tesis.getCodigoC();
            List<Jurado> juradoListOld = persistentTesis.getJuradoList();
            List<Jurado> juradoListNew = tesis.getJuradoList();
            List<Observacion> observacionListOld = persistentTesis.getObservacionList();
            List<Observacion> observacionListNew = tesis.getObservacionList();
            List<String> illegalOrphanMessages = null;
            for (Jurado juradoListOldJurado : juradoListOld) {
                if (!juradoListNew.contains(juradoListOldJurado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jurado " + juradoListOldJurado + " since its idTesis field is not nullable.");
                }
            }
            for (Observacion observacionListOldObservacion : observacionListOld) {
                if (!observacionListNew.contains(observacionListOldObservacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Observacion " + observacionListOldObservacion + " since its idTesis field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codigoCNew != null) {
                codigoCNew = em.getReference(codigoCNew.getClass(), codigoCNew.getIdConstancia());
                tesis.setCodigoC(codigoCNew);
            }
            List<Jurado> attachedJuradoListNew = new ArrayList<Jurado>();
            for (Jurado juradoListNewJuradoToAttach : juradoListNew) {
                juradoListNewJuradoToAttach = em.getReference(juradoListNewJuradoToAttach.getClass(), juradoListNewJuradoToAttach.getRelacion());
                attachedJuradoListNew.add(juradoListNewJuradoToAttach);
            }
            juradoListNew = attachedJuradoListNew;
            tesis.setJuradoList(juradoListNew);
            List<Observacion> attachedObservacionListNew = new ArrayList<Observacion>();
            for (Observacion observacionListNewObservacionToAttach : observacionListNew) {
                observacionListNewObservacionToAttach = em.getReference(observacionListNewObservacionToAttach.getClass(), observacionListNewObservacionToAttach.getIdObservacion());
                attachedObservacionListNew.add(observacionListNewObservacionToAttach);
            }
            observacionListNew = attachedObservacionListNew;
            tesis.setObservacionList(observacionListNew);
            tesis = em.merge(tesis);
            if (codigoCOld != null && !codigoCOld.equals(codigoCNew)) {
                codigoCOld.getTesisList().remove(tesis);
                codigoCOld = em.merge(codigoCOld);
            }
            if (codigoCNew != null && !codigoCNew.equals(codigoCOld)) {
                codigoCNew.getTesisList().add(tesis);
                codigoCNew = em.merge(codigoCNew);
            }
            for (Jurado juradoListNewJurado : juradoListNew) {
                if (!juradoListOld.contains(juradoListNewJurado)) {
                    Tesis oldIdTesisOfJuradoListNewJurado = juradoListNewJurado.getIdTesis();
                    juradoListNewJurado.setIdTesis(tesis);
                    juradoListNewJurado = em.merge(juradoListNewJurado);
                    if (oldIdTesisOfJuradoListNewJurado != null && !oldIdTesisOfJuradoListNewJurado.equals(tesis)) {
                        oldIdTesisOfJuradoListNewJurado.getJuradoList().remove(juradoListNewJurado);
                        oldIdTesisOfJuradoListNewJurado = em.merge(oldIdTesisOfJuradoListNewJurado);
                    }
                }
            }
            for (Observacion observacionListNewObservacion : observacionListNew) {
                if (!observacionListOld.contains(observacionListNewObservacion)) {
                    Tesis oldIdTesisOfObservacionListNewObservacion = observacionListNewObservacion.getIdTesis();
                    observacionListNewObservacion.setIdTesis(tesis);
                    observacionListNewObservacion = em.merge(observacionListNewObservacion);
                    if (oldIdTesisOfObservacionListNewObservacion != null && !oldIdTesisOfObservacionListNewObservacion.equals(tesis)) {
                        oldIdTesisOfObservacionListNewObservacion.getObservacionList().remove(observacionListNewObservacion);
                        oldIdTesisOfObservacionListNewObservacion = em.merge(oldIdTesisOfObservacionListNewObservacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tesis.getIdTesis();
                if (findTesis(id) == null) {
                    throw new NonexistentEntityException("The tesis with id " + id + " no longer exists.");
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
            Tesis tesis;
            try {
                tesis = em.getReference(Tesis.class, id);
                tesis.getIdTesis();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tesis with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Jurado> juradoListOrphanCheck = tesis.getJuradoList();
            for (Jurado juradoListOrphanCheckJurado : juradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tesis (" + tesis + ") cannot be destroyed since the Jurado " + juradoListOrphanCheckJurado + " in its juradoList field has a non-nullable idTesis field.");
            }
            List<Observacion> observacionListOrphanCheck = tesis.getObservacionList();
            for (Observacion observacionListOrphanCheckObservacion : observacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tesis (" + tesis + ") cannot be destroyed since the Observacion " + observacionListOrphanCheckObservacion + " in its observacionList field has a non-nullable idTesis field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Constancia codigoC = tesis.getCodigoC();
            if (codigoC != null) {
                codigoC.getTesisList().remove(tesis);
                codigoC = em.merge(codigoC);
            }
            em.remove(tesis);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tesis> findTesisEntities() {
        return findTesisEntities(true, -1, -1);
    }

    public List<Tesis> findTesisEntities(int maxResults, int firstResult) {
        return findTesisEntities(false, maxResults, firstResult);
    }

    private List<Tesis> findTesisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tesis.class));
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

    public Tesis findTesis(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tesis.class, id);
        } finally {
            em.close();
        }
    }

    public int getTesisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tesis> rt = cq.from(Tesis.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Tesis> findTesis2(String codigo) {
        EntityManager em = getEntityManager();
        List<Tesis> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT f FROM Tesis f WHERE f.codigoTesis LIKE CONCAT('%',?1,'%')";
            Query q = em.createQuery(jpql);
            q.setParameter(1, codigo);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }

    public String aprobarAnteproyecto(Observacion obs) {
            EntityManager em = getEntityManager();
            int codigo=obs.getIdTesis().getCodigoC().getAlumno().getIdAlumno();
            String respuesta = "";
        try {
            em = getEntityManager();
            String jpql="SELECT COUNT(d.idObservacion) FROM Observacion d WHERE (d.idTesis = ?1 AND d.levantada = ?2)";
            Query a = em.createQuery(jpql);
            a.setParameter(1, obs.getIdTesis());
            a.setParameter(2, "NO");
            System.out.println("solo");
            String aa = String.valueOf((long)a.getSingleResult());
            System.out.println("sdfs"+aa);
                if (aa.equals("0")) {
                    AlumnoModel ma = new AlumnoModel(emf);
                    Alumno al = ma.findAlumno(codigo);
                    al.setEstadot(1);
                    ma.edit(al);
//                    String sql = "UPDATE Alumno a SET a.estadot = 1 WHERE a.idAlumno = ?1";
//                    Query b = em.createQuery(sql);
//                    b.setParameter(1, alum.getIdAlumno());
                    respuesta = "Aprovado";
                    System.out.println("Observacion " + respuesta);
                } else {
                    respuesta = "Aun le quedan " + aa + " observacion por levantar";
                }
            
            
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return respuesta;

       }
    
}
