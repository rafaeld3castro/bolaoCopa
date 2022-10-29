package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.dao.ApostasDAO;
import br.com.bolaoCopaDoMundo.domain.Apostas;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Pontuacao;
import br.com.bolaoCopaDoMundo.service.JogosService;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;

@Component("estatisticasJogoBean")
@Scope("view")
public class EstatisticasJogoBean implements Serializable{

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

	//grafico1
	private PieChartModel pieModel; 
	
	//grafico2
	private CartesianChartModel categoryModel;

	//listas de tipo de acerto para cada participante
	private List<Participante> escoresCheios;
	private List<Participante> empateSemPlacar;
	private List<Participante> escoreSelecao1;
	private List<Participante> escoreSelecao2;
	private List<Participante> naoPontuou;

	//lista de todos os participates
	private List<Participante> participantes;

	//lista e todos os jogos Ok
	private List<Jogos> jogosOk;

	//jogo selecionado
	private Jogos jogoSelecionado = new Jogos();
	
	//flag rederiza pieModel
	private boolean flagPieModel = false;

	@PostConstruct
	public void init(){
		jogosOk = jogosService.findAllOk();
		participantes = participanteService.findAll();
		
		//inicializa grafico1
		pieModel = new PieChartModel();  
		pieModel.set("Não Selecionado", 100);
		
		//inicializa grafico2
//		categoryModel = new CartesianChartModel();
//		ChartSeries apostasSerie = new ChartSeries();  
//		apostasSerie.setLabel("Não Selecionado");  
//		apostasSerie.set("Apostas", 100); 
//		categoryModel.addSeries(apostasSerie);
	}

	public PieChartModel getPieModel() {  
		return pieModel;  
	}
	
	public CartesianChartModel getCategoryModel() {  
        return categoryModel;  
    }

	@SuppressWarnings("unused")
	public void createPieModel() {  
		setJogoSelecionado(jogosService.findById(jogoSelecionado.getId()));
//		flagPieModel = true;
		pieModel = new PieChartModel();  
		categoryModel = new CartesianChartModel();  

		Apostas aposta = new Apostas();
		escoresCheios = new ArrayList<Participante>();
		naoPontuou = new ArrayList<Participante>();
		empateSemPlacar = new ArrayList<Participante>();
		escoreSelecao1 = new ArrayList<Participante>();
		escoreSelecao2 = new ArrayList<Participante>();

		for (Participante participante : participantes) {

			Integer pontos = 0;

			aposta = apostaDao.getApostaByJogoParticipante(
					participante, jogoSelecionado);

			if(aposta.getGol1() == null || aposta.getGol2() == null) {
				pontos = 0;
				naoPontuou.add(participante);
			}
			else {
				// Escore Cheio
				if ((aposta.getGol1() == jogoSelecionado.getGol1())
						&& (aposta.getGol2() == jogoSelecionado.getGol2())) {
					pontos = 6;
					escoresCheios.add(participante);
				}
				// Empate sem acertar o placar
				else {

					if (((aposta.getGol1() - aposta.getGol2()) == 0)
							&& ((jogoSelecionado.getGol1() - jogoSelecionado.getGol2()) == 0)) {
						pontos = 3;
						empateSemPlacar.add(participante);
					}

						// Escore de um dos times
					else if (aposta.getGol1() == jogoSelecionado.getGol1()){
						pontos = 2;
						escoreSelecao1.add(participante);

					}
					else if((aposta.getGol2() == jogoSelecionado.getGol2())){
						pontos = 2;
						escoreSelecao2.add(participante);
					}
					else {
						pontos = 0;
						naoPontuou.add(participante);
					}
						//								// Acertar o 1º time como vencedor
						//								if ((aposta.getGol1() > aposta.getGol2())
						//										&& (jogo.getGol1() > jogo.getGol2()))
						//									pontos = pontos + 1;
						//	
						//								// Acertar o 2º time como vencedor
						//								if ((aposta.getGol1() < aposta.getGol2())
						//										&& (jogo.getGol1() < jogo.getGol2()))
						//									pontos = pontos + 1;

				}
			}
		}
		//grafico 1
		pieModel.set("Escores Cheios", escoresCheios.size());
		pieModel.set("Empate sem acertar o placar", empateSemPlacar.size());
		pieModel.set("Placar da Seleção 1", escoreSelecao1.size());
		pieModel.set("Placar da Seleção 2", escoreSelecao2.size());
		pieModel.set("Não Pontuou", naoPontuou.size());
		
		//grafico 2
//		ChartSeries escoresCheiosSerie = new ChartSeries();  
//		escoresCheiosSerie.setLabel("Escores Cheios");  
//		escoresCheiosSerie.set("Apostas", escoresCheios.size());
//		categoryModel.addSeries(escoresCheiosSerie);
//		
//		ChartSeries empateSemPlacarSerie = new ChartSeries();  
//		empateSemPlacarSerie.setLabel("Empate sem acertar o placar");  
//		empateSemPlacarSerie.set("Apostas", empateSemPlacar.size());
//		categoryModel.addSeries(empateSemPlacarSerie);
//		
//		ChartSeries escoreSelecao1Serie = new ChartSeries();  
//		escoreSelecao1Serie.setLabel("Placar da Seleção 1");  
//		escoreSelecao1Serie.set("Apostas", escoreSelecao1.size());
//		categoryModel.addSeries(escoreSelecao1Serie);
//		
//		ChartSeries escoreSelecao2Serie = new ChartSeries();  
//		escoreSelecao2Serie.setLabel("Placar da Seleção 2");  
//		escoreSelecao2Serie.set("Apostas", escoreSelecao2.size());
//		categoryModel.addSeries(escoreSelecao2Serie);
//		
//		ChartSeries naoPontuouSerie = new ChartSeries();  
//		naoPontuouSerie.setLabel("Não Pontuou");  
//		naoPontuouSerie.set("Apostas", naoPontuou.size());
//		categoryModel.addSeries(naoPontuouSerie);
	}
	
