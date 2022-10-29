package br.com.bolaoCopaDoMundo.dao;

import java.util.Date;
import java.util.List;

import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Selecao;

public interface JogosDAO {
	
	public Jogos salvar(Jogos jogos);
	
	public void excluir(Jogos jogos);
	
	public Selecao findByNome(String nome);

	public List<Jogos> findByCriterios(Date datajogo, Grupo grupo); 
	
	public List<Jogos> findAll();
	
	public List<Jogos> findAllOk();

	public Jogos findById(Long id);
	
}
