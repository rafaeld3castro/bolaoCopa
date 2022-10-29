package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.dao.ApostasDAO;
import br.com.bolaoCopaDoMundo.domain.Apostas;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.service.JogosService;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@Component("estatisticasParticipanteBean")
@Scope("view")
public class EstatisticasParticipanteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(EstatisticasJogoBean.class);
	
	@Autowired
	private ParticipanteService participanteService;

	@Autowired
	private JogosService jogosService;
	
	@Autowired
	private ApostasDAO apostaDao;
	
	//lista de participantes para consulta
	private List<Participante> participanteList = new ArrayList<Participante>(); 
	
	//participante selecionado
	private Participante participanteSelecionado = new Participante();
	
	//nome participante seleciionado
	private String nomeParticipanteSelecionado;
	
	//lista de jogos ok
	private List<Jogos> jogosOk = new ArrayList<Jogos>();
	
	//listas de tipo de acerto para cada participante
	private List<Jogos> escoresCheiosList;
	private List<Jogos> empateSemPlacarList;
	private List<Jogos> escoreSelecao1List;
	private List<Jogos> escoreSelecao2List;
	private List<Jogos> naoPontuouList;

	//grafico1
	private PieChartModel pieModel; 
	
	@PostConstruct
	public void init(){
		jogosOk = jogosService.findAllOk();
		
		for(Jogos jogo : jogosOk){
			Hibernate.initialize(jogo.getGrupo());
			Hibernate.initialize(jogo.getSelecao1());
			Hibernate.initialize(jogo.getSelecao2());
		}
		
		pieModel = new PieChartModel();  
		pieModel.set("Não Selecionado", 100);
	}
	
	public PieChartModel getPieModel() {  
		return pieModel;  
	}
	
	public void createPieModel() {  
		setParticipanteSelecionado(participanteService.findByParticipante(participanteSelecionado));

		pieModel = new PieChartModel();  

		Apostas aposta = new Apostas();
		escoresCheiosList = new ArrayList<Jogos>();
		naoPontuouList = new ArrayList<Jogos>();
		empateSemPlacarList = new ArrayList<Jogos>();
		escoreSelecao1List = new ArrayList<Jogos>();
		escoreSelecao2List = new ArrayList<Jogos>();

		for (Jogos jogo : jogosOk) {

			Integer pontos = 0;

			aposta = apostaDao.getApostaByJogoParticipante(
					participanteSelecionado, jogo);

			if(aposta.getGol1() == null || aposta.getGol2() == null) {
				pontos = 0;
				naoPontuouList.add(jogo);
			}
			else {
				// Escore Cheio
				if ((aposta.getGol1() == jogo.getGol1())
						&& (aposta.getGol2() == jogo.getGol2())) {
					pontos = 6;
					escoresCheiosList.add(jogo);
				}
				// Empate sem acertar o placar
				else {

					if (((aposta.getGol1() - aposta.getGol2()) == 0)
							&& ((jogo.getGol1() - jogo.getGol2()) == 0)) {
						pontos = 3;
						empateSemPlacarList.add(jogo);
					}

						// Escore de um dos times
					else if (aposta.getGol1() == jogo.getGol1()){
						pontos = 2;
						escoreSelecao1List.add(jogo);

					}
					else if((aposta.getGol2() == jogo.getGol2())){
						pontos = 2;
						escoreSelecao2List.add(jogo);
					}
					else {
						pontos = 0;
						naoPontuouList.add(jogo);
					}

				}
			}
		}
		//grafico 1
		pieModel.set("Escores Cheios", escoresCheiosList.size());
		pieModel.set("Empate sem acertar o placar", empateSemPlacarList.size());
		pieModel.set("Placar da Seleção 1", escoreSelecao1List.size());
		pieModel.set("Placar da Seleção 2", escoreSelecao2List.size());
		pieModel.set("Não Pontuou", naoPontuouList.size());
		
	}

	public void consultarParticipante() {
		try {
			participanteList = participanteService
					.searchByNome(getNomeParticipanteSelecionado());
		} catch (Exception e) {
			participanteList = new ArrayList<Participante>();
			FacesUtil.addErroMessage(e.getMessage());
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public void consultarParticipanteDialog() {
		consultarParticipante();

		if (!participanteList.isEmpty()) {
			RequestContext.getCurrentInstance().execute("dlg.show()");
		}

	}
	
	public void aposConsultarParticipante(Participante usu) {
		Participante p = new Participante();
		p.setId(getParticipanteSelecionado().getId());
		p = (Participante) usu;
		setParticipanteSelecionado(participanteService.findByParticipante(p));
		nomeParticipanteSelecionado = participanteSelecionado.getNome();
		
		//testar consulta automatica
		createPieModel();
	}
	
	public Participante getParticipanteSelecionado() {
		return participanteSelecionado;
	}

	public void setParticipanteSelecionado(Participante participanteSelecionado) {
		this.participanteSelecionado = participanteSelecionado;
	}

	public List<Jogos> getJogosOk() {
		return jogosOk;
	}

	public void setJogosOk(List<Jogos> jogosOk) {
		this.jogosOk = jogosOk;
	}

	public List<Jogos> getEscoresCheiosList() {
		return escoresCheiosList;
	}

	public void setEscoresCheiosList(List<Jogos> escoresCheiosList) {
		this.escoresCheiosList = escoresCheiosList;
	}

	public List<Jogos> getEmpateSemPlacarList() {
		return empateSemPlacarList;
	}

	public void setEmpateSemPlacarList(List<Jogos> empateSemPlacarList) {
		this.empateSemPlacarList = empateSemPlacarList;
	}

	public List<Jogos> getEscoreSelecao1List() {
		return escoreSelecao1List;
	}

	public void setEscoreSelecao1List(List<Jogos> escoreSelecao1List) {
		this.escoreSelecao1List = escoreSelecao1List;
	}

	public List<Jogos> getEscoreSelecao2List() {
		return escoreSelecao2List;
	}

	public void setEscoreSelecao2List(List<Jogos> escoreSelecao2List) {
		this.escoreSelecao2List = escoreSelecao2List;
	}

	public List<Jogos> getNaoPontuouList() {
		return naoPontuouList;
	}

	public void setNaoPontuouList(List<Jogos> naoPontuouList) {
		this.naoPontuouList = naoPontuouList;
	}

	public List<Participante> getParticipanteList() {
		return participanteList;
	}

	public void setParticipanteList(List<Participante> participanteList) {
		this.participanteList = participanteList;
	}

	public String getNomeParticipanteSelecionado() {
		return nomeParticipanteSelecionado;
	}

	public void setNomeParticipanteSelecionado(String nomeParticipanteSelecionado) {
		this.nomeParticipanteSelecionado = nomeParticipanteSelecionado;
	}
	
}
