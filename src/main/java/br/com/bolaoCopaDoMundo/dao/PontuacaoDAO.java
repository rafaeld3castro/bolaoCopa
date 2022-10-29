package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Pontuacao;

public interface PontuacaoDAO {
	
	public int count();
	
	public List<Pontuacao> findAll();
	
	public List<Pontuacao> findAll(int first, int rows);

	public List<Pontuacao> search(String nome, int first, int rows);
	
	public List<Pontuacao> search(String nome,String sigla, int first, int rows);
	
	public List<Pontuacao> searchByNome(String nome);
	
	public List<Pontuacao> searchByNome(String nome, Integer sistema);
	
	public Pontuacao salvar(Pontuacao pontuacao);
	
	public void excluir(Pontuacao pontuacao);

	public Pontuacao findByParticipante(Participante participante);
	
	public Pontuacao findByNome(String nome);
	
	public Pontuacao findById(Long id);
	
	public void habilitaFiltros(String nomeSigla);

	public void desabilitaFiltros();
	
	public int count(String nome, String sigla);
	
}