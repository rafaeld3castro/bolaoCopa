package br.com.bolaoCopaDoMundo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bolaoCopaDoMundo.dao.ApostaClassificacaoGrupoDAO;
import br.com.bolaoCopaDoMundo.dao.ApostasDAO;
import br.com.bolaoCopaDoMundo.dao.PontuacaoDAO;
import br.com.bolaoCopaDoMundo.domain.ApostaClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Apostas;
import br.com.bolaoCopaDoMundo.domain.ClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Pontuacao;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

/**
 *
 * @author robstown
 * 
 */
@Service("pontuacaoService")
public class PontuacaoServiceImpl implements PontuacaoService {

	@Autowired
	private PontuacaoDAO dao;
	
	@Autowired
	private ApostasDAO apostaDao;
	
	@Autowired
	private ApostaClassificacaoGrupoDAO apostaClassificacaoGrupoDAO;

	@Override
	@Transactional
	public void salvar(Pontuacao pontuacao) throws BolaoCopaDoMundoRuntimeException {
		dao.salvar(pontuacao);
	}

	@Override
	@Transactional
	public void excluir(Pontuacao Pontuacao) {
		dao.excluir(Pontuacao);
	}

	@Override
	public int count() {
		return dao.count();
	}

	@Override
	public List<Pontuacao> search(String nome, int first, int rows) {
		return dao.search(nome, first, rows);
	}

	@Override
	public Pontuacao findByNome(String nome) {
		return dao.findByNome(nome);
	}

	public void setDAO(PontuacaoDAO dao){this.dao = dao;}

	@Override
	public List<Pontuacao> searchByNome(String nome) {		
		return dao.searchByNome(nome);
	}

