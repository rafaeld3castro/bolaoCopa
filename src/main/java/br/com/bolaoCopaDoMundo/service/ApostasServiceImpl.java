/**
 * 
 */
package br.com.bolaoCopaDoMundo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bolaoCopaDoMundo.dao.ApostasDAO;
import br.com.bolaoCopaDoMundo.domain.Apostas;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;

/**
 * @author ana.batista
 * 
 */
@Service("apostasService")
public class ApostasServiceImpl implements ApostasService {

	@Autowired
	private ApostasDAO dao;

	@Override
	@Transactional
	public void salvar(Apostas aposta) {
		dao.salvar(aposta);
	}

	@Override
	public List<Apostas> findByUsername(String username) {
		return dao.findByUsername(username);
	}
	
	@Override
	public List<Apostas> findByIdParticipante(Long idParticipante) {
		return dao.findByIdParticipante(idParticipante);
	}
	
	@Override
	public List<Apostas> findByJogo(Long idJogo,int first, int rows) {
		return dao.findByJogo(idJogo, first, rows);
	}
	
	@Override
	public Long countByParticipanteAndGolNull(Participante participante) {
		return dao.countByParticipanteAndGolNull(participante);
	}

	@Override
	public void setDAO(ApostasDAO dao) {
		this.dao = dao;
	}

	@Override
	public int countByJogo(Long idJogo) {
		return dao.countByJogo( idJogo);
	}
	
	@Override
	public List<Apostas> findByJogoResultado(Jogos jogo,int first, int rows) {
		return dao.findByJogoResultado(jogo, first, rows);
	}
	
	@Override
	public int countByJogoResultado(Jogos jogo) {
		return dao.countByJogoResultado( jogo);
	}

}
