/**
 * 
 */
package br.com.bolaoCopaDoMundo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bolaoCopaDoMundo.dao.SelecaoDAO;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Selecao;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;

/**
 * @author ana.batista
 *
 */
@Service("selecaoService")
public class SelecaoServiceImpl implements SelecaoService {
	
	@Autowired
	private SelecaoDAO dao;

	@Override
	public List<Selecao> findAll() {		
		return null;
	}

	@Override
	public void setDAO(SelecaoDAO dao) {
		this.dao = dao;
	}

	@Override
	@Transactional
	public void salvar(Selecao secao) {
		 verificaAlteracao(secao);
		 dao.salvar(secao);
	}

	@Override
	@Transactional
	public void excluir(Selecao selecao) {		 
		 dao.excluir(selecao);
	}

	@Override
	public List<Selecao> search(String nome) {		
		return dao.search(nome);
	}

	@Override
	public List<Selecao> findByGrupo(Grupo grupo) {		
		return dao.findByGrupo(grupo);
	}

	@Override
	public Selecao findByNome(String nome) {		
		return dao.findByNome(nome);
	}
	
	@Override
	public List<Selecao> findSelecao(String nome, int startingAt, int maxPerPage){
		return dao.findSelecao(nome, startingAt, maxPerPage);
	}

	@Override
	public Selecao findById(Long id) {		
		return  dao.findById(id);
	}
	
	public Selecao findNomeByGrupo(String nome, Grupo grupo){
		return dao.findNomeByGrupo(nome, grupo);
	}
	
	public void verificaExistenteNome(Selecao selecao) throws BolaoCopaDoMundoRuntimeException{

		Selecao teste = dao.findNomeByGrupo(selecao.getNome(), selecao.getGrupo());		
		if(teste != null){
			throw new BolaoCopaDoMundoRuntimeException("N�o � poss�vel cadastrar duas Sele��o de mesmo nome");
		}
	}
	
	public void verificaAlteracao(Selecao selecao) throws BolaoCopaDoMundoRuntimeException{
		if (selecao.getId() == null || selecao.getId() == 0){
			verificaExistenteNome(selecao);
			
		}
		else {
			Selecao sis_aux = dao.findById(selecao.getId());

			if(!(sis_aux.getNome().equals(selecao.getNome()))){
				verificaExistenteNome(selecao);
			}			
		}
	}

	@Override
	public List<Selecao> findAllSelecao(int inicio, int fim) {		
		return dao.findAllSelecao(inicio, fim);
	}
	
	@Override
	public int countByGrupo(Grupo grupo, String nome) {
		return dao.countByGrupo(grupo, nome);
	}

	@Override
	public List<Selecao> findByGrupoPaginado(String nome, Grupo grupo, int inicio,
			int maxPerPage) {		
		return dao.findByGrupoPaginado(nome, grupo, inicio, maxPerPage);
	}

	@Override
	public List<Selecao> findByCriterios(String nome, Grupo grupo) {		
		return dao.findByCriterios(nome, grupo);
	}

	@Override
	public int count(String nome) {
		return 0;
	}

	@Override
	public Selecao findBySelecao(Selecao selecao) {
		return null;
	}

	@Override
	public List<Selecao> search(String nome, int first, int rows) {
		return dao.search(nome, first, rows);
	}
	
	@Override
	public List<Selecao> findSelecaoNaoClassificadaByGrupo(Grupo grupo) {
		return dao.findSelecaoNaoClassificadaByGrupo(grupo);
	}

	@Override
	public List<Selecao> findSelecaoNaoClassificadaByGrupoParticipante(
			Grupo grupo, Participante participante) {
		return dao.findSelecaoNaoClassificadaByGrupoParticipante(grupo, participante);
	}
	
}
