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
@Table(name="SELECAO", schema="BOLAOCOPA")
@NamedQueries({ 
	@NamedQuery(name = "Selecao.findAll", query = "SELECT s FROM Selecao s ORDER BY s.nome"),
	@NamedQuery(name = "Selecao.findById", query = "SELECT s FROM Selecao s WHERE s.id = :id"),
	@NamedQuery(name = "Selecao.findByNome", query = "SELECT s FROM Selecao s WHERE UPPER(s.nome) LIKE :nome ORDER BY s.id"),
	@NamedQuery(name = "Selecao.findMaxId", query = "SELECT MAX(s.id) FROM Selecao s"),
	@NamedQuery(name = "Selecao.findByGrupo", query = "SELECT s FROM Selecao s WHERE s.grupo = :grupo"),
	@NamedQuery(name = "Selecao.findByGrupoPaginado", query = "SELECT s FROM Selecao s JOIN FETCH s.grupo WHERE s.grupo = :grupo AND UPPER (s.nome) LIKE :nome"),
	@NamedQuery(name = "Selecao.countByNome", query = "SELECT count(s) FROM Selecao s WHERE UPPER( s.nome ) LIKE :nome "),
	@NamedQuery(name = "Selecao.countByGrupo", query = "SELECT count(s) FROM Selecao s WHERE s.grupo = :grupo AND UPPER (s.nome) LIKE :nome"),
	@NamedQuery(name = "Selecao.findNomeByGrupo", query = "SELECT s FROM Selecao s Where s.grupo =:grupo AND UPPER (s.nome) = :nome"),
	@NamedQuery(name = "Selecao.searchByNome", query = "SELECT s FROM Selecao s WHERE UPPER(s.nome) LIKE :nome ORDER BY s.nome")
})												
		
public class Selecao extends BasicEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="IDSELECAO")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "IDGRUPO")	
	private Grupo grupo;
		
	@Column(name="NOME")
	private String nome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	
}
