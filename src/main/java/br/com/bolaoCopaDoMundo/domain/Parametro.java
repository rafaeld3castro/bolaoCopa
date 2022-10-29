package br.com.bolaoCopaDoMundo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="PARAMETRO", schema="BOLAOCOPA")
@NamedQueries({ 
	@NamedQuery(name = "Parametro.findById", query = "SELECT p FROM Parametro p WHERE p.id = :id"),
	@NamedQuery(name = "Parametro.findByCahve", query = "SELECT p FROM Parametro p WHERE UPPER(p.chave) LIKE :chave ORDER BY p.id"),
	
})												
		
public class Parametro extends BasicEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="IDPARAMETRO")
	private Long id;
		
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@Column(name="CHAVE")
	private String chave;
	
	@Column(name="VALOR")
	private String valor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}	
	
}
