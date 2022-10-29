package br.com.bolaoCopaDoMundo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="JOGOS", schema="BOLAOCOPA")
@NamedQueries({ 
	@NamedQuery(name = "Jogos.findAll", query = "SELECT j FROM Jogos j ORDER BY j.dtJogo"),
	@NamedQuery(name = "Jogos.findAllOK", query = "SELECT j FROM Jogos j WHERE j.flResultadoOk = 1 ORDER BY j.dtJogo"),
	@NamedQuery(name = "Jogos.findById", query = "SELECT j FROM Jogos j WHERE j.id = :id"),
	@NamedQuery(name = "Jogos.findMaxId", query = "SELECT MAX(j.id) FROM Jogos j"),
})												
		
public class Jogos extends BasicEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="IDJOGOS")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "IDGRUPO")	
	private Grupo grupo;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "SELECAO1")	
	private Selecao selecao1;
	
	@Column(name="GOL1")
	private Integer gol1;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "SELECAO2")	
	private Selecao selecao2;
	
	@Column(name="GOL2")
	private Integer gol2;
	
	@Column(name="FLRESULTADOOK")
	private boolean flResultadoOk;
	
	@Column(name="FLJOGOBRASIL")
	private boolean flJogoBrasil;
		
	@Column(name="DTJOGO")
	private Date dtJogo;	
	
	@Transient
	private String gol1t;

	@Transient
	private String gol2t;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Selecao getSelecao1() {
		return selecao1;
	}

	public void setSelecao1(Selecao selecao1) {
		this.selecao1 = selecao1;
	}

	public Integer getGol1() {
		return gol1;
	}

	public void setGol1(Integer gol1) {
		this.gol1 = gol1;
	}

	public Selecao getSelecao2() {
		return selecao2;
	}

	public void setSelecao2(Selecao selecao2) {
		this.selecao2 = selecao2;
	}

	public Integer getGol2() {
		return gol2;
	}

	public void setGol2(Integer gol2) {
		this.gol2 = gol2;
	}

	public boolean isFlResultadoOk() {
		return flResultadoOk;
	}

	public void setFlResultadoOk(boolean flResultadoOk) {
		this.flResultadoOk = flResultadoOk;
	}

	public boolean isFlJogoBrasil() {
		return flJogoBrasil;
	}

	public void setFlJogoBrasil(boolean flJogoBrasil) {
		this.flJogoBrasil = flJogoBrasil;
	}

	public Date getDtJogo() {
		return dtJogo;
	}

	public void setDtJogo(Date dtJogo) {
		this.dtJogo = dtJogo;
	}

	public String getGol1t() {
		return gol1t;
	}

	public void setGol1t(String gol1t) {
		this.gol1t = gol1t;
	}

	public String getGol2t() {
		return gol2t;
	}

	public void setGol2t(String gol2t) {
		this.gol2t = gol2t;
	}

}
