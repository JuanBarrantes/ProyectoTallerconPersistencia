/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.session;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Beans.session.Perfil;
import Beans.session.Usuario;
import Extras.StringEncrypt;
import Modelo.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JhanxD
 */
public class UsuarioModel implements Serializable {

    public UsuarioModel(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfil idperfil = usuario.getIdperfil();
            if (idperfil != null) {
                idperfil = em.getReference(idperfil.getClass(), idperfil.getIdperfil());
                usuario.setIdperfil(idperfil);
            }
            em.persist(usuario);
            if (idperfil != null) {
                idperfil.getUsuarioList().add(usuario);
                idperfil = em.merge(idperfil);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdusuario());
            Perfil idperfilOld = persistentUsuario.getIdperfil();
            Perfil idperfilNew = usuario.getIdperfil();
            if (idperfilNew != null) {
                idperfilNew = em.getReference(idperfilNew.getClass(), idperfilNew.getIdperfil());
                usuario.setIdperfil(idperfilNew);
            }
            usuario = em.merge(usuario);
            if (idperfilOld != null && !idperfilOld.equals(idperfilNew)) {
                idperfilOld.getUsuarioList().remove(usuario);
                idperfilOld = em.merge(idperfilOld);
            }
            if (idperfilNew != null && !idperfilNew.equals(idperfilOld)) {
                idperfilNew.getUsuarioList().add(usuario);
                idperfilNew = em.merge(idperfilNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuario.getIdusuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdusuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Perfil idperfil = usuario.getIdperfil();
            if (idperfil != null) {
                idperfil.getUsuarioList().remove(usuario);
                idperfil = em.merge(idperfil);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Usuario getUserValidation(String login) {
        EntityManager em = getEntityManager();
        List<Usuario> lista = new ArrayList<>();
        String key = "92AE31A79FEEB2A3"; //llave-parametro 1 
        String iv = "0123456789ABCDEF"; // vector de inicialización parametro 2
        Usuario user = null;
        try {
            em = getEntityManager();
            String jpql = "SELECT f FROM Usuario f WHERE f.login= ?1";
            Query q = em.createQuery(jpql);
            q.setParameter(1, login);
            lista = q.getResultList();
            System.out.println("sdfas "+lista);
//            user.setLogin(lista.get(0).getLogin());
//            user.setIdusuario(lista.get(0).getIdusuario());
//            user.setUsuario(lista.get(0).getUsuario());
            
//            
//            user.setEstado(lista.get(0).getEstado());
//            user.setAtendido(lista.get(0).getAtendido());
//            user.setIdperfil(lista.get(0).getIdperfil());
             user=findUsuario(lista.get(0).getIdusuario());
             String pass = "";
            try {
                pass = StringEncrypt.decrypt(key, iv, user.getPass());
            } catch (Exception ex) {
                Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, ex);
            }
            user.setPass(pass);
        } catch (Exception e) {
        }
        if (em != null) {
            em.close();
        }
        return user;
    }

}
