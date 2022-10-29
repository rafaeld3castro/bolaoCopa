package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
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
import br.com.bolaoCopaDoMundo.domain.Selecao;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.GrupoService;
import br.com.bolaoCopaDoMundo.service.SelecaoService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@Component("selecaoFormBean")
@Scope("view")
public class SelecaoFormBean implements Serializable {

	private static final long serialVersionUID = 1L;
	// entidade da tela.
	private Selecao selecao = new Selecao();
	private boolean alterar = false;

	static Logger logger = Logger.getLogger(SelecaoFormBean.class);

	@Autowired
	private SelecaoService selecaoService;
	@Autowired
	private GrupoService grupoService;

	// variaveis para lidar com o Form
	private List<Grupo> grupos_list;
	private Grupo grupoSelecionado = selecao.getGrupo();
	private Long selecaoId;

	@PostConstruct
	public void init() {
		setGrupos_list(grupoService.findAll());
		if (!FacesContext.getCurrentInstance().isPostback()) { // verifica se a
																// ação vem de
																// outra view
			if (selecaoId == null) {
				return;
			} else {
				this.alterar = true; // setando informações do Objeto
				setSelecao(selecaoService.findById(selecaoId));
			}
			configuraEditorSelecao();
		}

	}

	public void configuraEditorSelecao() {
		setGrupoSelecionado(selecao.getGrupo());
	}

	public Selecao configuraAtributosVazios(Selecao secao) {
		selecao.setGrupo(grupoService.findById(grupoSelecionado.getId()));
		return selecao;
	}

	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {
			configuraAtributosVazios(selecao);
			selecaoService.salvar(selecao);
			limpar();
			FacesUtil
					.addInfoMessage("Operação realizada com sucesso. A nova seleção foi salva.");
			logger.info("Operacao realizada com sucesso.");
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

		return null;
	}

	/**
	 * Realizar antes de carregar tela incluir
	 * 
	 * @return
	 */
	public String prepareIncluir() {
		this.alterar = false;
		setSelecao(new Selecao());
		return "selecaoForm?faces-redirect=true";
	}

	/**
	 * Limpar form
	 * 
	 */
	private void limpar() {
		this.alterar = false;
		setSelecao(new Selecao());
		configuraEditorSelecao();
	}

	public String limpaTela() {
		limpar();
		return "selecaoForm";
	}

	public String voltar() {
		return "selecaoList?faces-redirect=true";
	}

	/**
	 * Gets and Sets
	 */

	public boolean isAlterar() {
		return alterar;
	}

	public List<Grupo> getGrupos_list() {
		return grupos_list;
	}

	public void setGrupos_list(List<Grupo> grupos_list) {
		this.grupos_list = grupos_list;
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

	public Long getSelecaoId() {
		return selecaoId;
	}

	public void setSelecaoId(Long selecaoId) {
		this.selecaoId = selecaoId;
	}

}
