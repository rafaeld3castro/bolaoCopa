package br.com.bolaoCopaDoMundo.service;

import java.util.List;

import br.com.bolaoCopaDoMundo.domain.ClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Pontuacao;

public interface PontuacaoService {

	public int count();
	
	public int count(String nome, String sigla);
	
	public List<Pontuacao> search(String nome, int first, int rows);
	
	public List<Pontuacao> findAll();
	
	public List<Pontuacao> findAll(int first, int rows);
	
	public List<Pontuacao> searchByNome(String nome);
	
	public boolean geracaoPontuacao(List<Participante> participantes, List<Jogos> jogos );
	
	public boolean geracaoPontuacaoGrupo(List<Participante> participantes, List<ClassificacaoGrupo> classificacaoGrupo );

	public void salvar(Pontuacao pontuacao);
	
	public void excluir(Pontuacao pontuacao);

	public Pontuacao findByNome(String nome);
	
	public Pontuacao findById(Long pontuacaoId);
	
	public void habilitaFiltros(String nomeSigla);

	public void desabilitaFiltros();

}
