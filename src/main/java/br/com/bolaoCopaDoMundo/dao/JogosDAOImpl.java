package br.com.bolaoCopaDoMundo.dao;



import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Selecao;

@Repository
public class JogosDAOImpl implements JogosDAO{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Jogos salvar(Jogos jogos) {
		if(jogos.getId()==null||jogos.getId()==0){
			jogos.setId(setMaxId());			
		}
		return entityManager.merge(jogos);
	}

	public Long setMaxId() {
		Query query = entityManager.createNamedQuery("Jogos.findMaxId");
		if (query.getSingleResult() == null) {
			return (long) 1;
		} else {
			return (Long) query.getSingleResult() + 1;
		}
	}

	@Override
	public void excluir(Jogos Jogos) {		
			entityManager.remove(entityManager.getReference(Jogos.class, Jogos.getId()));
	}

	@Override
	public Selecao findByNome(String nome) {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Jogos> findByCriterios(Date datajogo, Grupo grupo) {
		StringBuilder sql = new StringBuilder();
		sql.append("Select j from Jogos j where 1=1 ");
		
		if(datajogo != null)
			sql.append(" AND j.dtJogo = :datajogo");
		if(grupo != null)
			sql.append(" AND j.grupo = :grupo");

		sql.append(" ORDER BY  j.id");
		
		Query query = entityManager.createQuery(sql.toString());
		if(grupo != null)
			query.setParameter("grupo", grupo);
		
		if(datajogo != null)
			query.setParameter("datajogo", datajogo);		

		return query.getResultList();
	}

	@Override
	public Jogos findById(Long id) {
		Query query = entityManager.createNamedQuery("Jogos.findById");
		query.setParameter("id", id);
		return (Jogos) query.getSingleResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Jogos> findAll() {
		StringBuilder sql = new StringBuilder("Select j from Jogos j order by j.id");
		Query query = entityManager.createQuery(sql.toString());
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Jogos> findAllOk() {
		StringBuilder sql = new StringBuilder("Select j from Jogos j where j.flResultadoOk = 1 order by j.id");
		Query query = entityManager.createQuery(sql.toString());
		return query.getResultList();
	}

}
