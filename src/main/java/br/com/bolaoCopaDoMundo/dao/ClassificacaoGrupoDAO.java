package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.ClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Selecao;

public interface ClassificacaoGrupoDAO {
	
	public ClassificacaoGrupo salvar(ClassificacaoGrupo classificacaoGrupo);
	
	public void excluir(ClassificacaoGrupo classificacaoGrupo);
	
	public List<ClassificacaoGrupo> findAllOk();
	
	public Selecao findByNome(String nome);
	
	public List<ClassificacaoGrupo> findByCriterios(Grupo grupo); 
	
	public ClassificacaoGrupo findById(Long id);

	public ClassificacaoGrupo findByGrupo(Grupo grupo);
	
}
