package br.com.bolaoCopaDoMundo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="PONTUACAO", schema="BOLAOCOPA")
@NamedQueries({ 
	@NamedQuery(name = "Pontuacao.findAll", query = "SELECT p FROM Pontuacao p ORDER BY p.totalPontos desc , p.escoreCheio desc, p.pontosBrasil desc,p.acertoPrimeiroLugar desc,p.participante.nome "),
	@NamedQuery(name = "Pontuacao.findById", query = "SELECT p FROM Pontuacao p WHERE p.id = :id"),
	@NamedQuery(name = "Pontuacao.findByNome", query = "SELECT p FROM Pontuacao p WHERE UPPER(p.participante.nome) LIKE :nome ORDER BY p.totalPontos"),
	@NamedQuery(name = "Pontuacao.findMaxId", query = "SELECT MAX(p.id) FROM Pontuacao p"),
	@NamedQuery(name = "Pontuacao.findByParticipante", query = "SELECT p FROM Pontuacao p WHERE p.participante = :participante"),
	@NamedQuery(name = "Pontuacao.count", query = "SELECT count(p) FROM Pontuacao p "),
	@NamedQuery(name = "Pontuacao.countByNome", query = "SELECT count(p) FROM Pontuacao p WHERE UPPER( p.participante.nome ) LIKE :nome "),
	@NamedQuery(name = "Pontuacao.countByParticipante", query = "SELECT count(p) FROM Pontuacao p WHERE p.participante = :participante AND UPPER (p.participante.nome) LIKE :nome"),
})												
		
public class Pontuacao extends BasicEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="IDPONTUACAO")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "IDPARTICIPANTE")	
	private Participante participante;
		
	@Column(name="PONTOJOGOS")
	private Double pontosJogo;
	
	@Column(name="PONTOSCLASSIFICACAO")
	private Integer pontosClassificacao;
	
	@Column(name="PONTOSBRASIL")
	private Double pontosBrasil;
	
	@Column(name="ESCORECHEIO")
	private Integer escoreCheio;
	
	@Column(name="TOTALPONTOS")
	private Double totalPontos;
	
	@Column(name = "ACERTOPRIMEIROLUGAR")
	private Integer acertoPrimeiroLugar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public Double getPontosJogo() {
		return pontosJogo;
	}

	public void setPontosJogo(Double pontosJogo) {
		this.pontosJogo = pontosJogo;
	}

	public Integer getPontosClassificacao() {
		return pontosClassificacao;
	}

	public void setPontosClassificacao(Integer pontosClassificacao) {
		this.pontosClassificacao = pontosClassificacao;
	}

	public Double getPontosBrasil() {
		return pontosBrasil;
	}

	public void setPontosBrasil(Double pontosBrasil) {
		this.pontosBrasil = pontosBrasil;
	}

	public Integer getEscoreCheio() {
		return escoreCheio;
	}

	public void setEscoreCheio(Integer escoreCheio) {
		this.escoreCheio = escoreCheio;
	}

	public Double getTotalPontos() {
		return totalPontos;
	}

	public void setTotalPontos(Double totalPontos) {
		this.totalPontos = totalPontos;
	}

	public Integer getAcertoPrimeiroLugar() {
		return acertoPrimeiroLugar;
	}

	public void setAcertoPrimeiroLugar(Integer acertoPrimeiroLugar) {
		this.acertoPrimeiroLugar = acertoPrimeiroLugar;
	}
	
}
