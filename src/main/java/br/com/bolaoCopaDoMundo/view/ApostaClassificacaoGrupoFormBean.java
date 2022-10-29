package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.hibernate.JDBCException;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.ApostaClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Selecao;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.ApostaClassificacaoGrupoService;
import br.com.bolaoCopaDoMundo.service.GrupoService;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;
import br.com.bolaoCopaDoMundo.service.SelecaoService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;
import br.com.bolaoCopaDoMundo.util.RelatorioUtil;

@Component("apostaClassificacaoGrupoFormBean")
@Scope("view")
public class ApostaClassificacaoGrupoFormBean implements Serializable {

	private static final long serialVersionUID = 1L;
	// entidade da tela.
	private Selecao selecao = new Selecao();
	private boolean alterar = false;

	static Logger logger = Logger.getLogger(ApostaClassificacaoGrupoFormBean.class);

	@Autowired
	private ApostaClassificacaoGrupoService apostaClassificacaoGrupoService;

	@Autowired
	private GrupoService grupoService;

	@Autowired
	private SelecaoService selecaoService;
	
	@Autowired
	private ParticipanteService participanteService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	// entidades das telas
	private ApostaClassificacaoGrupo apostaClassificacao = new ApostaClassificacaoGrupo();
	private List<Grupo> opcaoGrupo;
	private Grupo grupoSelecionado;
	private Participante participante;
	private String usuario = FacesUtil.getName();
	private List<Selecao> listaSelecao = new ArrayList<Selecao>();
	private List<Selecao> listaSelecaoSelecionada = new ArrayList<Selecao>();

	private DualListModel<Selecao> selecoes;

	private List<ApostaClassificacaoGrupo> apostaClassificacaoGrupo;
	private Long classificacaoGrupoId;
	private int apostasPendentes;
	
	private boolean permiteSalvar = false;

	@PostConstruct
	public void init() {
		
		participante = participanteService.findByUsername(usuario);
		setOpcaoGrupo(grupoService.findAll());
		selecoes = new DualListModel<Selecao>(listaSelecao,
				listaSelecaoSelecionada);

		if (!FacesContext.getCurrentInstance().isPostback()) { // verifica se a
																// ação vem de
																// outra view
			if (classificacaoGrupoId == null) {
				return;
			} else {
				this.alterar = true; // setando informações do Objeto
				setApostaClassificacao(apostaClassificacaoGrupoService.findById(classificacaoGrupoId));
			}
		}

	}

