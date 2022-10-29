package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.Apostas;
import br.com.bolaoCopaDoMundo.domain.ClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Pontuacao;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.ApostaClassificacaoGrupoService;
import br.com.bolaoCopaDoMundo.service.ApostasService;
import br.com.bolaoCopaDoMundo.service.ClassificacaoGrupoService;
import br.com.bolaoCopaDoMundo.service.JogosService;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;
import br.com.bolaoCopaDoMundo.service.PontuacaoService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;
import br.com.bolaoCopaDoMundo.util.RelatorioUtil;

@Component("inicioBean")
@Scope("view")
public class InicioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(InicioBean.class);

	@Autowired
	private PontuacaoService pontuacaoService;

	@Autowired
	private ClassificacaoGrupoService classificacaoGrupoService;

	@Autowired
	private JogosService jogosService;

	@Autowired
	private ApostasService apostasService;

	@Autowired
	private ParticipanteService participanteService;

	@Autowired
	private ApostaClassificacaoGrupoService apostaClassificacaoGrupoService;

	@Autowired
	private RelatorioUtil relatorioUtil;

	private String nome = new String();
	private int ativo = -1;// traz a lista toda
	private Pontuacao Pontuacao = new Pontuacao();
	private List<Pontuacao> classificacaoList = new ArrayList<Pontuacao>();
	private List<Jogos> jogos = new ArrayList<Jogos>();
	private List<Apostas> apostas = new ArrayList<Apostas>();
	private List<ClassificacaoGrupo> classificacoesOk = new ArrayList<ClassificacaoGrupo>();
	private boolean mensagem = true;
	private Jogos ultimoJogo = new Jogos();
	private ClassificacaoGrupo ultimoClassificacaoGrupo = new ClassificacaoGrupo();
	private LazyDataModel<Pontuacao> model;
	private String usuario = FacesUtil.getName();
	private Participante participante;
	private Long apostasGolNull;
	private int apostasPendentes;
	private int totalParticipantes;
	private int totalParticipantesAtivos;

	// util
	// verifica se já foi feita alguma consulta
	private boolean consultar = false;

	@PostConstruct
	public void init() {
		participante = participanteService.findByUsername(usuario);

		model = new LazyDataModel<Pontuacao>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public List<Pontuacao> load(int inicio, int maxPerPage,
					String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				classificacaoList = pontuacaoService
						.findAll(inicio, maxPerPage);
				setPageSize(maxPerPage);
				setRowCount(pontuacaoService.count());
				System.out.println("tam " + classificacaoList.size());
				return classificacaoList;
			}

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}
		};
		
		jogos = jogosService.findAll();

		if (!participante.getUsername().equals("ADMIN")) {
			
			apostasGolNull = apostasService
					.countByParticipanteAndGolNull(participante);
			
			apostas = apostasService.findByUsername(participante.getUsername());
			
			if(apostasGolNull==0 && apostas.size()==0)
				apostasGolNull = 48l;
			
			apostasPendentes = apostaClassificacaoGrupoService
					.countByApostasPendentes(participante);
	
		}
		setTotalParticipantes(participanteService.findAll().size()-1);
		setTotalParticipantesAtivos(participanteService.count("", 1)-1);
		 	
	}

	public String limpaTela() {
		ativo = -1;
		Pontuacao = new Pontuacao();
		nome = new String();
		classificacaoList = new ArrayList<Pontuacao>();
		this.model = new LazyDataModel<Pontuacao>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Pontuacao> load(int inicio, int maxPerPage,
					String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				return classificacaoList;
			}

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}
		};
		consultar = false;

		return "classificacaoGeral";
	}

	/**
	 * Emitir Relatorio
	 * 
	 * @return
	 */
	public String relatorio() {

		try {
			System.out.println("inicio relatorio");
			Map<String, Object> parametros = new HashMap<String, Object>();
			StringBuilder resultado = new StringBuilder();
			jogos = jogosService.findAllOk();
			if (jogos != null) {
				ultimoJogo = jogos.get(jogos.size() - 1);
				resultado.append("Classificação gerada até o jogo "
						+ ultimoJogo.getSelecao1().getNome() + " "
						+ ultimoJogo.getGol1() + " X " + ultimoJogo.getGol2()
						+ " " + ultimoJogo.getSelecao2().getNome());
			}
			classificacoesOk = classificacaoGrupoService.findAllOk();
			if (classificacoesOk != null) {
				ultimoClassificacaoGrupo = classificacoesOk
						.get(classificacoesOk.size() - 1);
				resultado.append("\n Classificação do Grupo  "
						+ ultimoClassificacaoGrupo.getGrupo().getNome()
						+ " 1º Lugar: "
						+ ultimoClassificacaoGrupo.getPosicao1().getNome()
						+ " 2º Lugar: "
						+ ultimoClassificacaoGrupo.getPosicao2().getNome());
			}
			parametros.put("RESULTADO", resultado.toString());
			parametros.put("FILTRO", "WHERE 1=1");

			relatorioUtil.relatorio("classificacao.jasper", parametros,
					"classificacao.pdf");
			System.out.println("fim relatorio");
		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Erro na geração da Classificação. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	/**
	 * Getters e Setters
	 */

	public Pontuacao getPontuacao() {
		return Pontuacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPontuacao(Pontuacao Pontuacao) {
		this.Pontuacao = Pontuacao;
	}

	public List<Pontuacao> getClassificacaoList() {
		return classificacaoList;
	}

	public void setClassificacaoList(List<Pontuacao> classificacaoList) {
		this.classificacaoList = classificacaoList;
	}

	public boolean isConsultar() {
		return consultar;
	}

	public void setConsultar(boolean consultar) {
		this.consultar = consultar;
	}

	public LazyDataModel<Pontuacao> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Pontuacao> model) {
		this.model = model;
	}

	public int getAtivo() {
		return ativo;
	}

	public void setAtivo(int ativo) {
		this.ativo = ativo;
	}

	public Jogos getUltimoJogo() {
		return ultimoJogo;
	}

	public void setUltimoJogo(Jogos ultimoJogo) {
		this.ultimoJogo = ultimoJogo;
	}

	public boolean isMensagem() {
		return mensagem;
	}

	public void setMensagem(boolean mensagem) {
		this.mensagem = mensagem;
	}

	public List<ClassificacaoGrupo> getClassificacoesOk() {
		return classificacoesOk;
	}

	public void setClassificacoesOk(List<ClassificacaoGrupo> classificacoesOk) {
		this.classificacoesOk = classificacoesOk;
	}

	public List<Jogos> getJogos() {
		return jogos;
	}

	public void setJogos(List<Jogos> jogos) {
		this.jogos = jogos;
	}

	public Long getApostasGolNull() {
		return apostasGolNull;
	}

	public void setApostasGolNull(Long apostasGolNull) {
		this.apostasGolNull = apostasGolNull;
	}

	public int getApostasPendentes() {
		return apostasPendentes;
	}

	public void setApostasPendentes(int apostasPendentes) {
		this.apostasPendentes = apostasPendentes;
	}

	public int getTotalParticipantes() {
		return totalParticipantes;
	}

	public void setTotalParticipantes(int totalParticipantes) {
		this.totalParticipantes = totalParticipantes;
	}

	public int getTotalParticipantesAtivos() {
		return totalParticipantesAtivos;
	}

	public void setTotalParticipantesAtivos(int totalParticipantesAtivos) {
		this.totalParticipantesAtivos = totalParticipantesAtivos;
	}	

}
