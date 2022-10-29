package br.com.bolaoCopaDoMundo.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.Participante;

@Component
public class AuthenticationService {

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	@PersistenceContext
	private EntityManager entityManager;

	
	public String login(String username, String password) {

		try {
			
			Participante usuario1 = (Participante) entityManager.createNamedQuery("Participante.findByUsername", Participante.class)
      				.setParameter("username", username).getSingleResult();
			// verificando se o usuario esta ativo
						if (!usuario1.isEnabled()) {
							return "inativo";
						}

			// criptografia MD5
			password = toMd5(password);

			// autenticando spring security
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username.toUpperCase(), password);
			Authentication authenticate = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authenticate);

			// pergando o usuario
			Participante usuario = (Participante) getUsuarioLogado();
			
			// verificando se o usuario esta ativo
			if (!usuario.isEnabled()) {
				return "Usuário nao esta ativo!";
			}

			// Verificando se a senha esta expirada
			if (!usuario.isAccountNonExpired()) {
				return "Senha expirada!";
			}

			// verificando se o usuario tem alguma permissao
			if (usuario.getAuthorities().size() == 0) {
				return "Usuário nÃ£o tem permissão de acesso";
			}

			// verificando autenticacao
			if (authenticate.isAuthenticated()) {
				return "ok";
			}

		}

		catch (AuthenticationException e) {}
		
		return "Login ou senha inválidos";
	}


	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		invalidateSession();
	}


	public Participante getUsuarioLogado() {
		return (Participante) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	private void invalidateSession() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		session.invalidate();
	}


	/**
	 * Encripta valores com hash md5
	 * 
	 * @param String valor
	 * 
	 * @return String md5
	 */
	private static String toMd5(String valor){
		String md5 = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");			
			BigInteger hashForm = new BigInteger(1, md.digest(valor.getBytes()));
			md5 = hashForm.toString(16);			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}

	public static void main(String[] args) {
		System.out.println(toMd5("Bolao2014@TCE"));
	}
}
