package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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
import br.com.bolaoCopaDoMundo.domain.Selecao;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.GrupoService;
import br.com.bolaoCopaDoMundo.service.SelecaoService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@Component("selecaoListBean")
@Scope("view")
public class SelecaoListBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(SelecaoListBean.class);

	@Autowired
	private SelecaoService selecaoService;

	@Autowired
	private GrupoService grupoService;

	// parametos de tela de consulta
	private String nome = new String();

	// entidades das telas
	private List<Selecao> lista;
	private Selecao selecao = new Selecao();
	private List<Grupo> opcaoGrupo;
	private Grupo grupoSelecionado;
	private List<Selecao> selecoes;
	private Long selecaoId;

	private int first;

	private LazyDataModel<Selecao> modelo;

	@PostConstruct
	public void init() {
		setOpcaoGrupo(grupoService.findAll());
	}

	public void consultar() {

		lista = selecaoService.findByCriterios(nome, grupoSelecionado);

		if (lista == null || lista.isEmpty())
			FacesUtil
					.addInfoMessage("N�o existe sele��es cadastradas com esses crit�rios");
		else
			selecoes = lista;

	}

	public LazyDataModel<Selecao> getModelo() {
		return modelo;
	}

	public void onPageChange(PageEvent event) {
		this.setFirst(((DataTable) event.getSource()).getFirst());
	}

	private void limparListas() {
		setOpcaoGrupo(grupoService.findAll());
		setNome(new String());
		this.selecao = new Selecao();
		lista = new ArrayList<Selecao>();
		this.modelo = new LazyDataModel<Selecao>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<Selecao> load(int inicio, int maxPerPage,
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
		setSelecao(new Selecao());
		limparListas();

		return "selecaoList?faces-redirect=true";
	}

	public void excluir() {
		System.out.println(selecaoId);
		try {
			selecaoService.excluir(selecao);
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

	public Selecao getSelecao() {
		return selecao;
	}

	public void setSelecao(Selecao selecao) {
		this.selecao = selecao;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getFirst() {
		return first;
	}

	public List<Selecao> getLista() {
		return lista;
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	public List<Selecao> getSelecoes() {
		return selecoes;
	}

	public void setSelecoes(List<Selecao> selecoes) {
		this.selecoes = selecoes;
	}

	public Long getSelecaoId() {
		return selecaoId;
	}

	public void setSelecaoId(Long selecaoId) {
		this.selecaoId = selecaoId;
	}

}
