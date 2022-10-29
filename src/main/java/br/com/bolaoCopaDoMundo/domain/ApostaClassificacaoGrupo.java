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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "APOSTACLASSIFICACAOGRUPO", schema = "BOLAOCOPA")
@NamedQueries({
		@NamedQuery(name = "ApostaClassificacaoGrupo.findMaxId", query = "SELECT MAX(a.id) FROM ApostaClassificacaoGrupo a"),
		@NamedQuery(name = "ApostaClassificacaoGrupo.findById", query = "SELECT a FROM ApostaClassificacaoGrupo a WHERE a.id = :id"),
		@NamedQuery(name = "ApostaClassificacaoGrupo.findAll", query = "SELECT a FROM ApostaClassificacaoGrupo a ORDER BY a.id")})

public class ApostaClassificacaoGrupo extends BasicEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "IDAPOSTACLASSIFICACAOGRUPO")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDPARTICIPANTE")
	private Participante participante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDGRUPO")
	private Grupo grupo;

	@ManyToOne(fetch=FetchType.LAZY) 
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "POSICAO1")	
	private Selecao posicao1;
		
	@ManyToOne(fetch=FetchType.LAZY) 
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "POSICAO2")	
	private Selecao posicao2;	

	@JoinColumn(name = "PONTOS")
	private BigDecimal pontos;
	
	
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

	public BigDecimal getPontos() {
		return pontos;
	}

	public void setPontos(BigDecimal pontos) {
		this.pontos = pontos;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Selecao getPosicao1() {
		return posicao1;
	}

	public void setPosicao1(Selecao posicao1) {
		this.posicao1 = posicao1;
	}

	public Selecao getPosicao2() {
		return posicao2;
	}

	public void setPosicao2(Selecao posicao2) {
		this.posicao2 = posicao2;
	}
	
}
