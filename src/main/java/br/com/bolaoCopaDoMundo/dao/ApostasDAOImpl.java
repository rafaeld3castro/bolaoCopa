package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.bolaoCopaDoMundo.domain.Apostas;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;

@Repository
public class ApostasDAOImpl implements ApostasDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public Long setMaxId() {
		Query query = entityManager.createNamedQuery("Apostas.findMaxId");
		if (query.getSingleResult() == null) {
			return (long) 1;
		} else {
			return (Long) query.getSingleResult() + 1;
		}
	}

	@Override
	public Apostas salvar(Apostas aposta) {
		if (aposta.getId() == null || aposta.getId() == 0) {
			aposta.setId(setMaxId());
		}
		return entityManager.merge(aposta);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Apostas> findByUsername(String username) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM Apostas a JOIN FETCH a.participante p WHERE p.username =:username");
		sql.append(" ORDER BY a.id");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("username", username);
		return query.getResultList();
	}

	@Override
	public Long countByParticipanteAndGolNull(Participante participante) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(a) FROM Apostas a WHERE a.participante.id =:idParticipante AND (a.gol1 IS NULL OR a.gol2 IS NULL)");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idParticipante", participante.getId());
		return (Long) query.getSingleResult();
	}
	
	@Override
	public Apostas getApostaByJogoParticipante(Participante participante, Jogos jogo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM Apostas a WHERE a.participante.id =:idParticipante AND  a.jogos.id =:idJogo");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idParticipante", participante.getId());
		query.setParameter("idJogo", jogo.getId());
		return (Apostas) query.getSingleResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Apostas> findByJogo(Long idJogo,int first, int maxPerPage) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM Apostas a WHERE a.jogos.id =:idJogo order by a.participante.nome");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idJogo", idJogo);
		query.setFirstResult(first);
		query.setMaxResults(maxPerPage);
		return  query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Apostas> findByJogoResultado(Jogos jogo,int first, int maxPerPage) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM Apostas a WHERE a.jogos.id =:idJogo ");
		
		if(jogo.getGol1() != null)
			sql.append(" and a.gol1 =:gol1 ");
		
		if(jogo.getGol2() != null)
			sql.append(" and  a.gol2 =:gol2 ");
		
		sql.append(" order by a.participante.nome");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idJogo", jogo.getId());
		
		if(jogo.getGol1() != null)
			query.setParameter("gol1", jogo.getGol1());
		
		if(jogo.getGol2() != null)
			query.setParameter("gol2", jogo.getGol2());
		query.setFirstResult(first);
		query.setMaxResults(maxPerPage);
		return  query.getResultList();
	}
	
	@Override
	public int countByJogoResultado(Jogos jogo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM Apostas a WHERE a.jogos.id =:idJogo ");
		
		if(jogo.getGol1() != null)
			sql.append(" and a.gol1 =:gol1 ");
		
		if(jogo.getGol2() != null)
			sql.append(" and  a.gol2 =:gol2 ");
		
		sql.append(" order by a.participante.nome");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idJogo", jogo.getId());
		
		if(jogo.getGol1() != null)
			query.setParameter("gol1", jogo.getGol1());
		
		if(jogo.getGol2() != null)
			query.setParameter("gol2", jogo.getGol2());
		return  query.getResultList().size();
	}

	@Override
	public int countByJogo(Long idJogo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM Apostas a WHERE a.jogos.id =:idJogo order by a.participante.nome");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idJogo", idJogo);
		return  query.getResultList().size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Apostas> findByIdParticipante(Long idParticipante) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a FROM Apostas a JOIN FETCH a.participante p WHERE p.id =:idParticipante");
		sql.append(" ORDER BY a.id");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idParticipante", idParticipante);
		return query.getResultList();
	}

}
