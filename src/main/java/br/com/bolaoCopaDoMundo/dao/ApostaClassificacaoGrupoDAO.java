package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import br.com.bolaoCopaDoMundo.domain.ApostaClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Participante;

public interface ApostaClassificacaoGrupoDAO {

	public ApostaClassificacaoGrupo salvar(ApostaClassificacaoGrupo aposta);

	public List<ApostaClassificacaoGrupo> findByUsername(String username);
	
	public List<ApostaClassificacaoGrupo> findByIdParticipante(Long idParticipante);
	
	public List<ApostaClassificacaoGrupo> findByGrupo(Long igGrupo, int first, int rows);
	
	public int countByGrupo (Long idGrupo);
	
	public ApostaClassificacaoGrupo getApostaByGrupoParticipante(Participante participante, Grupo grupo);
	
	public ApostaClassificacaoGrupo findById(Long id);
	
	public int countByApostasPendentes (Participante participante);

}
