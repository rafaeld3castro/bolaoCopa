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

import br.com.bolaoCopaDoMundo.domain.ClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.ClassificacaoGrupoService;
import br.com.bolaoCopaDoMundo.service.GrupoService;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;
import br.com.bolaoCopaDoMundo.service.PontuacaoService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@Component("classificacaoGrupoListBean")
@Scope("view")
public class ClassificacaoGrupoListBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ClassificacaoGrupoListBean.class);

	@Autowired
	private ClassificacaoGrupoService classificacaoGrupoService;

	@Autowired
	private ParticipanteService participantesService;

	@Autowired
	private PontuacaoService pontuacaoService;

	@Autowired
	private GrupoService grupoService;

	// parametos de tela de consulta
	private String nome = new String();

	// entidades das telas
	private List<ClassificacaoGrupo> lista;
	private ClassificacaoGrupo classificacaoGrupo = new ClassificacaoGrupo();
	private List<Grupo> opcaoGrupo;
	private Grupo grupoSelecionado;
	private List<ClassificacaoGrupo> classificacoes;
	private Long classificacaoGrupoId;
	private List<ClassificacaoGrupo> classificacoesOk;
	private List<Participante> participantes;

	private int first;

	private LazyDataModel<ClassificacaoGrupo> modelo;

	@PostConstruct
	public void init() {
		setOpcaoGrupo(grupoService.findAll());
	}

	public void consultar() {
		lista = classificacaoGrupoService.findByCriterios(grupoSelecionado);

		if (lista == null || lista.isEmpty())
			FacesUtil.addInfoMessage("Não existe registros cadastrados.");
		else
			classificacoes = lista;

	}

	public void geraPontuacao() {

		participantes = participantesService.findAllOk();
		System.out.println(participantes.size());
		classificacoesOk = classificacaoGrupoService.findAllOk();

		System.out.println(classificacoesOk.size());
		if (participantes == null || participantes.isEmpty()
				|| classificacoesOk == null || classificacoesOk.isEmpty())
			FacesUtil
					.addInfoMessage("Não foi possível gerar a pontuação! Não foi gravado nenhum resultado final.");
		else {
			if (pontuacaoService.geracaoPontuacaoGrupo(participantes,
					classificacoesOk))
				FacesUtil.addInfoMessage("Pontuação gerada com sucesso.");
			else
				FacesUtil.addInfoMessage("Não foi possível gerar a pontuação!");
		}
	}

	public LazyDataModel<ClassificacaoGrupo> getModelo() {
		return modelo;
	}

	public void onPageChange(PageEvent event) {
		this.setFirst(((DataTable) event.getSource()).getFirst());
	}

	private void limparListas() {
		setOpcaoGrupo(grupoService.findAll());
		setNome(new String());
		this.classificacaoGrupo = new ClassificacaoGrupo();
		lista = new ArrayList<ClassificacaoGrupo>();
		this.modelo = new LazyDataModel<ClassificacaoGrupo>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<ClassificacaoGrupo> load(int inicio, int maxPerPage,
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
		setClassificacaoGrupo(new ClassificacaoGrupo());
		limparListas();

		return "classificacaoGrupoList";
	}

	public void excluir() {
		System.out.println(classificacaoGrupoId);
		try {
			classificacaoGrupoService.excluir(classificacaoGrupo);
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

	public List<ClassificacaoGrupo> getLista() {
		return lista;
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	public ClassificacaoGrupo getClassificacaoGrupo() {
		return classificacaoGrupo;
	}

	public void setClassificacaoGrupo(ClassificacaoGrupo classificacaoGrupo) {
		this.classificacaoGrupo = classificacaoGrupo;
	}

	public List<ClassificacaoGrupo> getClassificacoes() {
		return classificacoes;
	}

	public void setClassificacoes(List<ClassificacaoGrupo> classificacoes) {
		this.classificacoes = classificacoes;
	}

	public Long getClassificacaoGrupoId() {
		return classificacaoGrupoId;
	}

	public void setClassificacaoGrupoId(Long classificacaoGrupoId) {
		this.classificacaoGrupoId = classificacaoGrupoId;
	}

	public List<ClassificacaoGrupo> getClassificacoesOk() {
		return classificacoesOk;
	}

	public void setClassificacoesOk(List<ClassificacaoGrupo> classificacoesOk) {
		this.classificacoesOk = classificacoesOk;
	}

	public List<Participante> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<Participante> participantes) {
		this.participantes = participantes;
	}

}
