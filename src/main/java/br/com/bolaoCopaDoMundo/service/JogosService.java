package br.com.bolaoCopaDoMundo.service;

import java.util.Date;
import java.util.List;

import br.com.bolaoCopaDoMundo.dao.JogosDAO;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Jogos;

public interface JogosService {

	public void setDAO(JogosDAO dao);
	
	public void salvar(Jogos Jogos);
	
	public void excluir(Jogos Jogos);
    
	public List<Jogos> findAll();
	
	public Jogos findById(Long id);

	public List<Jogos> findAllOk();

	public List<Jogos> findByCriterios(Date datajogo, Grupo grupo); 
	
}
