package br.com.bolaoCopaDoMundo.service;

import java.util.List;

import br.com.bolaoCopaDoMundo.dao.ClassificacaoGrupoDAO;
import br.com.bolaoCopaDoMundo.domain.ClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Grupo;

public interface ClassificacaoGrupoService {
	
	public List<ClassificacaoGrupo> findAllOk();
	
	public void setDAO(ClassificacaoGrupoDAO dao);
	
	public void salvar(ClassificacaoGrupo classificacaoGrupo);
	
	public void excluir(ClassificacaoGrupo classificacaoGrupo);
    
	public ClassificacaoGrupo findById(Long id);
	
	public List<ClassificacaoGrupo> findByCriterios(Grupo grupo); 
	
	public ClassificacaoGrupo findByGrupo(Grupo grupo); 
	
}
