package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.GrupoService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@SuppressWarnings("serial")
@Component("grupoListBean")
@Scope("view")
public class GrupoListBean implements Serializable {

	static Logger logger = Logger.getLogger(GrupoListBean.class);

	@Autowired
	private GrupoService grupoService;

	// parametos de tela de consulta
	private String nome = new String();

	// entidades das telas
	private List<Grupo> lista;
	private Grupo grupo = new Grupo();
	private LazyDataModel<Grupo> modelo;

	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {
		// PAGINACAO
		modelo = new LazyDataModel<Grupo>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<Grupo> load(int inicio, int maxPerPage,
					String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				lista = grupoService.search(getNome(), inicio, maxPerPage);
				setRowCount(grupoService.count(nome));
				setPageSize(maxPerPage);
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
		return null;
	}

	/**
	 * Limpar tela
	 */
	public String limpaTela() {
		setGrupo(new Grupo());
		nome = new String();
		limparListas();
		return "grupoList?faces-redirect=true";
	}

	private void limparListas() {
		lista = new ArrayList<Grupo>();
		this.modelo = new LazyDataModel<Grupo>() {
			@Override
			public List<Grupo> load(int inicio, int maxPerPage,
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
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

		try {
			grupoService.excluir(grupo);
			FacesUtil.addInfoMessage("Registro exclu�do com sucesso.");
			logger.info("Registro exclu�do com sucesso.");

		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (DataAccessException e) {
			FacesUtil
					.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclus�o n�o poder� ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao excluir. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return consultar();
	}

	/**
	 * Gets and Sets
	 */
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Grupo> getLista() {
		return lista;
	}

	public LazyDataModel<Grupo> getModelo() {
		return modelo;
	}

	public void setModelo(LazyDataModel<Grupo> modelo) {
		this.modelo = modelo;
	}

}