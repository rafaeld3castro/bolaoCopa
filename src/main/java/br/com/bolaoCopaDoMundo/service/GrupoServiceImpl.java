package br.com.bolaoCopaDoMundo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bolaoCopaDoMundo.dao.GrupoDAO;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;

/**
 *
 * @author robstown
 * 
 */
@Service("grupoService")
public class GrupoServiceImpl implements GrupoService {

	@Autowired
	private GrupoDAO dao;

	@Override
	@Transactional
	public void salvar(Grupo grupo) throws BolaoCopaDoMundoRuntimeException {

		//validando dados obrigatorios
		validaDados(grupo);

		/*
		 * Regra:
		 * 
		 * Validando Grupos existentes.
		 * 
		 */
		verificandoGruposExistentes(grupo);

		// persistindo
		dao.salvar(grupo);
	}

	@Override
	@Transactional
	public void excluir(Grupo grupo) {
		dao.excluir(grupo);
	}

	@Override
	public int count(String nome) {
		return dao.count(nome);
	}

	@Override
	public List<Grupo> search(String nome, int first, int rows) {
		return dao.search(nome, first, rows);
	}

	@Override
	public Grupo findByNome(String nome) {
		return dao.findByNome(nome);
	}

	@Override
	public Grupo findByGrupo(Grupo grupo) {
		return dao.findByGrupo(grupo);
	}

	/**
	 * Validar:
	 * 
	 * � Deve ser setado o grupo.
	 * 
	 * @param grupo
	 * 
	 * @throws BolaoCopaDoMundoRuntimeException 
	 *  
	 */
	private void validaDados(Grupo grupo) throws BolaoCopaDoMundoRuntimeException {

		// validando o grupo
		if (grupo.getNome() == null)
			throw new BolaoCopaDoMundoRuntimeException("O Nome do Grupo � obrigat�rio.");

	}

	/**
	 * Regra de Negocio:
	 *  
	 * @param grupo
	 *
	 * @throws BolaoCopaDoMundoRuntimeException
	 * 
	 */

	/**Verifica na base de dados se existe algum registro com o mesmo nome do <b> sistema <b> dado.
	 * 
	 * @param Sistema sistema
	 * @throws BolaoCopaDoMundoRuntimeException
	 * */
	public void verificaExistenteNome(Grupo grupo) throws BolaoCopaDoMundoRuntimeException{

		Grupo teste = dao.findByNome(grupo.getNome());
		if(teste != null){
			throw new BolaoCopaDoMundoRuntimeException("N�o foi poss�vel cadastrar dois grupos de mesmo nome");
		}

	}

	private void verificandoGruposExistentes(Grupo grupo) throws BolaoCopaDoMundoRuntimeException {

		if (grupo.getId() == null || grupo.getId() == 0){
			verificaExistenteNome(grupo);

		}
		else {
			Grupo grup_aux = dao.findById(grupo.getId());

			if(!(grup_aux.getNome().equals(grupo.getNome()))){
				verificaExistenteNome(grupo);
			}
		}
	}

	public void setDAO(GrupoDAO dao){this.dao = dao;}

	@Override
	public List<Grupo> searchByNome(String nome) {		
		return dao.searchByNome(nome);
	}

	@Override
	public Grupo findById(Long grupoId) {
		return dao.findById(grupoId);
	}

	@Override
	public void habilitaFiltros(String nomeSigla) {
		dao.habilitaFiltros(nomeSigla);
	}

	@Override
	public void desabilitaFiltros() {
		dao.desabilitaFiltros();
	}

	@Override
	public int count(String nome, String sigla) {
		return dao.count(nome, sigla);
	}

	@Override
	public List<Grupo> findAll() {
		return dao.findAll();
	}
}
