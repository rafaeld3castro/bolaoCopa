package br.com.bolaoCopaDoMundo.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="PARTICIPANTE", schema="BOLAOCOPA")
@NamedQueries({
  @NamedQuery(name = "Participante.findMaxId", query = "SELECT MAX(p.id) FROM Participante p"),
  @NamedQuery(name = "Participante.findAll", query = "SELECT p FROM Participante p ORDER BY p.nome"),
  @NamedQuery(name = "Participante.findAllOK", query = "SELECT p FROM Participante p WHERE p.ativo = 1 and p.id <> 1 ORDER BY p.nome"),
  @NamedQuery(name = "Participante.findByUsername", query = "SELECT p FROM Participante p WHERE p.username = :username"),
  @NamedQuery(name = "Participante.findByParticipante", query = "SELECT p FROM Participante p WHERE p.id = :id"),
  @NamedQuery(name = "Participante.searchByNome", query = "SELECT p FROM Participante p WHERE UPPER(p.nome) LIKE :nome AND p.ativo = :ativo ORDER BY p.nome ")
})
public class Participante extends BasicEntity<Long> implements Serializable, UserDetails {

	private static final long serialVersionUID = -8451679170281063697L;

	@Id
	@Column(name="IDPARTICIPANTE")
	private Long id;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "participante", cascade = CascadeType.ALL)
	private List<Apostas> apostas;
	
	@Column(name="NOME")
	private String nome;

	@Column(name="LOGIN", unique = true)
	private String username;

	@Column(name="SENHA")
	private String password;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="TELEFONE")
	private String telefone;
	
	@Column(name="CONTATO")
	private String contato;

	@Column(name="FLATIVO")
	private boolean ativo;
	
	@Transient
	private Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
    	return this.authorities;
    }
    
    @Transient
    public boolean isEnabled() {
    	return ativo;
    }

    @Transient
    public boolean isAccountNonExpired() {
    	return ativo;
    }

    @Transient
    public boolean isAccountNonLocked() {
    	return true;
    }

    @Transient
    public boolean isCredentialsNonExpired() {
    	return true;
    }
    
    @Transient
    public int classificacao;
    
    @Transient
    public long apostJogoPendente;

    @Transient
    public long apostaGrupoPendente;
    
    public int getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(int classificacao) {
		this.classificacao = classificacao;
	}

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

	public String getUsername() {
    	return username;
    }

    public void setUsername(String username) {
    	this.username = username;
    }

    public String getPassword() {
    	return password;
    }

    public void setPassword(String password) {
    	this.password = password;
    }

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public void setAtivo(boolean ativo) {
    	this.ativo = ativo;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
    	this.authorities = authorities;
    }
    
    public Boolean hasAuthority(String authority){
    	for (GrantedAuthority aut : authorities) {
			if(aut.getAuthority().equals(authority)){
				return true;
			}
		}
    	return false;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public List<Apostas> getApostas() {
		return apostas;
	}

	public void setApostas(List<Apostas> apostas) {
		this.apostas = apostas;
	}

	public long getApostJogoPendente() {
		return apostJogoPendente;
	}

	public void setApostJogoPendente(long apostJogoPendente) {
		this.apostJogoPendente = apostJogoPendente;
	}

	public long getApostaGrupoPendente() {
		return apostaGrupoPendente;
	}

	public void setApostaGrupoPendente(long apostaGrupoPendente) {
		this.apostaGrupoPendente = apostaGrupoPendente;
	}
	
	
}
