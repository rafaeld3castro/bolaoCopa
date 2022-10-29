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

import br.com.bolaoCopaDoMundo.domain.ClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Pontuacao;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.AuthenticationService;
import br.com.bolaoCopaDoMundo.service.ClassificacaoGrupoService;
import br.com.bolaoCopaDoMundo.service.JogosService;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;
import br.com.bolaoCopaDoMundo.service.PontuacaoService;
import br.com.bolaoCopaDoMundo.util.BolaoUtil;
import br.com.bolaoCopaDoMundo.util.FacesUtil;
import br.com.bolaoCopaDoMundo.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("classificacaoGeralBean")
@Scope("session")
public class ClassificacaoGeralBean implements Serializable {

	static Logger logger = Logger.getLogger(ClassificacaoGeralBean.class);

	@Autowired
	private PontuacaoService pontuacaoService;
	
	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private ClassificacaoGrupoService classificacaoGrupoService;

	@Autowired
	private JogosService jogosService;
	
	@Autowired
	private ParticipanteService ParticipanteService;

	@Autowired
	private BolaoUtil bolaoUtil;

	@Autowired
	private RelatorioUtil relatorioUtil;

	private String nome = new String();
	private int ativo = -1;// traz a lista toda
	private Pontuacao Pontuacao = new Pontuacao();
	private List<Pontuacao> classificacaoList = new ArrayList<Pontuacao>();
	private List<Jogos> jogosOk = new ArrayList<Jogos>();
	private List<ClassificacaoGrupo> classificacoesOk = new ArrayList<ClassificacaoGrupo>();
	private boolean mensagem = true;
	private Jogos ultimoJogo = new Jogos();
	private ClassificacaoGrupo ultimoClassificacaoGrupo = new ClassificacaoGrupo();
	private LazyDataModel<Pontuacao> model;
	private List<Participante> participanteList = new ArrayList<Participante>();
	List<String> emails =  new ArrayList<String>();

	// util
	// verifica se já foi feita alguma consulta
	private boolean consultar = false;

	@PostConstruct
	public void init() {
		model = new LazyDataModel<Pontuacao>() {

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
		jogosOk = jogosService.findAllOk();
		if (jogosOk != null && !jogosOk.isEmpty()) {
			ultimoJogo = jogosOk.get(jogosOk.size() - 1);
			FacesUtil
					.addWarnMessageWithouDetail("Classificação gerada até o jogo "
							+ ultimoJogo.getSelecao1().getNome()
							+ " "
							+ ultimoJogo.getGol1()
							+ " X "
							+ ultimoJogo.getGol2()
							+ " "
							+ ultimoJogo.getSelecao2().getNome());
		}
		classificacoesOk = classificacaoGrupoService.findAllOk();
		if (classificacoesOk != null && !classificacoesOk.isEmpty()) {
			ultimoClassificacaoGrupo = classificacoesOk.get(classificacoesOk
					.size() - 1);
			FacesUtil.addWarnMessageWithouDetail(" Classificação do Grupo  "
					+ ultimoClassificacaoGrupo.getGrupo().getNome()
					+ " 1º Lugar: "
					+ ultimoClassificacaoGrupo.getPosicao1().getNome()
					+ " 2º Lugar: "
					+ ultimoClassificacaoGrupo.getPosicao2().getNome());
		}
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
	
	
	public void enviarEmailParticipantes () {
		
		participanteList = ParticipanteService.findAllOk();
		for (Participante participante : participanteList) {
			if(participante.getEmail() != null)
				emails.add(participante.getEmail());
		}
		/*//Jogos ultimoJogo = new Jogos();
		jogosOk = jogosService.findAllOk();
		if (jogosOk != null && !jogosOk.isEmpty()) {
			ultimoJogo = jogosOk.get(jogosOk.size() - 1);
			FacesUtil
					.addWarnMessageWithouDetail("Classificação gerada até o jogo "
							+ ultimoJogo.getSelecao1().getNome()
							+ " "
							+ ultimoJogo.getGol1()
							+ " X "
							+ ultimoJogo.getGol2()
							+ " "
							+ ultimoJogo.getSelecao2().getNome());
		}*/
		StringBuilder assunto = new StringBuilder();
		assunto.append("Classificação após jogo ");
		assunto.append(ultimoJogo.getId());
		assunto.append(" - (");
		assunto.append(ultimoJogo.getSelecao1().getNome());
		assunto.append(" ");
		assunto.append(ultimoJogo.getGol1());
		assunto.append(" X ");
		assunto.append(ultimoJogo.getGol2());
		assunto.append(" ");
		assunto.append(ultimoJogo.getSelecao2().getNome());
		assunto.append(")");
		
						
		
		
		
			StringBuilder msg = new StringBuilder();
			
		    
		    msg.append("Segue em anexo a classificação geral do Bolão após o jogo ");
		    msg.append(ultimoJogo.getId());
		    msg.append("com o resultado:\\n\\n");
		    msg.append(ultimoJogo.getSelecao1().getNome());
		    msg.append(" ");
		    msg.append(ultimoJogo.getGol1());
		    msg.append(" X ");
		    msg.append(ultimoJogo.getGol2());
		    msg.append(" ");
		    msg.append(ultimoJogo.getSelecao2().getNome());
		    
		    
		    msg.append("\\n\\nO site encontra-se no link  http://bolao.noip.me/ ");
		    msg.append("\\nLembrando que se você esqueceu a senha, você pode recuperá-la através da opção Esqueci minha senha na tela de login.");
		    msg.append("\\nCaso não consiga efetuar seu login. Entre em contato com o Administrador atraves do email <bolaocopadomundofc@gmail.com> ou pelo telefone 987533736\n\n");
			msg.append("Atenciosamente,\n");
			msg.append("Administração do Bolão.\n\n");
		    msg.append("E-mail gerado automaticamente, favor não responder.");
		    
		   

			bolaoUtil.sendAttachEmail(emails,assunto.toString(),msg.toString(),relatorioUtil.getRelatorio());
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
			jogosOk = jogosService.findAllOk();
			if (jogosOk != null && jogosOk.size()>0) {
				ultimoJogo = jogosOk.get(jogosOk.size() - 1);
				resultado.append("Classificação gerada até o jogo "
						+ ultimoJogo.getSelecao1().getNome() + " "
						+ ultimoJogo.getGol1() + " X " + ultimoJogo.getGol2()
						+ " " + ultimoJogo.getSelecao2().getNome());
			}
			classificacoesOk = classificacaoGrupoService.findAllOk();
			if (classificacoesOk != null && classificacoesOk.size()>0 ) {
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

}
