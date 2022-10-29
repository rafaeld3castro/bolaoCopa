package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import br.com.bolaoCopaDoMundo.domain.Grupo;

public interface GrupoDAO {
	
	public int count(String nome);
	
	public List<Grupo> findAll();

	public List<Grupo> search(String nome, int first, int rows);
	
	public List<Grupo> search(String nome,String sigla, int first, int rows);
	
	public List<Grupo> searchByNome(String nome);
	
	public List<Grupo> searchByNome(String nome, Integer sistema);
	
	public Grupo salvar(Grupo grupo);
	
	public void excluir(Grupo grupo);

	public Grupo findByGrupo(Grupo grupo);
	
	public Grupo findByNome(String nome);
	
	public Grupo findById(Long id);
	
	public void habilitaFiltros(String nomeSigla);

	public void desabilitaFiltros();
	
	public int count(String nome, String sigla);
	
}