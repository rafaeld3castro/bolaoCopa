/**
 * 
 */
package br.com.bolaoCopaDoMundo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bolaoCopaDoMundo.dao.ClassificacaoGrupoDAO;
import br.com.bolaoCopaDoMundo.domain.ClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Grupo;

/**
 * @author ana.batista
 *
 */
@Service("classificacaoGrupoService")
public class ClassificacaoGrupoServiceImpl implements ClassificacaoGrupoService {
	
	@Autowired
	private ClassificacaoGrupoDAO dao;

	@Override
	public List<ClassificacaoGrupo> findAllOk() {		
		return dao.findAllOk();
	}

	@Override
	public void setDAO(ClassificacaoGrupoDAO dao) {
		this.dao = dao;
	}

	@Override
	@Transactional
	public void salvar(ClassificacaoGrupo classificacaoGrupo) {
		 dao.salvar(classificacaoGrupo);
	}

	@Override
	@Transactional
	public void excluir(ClassificacaoGrupo classificacaoGrupo) {		 
		 dao.excluir(classificacaoGrupo);
	}

	@Override
	public List<ClassificacaoGrupo> findByCriterios( Grupo grupo) {		 
		return dao.findByCriterios(grupo);
	}

	@Override
	public ClassificacaoGrupo findById(Long id) {		
		return  dao.findById(id);
	}
	
	@Override
	public ClassificacaoGrupo findByGrupo(Grupo grupo) {		
		return dao.findByGrupo(grupo);
	}

}
