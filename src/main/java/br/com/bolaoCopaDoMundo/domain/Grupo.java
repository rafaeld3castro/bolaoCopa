package br.com.bolaoCopaDoMundo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="GRUPO", schema="BOLAOCOPA")
@NamedQueries({
	@NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g ORDER BY g.nome"),
	@NamedQuery(name = "Grupo.findMaxId", query = "SELECT MAX(g.id) FROM Grupo g"),
	@NamedQuery(name = "Grupo.findByGrupo", query = "SELECT g FROM Grupo g WHERE g.id = :id"),
	@NamedQuery(name = "Grupo.findByNome", query = "SELECT g FROM Grupo g WHERE UPPER(g.nome) LIKE :nome ORDER BY g.id"),
	@NamedQuery(name = "Grupo.findById", query = "SELECT g FROM Grupo g WHERE g.id = :id"),
	@NamedQuery(name = "Grupo.countByNome", query = "SELECT count(g) FROM Grupo g WHERE UPPER( g.nome ) LIKE :nome"),
	@NamedQuery(name = "Grupo.searchByNome", query = "SELECT g FROM Grupo g WHERE UPPER( g.nome ) LIKE :nome ORDER BY g.nome")
})
public class Grupo extends BasicEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="IDGRUPO")
	private Long id;

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


}
