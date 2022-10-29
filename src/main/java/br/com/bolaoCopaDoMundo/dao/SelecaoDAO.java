package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Selecao;

public interface SelecaoDAO {
	
	public Selecao salvar(Selecao selecao);
	
	public void excluir(Selecao selecao);
	
	public Selecao findByNome(String nome);
	
	public List<Selecao> findAllSelecao(int inicio, int max);
	
	public Selecao findById(Long id);
	
	public List<Selecao> findByGrupo(Grupo grupo);

	public List<Selecao> search(String nome);
	
	public List<Selecao> findSelecao(String nome, int startingAt, int maxPerPage);

	public Selecao findNomeByGrupo(String nome, Grupo grupo);
	
	public int countByGrupo(Grupo sistema, String nome);
	
	public int countByNome(String nome);
	
	public List<Selecao> search(String nome, int first, int rows);
	
	public List<Selecao> findByGrupoPaginado(String nome, Grupo grupo, int inicio, int maxPerPage); 
	
	public List<Selecao> findByCriterios(String nome, Grupo grupo);

	public List<Selecao> findSelecaoNaoClassificadaByGrupo(Grupo grupo); 
	
	public List<Selecao> findSelecaoNaoClassificadaByGrupoParticipante(Grupo grupo, Participante participante);
	
}
