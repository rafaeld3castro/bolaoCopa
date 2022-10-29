package br.com.bolaoCopaDoMundo.service;

import java.util.List;

import br.com.bolaoCopaDoMundo.dao.ApostaClassificacaoGrupoDAO;
import br.com.bolaoCopaDoMundo.domain.ApostaClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Participante;

public interface ApostaClassificacaoGrupoService {

	public void salvar(ApostaClassificacaoGrupo aposta);
	
	public ApostaClassificacaoGrupo findById(Long id);
	
	public ApostaClassificacaoGrupo findByParticipanteGrupo(Grupo grupo, Participante participante);
	
	public List<ApostaClassificacaoGrupo> findByUsername(String username);
	
	public List<ApostaClassificacaoGrupo> findByGrupo(Long idGrupo,int first, int rows);
	
	public int countByGrupo (Long idGrupo);
	
	public List<ApostaClassificacaoGrupo> findByIdParticipante(Long idParticipante);
	
	public void setDAO(ApostaClassificacaoGrupoDAO dao);
	
	public int countByApostasPendentes (Participante participante);
	
	

}
