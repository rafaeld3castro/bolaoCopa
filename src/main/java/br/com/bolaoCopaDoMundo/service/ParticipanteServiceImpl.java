package br.com.bolaoCopaDoMundo.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bolaoCopaDoMundo.dao.ParticipanteDAO;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.util.BolaoUtil;

@Service("ParticipanteService")
public class ParticipanteServiceImpl implements ParticipanteService{

	@Autowired
	private ParticipanteDAO dao;
	
	@Autowired
	private BolaoUtil bolaoUtil;
	
	@Override
	public Participante findByUsername(String nome) {

		return dao.findByUsername(nome);
	}

	@Override
	public List<Participante> searchByNome(String nome) {
		
		validaNomeParticipante(nome);
		List<Participante> usuarioList = dao.searchByNome(nome, 1);
		if(usuarioList.isEmpty()){
			throw new BolaoCopaDoMundoRuntimeException("Nenhum resultado retornado");
		}
		
		return usuarioList;

	}

	
	@Override
	public Participante findByParticipante(Participante participante) {
		
		if(participante.getId() == null || participante.getId() == 0) {
			throw new BolaoCopaDoMundoRuntimeException("O usuário não foi selecionado.");
		}
		return dao.findByParticipante(participante);
	}

	@Override
	@Transactional
	public void salvar(Participante Participante) {
		
		verificaAlteracao(Participante);
		
		if(!Participante.getEmail().isEmpty()) {
			if(!bolaoUtil.validarEmail(Participante.getEmail()))
			{
				throw new BolaoCopaDoMundoRuntimeException("E-mail inválido");
			}
		}
		
		dao.salvar(Participante);
	}

	public void verificaAlteracao(Participante participante)
	{
		//se é um novo usuário
		if(participante.getId() == null)
		{
			verificaParticipanteExistenteUsername(participante);
//			if(dao.findByEmail(participante.getEmail()) != null){
//				throw new BolaoCopaDoMundoRuntimeException("Esse e-mail já foi cadastrado");
//			}
		}
		//se é apenas uma alteração
		else
		{
			Participante user = findByParticipante(participante);
			if( !user.getUsername().equals(participante.getUsername()))
			{
				verificaParticipanteExistenteUsername(participante);
			}
		}
	}	
	public void verificaParticipanteExistenteUsername(Participante Participante)
	{
		Participante u = findByUsername(Participante.getUsername());
		if(u != null) {
			throw new BolaoCopaDoMundoRuntimeException("Já existe um Usuário cadastrado com esse Login.");
		}
	}

	@Override
	public List<Participante> findByNome(String nome, int ativo, int inicio, int maxPerPage) {
		return dao.findByNome(nome, ativo, inicio, maxPerPage);
	}

	@Override
	public int count(String nome, int ativo) {
		return dao.count(nome, ativo);
	}
	
	private void validaNomeParticipante(String nomeUsuario) throws BolaoCopaDoMundoRuntimeException
	{
		if (nomeUsuario.length() < 3)
			 throw new BolaoCopaDoMundoRuntimeException("O Nome do Usuário deve possuir no mínimo 3(três) caracteres.");
	}

	@Override
	@Transactional
	public void recuperarSenha(String login, String email) {
		Random chave = new Random();
		Participante participante = dao.findByLoginAndEmail(login.toUpperCase(), email);
		if(participante != null){
			try {
				String newPassword = new String(participante.getUsername()+chave.nextInt(99)+1);
				bolaoUtil.enviaEmailSimples(participante, "Recuperar senha", "Sua nova senha de acesso: "+newPassword);
				participante.setPassword(bolaoUtil.criptografarSenha(newPassword));
			} catch (Exception e) {
				e.printStackTrace();
			}
			salvar(participante);
		}
		else{
			throw new BolaoCopaDoMundoRuntimeException("Login ou email não cadastrados");
		}
		
	}
	
	@Override
	public List<Participante> findAll() {
		return dao.findAll();

	}
	
	@Override
	public List<Participante> findAllOk() {
		return dao.findAllOk();

	}
}
