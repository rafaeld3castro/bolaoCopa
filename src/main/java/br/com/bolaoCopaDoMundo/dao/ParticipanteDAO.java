package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import br.com.bolaoCopaDoMundo.domain.Participante;

public interface ParticipanteDAO {
	
	public Participante findByUsername(String nome);
	
	public List<Participante> searchByNome(String nome, int ativo);
	
	public Participante findByParticipante(Participante Participante);
	
	public Participante salvar(Participante Participante);
	
	public Participante findByCpf(String cpf);
	
	public List<Participante> findByNome(String nome, int ativo, int inicio, int maxPerPage);
	
	public int count(String nome, int ativo);
	
	public Participante findByEmail(String email);
	
	public Participante findByLoginAndEmail(String login, String email);
	
	public List<Participante> findAllOk();
	
	public List<Participante> findAll();
	
}
