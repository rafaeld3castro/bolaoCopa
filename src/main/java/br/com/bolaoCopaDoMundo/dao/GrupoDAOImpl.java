/**
 * 
 */
package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.com.bolaoCopaDoMundo.domain.Grupo;

@Repository
public class GrupoDAOImpl implements GrupoDAO {
	
	static Logger logger = Logger.getLogger(GrupoDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Long getMaxId() {
		Query query = entityManager.createNamedQuery("Grupo.findMaxId");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	@Override
	public Grupo salvar(Grupo grupo) {
		if (grupo.getId() == null || grupo.getId() == 0){
			grupo.setId(getMaxId());
		}
		return entityManager.merge(grupo);
	}
	
	@Override
	public void excluir(Grupo grupo) {
		entityManager.remove(entityManager.getReference(Grupo.class, grupo.getId()));
	}
	
	@Override
	public int count(String nome) {
		Query query = entityManager.createNamedQuery("Grupo.countByNome");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Grupo> search(String nome, int first, int rows) {
		Query query = entityManager.createNamedQuery("Grupo.searchByNome");		
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList(); 
	}
	
	@Override
	public Grupo findByGrupo(Grupo grupo) {
		Query query = entityManager.createNamedQuery("Grupo.findByGrupo");
		query.setParameter("id", grupo.getId());
		return (Grupo) query.getSingleResult();
	}
	
	@Override
	public Grupo findByNome(String nome) {
		try {
			Query query = entityManager.createNamedQuery("Grupo.findByNome", Grupo.class);
			query.setParameter("nome",nome.toUpperCase());
			return (Grupo) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Grupo findById(Long id) {
		try {
			Query query = entityManager.createNamedQuery("Grupo.findById", Grupo.class);
			query.setParameter("id",id );
			return (Grupo) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Grupo> searchByNome(String nome) {
		Query query = entityManager.createNamedQuery("Grupo.searchByNome");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Grupo> searchByNome(String nome, Integer sistema) {
		Query query = entityManager.createNamedQuery("Grupo.searchByNome");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		query.setParameter("sistema", sistema);
		return query.getResultList();
	}
	
	public void habilitaFiltros(String nomeSigla){
		org.hibernate.Session session = entityManager.unwrap(org.hibernate.Session.class);
		session.enableFilter("siglaSistema").setParameter("siglaSistema", nomeSigla);
		session.getNamedQuery("Grupo.searchByNome").setParameter("sigla", nomeSigla);
	}
	
	@Override
	public void desabilitaFiltros(){
		org.hibernate.Session session = entityManager.unwrap(org.hibernate.Session.class);
		session.disableFilter("siglaSistema");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Grupo> search(String nome, String sigla, int first, int rows) {
		Query query = entityManager.createNamedQuery("Grupo.searchBySiglaNome");		
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		query.setParameter("sigla", "%"+ sigla.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList(); 
	}
	
	@Override
	public int count(String nome, String sigla) {
		Query query = entityManager.createNamedQuery("Grupo.count");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		query.setParameter("sigla", "%" + sigla.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Grupo> findAll() {
		return entityManager.createNamedQuery("Grupo.findAll").getResultList();
	}
	
	
}
