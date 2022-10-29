package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.Apostas;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Parametro;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.ApostasService;
import br.com.bolaoCopaDoMundo.service.JogosService;
import br.com.bolaoCopaDoMundo.service.ParametroService;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;
import br.com.bolaoCopaDoMundo.util.RelatorioUtil;

@Component("apostasBean")
@Scope("view")
public class ApostasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ApostasBean.class);

	@Autowired
	private JogosService jogosService;

	@Autowired
	private ApostasService apostasService;


	@Autowired
	private ParticipanteService participanteService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	@Autowired
	private ParametroService parametroService;

	// entidades das telas
	private List<Jogos> jogos;
	private List<Apostas> apostas;
	private String usuario = FacesUtil.getName();
	private Participante participante;
	private Apostas aposta;
	private Long apostasGolNull;
	private boolean podeAlterar;
	
	private boolean flagApostasIncompletas = false;

	@PostConstruct
	public void init() {
		
		Parametro parametro = parametroService.findByCahve("dataLimite");
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date dataLimite = null;
		try {
			 dataLimite = formato.parse(parametro.getValor());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		podeAlterar = new Date().after(dataLimite);

		participante = participanteService.findByUsername(usuario);

		apostas = apostasService.findByUsername(usuario);
		for (Apostas a : apostas) {
			if (a.getGol1() != null)
				a.setGol1t(a.getGol1().toString());
			if (a.getGol2() != null)
				a.setGol2t(a.getGol2().toString());
		}

		if (apostas.size() == 0) {
			apostas.clear();

			jogos = jogosService.findAll();

			for (Jogos j : jogos) {
				aposta = new Apostas();
				aposta.setParticipante(participante);
				aposta.setJogos(j);
				if (aposta.getGol1() != null)
					aposta.setGol1t(aposta.getGol1().toString());
				if (aposta.getGol2() != null)
					aposta.setGol2t(aposta.getGol2().toString());
				apostas.add(aposta);
			}
			participante.setApostas(apostas);

			participanteService.salvar(participante);
		}

		apostasGolNull = apostasService
				.countByParticipanteAndGolNull(participante);
		System.out.println(apostasGolNull);

		FacesUtil.addWarnMessageWithouDetail("Existem "
				+ apostasGolNull.toString() + " apostas incompletas!");
	}
	
	/**
	 * Emitir Relatorio
	 * 
	 * @return
	 */
	public String relatorio() {

		try {
			FacesContext facesContext = FacesContext.getCurrentInstance(); 
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
			Map<String, Object> parametros = new HashMap<String, Object>();
			
			String pathRel = servletContext.getRealPath("//WEB-INF/relatorios/");  
			String filtro = " WHERE participante.idparticipante = " + participante.getId();
			
			parametros.put("FILTRO", filtro.toString());
			parametros.put("SUBREPORT_DIR",pathRel);
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

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public Apostas getAposta() {
		return aposta;
	}

	public void setAposta(Apostas aposta) {
		this.aposta = aposta;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Long getApostasGolNull() {
		return apostasGolNull;
	}

	public void setApostasGolNull(Long apostasGolNull) {
		this.apostasGolNull = apostasGolNull;
	}

	public boolean isFlagApostasIncompletas() {
		return flagApostasIncompletas;
	}

	public void setFlagApostasIncompletas(boolean flagApostasIncompletas) {
		this.flagApostasIncompletas = flagApostasIncompletas;
	}
	

	public boolean isPodeAlterar() {
		return podeAlterar;
	}

	public void setPodeAlterar(boolean podeAlterar) {
		this.podeAlterar = podeAlterar;
	}

	//	public void onCellEdit(CellEditEvent event) {
	public void onCellEdit() {

		Apostas apostaAtualizar = FacesUtil.getApplication()
				.evaluateExpressionGet(FacesUtil.getFacesContext(), "#{item}",
						Apostas.class);

		if (apostaAtualizar.getGol1t() == null
				|| apostaAtualizar.getGol1t().equals("")) {
			apostaAtualizar.setGol1(null);
		} else {
			apostaAtualizar
					.setGol1(Integer.parseInt(apostaAtualizar.getGol1t()));
		}

		if (apostaAtualizar.getGol2t() == null
				|| apostaAtualizar.getGol2t().equals("")) {
			apostaAtualizar.setGol2(null);
		} else {
			apostaAtualizar
					.setGol2(Integer.parseInt(apostaAtualizar.getGol2t()));
		}

		try {
			apostasService.salvar(apostaAtualizar);

			apostasGolNull = apostasService
					.countByParticipanteAndGolNull(participante);
			FacesUtil.addWarnMessageWithouDetail("Existem "
					+ apostasGolNull.toString() + " apostas incompletas!");

			FacesUtil
					.addInfoMessageWithouDetail("Operção realizada com sucesso. Resultado da aposta foi gravado.");

			if (apostaAtualizar.getGol1() != null
					&& apostaAtualizar.getGol2() != null) {

				FacesUtil.addInfoDetail(apostaAtualizar.getJogos().getGrupo()
						.getNome()
						+ " | "
						+ new SimpleDateFormat("dd/MM/yyyy")
								.format(apostaAtualizar.getJogos().getDtJogo())
						+ " | "
						+ apostaAtualizar.getJogos().getSelecao1().getNome()
						+ " "
						+ apostaAtualizar.getGol1()
						+ " x "
						+ +apostaAtualizar.getGol2()
						+ " "
						+ apostaAtualizar.getJogos().getSelecao2().getNome()
						+ ".");

			} else if (apostaAtualizar.getGol1() != null) {

				FacesUtil.addInfoDetail(apostaAtualizar.getJogos().getGrupo()
						.getNome()
						+ " | "
						+ new SimpleDateFormat("dd/MM/yyyy")
								.format(apostaAtualizar.getJogos().getDtJogo())
						+ " | "
						+ apostaAtualizar.getJogos().getSelecao1().getNome()
						+ " "
						+ apostaAtualizar.getGol1()
						+ ". Preencha o campo da seleção "
						+ apostaAtualizar.getJogos().getSelecao2().getNome());

			} else if (apostaAtualizar.getGol2() != null) {

				FacesUtil.addInfoDetail(apostaAtualizar.getJogos().getGrupo()
						.getNome()
						+ " | "
						+ new SimpleDateFormat("dd/MM/yyyy")
								.format(apostaAtualizar.getJogos().getDtJogo())
						+ " | "
						+ apostaAtualizar.getJogos().getSelecao2().getNome()
						+ " "
						+ apostaAtualizar.getGol2()
						+ ". Preencha o campo da seleção "
						+ apostaAtualizar.getJogos().getSelecao1().getNome());
			} else {
				FacesUtil.addInfoDetail("Preencha os campos das seleções "
						+ apostaAtualizar.getJogos().getSelecao1().getNome()
						+ " e "
						+ apostaAtualizar.getJogos().getSelecao2().getNome());
			}
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
	
	public void apostasIncompletas() {
		setFlagApostasIncompletas(true);
	}

}
