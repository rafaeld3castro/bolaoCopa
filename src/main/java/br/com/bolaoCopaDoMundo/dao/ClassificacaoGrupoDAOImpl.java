package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.bolaoCopaDoMundo.domain.ClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Selecao;

@Repository
public class ClassificacaoGrupoDAOImpl implements ClassificacaoGrupoDAO{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public ClassificacaoGrupo salvar(ClassificacaoGrupo classificacaoGrupo) {
		if(classificacaoGrupo.getId()==null||classificacaoGrupo.getId()==0){
			classificacaoGrupo.setId(setMaxId());			
		}
		return entityManager.merge(classificacaoGrupo);
	}

	public Long setMaxId() {
		Query query = entityManager.createNamedQuery("ClassificacaoGrupo.findMaxId");
		if (query.getSingleResult() == null) {
			return (long) 1;
		} else {
			return (Long) query.getSingleResult() + 1;
		}
	}

	@Override
	public void excluir(ClassificacaoGrupo classificacaoGrupo) {		
		entityManager.remove(entityManager.getReference(ClassificacaoGrupo.class, classificacaoGrupo.getId()));
	}

	@Override
	public Selecao findByNome(String nome) {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ClassificacaoGrupo> findByCriterios( Grupo grupo) {
		StringBuilder sql = new StringBuilder();
		sql.append("Select j from ClassificacaoGrupo j where 1=1 ");
		
		if(grupo != null)
			sql.append(" AND j.grupo = :grupo");

		sql.append(" ORDER BY  j.id");
		
		Query query = entityManager.createQuery(sql.toString());

		if(grupo != null)
			query.setParameter("grupo", grupo);
			
		return query.getResultList();
	}

	@Override
	public ClassificacaoGrupo findById(Long id) {
		Query query = entityManager.createNamedQuery("ClassificacaoGrupo.findById");
		query.setParameter("id", id);
		return (ClassificacaoGrupo) query.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClassificacaoGrupo> findAllOk() {
		Query query = entityManager.createNamedQuery("ClassificacaoGrupo.findAllOk");		
		return query.getResultList(); 
	}
	
	@Override
	public ClassificacaoGrupo findByGrupo(Grupo grupo) {
		try {
		Query query = entityManager.createNamedQuery("ClassificacaoGrupo.findByGrupo");
		query.setParameter("idGrupo", grupo.getId());
		return (ClassificacaoGrupo) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
