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

import br.com.bolaoCopaDoMundo.domain.ApostaClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Grupo;
import br.com.bolaoCopaDoMundo.service.ApostaClassificacaoGrupoService;
import br.com.bolaoCopaDoMundo.service.GrupoService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@SuppressWarnings("serial")
@Component("apostasGrupoBean")
@Scope("view")
public class ApostasGrupoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ApostasGrupoBean.class);

	@Autowired
	private GrupoService grupoService;

	@Autowired
	private ApostaClassificacaoGrupoService apostaClassificacaoGrupoService;

	// entidades das telas
	private Grupo grupoSelecionado;
	
	//listas
	private List<Grupo> grupos;
	private List<ApostaClassificacaoGrupo> apostas;
	private LazyDataModel<ApostaClassificacaoGrupo> model;


	@PostConstruct
	public void init(){	
		setGrupos(grupoService.findAll());			
	}
		
	public void searchByBJogo() {	
		
		if(grupoSelecionado == null){
			FacesUtil.addErroMessage("Nenhum Grupo selecinado. Selecione um Grupo.");
		}
		else{
				model = new LazyDataModel<ApostaClassificacaoGrupo>() {
					
				@Override
				public List<ApostaClassificacaoGrupo> load(int inicio, int maxPerPage,
						String sortField, SortOrder sortOrder,Map<String, Object> filters) {
						apostas = apostaClassificacaoGrupoService.findByGrupo(grupoSelecionado.getId(),inicio,maxPerPage);
						setPageSize(maxPerPage);
						setRowCount(apostaClassificacaoGrupoService.countByGrupo(grupoSelecionado.getId()));
						System.out.println("tam " + apostas.size());
						return apostas;
					}
					@Override
					public void setRowIndex(int rowIndex) {
						if(rowIndex==-1||getPageSize()==0)
					    {super.setRowIndex(-1);	}
					    else {super.setRowIndex(rowIndex%getPageSize());}
					}}; 
		}
					
	}
	
	public String limpaTela() {
		grupoSelecionado = new Grupo();
		apostas = new ArrayList<ApostaClassificacaoGrupo>();
		return "apostaGrupo";
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public List<ApostaClassificacaoGrupo> getApostas() {
		return apostas;
	}

	public void setApostas(List<ApostaClassificacaoGrupo> apostas) {
		this.apostas = apostas;
	}

	public LazyDataModel<ApostaClassificacaoGrupo> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<ApostaClassificacaoGrupo> model) {
		this.model = model;
	}	
		
}
