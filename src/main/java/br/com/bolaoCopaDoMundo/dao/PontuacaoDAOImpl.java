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

import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Pontuacao;

@Repository
public class PontuacaoDAOImpl implements PontuacaoDAO {
	
	static Logger logger = Logger.getLogger(PontuacaoDAOImpl.class);
	
	//o entity manager chama uma instancia unica da entidade a ser persistida.
	@PersistenceContext
	private EntityManager entityManager;
	
	private Long getMaxId() {
		Query query = entityManager.createNamedQuery("Pontuacao.findMaxId");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	@Override
	public Pontuacao salvar(Pontuacao pontuacao) {
		if (pontuacao.getId() == null || pontuacao.getId() == 0){
			pontuacao.setId(getMaxId());
		}
		return entityManager.merge(pontuacao);
	}	

	@Override
	public void excluir(Pontuacao pontuacao) {
		entityManager.remove(entityManager.getReference(Pontuacao.class, pontuacao.getId()));
	}
	
	@Override
	public int count() {
		Query query = entityManager.createNamedQuery("Pontuacao.count");
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Pontuacao> search(String nome, int first, int rows) {
		Query query = entityManager.createNamedQuery("Pontuacao.searchByNome");		
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList(); 
	}
	
	@Override
	public Pontuacao findByNome(String nome) {
		try {
			Query query = entityManager.createNamedQuery("Pontuacao.findByNome", Pontuacao.class);
			query.setParameter("nome",nome.toUpperCase());
			return (Pontuacao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Pontuacao findById(Long id) {
		try {
			Query query = entityManager.createNamedQuery("Pontuacao.findById", Pontuacao.class);
			query.setParameter("id",id );
			return (Pontuacao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pontuacao> searchByNome(String nome) {
		Query query = entityManager.createNamedQuery("Pontuacao.searchByNome");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pontuacao> searchByNome(String nome, Integer sistema) {
		Query query = entityManager.createNamedQuery("Pontuacao.searchByNome");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		query.setParameter("sistema", sistema);
		return query.getResultList();
	}
	
	public void habilitaFiltros(String nomeSigla){
		org.hibernate.Session session = entityManager.unwrap(org.hibernate.Session.class);
		session.enableFilter("siglaSistema").setParameter("siglaSistema", nomeSigla);
		session.getNamedQuery("Pontuacao.searchByNome").setParameter("sigla", nomeSigla);
	}
	
	@Override
	public void desabilitaFiltros(){
		org.hibernate.Session session = entityManager.unwrap(org.hibernate.Session.class);
		session.disableFilter("siglaSistema");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pontuacao> search(String nome, String sigla, int first, int rows) {
		Query query = entityManager.createNamedQuery("Pontuacao.searchBySiglaNome");		
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		query.setParameter("sigla", "%"+ sigla.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList(); 
	}
	
	@Override
	public int count(String nome, String sigla) {
		Query query = entityManager.createNamedQuery("Pontuacao.count");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		query.setParameter("sigla", "%" + sigla.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Pontuacao> findAll() {
		return entityManager.createNamedQuery("Pontuacao.findAll").getResultList();
	}

	@Override
	public Pontuacao findByParticipante(Participante participante) {
		try {
			Query query = entityManager.createNamedQuery("Pontuacao.findByParticipante", Pontuacao.class);
			query.setParameter("participante",participante );
			return (Pontuacao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Pontuacao> findAll(int inicio, int maxPerPage) {
		Query query = entityManager.createNamedQuery("Pontuacao.findAll");
		query.setFirstResult(inicio);
		query.setMaxResults(maxPerPage);		
		return query.getResultList();		
	}
	
}
