package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Selecao;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.GrupoService;
import br.com.bolaoCopaDoMundo.service.JogosService;
import br.com.bolaoCopaDoMundo.service.SelecaoService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@Component("jogosFormBean")
@Scope("view")
public class JogosFormBean implements Serializable {

	private static final long serialVersionUID = 1L;
	// entidade da tela.
	private Selecao selecao = new Selecao();
	private boolean alterar = false;

	static Logger logger = Logger.getLogger(JogosFormBean.class);

	@Autowired
	private JogosService jogosService;

	@Autowired
	private GrupoService grupoService;

	@Autowired
	private SelecaoService selecaoService;

	// entidades das telas
	private Jogos jogo = new Jogos();
	private List<Grupo> opcaoGrupo;
	private Grupo grupoSelecionado;
	private Selecao selecao1Selecionado;
	private Selecao selecao2Selecionado;
	private List<Selecao> opcaoSelecao1;
	private List<Selecao> opcaoSelecao2;
	private Date datajogo;

	private List<Jogos> jogos;
	private Long jogosId;

	@PostConstruct
	public void init() {
		setOpcaoGrupo(grupoService.findAll());
		if (!FacesContext.getCurrentInstance().isPostback()) { // verifica se a
																// ação vem de
																// outra view
			if (jogosId == null) {
				return;
			} else {
				this.alterar = true; // setando informações do Objeto
				setJogo(jogosService.findById(jogosId));
			}
		}

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
					.addErroMessage("Ocorreu algum erro na consulta. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public void configuraEditorJogos() {
		setGrupoSelecionado(selecao.getGrupo());
	}

	public Jogos configuraAtributosVazios(Jogos jogos) {
		System.out.println(grupoSelecionado);
		System.out.println(selecao1Selecionado);
		System.out.println(selecao2Selecionado);
		System.out.println(datajogo);
		System.out.println(jogo);
		jogo.setGrupo(grupoService.findById(grupoSelecionado.getId()));
		jogo.setSelecao1(selecaoService.findById(selecao1Selecionado.getId()));
		jogo.setSelecao2(selecaoService.findById(selecao2Selecionado.getId()));
		jogo.setDtJogo(datajogo);
		return jogo;
	}

	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {
			configuraAtributosVazios(jogo);
			jogosService.salvar(jogo);
			limpar();
			FacesUtil
					.addInfoMessage("Opera��o realizada com sucesso. A nova secao foi salva.");
			logger.info("Operacao realizada com sucesso.");
		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage("Ocorreu o seguinte erro: "
					+ e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (PersistenceException e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (JDBCException e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Opera��o cancelada.");
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
		setJogo(new Jogos());
		return "jogosForm?faces-redirect=true";
	}

	/**
	 * Limpar form
	 * 
	 */
	private void limpar() {
		this.alterar = false;
		setSelecao(new Selecao());
		configuraEditorJogos();
	}

	public String limpaTela() {
		limpar();
		return "jogosForm";
	}

	public String voltar() {
		return "jogosList?faces-redirect=true";
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

	public Jogos getJogo() {
		return jogo;
	}

	public void setJogo(Jogos jogo) {
		this.jogo = jogo;
	}

	public List<Grupo> getOpcaoGrupo() {
		return opcaoGrupo;
	}

	public void setOpcaoGrupo(List<Grupo> opcaoGrupo) {
		this.opcaoGrupo = opcaoGrupo;
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

	public Date getDatajogo() {
		return datajogo;
	}

	public void setDatajogo(Date datajogo) {
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

}
