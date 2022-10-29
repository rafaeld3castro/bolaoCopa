package br.com.bolaoCopaDoMundo.service;

import java.util.List;

import br.com.bolaoCopaDoMundo.domain.Grupo;

public interface GrupoService {

	public int count(String nome);

	public int count(String nome, String sigla);
	
	public List<Grupo> search(String nome, int first, int rows);
	
	public List<Grupo> findAll();
	
	public List<Grupo> searchByNome(String nome);

	public void salvar(Grupo grupo);
	
	public void excluir(Grupo grupo);

	public Grupo findByGrupo(Grupo grupo);
	
	public Grupo findByNome(String nome);
	
	public Grupo findById(Long grupoId);
	
	public void habilitaFiltros(String nomeSigla);
	
	public void desabilitaFiltros();

}
