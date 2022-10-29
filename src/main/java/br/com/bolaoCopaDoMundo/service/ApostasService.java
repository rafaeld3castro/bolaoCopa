package br.com.bolaoCopaDoMundo.service;

import java.util.List;

import br.com.bolaoCopaDoMundo.dao.ApostasDAO;
import br.com.bolaoCopaDoMundo.domain.Apostas;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;

public interface ApostasService {

	public void salvar(Apostas aposta);

	public List<Apostas> findByUsername(String username);
	
	public List<Apostas> findByIdParticipante(Long idParticipante);
	
	public List<Apostas> findByJogo(Long idJogo,int first, int rows);
	
	public int countByJogo (Long idJogo);
	
	public Long countByParticipanteAndGolNull(Participante participante);

	public void setDAO(ApostasDAO dao);
	
	public List<Apostas> findByJogoResultado(Jogos jogo, int first, int rows);
	
	public int countByJogoResultado (Jogos jogo);

}
