package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.component.UIViewParameter;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewMetadata;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.GrupoService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@SuppressWarnings("serial")
@Component("grupoFormBean")
@Scope("view")
public class GrupoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(GrupoFormBean.class);

	@Autowired
	private GrupoService grupoService;

	// entidade das telas
	private Grupo grupo = new Grupo();
	private Long grupoId;
	private boolean alterar = false;
	private boolean naopermitirEdicao = false;

	Collection<UIViewParameter> viewParams = ViewMetadata
			.getViewParameters(FacesContext.getCurrentInstance().getViewRoot());

	public void prepara() {
		if (!FacesContext.getCurrentInstance().isPostback()) {
			if (grupoId == null) {
				return;
			} else {
				this.alterar = true;
				setGrupo(grupoService.findById(grupoId));
			}
		}
	}

	public String prepareIncluir() {
		return "grupoForm?faces-redirect=true";
	}

	public String redirecionaView() {
		return "grupoForm";
	}

	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {
			grupoService.salvar(grupo);
			limpar();
			FacesUtil.addInfoMessage("Opera��o realizada com sucesso.");
			logger.info("Opera��o realizada com sucesso.");

		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Limpar form
	 */
	private void limpar() {
		this.alterar = false;
		setGrupo(new Grupo());
	}

	public String limpaTela() {
		limpar();
		return "grupoForm";
	}

	/**
	 * Gets and Sets
	 */
	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public boolean isAlterar() {
		return alterar;
	}

	public Long getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Long grupoId) {
		this.grupoId = grupoId;
	}

	public boolean isNaopermitirEdicao() {
		return naopermitirEdicao;
	}

	public void setNaopermitirEdicao(boolean naopermitirEdicao) {
		this.naopermitirEdicao = naopermitirEdicao;
	}

}