	@Override
	public Pontuacao findById(Long PontuacaoId) {
		return dao.findById(PontuacaoId);
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
	public List<Pontuacao> findAll() {
		return dao.findAll();
	}

	@Override
	@Transactional
	public boolean geracaoPontuacao(List<Participante> participantes,List<Jogos> listJogos) {
	
		Apostas aposta = new Apostas();
		try {
			for (Participante participante : participantes) {
				Double pontosJogo = 0.0;
				Pontuacao pontuacao = dao.findByParticipante(participante);
				if(pontuacao == null)
					pontuacao = new Pontuacao();
				Integer pontosClassificacao = 0;
				if(pontuacao.getPontosClassificacao()!=null)
					pontosClassificacao = pontuacao.getPontosClassificacao();
				Double pontosBrasil = 0.0;
				Integer escoreCheio = 0;
				Double totalPontos = new Double(pontosClassificacao);
				
				System.out.println(participante.getNome());
				//System.out.println(listJogos);
				for (Jogos jogo : listJogos) {
					//System.out.println(jogo.getId());
					aposta = apostaDao.getApostaByJogoParticipante(participante, jogo);
					//System.out.println(aposta);
					Double pontos = 0.0;
					// apostas incompletas
					if((aposta.getGol1() != null) || (aposta.getGol2() != null))
					{
						 // Escore Cheio
						if((aposta.getGol1() == jogo.getGol1()) && (aposta.getGol2() == jogo.getGol2())){
							pontos =(double) 7;
							escoreCheio = escoreCheio +1;
						}
						//Empate sem acertar o placar
						else
						{
							
							if(((aposta.getGol1() - aposta.getGol2())==0) && ((jogo.getGol1() - jogo.getGol2())==0)){
								pontos =(double) 3;
							}
							
							else
							{
								// Escore de um dos times	
								if((aposta.getGol1() == jogo.getGol1()) || (aposta.getGol2() == jogo.getGol2()))
									pontos =(double) 2;
							
								//Acertar o 1º time como vencedor
								if((aposta.getGol1() > aposta.getGol2()) && (jogo.getGol1() > jogo.getGol2()))
									pontos = pontos +2;
				
								// Acertar o 2º time como vencedor
								if((aposta.getGol1() < aposta.getGol2()) && (jogo.getGol1() < jogo.getGol2()))
									pontos = pontos +2;
		
							}
						}
					}
					//Jogos do Brasil os pontos são dobrados
					if(jogo.isFlJogoBrasil()){
						//pontos = pontos*2;
						pontos = pontos + pontos*0.5;
						pontosBrasil = pontosBrasil + pontos;
					}	
					aposta.setPontos(new BigDecimal(pontos));
					System.out.println(pontos);
					apostaDao.salvar(aposta);
					
					pontosJogo = pontosJogo + pontos;
					totalPontos = totalPontos + pontos;
				}
				pontuacao.setEscoreCheio(escoreCheio);
				pontuacao.setParticipante(participante);
				pontuacao.setPontosBrasil(pontosBrasil);
				System.out.println("Pontos Jogos: " + pontosJogo);
				pontuacao.setPontosJogo(pontosJogo);
				System.out.println("Pontos Jogos: " + pontuacao.getPontosJogo());
				pontuacao.setTotalPontos(totalPontos);
				salvar(pontuacao);
			}
			
		} catch (Exception e) {
			 
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Opera��o cancelada.");
			return false;
		}
		
		return true;
	}

	@Override
	public List<Pontuacao> findAll(int first, int rows) {
		return dao.findAll(first,rows);
	}

	@Override
	@Transactional
	public boolean geracaoPontuacaoGrupo(List<Participante> participantes, List<ClassificacaoGrupo> classificacaoGrupoList) {
		 
		ApostaClassificacaoGrupo apostaClassificacaoGrupo = new ApostaClassificacaoGrupo();
		try {
			for (Participante participante : participantes) {
				
				Pontuacao pontuacao = dao.findByParticipante(participante);
				if(pontuacao == null)
					pontuacao = new Pontuacao();
				Integer pontosClassificacao = 0;
				Integer acertoPrimeiroLugar = 0;
				Double totalPontos = pontuacao.getPontosJogo();
				
				for (ClassificacaoGrupo classificacaoGrupo : classificacaoGrupoList) {
					apostaClassificacaoGrupo = apostaClassificacaoGrupoDAO.getApostaByGrupoParticipante(participante, classificacaoGrupo.getGrupo());
					Integer pontos = 0;
					// Pontuação para acerto do 1º Lugar
					if((apostaClassificacaoGrupo.getPosicao1().getId() == classificacaoGrupo.getPosicao1().getId())){
						pontos = 5;
						acertoPrimeiroLugar = acertoPrimeiroLugar +1;
					}
					// Pontuação para acerto do 2º Lugar
					if((apostaClassificacaoGrupo.getPosicao2().getId() == classificacaoGrupo.getPosicao2().getId())){
						pontos = pontos +2;
					}
					// Pontuação inversão de ordem entre 1º Lugar e 2º Lugar
					if((apostaClassificacaoGrupo.getPosicao1().getId() == classificacaoGrupo.getPosicao2().getId()) && (apostaClassificacaoGrupo.getPosicao2().getId() == classificacaoGrupo.getPosicao1().getId())){
						pontos = 3;
					}
					
					apostaClassificacaoGrupo.setPontos(new BigDecimal(pontos));
					//apostaClassificacaoGrupo.setPontos(new BigDecimal(5));
					apostaClassificacaoGrupoDAO.salvar(apostaClassificacaoGrupo);
//					if(pontuacao.getPontosClassificacao()==null)
//						pontuacao.setPontosClassificacao(0);
					pontosClassificacao = pontosClassificacao + pontos;
					totalPontos = totalPontos + pontos;
					System.out.println(totalPontos);
				}
				
				pontuacao.setParticipante(participante);
				pontuacao.setAcertoPrimeiroLugar(acertoPrimeiroLugar);
				pontuacao.setPontosClassificacao(pontosClassificacao);
				pontuacao.setTotalPontos(totalPontos);
				salvar(pontuacao);
			}
			
		} catch (Exception e) {
			 e.printStackTrace();
			FacesUtil.addErroMessage("Ocorreu algum erro na geração dos ponto. Operação cancelada.");
			return false;
		}
	
		return true;
	}
}
