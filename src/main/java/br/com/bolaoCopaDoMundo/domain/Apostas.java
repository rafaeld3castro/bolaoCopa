package br.com.bolaoCopaDoMundo.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "APOSTAJOGO", schema = "BOLAOCOPA")
@NamedQueries({
		@NamedQuery(name = "Apostas.findMaxId", query = "SELECT MAX(a.id) FROM Apostas a"),
		@NamedQuery(name = "Apostas.findById", query = "SELECT a FROM Apostas a WHERE a.id = :id"),
		@NamedQuery(name = "Apostas.findAll", query = "SELECT a FROM Apostas a ORDER BY a.id")})

public class Apostas extends BasicEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "IDAPOSTAJOGO")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDPARTICIPANTE")
	private Participante participante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDJOGO")
	private Jogos jogos;

	@Column(name = "GOL1")
	private Integer gol1;

	@Column(name = "GOL2")
	private Integer gol2;

	@JoinColumn(name = "PONTOS")
	private BigDecimal pontos;

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

	public Integer getGol1() {
		return gol1;
	}

	public void setGol1(Integer gol1) {
		this.gol1 = gol1;
	}

	public Integer getGol2() {
		return gol2;
	}

	public void setGol2(Integer gol2) {
		this.gol2 = gol2;
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public Jogos getJogos() {
		return jogos;
	}

	public void setJogos(Jogos jogos) {
		this.jogos = jogos;
	}

	public BigDecimal getPontos() {
		return pontos;
	}

	public void setPontos(BigDecimal pontos) {
		this.pontos = pontos;
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