	public List<Participante> getEscoresCheios() {
		return escoresCheios;
	}

	public void setEscoresCheios(List<Participante> escoresCheios) {
		this.escoresCheios = escoresCheios;
	}

	public List<Participante> getEmpateSemPlacar() {
		return empateSemPlacar;
	}

	public void setEmpateSemPlacar(List<Participante> empateSemPlacar) {
		this.empateSemPlacar = empateSemPlacar;
	}

	public List<Participante> getEscoreSelecao1() {
		return escoreSelecao1;
	}

	public void setEscoreSelecao1(List<Participante> escoreSelecao1) {
		this.escoreSelecao1 = escoreSelecao1;
	}

	public List<Participante> getEscoreSelecao2() {
		return escoreSelecao2;
	}

	public void setEscoreSelecao2(List<Participante> escoreSelecao2) {
		this.escoreSelecao2 = escoreSelecao2;
	}

	public List<Participante> getNaoPontuou() {
		return naoPontuou;
	}

	public void setNaoPontuou(List<Participante> naoPontuou) {
		this.naoPontuou = naoPontuou;
	}

	public List<Participante> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<Participante> participantes) {
		this.participantes = participantes;
	}

	public List<Jogos> getJogosOk() {
		return jogosOk;
	}

	public void setJogosOk(List<Jogos> jogosOk) {
		this.jogosOk = jogosOk;
	}

	public Jogos getJogoSelecionado() {
		return jogoSelecionado;
	}

	public void setJogoSelecionado(Jogos jogoSelecionado) {
		this.jogoSelecionado = jogoSelecionado;
	}

	public boolean isFlagPieModel() {
		return flagPieModel;
	}

	public void setFlagPieModel(boolean flagPieModel) {
		this.flagPieModel = flagPieModel;
	} 

	

}
