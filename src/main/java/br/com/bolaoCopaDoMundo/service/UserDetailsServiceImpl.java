package br.com.bolaoCopaDoMundo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.Parametro;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.domain.Pontuacao;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PontuacaoService pontuacaoService;
	
	@Autowired
	private ParametroService parametroService;
	
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return findByUsername(username);
	}

	private Participante findByUsername(String username) {

		try {

			GrantedAuthority roleConsultas;
			roleConsultas = new GrantedAuthorityImpl("ROLE_CONSULTAS");
			
			Participante usuario = (Participante) entityManager.createNamedQuery("Participante.findByUsername", Participante.class)
      				.setParameter("username", username).getSingleResult();

			// verificando a lista de usuarios
			if (usuario == null || usuario.equals(""))
				return null;

			if (usuario.getUsername().equals("ADMIN")) {
				usuario.getAuthorities().add(new GrantedAuthorityImpl("ROLE_ADMIN"));
				usuario.getAuthorities().add(roleConsultas);
			} else {
				usuario.getAuthorities().add(new GrantedAuthorityImpl("ROLE_PARTICIPANTE"));
			}

			//usuario.getAuthorities().add(result);

			List<Pontuacao> pontuacaoAll = pontuacaoService.findAll();
			
			Pontuacao pontuacaoUsuario = pontuacaoService.findByNome(usuario.getNome());
			
			Parametro parametro = parametroService.findByCahve("dataLimite");
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Date dataLimite = null;
			try {
				 dataLimite = formato.parse(parametro.getValor());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			if( !usuario.getUsername().equals("ADMIN") && new Date().after(dataLimite)) {
				usuario.setClassificacao(pontuacaoAll.indexOf(pontuacaoUsuario) + 1);
				usuario.getAuthorities().add(roleConsultas);
			} else if (usuario.getAuthorities().contains(roleConsultas)) {
				usuario.getAuthorities().remove(roleConsultas);
			}
			
			return usuario;

		} catch (NoResultException e) {
			throw new UsernameNotFoundException("Usuario nao encontrado");
		}

	}

}
