package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.hibernate.JDBCException;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Selecao;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.GrupoService;
import br.com.bolaoCopaDoMundo.service.JogosService;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;
import br.com.bolaoCopaDoMundo.service.PontuacaoService;
import br.com.bolaoCopaDoMundo.service.SelecaoService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;
import br.com.bolaoCopaDoMundo.util.RelatorioUtil;

@Component("jogosListBean")
@Scope("view")
public class JogosListBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(JogosListBean.class);

	@Autowired
	private JogosService jogosService;

	@Autowired
	private ParticipanteService participantesService;

	@Autowired
	private PontuacaoService pontuacaoService;

	@Autowired
	private GrupoService grupoService;

	@Autowired
	private SelecaoService selecaoService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	// parametos de tela de consulta
	private String nome = new String();

	// entidades das telas
	private List<Jogos> lista;
	private Jogos jogo = new Jogos();
	private List<Grupo> opcaoGrupo;
	private Grupo grupoSelecionado;
	private Selecao selecao1Selecionado;
	private Selecao selecao2Selecionado;
	private List<Selecao> opcaoSelecao1;
	private List<Selecao> opcaoSelecao2;
	private String datajogo;
	private List<Jogos> jogos;
	private List<Jogos> jogosOk;
	private List<Participante> participantes;
	private Long jogosId;

	private int first;

	private LazyDataModel<Jogos> modelo;

	@PostConstruct
	public void init() {
		setOpcaoGrupo(grupoService.findAll());
	}

	public void consultarGrupo() {
		try {
			opcaoSelecao1 = new ArrayList<Selecao>();
			opcaoSelecao2 = new ArrayList<Selecao>();
			if (grupoSelecionado.getId() != null
					&& grupoSelecionado.getId() != 0) {
				opcaoSelecao1 = selecaoService.findByGrupo(grupoSelecionado);
				opcaoSelecao2 = selecaoService.findByGrupo(grupoSelecionado);
			}

		} catch (Exception e) {
			opcaoSelecao1 = new ArrayList<Selecao>();
			opcaoSelecao2 = new ArrayList<Selecao>();
			FacesUtil
					.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public void consultar() {

		Date data = null;
				
		if (datajogo != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				data = format.parse(datajogo);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		lista = jogosService.findByCriterios(data, grupoSelecionado);

		if (lista == null || lista.isEmpty())
			FacesUtil
					.addInfoMessage("Não existe jogos cadastrados com esses critérios");
		else
			jogos = lista;
		
		//setando as variaveis gol1t e gol2t
		for(Jogos jogo : lista){
			if(jogo.getGol1() != null) {
				jogo.setGol1t(jogo.getGol1().toString());
			}
			else {
				jogo.setGol1t("");
			}
			if(jogo.getGol2() != null) {
				jogo.setGol2t(jogo.getGol2().toString());
			}
			else {
				jogo.setGol2t("");
			}
		}

	}

	public void geraPontuacao() {

		participantes = participantesService.findAllOk();
		jogosOk = jogosService.findAllOk();

		if (participantes == null || participantes.isEmpty() || jogosOk == null
				|| jogosOk.isEmpty())
			FacesUtil
					.addInfoMessage("Não foi possível gerar a pontuação! Não foi gravado nenhum resultado final.");
		else {
			if (pontuacaoService.geracaoPontuacao(participantes, jogosOk))
				FacesUtil.addInfoMessage("Pontuação gerada com sucesso.");
			else
				FacesUtil.addInfoMessage("Não foi possível gerar a pontuação!");
		}
	}

	public LazyDataModel<Jogos> getModelo() {
		return modelo;
	}

	public void onPageChange(PageEvent event) {
		this.setFirst(((DataTable) event.getSource()).getFirst());
	}

	private void limparListas() {
		setOpcaoGrupo(grupoService.findAll());
		setNome(new String());
		this.jogo = new Jogos();
		lista = new ArrayList<Jogos>();
		this.modelo = new LazyDataModel<Jogos>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<Jogos> load(int inicio, int maxPerPage,
					String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				return lista;
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
	}

	/**
	 * Limpar tela
	 */
	public String limpaTela() {
		setJogo(new Jogos());
		limparListas();

		return "jogosList?faces-redirect=true";
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
			//parametros.put("FILTRO", "WHERE participante.idparticipante <> 1");
			relatorioUtil.relatorio("jogos.jasper", parametros,
					"jogos.pdf");
			System.out.println("fim relatorio");
		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Erro na geração do Aposta por Participantes. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public void excluir() {
		System.out.println(jogo);
		try {
			jogosService.excluir(jogo);
			FacesUtil.addInfoMessage("Registro exclu�do com sucesso.");
		} catch (DataIntegrityViolationException e) {
			FacesUtil
					.addErroMessage("Existem registros filhos cadastrados na seção selecionada. Exclusão não pode ser realizada.");
			logger.error(e);
		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
		} catch (DataAccessException e) {
			FacesUtil.addErroMessage(e.getMessage());
		} catch (JDBCException e) {
			FacesUtil
					.addErroMessage("Ocorreu algum um erro ao excluir. Opera��o cancelada.");
		} catch (Exception e) {
			System.out.println(e.getCause());
			System.out.println(e.getMessage());
			e.printStackTrace();
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao excluir. Opera��o cancelada.");

		}
		consultar();
	}

	public String getNome() {
		return nome;
	}

	public List<Grupo> getOpcaoGrupo() {
		return opcaoGrupo;
	}

	public void setOpcaoGrupo(List<Grupo> opcaoGrupo) {
		this.opcaoGrupo = opcaoGrupo;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getFirst() {
		return first;
	}

	public List<Jogos> getLista() {
		return lista;
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	public Jogos getJogo() {
		return jogo;
	}

	public void setJogo(Jogos jogo) {
		this.jogo = jogo;
	}

	public Selecao getSelecao1Selecionado() {
		return selecao1Selecionado;
	}

	public void setSelecao1Selecionado(Selecao selecao1Selecionado) {
		this.selecao1Selecionado = selecao1Selecionado;
	}

	public Selecao getSelecao2Selecionado() {
		return selecao2Selecionado;
	}

	public void setSelecao2Selecionado(Selecao selecao2Selecionado) {
		this.selecao2Selecionado = selecao2Selecionado;
	}

	public List<Selecao> getOpcaoSelecao1() {
		return opcaoSelecao1;
	}

	public void setOpcaoSelecao1(List<Selecao> opcaoSelecao1) {
		this.opcaoSelecao1 = opcaoSelecao1;
	}

	public List<Selecao> getOpcaoSelecao2() {
		return opcaoSelecao2;
	}

	public void setOpcaoSelecao2(List<Selecao> opcaoSelecao2) {
		this.opcaoSelecao2 = opcaoSelecao2;
	}

	public String getDatajogo() {
		return datajogo;
	}

	public void setDatajogo(String datajogo) {
		this.datajogo = datajogo;
	}

	public List<Jogos> getJogos() {
		return jogos;
	}

	public void setJogos(List<Jogos> jogos) {
		this.jogos = jogos;
	}

	public Long getJogosId() {
		return jogosId;
	}

	public void setJogosId(Long jogosId) {
		this.jogosId = jogosId;
	}

	public void setLista(List<Jogos> lista) {
		this.lista = lista;
	}

//	public void onCellEdit(CellEditEvent event) {
	public void onCellEdit() {

		FacesContext context = FacesContext.getCurrentInstance();
		Jogos jogoAtualizar = context.getApplication().evaluateExpressionGet(
				context, "#{item}", Jogos.class);
		jogoAtualizar.setFlResultadoOk(false);
		
		//colocando o valor da string gol1t em gol1
		if (!jogoAtualizar.getGol1t().isEmpty()) {
			jogoAtualizar.setGol1(Integer.parseInt(jogoAtualizar.getGol1t()));
		} else {
			jogoAtualizar.setGol1(null);
		}
		//colocando a valor da string gol2t em gol2
		if (!jogoAtualizar.getGol2t().isEmpty()) {
			jogoAtualizar.setGol2(Integer.parseInt(jogoAtualizar.getGol2t()));
		} else {
			jogoAtualizar.setGol2(null);
		}
		//verificando se os dois valores preenchidos estao oks
		if(!jogoAtualizar.getGol1t().isEmpty() && !jogoAtualizar.getGol2t().isEmpty()) {
			jogoAtualizar.setFlResultadoOk(true);
		}
		
		System.out.println(jogoAtualizar.getDtJogo() + " "
				+ jogoAtualizar.getGol1() + " " + jogoAtualizar.getGol2() + " "
				+ jogoAtualizar.getId());

		try {

			jogosService.salvar(jogoAtualizar);

			FacesUtil
					.addInfoMessage("Operação realizada com sucesso. Resultados do jogo foi gravado.");
			logger.info("Operação realizada com sucesso.");
		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage("Ocorreu o seguinte erro: "
					+ e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (PersistenceException e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (JDBCException e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

}
