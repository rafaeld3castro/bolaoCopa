package br.com.bolaoCopaDoMundo.dao;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.bolaoCopaDoMundo.domain.Parametro;

@Repository
public class ParametroDAOImpl implements ParametroDAO{

	@PersistenceContext
	private EntityManager entityManager;


	@Override
	public Parametro findByCahve(String chave) {
		
		Query query = entityManager.createNamedQuery("Parametro.findByCahve");
		query.setParameter("chave", chave.toUpperCase());
		
		if (query.getResultList().size() == 1){
			return (Parametro) query.getResultList().get(0);
		}else{
			return null;
		}
		
	}
	

	@Override
	public Parametro findById(Long id) {
		Query query = entityManager.createNamedQuery("Parametro.findById");
		query.setParameter("id", id);
		return (Parametro) query.getSingleResult();
	}

	

}
