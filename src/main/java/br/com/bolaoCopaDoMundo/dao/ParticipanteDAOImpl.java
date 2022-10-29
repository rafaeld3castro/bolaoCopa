package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.com.bolaoCopaDoMundo.domain.Participante;


@Repository
public class ParticipanteDAOImpl implements ParticipanteDAO{

	static Logger logger = Logger.getLogger(ParticipanteDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Long getMaxId() {
		Query query;
		String consulta; 
		consulta = new String("SELECT MAX(u.id) FROM Participante u");
		query = entityManager.createQuery(consulta);
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}
	
	@Override
	public Participante findByUsername(String nome) {
		try {
			Query query = entityManager.createNamedQuery("Participante.findByUsername");
			query.setParameter("username", nome.toUpperCase());
			Participante u = (Participante) query.getSingleResult();
			return u;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Participante> searchByNome(String nome, int ativo) {
		try {
			StringBuilder consulta = 
					new StringBuilder("SELECT *" +
									  " FROM Participante" +
									  " WHERE UPPER(replace(nome, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))" +
									  " LIKE UPPER(replace('%" + nome +"%', 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))");
			if(ativo == 0 || ativo == 1) {
				consulta.append(" AND flativo = " + ativo);
			}
			consulta.append(" ORDER BY nome");
			
			Query query = entityManager.createNativeQuery(consulta.toString(), Participante.class);
			
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Participante findByParticipante(Participante Participante) {
		try{
			Query query = entityManager.createNamedQuery("Participante.findByParticipante");
			query.setParameter("id", Participante.getId());
			return (Participante) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Participante salvar(Participante participante) {
		//verifica se é um novo usuário ou se é apenas uma alteração 
		if(participante.getId() == null || participante.getId() == 0) {
			participante.setId(getMaxId());
		}
	
		System.out.println("Inserindo participante... "+participante.getNome());
		return entityManager.merge(participante);
	}

	@Override
	public Participante findByCpf(String cpf) 
	{
		try
		{
			String consulta = new String("SELECT * FROM Participante WHERE cpf LIKE " + cpf);
			Query query = entityManager.createNativeQuery(consulta, Participante.class);
			return (Participante)query.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	//consultas com lazy para tela de tabela Participante
	public List<Participante> findByNome(String nome, int ativo, int inicio, int maxPerPage) {
		StringBuilder consulta = 
			new StringBuilder("SELECT *" +
							  " FROM Participante" +
							  " WHERE UPPER(replace(nome, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))" +
							  " LIKE UPPER(replace('%" + nome +"%', 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))");
		if(ativo == 0 || ativo == 1) {
			consulta.append(" AND flativo = " + ativo);
		}
		consulta.append(" ORDER BY nome");
		Query query = entityManager.createNativeQuery(consulta.toString(), Participante.class);
		query.setFirstResult(inicio);
		query.setMaxResults(maxPerPage);		
		return query.getResultList();		
	}

	@Override
	public int count(String nome, int ativo) {
		StringBuilder consulta = 
				new StringBuilder("SELECT *" +
								  " FROM Participante" +
								  " WHERE UPPER(replace(nome, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))" +
								  " LIKE UPPER(replace('%" + nome +"%', 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))");
			if(ativo == 0 || ativo == 1) {
				consulta.append(" AND flativo = " + ativo);
			}
			consulta.append(" ORDER BY nome");
		Query query = entityManager.createNativeQuery(consulta.toString(), Participante.class);
		return query.getResultList().size();
	}

	@Override
	public Participante findByEmail(String email) {
		try{
			StringBuilder sql = new StringBuilder("SELECT * FROM Participante WHERE email LIKE '"+ email +"'");
			Query query = entityManager.createNativeQuery(sql.toString(), Participante.class);
			
			return (Participante)query.getSingleResult();
		}catch (NoResultException e){
			return null;
		}
	}

	@Override
	public Participante findByLoginAndEmail(String login, String email) {
		try{
			String sql = new String("SELECT p FROM Participante p WHERE p.email LIKE '" + email + "' AND p.username LIKE '" + login + "'");
			Query query = entityManager.createQuery(sql, Participante.class);
			return (Participante)query.getSingleResult();
		}catch (NoResultException e){
			return null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Participante> findAll() {
		StringBuilder sql = new StringBuilder("Select p from Participante p order by p.nome");
		Query query = entityManager.createQuery(sql.toString());
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Participante> findAllOk() {
		StringBuilder sql = new StringBuilder("Select p from Participante p where p.ativo = 1 and p.id <>1 order by p.nome");
		Query query = entityManager.createQuery(sql.toString());
		return query.getResultList();
	}

}
