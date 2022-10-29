package br.com.bolaoCopaDoMundo.service;

import br.com.bolaoCopaDoMundo.dao.ParametroDAO;
import br.com.bolaoCopaDoMundo.domain.Parametro;

public interface ParametroService {
	
	public void setDAO(ParametroDAO dao);
	
	public Parametro findByCahve(String nome);
	
	public Parametro findById(Long id);

	
}
