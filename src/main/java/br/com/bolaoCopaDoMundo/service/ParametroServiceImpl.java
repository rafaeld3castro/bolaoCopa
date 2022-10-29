/**
 * 
 */
package br.com.bolaoCopaDoMundo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bolaoCopaDoMundo.dao.ParametroDAO;
import br.com.bolaoCopaDoMundo.domain.Parametro;

/**
 * @author ana.batista
 *
 */
@Service("parametroService")
public class ParametroServiceImpl implements ParametroService {
	
	@Autowired
	private ParametroDAO dao;

	@Override
	public Parametro findByCahve(String nome) {		
		return dao.findByCahve(nome);
	}
	
	
	@Override
	public Parametro findById(Long id) {		
		return  dao.findById(id);
	}


	@Override
	public void setDAO(ParametroDAO dao) {
		this.dao = dao;
		
	}
	
	
	
}
