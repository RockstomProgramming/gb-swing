package br.com.finan.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.entidade.Entidade;
import br.com.finan.enumerator.EnumStatus;

/**
 *
 * @author Wesley Luiz
 */
public final class HibernateUtil {

	public static EntityManagerFactory factory;

	public static Session getSessao() {
		return (Session) factory.createEntityManager().getDelegate();
	}

	public static CriteriaBuilder getCriteriaBuilder(final Class<?> entidade) {
		return new CriteriaBuilder(getSessao().createCriteria(entidade));
	}
	
	public static void salvarOuAlterar(final Entidade entidade) {
		if (ObjetoUtil.isReferencia(entidade.getId())) {
			alterar(entidade);
		} else {
			salvar(entidade);
		}
	}

	public static void salvar(final Entidade entidade) {
		final EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		try {
			em.persist(entidade);
			em.getTransaction().commit();
		} catch (final Exception ex) {
			em.getTransaction().rollback();
			Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			em.close();
		}
	}

	public static void alterar(final Entidade entidade) {
		final EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		try {
			em.merge(entidade);
			em.getTransaction().commit();
		} catch (final Exception ex) {
			em.getTransaction().rollback();
			Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			em.close();
		}
	}

	public static void remover(final Entidade entidade) {
		final EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		try {
			em.remove(entidade);
			em.getTransaction().commit();
		} catch (final Exception ex) {
			em.getTransaction().rollback();
			Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			em.close();
		}
	}

	public static void inativar(final Long id, final String classe) {
		final Query query = getSessao().createQuery("UPDATE ".concat(classe).concat(" SET status = :status WHERE id = :id"));
		query.setParameter("status", EnumStatus.INATIVO);
		query.setParameter("id", id);
		query.executeUpdate();
	}
}
