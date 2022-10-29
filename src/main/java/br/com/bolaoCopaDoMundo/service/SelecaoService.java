package br.com.bolaoCopaDoMundo.service;

import java.util.List;

import br.com.bolaoCopaDoMundo.dao.SelecaoDAO;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Selecao;

public interface SelecaoService {
	
	public List<Selecao> findAll();
	
	public List<Selecao> findAllSelecao(int inicio, int fim);
	
	public void setDAO(SelecaoDAO dao);
	
	public void salvar(Selecao selecao);
	
	public void excluir(Selecao selecao);
    
	public int count(String nome);
	
	public Selecao findBySelecao(Selecao selecao);
	
	public List<Selecao> search(String nome);
	
	public List<Selecao> findByGrupo(Grupo grupo);
	
	public Selecao findByNome(String nome);
	
	public Selecao findById(Long id);
	
	public List<Selecao> findSelecao(String nome, int startingAt, int maxPerPage);
	
	public List<Selecao> search(String nome, int first, int rows);
	
	public Selecao findNomeByGrupo(String nome, Grupo grupo);
	
	public int countByGrupo(Grupo grupo, String nome);
	
	public List<Selecao> findByGrupoPaginado(String nome, Grupo grupo, int inicio, int maxPerPage);
	
	public List<Selecao> findByCriterios(String nome, Grupo grupo);
	
	public List<Selecao> findSelecaoNaoClassificadaByGrupo(Grupo grupo);
	
	public List<Selecao> findSelecaoNaoClassificadaByGrupoParticipante(Grupo grupo, Participante participante);
	
	
	
}
