package br.com.bolaoCopaDoMundo.dao;



import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Selecao;

@Repository
public class SelecaoDAOImpl implements SelecaoDAO{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Selecao salvar(Selecao secao) {
		if(secao.getId()==null||secao.getId()==0){
			secao.setId(setMaxId());			
		}
		return entityManager.merge(secao);
	}

	public Long setMaxId() {
		Query query = entityManager.createNamedQuery("Selecao.findMaxId");
		if(query.getSingleResult() == null){
			return (long) 1;
		}
		else return (Long) query.getSingleResult() + 1;
	}

	@Override
	public void excluir(Selecao selecao) {		
			entityManager.remove(entityManager.getReference(Selecao.class, selecao.getId()));
	}

	@Override
	public Selecao findByNome(String nome) {
		
		Query query = entityManager.createNamedQuery("Selecao.findByNome");
		query.setParameter("nome", nome.toUpperCase());
		
		if (query.getResultList().size() == 1){
			return (Selecao) query.getResultList().get(0);
		}else{
			return null;
		}
		
	}
	
	@Override
	public Selecao findNomeByGrupo(String nome, Grupo grupo) {
		
		Query query = entityManager.createNamedQuery("Selecao.findNomeByGrupo");
		query.setParameter("nome", nome.toUpperCase());
		query.setParameter("grupo", grupo);
		if (query.getResultList().size() == 1){
			return (Selecao) query.getResultList().get(0);
		}else{
			return null;
		}
		
	}

	@Override
	public Selecao findById(Long id) {
		Query query = entityManager.createNamedQuery("Selecao.findById");
		query.setParameter("id", id);
		return (Selecao) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Selecao> findByGrupo(Grupo grupo) {
		Query query = entityManager.createNamedQuery("Selecao.findByGrupo", Selecao.class);
		query.setParameter("grupo", grupo);
		return query.getResultList();
	
	}
	
	@Transactional
	@Override
	public int countByGrupo(Grupo grupo, String nome) {
		Query query = entityManager.createNamedQuery("Selecao.countByGrupo");
		query.setParameter("grupo", grupo);
		query.setParameter("nome","%" + nome.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();	
	}
	
	@Transactional
	@Override
	public int countByNome( String nome) {
		Query query = entityManager.createNamedQuery("Selecao.countByNome");
		query.setParameter("nome","%" + nome.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();	
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Selecao> findByGrupoPaginado(String nome,Grupo grupo,int inicio,int maxPerPage) {
		Query query = entityManager.createNamedQuery("Selecao.findByGrupoPaginado", Selecao.class);
		query.setParameter("nome","%" + nome.toUpperCase() + "%");
		query.setParameter("grupo", grupo);
		query.setFirstResult(inicio);
		query.setMaxResults(maxPerPage);	
		return query.getResultList();		
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Selecao> findByCriterios(String nome,Grupo grupo) {
		String consulta = "Selecao.findByNome";
		
		if(grupo!=null)
			consulta = "Selecao.findByGrupoPaginado";
		
		Query query = entityManager.createNamedQuery(consulta, Selecao.class);
		query.setParameter("nome","%" + nome.toUpperCase() + "%");
		if(grupo!=null)
			query.setParameter("grupo", grupo);	
		return query.getResultList();		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Selecao> search(String nome) {
		Query query = entityManager.createNamedQuery("Selecao.searchByNome");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");		
		return query.getResultList(); 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Selecao> findSelecao(String nome, int startingAt, int maxPerPage) {
			
			Query query = entityManager.createNamedQuery("Selecao.findByNome");
			query.setParameter("nome", "%" + nome.toUpperCase() + "%");
			query.setFirstResult(startingAt);
			query.setMaxResults(maxPerPage);			
			return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Selecao> findAllSelecao(int inicio, int max) {
		Query query = entityManager.createNamedQuery("Selecao.findAll");
		query.setFirstResult(inicio);
		query.setMaxResults(max);
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Selecao> search(String nome, int first, int rows) {
		Query query = entityManager.createNamedQuery("Selecao.searchByNome");		
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList(); 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Selecao> findSelecaoNaoClassificadaByGrupo(Grupo grupo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM SELECAO s INNER JOIN CLASSIFICACAOGRUPO cg ON cg.IDGRUPO = s.IDGRUPO WHERE s.IDGRUPO = :idGrupo AND s.IDSELECAO NOT IN (cg.POSICAO1, cg.POSICAO2)");
		Query query = entityManager.createNativeQuery(sql.toString(), Selecao.class);
		query.setParameter("idGrupo", grupo.getId());
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Selecao> findSelecaoNaoClassificadaByGrupoParticipante(
			Grupo grupo, Participante participante) {
		
		StringBuilder consulta = new StringBuilder("select * from selecao, ( ");
			consulta.append(" select posicao1, posicao2 from apostaclassificacaogrupo ");
			consulta.append(" where apostaclassificacaogrupo.IDGRUPO =  " + grupo.getId());
			consulta.append(" and apostaclassificacaogrupo.IDPARTICIPANTE =  "+participante.getId());
			consulta.append(" )  apostagrupo ");
			
			consulta.append(" where selecao.IDGRUPO =  "  + grupo.getId());
			consulta.append(" and selecao.IDSELECAO <> apostagrupo.posicao1 ");
			consulta.append(" and selecao.IDSELECAO <> apostagrupo.posicao2 ");
			consulta.append(" ORDER BY selecao.idselecao");
			
			System.out.println(consulta.toString());
			Query query = entityManager.createNativeQuery(consulta.toString(), Selecao.class);
			
			return query.getResultList();
	}

}