	public void consultarGrupo() {
		try {
			listaSelecao = new ArrayList<Selecao>();
			listaSelecaoSelecionada = new ArrayList<Selecao>();
			if (grupoSelecionado.getId() != null
					&& grupoSelecionado.getId() != 0) {

				listaSelecao = selecaoService
						.findSelecaoNaoClassificadaByGrupoParticipante(grupoSelecionado,participante);

				if (listaSelecao.size() == 0)
					listaSelecao = selecaoService.findByGrupo(grupoSelecionado);

				apostaClassificacao = apostaClassificacaoGrupoService
						.findByParticipanteGrupo(grupoSelecionado,participante);

				if (apostaClassificacao != null) {
					listaSelecaoSelecionada.add(apostaClassificacao.getPosicao1());
					listaSelecaoSelecionada.add(apostaClassificacao.getPosicao2());
				}

				selecoes = new DualListModel<Selecao>(listaSelecao,
						listaSelecaoSelecionada);
			}
			
			if (listaSelecaoSelecionada.size() != 2)
				permiteSalvar = false;
			else
				permiteSalvar = true;

		} catch (Exception e) {
			listaSelecao = new ArrayList<Selecao>();
			listaSelecaoSelecionada = new ArrayList<Selecao>();
			FacesUtil
					.addErroMessage("Ocorreu algum erro na consulta. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public void configuraEditorclassificacaoGrupo() {
		setGrupoSelecionado(selecao.getGrupo());
	}

	public ApostaClassificacaoGrupo configuraAtributosVazios() {

		if (apostaClassificacao == null)
			apostaClassificacao = new ApostaClassificacaoGrupo();
			
			apostaClassificacao.setParticipante(participante);
			apostaClassificacao.setGrupo(grupoService.findById(grupoSelecionado.getId()));
			//apostaClassificacao.setFlResultadoOk(true);
			apostaClassificacao.setPosicao1(listaSelecaoSelecionada.get(0));
			apostaClassificacao.setPosicao2(listaSelecaoSelecionada.get(1));
			//apostaClassificacaoGrupoService.salvar(apostaClassificacao);
//		} //else if (classificacao != null) {
//			classificacaoGrupoService.excluir(classificacao);
//		}

		return apostaClassificacao;
	}

	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {
			if(grupoSelecionado == null )
				FacesUtil.addErroMessage("Ocorreu o seguinte erro: O campo grupo é obrigatório.");
			
			configuraAtributosVazios();

			apostaClassificacaoGrupoService.salvar(apostaClassificacao);
			apostasPendentes = apostaClassificacaoGrupoService.countByApostasPendentes(participante);
			//limpar();
			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			FacesUtil.addWarnMessageWithouDetail("Existem "
					+ apostasPendentes + " apostas incompletas!");
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
					.addErroMessage("Ocorreu algum erro ao salvar.Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	/**
	 * Emitir Relatorio
	 * 
	 * @return
	 */
	public String relatorio() {

		try {
			
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			String filtro = " WHERE participante.idparticipante =  " +participante.getId();
			parametros.put("FILTRO", filtro.toString());
			
			FacesContext facesContext = FacesContext.getCurrentInstance();  
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
			String pathRel = servletContext.getRealPath("//WEB-INF/relatorios/"); 
			parametros.put("SUBREPORT_DIR", pathRel);
			
			relatorioUtil.relatorio("apostasParticipante.jasper", parametros,
					"apostasParticipante.pdf");

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

	/**
	 * Realizar antes de carregar tela incluir
	 * 
	 * @return
	 */
	public String prepareIncluir() {
		this.alterar = false;
		setApostaClassificacao(new ApostaClassificacaoGrupo());
		return "apostaClassificacaoGrupoForm?faces-redirect=true";
	}

	/**
	 * Limpar form
	 * 
	 */
	private void limpar() {
		this.alterar = false;
		setSelecao(new Selecao());
		configuraEditorclassificacaoGrupo();
	}

	public String limpaTela() {
		limpar();
		return "apostaClassificacaoGrupoForm";
	}

	public String voltar() {
		return "apostas?faces-redirect=true";
	}

	/**
	 * Gets and Sets
	 */

	public boolean isAlterar() {
		return alterar;
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	public Selecao getSelecao() {
		return selecao;
	}

	public void setSelecao(Selecao selecao) {
		this.selecao = selecao;
	}

	public List<Grupo> getOpcaoGrupo() {
		return opcaoGrupo;
	}

	public void setOpcaoGrupo(List<Grupo> opcaoGrupo) {
		this.opcaoGrupo = opcaoGrupo;
	}

	public List<Selecao> getListaSelecao() {
		return listaSelecao;
	}

	public void setListaSelecao(List<Selecao> listaSelecao) {
		this.listaSelecao = listaSelecao;
	}

	public List<Selecao> getListaSelecaoSelecionada() {
		return listaSelecaoSelecionada;
	}

	public void setListaSelecaoSelecionada(List<Selecao> listaSelecaoSelecionada) {
		this.listaSelecaoSelecionada = listaSelecaoSelecionada;
	}

	public Long getclassificacaoGrupoId() {
		return classificacaoGrupoId;
	}

	public void setclassificacaoGrupoId(Long classificacaoGrupoId) {
		this.classificacaoGrupoId = classificacaoGrupoId;
	}

	public ApostaClassificacaoGrupo getApostaClassificacao() {
		return apostaClassificacao;
	}

	public void setApostaClassificacao(ApostaClassificacaoGrupo apostaClassificacao) {
		this.apostaClassificacao = apostaClassificacao;
	}

	public List<ApostaClassificacaoGrupo> getApostaClassificacaoGrupo() {
		return apostaClassificacaoGrupo;
	}

	public void setApostaClassificacaoGrupo(
			List<ApostaClassificacaoGrupo> apostaClassificacaoGrupo) {
		this.apostaClassificacaoGrupo = apostaClassificacaoGrupo;
	}

	public Long getClassificacaoGrupoId() {
		return classificacaoGrupoId;
	}

	public void setClassificacaoGrupoId(Long classificacaoGrupoId) {
		this.classificacaoGrupoId = classificacaoGrupoId;
	}

	public DualListModel<Selecao> getSelecoes() {
		return selecoes;
	}

	public void setSelecoes(DualListModel<Selecao> selecoes) {
		this.selecoes = selecoes;
	}

	@SuppressWarnings("unchecked")
	public void onTransfer(TransferEvent event) {

		if (event.isAdd()) {
			listaSelecaoSelecionada.addAll((List<Selecao>) event.getItems());
			listaSelecao.removeAll((List<Selecao>) event.getItems());
		} else if (event.isRemove()) {
			listaSelecaoSelecionada.removeAll((List<Selecao>) event.getItems());
			listaSelecao.addAll((List<Selecao>) event.getItems());
		}
		
		StringBuilder builder = new StringBuilder();
		for (Object item : event.getItems()) {
			builder.append(item.toString());
		}

		FacesMessage msg = new FacesMessage();
		msg.setDetail(builder.toString());

		if (listaSelecaoSelecionada.size() != 2) {
			this.permiteSalvar = false;
			msg.setSeverity(FacesMessage.SEVERITY_WARN);
			msg.setSummary("Selecione 02 seleções.");
		} else {
			this.permiteSalvar = true;
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("Seleções transferidas.");
		}
		

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public boolean isPermiteSalvar() {
		return permiteSalvar;
	}

	public void setPermiteSalvar(boolean permiteSalvar) {
		this.permiteSalvar = permiteSalvar;
	}

}
