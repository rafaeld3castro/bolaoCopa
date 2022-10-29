package br.com.bolaoCopaDoMundo.dao;

import br.com.bolaoCopaDoMundo.domain.Parametro;

public interface ParametroDAO {
	
	public Parametro findByCahve(String nome);
	
	public Parametro findById(Long id);
	
	
}
