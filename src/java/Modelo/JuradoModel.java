/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Beans.Constancia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Beans.Tesis;
import Beans.Docente;
import Beans.Jurado;
import Beans.LineaDocente;
import Beans.LineaInvestigacion;
import Beans.Observacion;
import Modelo.exceptions.IllegalOrphanException;
import Modelo.exceptions.NonexistentEntityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JhanxD
 */
public class JuradoModel implements Serializable {

    public JuradoModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jurado jurado) {
        if (jurado.getObservacionList() == null) {
            jurado.setObservacionList(new ArrayList<Observacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tesis idTesis = jurado.getIdTesis();
            if (idTesis != null) {
                idTesis = em.getReference(idTesis.getClass(), idTesis.getIdTesis());
                jurado.setIdTesis(idTesis);
            }
            Docente idDocente = jurado.getIdDocente();
            if (idDocente != null) {
                idDocente = em.getReference(idDocente.getClass(), idDocente.getIdDocente());
                jurado.setIdDocente(idDocente);
            }
            List<Observacion> attachedObservacionList = new ArrayList<Observacion>();
            for (Observacion observacionListObservacionToAttach : jurado.getObservacionList()) {
                observacionListObservacionToAttach = em.getReference(observacionListObservacionToAttach.getClass(), observacionListObservacionToAttach.getIdObservacion());
                attachedObservacionList.add(observacionListObservacionToAttach);
            }
            jurado.setObservacionList(attachedObservacionList);
            em.persist(jurado);
            if (idTesis != null) {
                idTesis.getJuradoList().add(jurado);
                idTesis = em.merge(idTesis);
            }
            if (idDocente != null) {
                idDocente.getJuradoList().add(jurado);
                idDocente = em.merge(idDocente);
            }
            for (Observacion observacionListObservacion : jurado.getObservacionList()) {
                Jurado oldIdJuradoOfObservacionListObservacion = observacionListObservacion.getIdJurado();
                observacionListObservacion.setIdJurado(jurado);
                observacionListObservacion = em.merge(observacionListObservacion);
                if (oldIdJuradoOfObservacionListObservacion != null) {
                    oldIdJuradoOfObservacionListObservacion.getObservacionList().remove(observacionListObservacion);
                    oldIdJuradoOfObservacionListObservacion = em.merge(oldIdJuradoOfObservacionListObservacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jurado jurado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jurado persistentJurado = em.find(Jurado.class, jurado.getRelacion());
            Tesis idTesisOld = persistentJurado.getIdTesis();
            Tesis idTesisNew = jurado.getIdTesis();
            Docente idDocenteOld = persistentJurado.getIdDocente();
            Docente idDocenteNew = jurado.getIdDocente();
            List<Observacion> observacionListOld = persistentJurado.getObservacionList();
            List<Observacion> observacionListNew = jurado.getObservacionList();
            List<String> illegalOrphanMessages = null;
            for (Observacion observacionListOldObservacion : observacionListOld) {
                if (!observacionListNew.contains(observacionListOldObservacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Observacion " + observacionListOldObservacion + " since its idJurado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTesisNew != null) {
                idTesisNew = em.getReference(idTesisNew.getClass(), idTesisNew.getIdTesis());
                jurado.setIdTesis(idTesisNew);
            }
            if (idDocenteNew != null) {
                idDocenteNew = em.getReference(idDocenteNew.getClass(), idDocenteNew.getIdDocente());
                jurado.setIdDocente(idDocenteNew);
            }
            List<Observacion> attachedObservacionListNew = new ArrayList<Observacion>();
            for (Observacion observacionListNewObservacionToAttach : observacionListNew) {
                observacionListNewObservacionToAttach = em.getReference(observacionListNewObservacionToAttach.getClass(), observacionListNewObservacionToAttach.getIdObservacion());
                attachedObservacionListNew.add(observacionListNewObservacionToAttach);
            }
            observacionListNew = attachedObservacionListNew;
            jurado.setObservacionList(observacionListNew);
            jurado = em.merge(jurado);
            if (idTesisOld != null && !idTesisOld.equals(idTesisNew)) {
                idTesisOld.getJuradoList().remove(jurado);
                idTesisOld = em.merge(idTesisOld);
            }
            if (idTesisNew != null && !idTesisNew.equals(idTesisOld)) {
                idTesisNew.getJuradoList().add(jurado);
                idTesisNew = em.merge(idTesisNew);
            }
            if (idDocenteOld != null && !idDocenteOld.equals(idDocenteNew)) {
                idDocenteOld.getJuradoList().remove(jurado);
                idDocenteOld = em.merge(idDocenteOld);
            }
            if (idDocenteNew != null && !idDocenteNew.equals(idDocenteOld)) {
                idDocenteNew.getJuradoList().add(jurado);
                idDocenteNew = em.merge(idDocenteNew);
            }
            for (Observacion observacionListNewObservacion : observacionListNew) {
                if (!observacionListOld.contains(observacionListNewObservacion)) {
                    Jurado oldIdJuradoOfObservacionListNewObservacion = observacionListNewObservacion.getIdJurado();
                    observacionListNewObservacion.setIdJurado(jurado);
                    observacionListNewObservacion = em.merge(observacionListNewObservacion);
                    if (oldIdJuradoOfObservacionListNewObservacion != null && !oldIdJuradoOfObservacionListNewObservacion.equals(jurado)) {
                        oldIdJuradoOfObservacionListNewObservacion.getObservacionList().remove(observacionListNewObservacion);
                        oldIdJuradoOfObservacionListNewObservacion = em.merge(oldIdJuradoOfObservacionListNewObservacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jurado.getRelacion();
                if (findJurado(id) == null) {
                    throw new NonexistentEntityException("The jurado with id " + id + " no longer exists.");
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
            Jurado jurado;
            try {
                jurado = em.getReference(Jurado.class, id);
                jurado.getRelacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jurado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Observacion> observacionListOrphanCheck = jurado.getObservacionList();
            for (Observacion observacionListOrphanCheckObservacion : observacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jurado (" + jurado + ") cannot be destroyed since the Observacion " + observacionListOrphanCheckObservacion + " in its observacionList field has a non-nullable idJurado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tesis idTesis = jurado.getIdTesis();
            if (idTesis != null) {
                idTesis.getJuradoList().remove(jurado);
                idTesis = em.merge(idTesis);
            }
            Docente idDocente = jurado.getIdDocente();
            if (idDocente != null) {
                idDocente.getJuradoList().remove(jurado);
                idDocente = em.merge(idDocente);
            }
            em.remove(jurado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jurado> findJuradoEntities() {
        return findJuradoEntities(true, -1, -1);
    }

    public List<Jurado> findJuradoEntities(int maxResults, int firstResult) {
        return findJuradoEntities(false, maxResults, firstResult);
    }

    private List<Jurado> findJuradoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jurado.class));
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

    public Jurado findJurado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jurado.class, id);
        } finally {
            em.close();
        }
    }

    public int getJuradoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jurado> rt = cq.from(Jurado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Object[] asignar(int asesor) {
        
        EntityManager em = getEntityManager();
        List<LineaDocente> lista=new ArrayList<>();
         Object[] res = null;
        try {
            res = new Object[2];
            em = getEntityManager();
            String jpql="SELECT d FROM LineaDocente d WHERE d.idRelacion= ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, asesor);
            lista = q.getResultList();
            for (int i = 0; i < lista.size(); i++) {
                res[0]=String.valueOf(lista.get(i).getLineaId().getIdLinea());
                res[1]=String.valueOf(lista.get(i).getDocenteId().getIdDocente());
            }
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return res;
    }

    public Object[] buscar(LineaInvestigacion findLineaInvestigacion, Docente findDocente, Constancia codigo) {
        EntityManager em = getEntityManager();
        List<LineaDocente> lista=new ArrayList<>();
        Object[] res=null;
        try {
            res = new Object[2];
            em = getEntityManager();
            String jpql="SELECT d FROM LineaDocente d JOIN d.docenteId doc WHERE (d.docenteId!= ?1) AND (d.lineaId= ?2) ORDER BY doc.c ASC, doc.ga ASC, doc.fechaIngreso ASC";
            Query q = em.createQuery(jpql);
            q.setParameter(1, findDocente);
            q.setParameter(2, findLineaInvestigacion);
            lista = q.setMaxResults(3).getResultList();
            res[0]=lista;
            
            jpql="SELECT d FROM Tesis d WHERE d.codigoC = ?1";
            q = em.createQuery(jpql);
            q.setParameter(1, codigo);
            List<Tesis> lis = q.getResultList();
            res[1]=lis.get(0).getIdTesis();
            System.out.println("cdddc"+res[1]);
            
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return res;


    }
    
//    public int metodo (){
//        int res=0;
//        EntityManager em = getEntityManager();
//        List<Tesis> lista=new ArrayList<>();
//        try {
//            em = getEntityManager();
//            String jpql="SELECT d FROM Tesis d WHERE d.codigoC = ?1";
//            Query q = em.createQuery(jpql);
//            q.setParameter(1, findDocente);
//            q.setParameter(2, findLineaInvestigacion);
//            lista = q.setMaxResults(3).getResultList();
//            res[0]=lista;
//            
//            jpql="SELECT d FROM Tesis d WHERE d.codigoC = ?1";
//            q = em.createQuery(jpql);
//            q.setParameter(1, codigo);
//            List<Tesis> lis = q.getResultList();
//            res[1]=lis.get(0).getIdTesis();
//            System.out.println("cdddc"+res[1]);
//            
//        } catch(Exception e) {}
//        if (em != null) {
//            em.close();
//        }
//        
//        return res;
//    }

    public String cotejar(Observacion obs) {
        EntityManager em = getEntityManager();
        List<Observacion> lista=new ArrayList<>();
        String res = "", respuesta="";
        try {
            em = getEntityManager();
            String jpql="SELECT d FROM Observacion d WHERE (d.idTesis = ?1 AND d.idJurado = ?2 )";
            Query q = em.createQuery(jpql);
            q.setParameter(1, obs.getIdTesis());
            q.setParameter(2, obs.getIdJurado());
            lista = q.getResultList();
            System.out.println("ass"+lista);
            for (int i = 0; i < lista.size(); i++) {
                res=lista.get(i).getLevantada();
                System.out.println("habia "+res);
            }
            if (res.equals("")) {
                    respuesta="notiene";
                }else if (res.equals("SI")) {
                    respuesta="tuvo";
                }else{
                    respuesta="tiene";
                }
                System.out.println(res);
            
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return respuesta;
    }

    public Object[] ordenar(Constancia asesor) {
        EntityManager em = getEntityManager();
        List<Tesis> lista=new ArrayList<>();
        Object[] respuesta=null;
        try {
            respuesta = new Object[2];
            em = getEntityManager();
            String jpql="SELECT d FROM Tesis d WHERE d.codigoC = ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, asesor);
            System.out.println("ok");
            lista = q.setMaxResults(1).getResultList();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = lista.get(0).getFormatoA();
                System.out.println(fecha);
                Date Formato = fecha;
                System.out.println("cam"+Formato);
                Calendar cal = Calendar.getInstance();
                cal.setTime(Formato);
                cal.add(Calendar.DAY_OF_YEAR, 15);
                Date Formato1 = cal.getTime();

                // FECHA DEL SISTEMA
                Calendar c1 = Calendar.getInstance();
                String dia = Integer.toString(c1.get(Calendar.DATE));
                String mes1 = Integer.toString(c1.get(Calendar.MONTH) + 1);
                String año = Integer.toString(c1.get(Calendar.YEAR));
                String dia2 = "", mes2 = "";
                if (dia.length() == 1) {
                    dia2 = "0" + dia;
                } else {
                    dia2 = dia;
                }
                if (mes1.length() == 1) {
                    mes2 = "0" + mes1;
                } else {
                    mes2 = mes1;
                }
                Date Formato2 = sdf.parse(año + "-" + mes2 + "-" + dia2);

                // COMPARAR FECHAS 
                System.out.println("Fecha base " + Formato1 + " fecha actual " + Formato2);
                int respuestafecha = Formato1.compareTo(Formato2);
                System.out.println(respuestafecha);
                if (respuestafecha == -1) {
                    respuesta[0] = "Plazo de entrega de Formato A terminado, ya no se pueden aplicar observaciones. FECHA LIMITE: " + Formato1;
                } else {
                    respuesta[0] = "ok";
                }
            
            jpql="SELECT d FROM Jurado d  JOIN d.idTesis t  WHERE t.codigoC = ?1";
            q = em.createQuery(jpql);
            q.setParameter(1, asesor);
            List<Jurado> lis = q.getResultList();
            respuesta[1]=lis;
            
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return respuesta;

    }

    public String levantarObservacion(int juardo, Tesis findTesis) {
        EntityManager em = getEntityManager();
        List<Observacion> lista=new ArrayList<>();
        String respuesta="";
        try {
            em = getEntityManager();
            String jpql="SELECT d FROM Observacion d JOIN d.idJurado j WHERE (d.idTesis = ?1 AND j.relacion = ?2 )";
            Query q = em.createQuery(jpql);
            q.setParameter(1, findTesis);
            q.setParameter(2, juardo);
            lista = q.getResultList();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = lista.get(0).getFechaFin();
                int codigo = lista.get(0).getIdObservacion();
                System.out.println("dds"+codigo);
                Date Formato = fecha;
                // FECHA DEL SISTEMA
                Calendar c1 = Calendar.getInstance();
                String dia = Integer.toString(c1.get(Calendar.DATE));
                String mes1 = Integer.toString(c1.get(Calendar.MONTH) + 1);
                String año = Integer.toString(c1.get(Calendar.YEAR));
                String dia2 = "", mes2 = "";
                if (dia.length() == 1) {
                    dia2 = "0" + dia;
                } else {
                    dia2 = dia;
                }
                if (mes1.length() == 1) {
                    mes2 = "0" + mes1;
                } else {
                    mes2 = mes1;
                }
                Date Formato2 = sdf.parse(año + "-" + mes2 + "-" + dia2);
                // COMPARAR FECHAS 
                System.out.println("Fecha base " + Formato + " fecha actual " + Formato2);
                int respuestafecha = Formato.compareTo(Formato2);
                System.out.println(respuestafecha);
                if (respuestafecha == -1) {
                    respuesta = "Plazo para levantar observaciones finalizado. FECHA LIMITE: " + Formato;
                } else {
                    ObservacionModel mo = new ObservacionModel(emf);
                    Observacion obs = mo.findObservacion(codigo);
                    obs.setLevantada("SI");
                    mo.edit(obs);
//                    String sql = "UPDATE Observacion a SET a.levantada = ?1 WHERE a.idObservacion = ?2 ";
//                    Query a = em.createQuery(sql);
//                    a.setParameter(1, "SI");
//                    a.setParameter(2, codigo);
                    respuesta = "Levantado";
                    
                }            
                System.out.println("Observacion " + respuesta);
        } catch(Exception e) {}
        if (em != null) {
            em.close();
        }
        return respuesta;

    }
    
}
