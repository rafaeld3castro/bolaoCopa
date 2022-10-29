package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.bolaoCopaDoMundo.domain.ApostaClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Participante;

@Repository
public class ApostaClassificacaoGrupoDAOImpl implements ApostaClassificacaoGrupoDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public Long setMaxId() {
		Query query = entityManager.createNamedQuery("ApostaClassificacaoGrupo.findMaxId");
		if (query.getSingleResult() == null) {
			return (long) 1;
		} else {
			return (Long) query.getSingleResult() + 1;
		}
	}

	@Override
	public ApostaClassificacaoGrupo salvar(ApostaClassificacaoGrupo aposta) {
		if (aposta.getId() == null || aposta.getId() == 0) {
			aposta.setId(setMaxId());
		}
		return entityManager.merge(aposta);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ApostaClassificacaoGrupo> findByUsername(String username) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM ApostaClassificacaoGrupo a JOIN FETCH a.participante p WHERE p.username =:username");
		sql.append(" ORDER BY a.id");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("username", username);
		return query.getResultList();
	}

	@Override
	public ApostaClassificacaoGrupo getApostaByGrupoParticipante(Participante participante, Grupo grupo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM ApostaClassificacaoGrupo a WHERE a.participante.id =:idParticipante AND  a.grupo.id =:idGrupo");
		
		try {
			Query query = entityManager.createQuery(sql.toString());
			query.setParameter("idParticipante", participante.getId());
			query.setParameter("idGrupo", grupo.getId());
			
			return (ApostaClassificacaoGrupo) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ApostaClassificacaoGrupo> findByGrupo(Long idGrupo,int first, int maxPerPage) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM ApostaClassificacaoGrupo a WHERE a.grupo.id =:idGrupo order by a.participante.nome");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idGrupo", idGrupo);
		query.setFirstResult(first);
		query.setMaxResults(maxPerPage);
		return  query.getResultList();
	}

	@Override
	public int countByGrupo(Long idGrupo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM ApostaClassificacaoGrupo a WHERE a.grupo.id =:idGrupo order by a.participante.nome");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idGrupo", idGrupo);
		return  query.getResultList().size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ApostaClassificacaoGrupo> findByIdParticipante(Long idParticipante) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM ApostaClassificacaoGrupo a WHERE a.participante.id =:idParticipante  ORDER BY a.grupo.id");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idParticipante", idParticipante);
		return query.getResultList();
	}

	@Override
	public ApostaClassificacaoGrupo findById(Long id) {
		Query query = entityManager.createNamedQuery("ClassificacaoGrupo.findById");
		query.setParameter("id", id);
		return (ApostaClassificacaoGrupo) query.getSingleResult();	}

	@Override
	public int countByApostasPendentes(Participante participante) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM ApostaClassificacaoGrupo a WHERE a.participante.id =:idParticipante order by a.participante.nome");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idParticipante", participante.getId());
		return 8 - query.getResultList().size();
	}

}
