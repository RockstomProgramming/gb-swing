package br.com.finan.util;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.entidade.Entidade;
import br.com.finan.entidade.enumerator.EnumStatus;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Wesley Luiz
 */
public final class HibernateUtil {

    public static EntityManagerFactory factory;
    
    public static Session getSessao() {
        return (Session) factory.createEntityManager().getDelegate();
    }

    public static CriteriaBuilder getCriteriaBuilder(Class<? extends Entidade> entidade) {
        return new CriteriaBuilder(getSessao().createCriteria(entidade));
    }
    
    public static void salvar(Entidade entidade) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(entidade);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    public static void alterar(Entidade entidade) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.refresh(entidade);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    public static void remover(Entidade entidade) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.remove(entidade);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    public static void inativar(Long id, String classe) {
        Query query = getSessao().createQuery("UPDATE ".concat(classe).concat(" SET status = :status WHERE id = :id"));
        query.setParameter("status", EnumStatus.INATIVO);
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
