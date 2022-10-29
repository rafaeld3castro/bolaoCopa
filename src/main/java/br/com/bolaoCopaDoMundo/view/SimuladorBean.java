package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.primefaces.event.CellEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.dao.ApostasDAO;
import br.com.bolaoCopaDoMundo.dao.PontuacaoDAO;
import br.com.bolaoCopaDoMundo.domain.Apostas;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Pontuacao;
import br.com.bolaoCopaDoMundo.service.AuthenticationService;
import br.com.bolaoCopaDoMundo.service.JogosService;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;
import br.com.bolaoCopaDoMundo.service.PontuacaoService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@Component("simuladorListBean")
@Scope("view")
public class SimuladorBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(SimuladorBean.class);

	@Autowired
	private PontuacaoDAO pontuacaoDao;

	@Autowired
	private ApostasDAO apostaDao;

	@Autowired
	private ParticipanteService participanteService;
	
	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private JogosService jogosService;

	@Autowired
	private PontuacaoService pontuacaoService;

	private List<Pontuacao> classificacaoList = new ArrayList<Pontuacao>();

	// todos os particiantes
	private List<Participante> participantes = new ArrayList<Participante>();

	private List<Jogos> jogos = new ArrayList<Jogos>();

	private Map<Long, Jogos> jogosSimular = new HashMap<Long, Jogos>();

	@PostConstruct
	public void init() {

		classificacaoList = pontuacaoService.findAll();

		Collections.sort(classificacaoList, new Ordenar());

		participantes = participanteService.findAllOk();
		jogos = jogosService.findAll();

		for (Jogos jogo : jogos) {
			Hibernate.initialize(jogo.getGrupo());
			Hibernate.initialize(jogo.getSelecao1());
			Hibernate.initialize(jogo.getSelecao2());
			if (!jogo.isFlResultadoOk()) {
				jogo.setGol1t("");
				jogo.setGol2t("");
			}
		}

	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		FacesContext context = FacesContext.getCurrentInstance();
		Jogos jogoAtualizar = context.getApplication().evaluateExpressionGet(
				context, "#{item}", Jogos.class);
		jogoAtualizar.setFlResultadoOk(true);

		System.out.println(jogoAtualizar.getDtJogo() + " "
				+ jogoAtualizar.getGol1() + " " + jogoAtualizar.getGol2() + " "
				+ jogoAtualizar.getId());

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void tstAltera() {

		Boolean flGerarPontuacao = false;

		FacesContext context = FacesContext.getCurrentInstance();
		Jogos jogoAtualizar = context.getApplication().evaluateExpressionGet(
				context, "#{item}", Jogos.class);
		try {
			if (!jogoAtualizar.getGol1t().isEmpty()) {
				jogoAtualizar.setGol1(Integer.parseInt(jogoAtualizar.getGol1t()));
			} else {
				jogoAtualizar.setGol1(null);
				if (jogosSimular.containsKey(jogoAtualizar.getId())) {
					jogosSimular.remove(jogoAtualizar.getId());
					flGerarPontuacao = true;
				}
			}
		} catch(Exception e){
			FacesUtil.addErroMessage("Valor digitado incorreto.");
		}
		try{
			if (!jogoAtualizar.getGol2t().isEmpty()) {
				jogoAtualizar.setGol2(Integer.parseInt(jogoAtualizar.getGol2t()));
			} else {
				jogoAtualizar.setGol2(null);
				if (jogosSimular.containsKey(jogoAtualizar.getId())) {
					jogosSimular.remove(jogoAtualizar.getId());
					flGerarPontuacao = true;
				}
			}
		} catch(Exception e){
			FacesUtil.addErroMessage("Valor digitado incorreto.");
		}
		
		System.out.println(jogoAtualizar.getDtJogo() + " "
				+ jogoAtualizar.getGol1() + " " + jogoAtualizar.getGol2() + " "
				+ jogoAtualizar.getId());

		if (jogoAtualizar.getGol1() != null && jogoAtualizar.getGol2() != null) {
			if (jogosSimular.containsKey(jogoAtualizar.getId())) {
				jogosSimular.remove(jogoAtualizar.getId());
			}
			jogosSimular.put(jogoAtualizar.getId(), jogoAtualizar);
			flGerarPontuacao = true;
		}

		if (flGerarPontuacao == true) {
			geracaoPontuacao();
		}
	}

	public boolean geracaoPontuacao() {

		classificacaoList = new ArrayList<Pontuacao>();

		if (jogosSimular.isEmpty()) {
			classificacaoList = pontuacaoService.findAll();
		} else {
			Apostas aposta = new Apostas();

			for (Participante participante : participantes) {

				Pontuacao pontuacao = new Pontuacao();
				pontuacao = pontuacaoDao.findByParticipante(participante);

				if (pontuacao == null)
					pontuacao = new Pontuacao();

				Double pontosJogo = pontuacao.getPontosJogo();
				Double pontosBrasil = pontuacao.getPontosBrasil();
				Integer escoreCheio = pontuacao.getEscoreCheio();
				Double totalPontos = pontuacao.getTotalPontos();

				for (Jogos jogo : jogosSimular.values()) {
					Double pontos = (double) 0;

					aposta = apostaDao.getApostaByJogoParticipante(
							participante, jogo);
					
					if(aposta.getGol1() == null || aposta.getGol2() == null) {
						pontos = (double) 0;
					}
					else {
						// Escore Cheio
						if ((aposta.getGol1() == jogo.getGol1())
								&& (aposta.getGol2() == jogo.getGol2())) {
							pontos = (double) 7;
							escoreCheio = escoreCheio + 1;
						}
						// Empate sem acertar o placar
						else {
	
							if (((aposta.getGol1() - aposta.getGol2()) == 0)
									&& ((jogo.getGol1() - jogo.getGol2()) == 0)) {
								pontos = (double) 3;
							}
	
							else {
								// Escore de um dos times
								if ((aposta.getGol1() == jogo.getGol1())
										|| (aposta.getGol2() == jogo.getGol2()))
									pontos = (double) 2;
	
								// Acertar o 1º time como vencedor
								if ((aposta.getGol1() > aposta.getGol2())
										&& (jogo.getGol1() > jogo.getGol2()))
									pontos = pontos + 2;
	
								// Acertar o 2º time como vencedor
								if ((aposta.getGol1() < aposta.getGol2())
										&& (jogo.getGol1() < jogo.getGol2()))
									pontos = pontos + 2;
	
							}
						}
						// Jogos do Brasil os pontos são dobrados
						if (jogo.isFlJogoBrasil()) {
							//pontos = pontos * 2;
							pontos = pontos + pontos * 0.5;
							pontosBrasil = pontosBrasil + pontos;
						}
						aposta.setPontos(new BigDecimal(pontos));
	
						pontosJogo = pontosJogo + pontos;
						totalPontos = totalPontos + pontos;
					}

				}
				pontuacao.setEscoreCheio(escoreCheio);
				pontuacao.setParticipante(participante);
				pontuacao.setPontosBrasil(pontosBrasil);
				pontuacao.setPontosJogo(pontosJogo);
				pontuacao.setTotalPontos(totalPontos);

				classificacaoList.add(pontuacao);

			}

		}

		Collections.sort(classificacaoList, new Ordenar());

		return true;
	}

	public class Ordenar implements Comparator<Pontuacao> {
		@Override
		public int compare(Pontuacao o1, Pontuacao o2) {
			int c;
			c = o1.getTotalPontos().compareTo(o2.getTotalPontos());
			if (c == 0)
				c = o1.getEscoreCheio().compareTo(o2.getEscoreCheio());
			if (c == 0)
				c = o1.getPontosBrasil().compareTo(o2.getPontosBrasil());
			return -c;
		}
	}
	
	public Participante getUsuarioLogado() {

		try {

			return authenticationService.getUsuarioLogado();

		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Erro na identificação do usuario logado. Favor efetuar novo login.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public List<Pontuacao> getClassificacaoList() {
		return classificacaoList;
	}

	public void setClassificacaoList(List<Pontuacao> classificacaoList) {
		this.classificacaoList = classificacaoList;
	}

	public List<Participante> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<Participante> participantes) {
		this.participantes = participantes;
	}

	public List<Jogos> getJogos() {
		return jogos;
	}

	public void setJogos(List<Jogos> jogos) {
		this.jogos = jogos;
	}

}
