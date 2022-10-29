/**
 * 
 */
package br.com.bolaoCopaDoMundo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bolaoCopaDoMundo.dao.JogosDAO;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Jogos;

/**
 * @author ana.batista
 *
 */
@Service("jogosService")
public class JogosServiceImpl implements JogosService {
	
	@Autowired
	private JogosDAO dao;
	
	@Override
	public void setDAO(JogosDAO dao) {
		this.dao = dao;
	}

	@Override
	@Transactional
	public void salvar(Jogos jogo) {
		 dao.salvar(jogo);
	}

	@Override
	@Transactional
	public void excluir(Jogos Jogos) {		 
		 dao.excluir(Jogos);
	}

	@Override
	public List<Jogos> findByCriterios(Date datajogo, Grupo grupo) {		 
		return dao.findByCriterios(datajogo,grupo);
	}

	@Override
	public Jogos findById(Long id) {		
		return  dao.findById(id);
	}

	@Override
	public List<Jogos> findAll() {
		return dao.findAll();
	}
	
	@Override
	public List<Jogos> findAllOk() {
		return dao.findAllOk();
	}
}
