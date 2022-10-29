/**
 * 
 */
package br.com.bolaoCopaDoMundo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bolaoCopaDoMundo.dao.ApostaClassificacaoGrupoDAO;
import br.com.bolaoCopaDoMundo.domain.ApostaClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Participante;

/**
 * @author ana.batista
 * 
 */
@Service("ApostaClassificacaoGrupoService")
public class ApostaClassificacaoGrupoServiceImpl implements ApostaClassificacaoGrupoService {

	@Autowired
	private ApostaClassificacaoGrupoDAO dao;

	@Override
	@Transactional
	public void salvar(ApostaClassificacaoGrupo aposta) {
		dao.salvar(aposta);
	}

	@Override
	public List<ApostaClassificacaoGrupo> findByUsername(String username) {
		return dao.findByUsername(username);
	}
	
	@Override
	public List<ApostaClassificacaoGrupo> findByGrupo(Long idGrupo,int first, int rows) {
		return dao.findByGrupo(idGrupo, first, rows);
	}

	@Override
	public void setDAO(ApostaClassificacaoGrupoDAO dao) {
		this.dao = dao;
	}

	@Override
	public int countByGrupo(Long idGrupo) {
		return dao.countByGrupo( idGrupo);
	}

	@Override
	public List<ApostaClassificacaoGrupo> findByIdParticipante(Long idParticipante) {
		return dao.findByIdParticipante(idParticipante);
	}

	@Override
	public ApostaClassificacaoGrupo findById(Long id) {
		return  dao.findById(id);
	}

	@Override
	public ApostaClassificacaoGrupo findByParticipanteGrupo(Grupo grupo,
			Participante participante) {
		return  dao.getApostaByGrupoParticipante(participante,grupo);
	}

	@Override
	public int countByApostasPendentes(Participante participante) {
		return dao.countByApostasPendentes(participante);
	}

}
