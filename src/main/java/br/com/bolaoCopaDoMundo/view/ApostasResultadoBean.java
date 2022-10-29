package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.util.ArrayList;
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
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.service.ApostasService;
import br.com.bolaoCopaDoMundo.service.JogosService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@SuppressWarnings("serial")
@Component("apostasResultadoBean")
@Scope("view")
public class ApostasResultadoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ApostasResultadoBean.class);

	@Autowired
	private JogosService jogosService;

	@Autowired
	private ApostasService apostasService;

	// entidades das telas
	private Jogos jogoSelecionado;

	// listas
	private List<Jogos> jogos;
	private List<Apostas> apostas;
	private LazyDataModel<Apostas> model;

	@PostConstruct
	public void init() {
		setJogos(jogosService.findAll());
		jogoSelecionado = new Jogos();
	}

	public void searchByBJogo() {
		if(jogoSelecionado.getId() == null) {
			FacesUtil.addErroDetail("Selecione o jogo!");
		}
		model = new LazyDataModel<Apostas>() {

			@Override
			public List<Apostas> load(int inicio, int maxPerPage,
					String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				apostas = apostasService.findByJogoResultado(jogoSelecionado,
						inicio, maxPerPage);
				setPageSize(maxPerPage);
				setRowCount(apostasService.countByJogoResultado(jogoSelecionado));
				System.out.println("tam " + apostas.size());
				return apostas;
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

	public String limpaTela() {
		jogoSelecionado = new Jogos();
		apostas = new ArrayList<Apostas>();
		return "apostaJogo";
	}
	
	public List<Jogos> getJogos() {
		return jogos;
	}

	public void setJogos(List<Jogos> jogos) {
		this.jogos = jogos;
	}

	public List<Apostas> getApostas() {
		return apostas;
	}

	public void setApostas(List<Apostas> apostas) {
		this.apostas = apostas;
	}

	public Jogos getJogoSelecionado() {
		return jogoSelecionado;
	}

	public void setJogoSelecionado(Jogos jogoSelecionado) {
		this.jogoSelecionado = jogoSelecionado;
	}

	public LazyDataModel<Apostas> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Apostas> model) {
		this.model = model;
	}

}
