/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Beans.Constancia;
import Beans.Jurado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Beans.LineaDocente;
import Beans.Observacion;
import Beans.Tesis;
import Modelo.exceptions.IllegalOrphanException;
import Modelo.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JhanxD
 */
public class ConstanciaModel implements Serializable {

    public ConstanciaModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(Constancia constancia) {
        String res="";
        if (constancia.getTesisList() == null) {
            constancia.setTesisList(new ArrayList<Tesis>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LineaDocente docenteLinea = constancia.getDocenteLinea();
            if (docenteLinea != null) {
                docenteLinea = em.getReference(docenteLinea.getClass(), docenteLinea.getIdRelacion());
                constancia.setDocenteLinea(docenteLinea);
            }
            List<Tesis> attachedTesisList = new ArrayList<Tesis>();
            for (Tesis tesisListTesisToAttach : constancia.getTesisList()) {
                tesisListTesisToAttach = em.getReference(tesisListTesisToAttach.getClass(), tesisListTesisToAttach.getIdTesis());
                attachedTesisList.add(tesisListTesisToAttach);
            }
            constancia.setTesisList(attachedTesisList);
            em.persist(constancia);
            if (docenteLinea != null) {
                docenteLinea.getConstanciaList().add(constancia);
                docenteLinea = em.merge(docenteLinea);
            }
            for (Tesis tesisListTesis : constancia.getTesisList()) {
                Constancia oldCodigoCOfTesisListTesis = tesisListTesis.getCodigoC();
                tesisListTesis.setCodigoC(constancia);
                tesisListTesis = em.merge(tesisListTesis);
                if (oldCodigoCOfTesisListTesis != null) {
                    oldCodigoCOfTesisListTesis.getTesisList().remove(tesisListTesis);
                    oldCodigoCOfTesisListTesis = em.merge(oldCodigoCOfTesisListTesis);
                }
            }
            em.getTransaction().commit();
            return res="Registrado";
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Constancia constancia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Constancia persistentConstancia = em.find(Constancia.class, constancia.getIdConstancia());
            LineaDocente docenteLineaOld = persistentConstancia.getDocenteLinea();
            LineaDocente docenteLineaNew = constancia.getDocenteLinea();
            List<Tesis> tesisListOld = persistentConstancia.getTesisList();
            List<Tesis> tesisListNew = constancia.getTesisList();
            List<String> illegalOrphanMessages = null;
            for (Tesis tesisListOldTesis : tesisListOld) {
                if (!tesisListNew.contains(tesisListOldTesis)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tesis " + tesisListOldTesis + " since its codigoC field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (docenteLineaNew != null) {
                docenteLineaNew = em.getReference(docenteLineaNew.getClass(), docenteLineaNew.getIdRelacion());
                constancia.setDocenteLinea(docenteLineaNew);
            }
            List<Tesis> attachedTesisListNew = new ArrayList<Tesis>();
            for (Tesis tesisListNewTesisToAttach : tesisListNew) {
                tesisListNewTesisToAttach = em.getReference(tesisListNewTesisToAttach.getClass(), tesisListNewTesisToAttach.getIdTesis());
                attachedTesisListNew.add(tesisListNewTesisToAttach);
            }
            tesisListNew = attachedTesisListNew;
            constancia.setTesisList(tesisListNew);
            constancia = em.merge(constancia);
            if (docenteLineaOld != null && !docenteLineaOld.equals(docenteLineaNew)) {
                docenteLineaOld.getConstanciaList().remove(constancia);
                docenteLineaOld = em.merge(docenteLineaOld);
            }
            if (docenteLineaNew != null && !docenteLineaNew.equals(docenteLineaOld)) {
                docenteLineaNew.getConstanciaList().add(constancia);
                docenteLineaNew = em.merge(docenteLineaNew);
            }
            for (Tesis tesisListNewTesis : tesisListNew) {
                if (!tesisListOld.contains(tesisListNewTesis)) {
                    Constancia oldCodigoCOfTesisListNewTesis = tesisListNewTesis.getCodigoC();
                    tesisListNewTesis.setCodigoC(constancia);
                    tesisListNewTesis = em.merge(tesisListNewTesis);
                    if (oldCodigoCOfTesisListNewTesis != null && !oldCodigoCOfTesisListNewTesis.equals(constancia)) {
                        oldCodigoCOfTesisListNewTesis.getTesisList().remove(tesisListNewTesis);
                        oldCodigoCOfTesisListNewTesis = em.merge(oldCodigoCOfTesisListNewTesis);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = constancia.getIdConstancia();
                if (findConstancia(id) == null) {
                    throw new NonexistentEntityException("The constancia with id " + id + " no longer exists.");
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
            Constancia constancia;
            try {
                constancia = em.getReference(Constancia.class, id);
                constancia.getIdConstancia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The constancia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tesis> tesisListOrphanCheck = constancia.getTesisList();
            for (Tesis tesisListOrphanCheckTesis : tesisListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Constancia (" + constancia + ") cannot be destroyed since the Tesis " + tesisListOrphanCheckTesis + " in its tesisList field has a non-nullable codigoC field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            LineaDocente docenteLinea = constancia.getDocenteLinea();
            if (docenteLinea != null) {
                docenteLinea.getConstanciaList().remove(constancia);
                docenteLinea = em.merge(docenteLinea);
            }
            em.remove(constancia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Constancia> findConstanciaEntities() {
        return findConstanciaEntities(true, -1, -1);
    }

    public List<Constancia> findConstanciaEntities(int maxResults, int firstResult) {
        return findConstanciaEntities(false, maxResults, firstResult);
    }

    private List<Constancia> findConstanciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Constancia.class));
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

    public Constancia findConstancia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Constancia.class, id);
        } finally {
            em.close();
        }
    }

    public int getConstanciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Constancia> rt = cq.from(Constancia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public String cotejar(String facul) {
       EntityManager em = getEntityManager();
        String lista="";
        try {
            em = getEntityManager();
            String jpql="SELECT COUNT(d.idConstancia) FROM Constancia d WHERE d.tituloTesis= ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, facul);
            lista = String.valueOf((long)q.getSingleResult());
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;
    }

    public List<Constancia> findConstancias2(String codigo) {
        EntityManager em = getEntityManager();
        List<Constancia> lista=new ArrayList<>();
        try {
            em = getEntityManager();
            String jpql="SELECT f FROM Constancia f WHERE f.nroConstancia = ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, codigo);
            lista = q.getResultList();
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return lista;

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
