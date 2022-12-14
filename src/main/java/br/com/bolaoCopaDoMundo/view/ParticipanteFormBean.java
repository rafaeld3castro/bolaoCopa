package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;
import br.com.bolaoCopaDoMundo.util.BolaoUtil;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@SuppressWarnings("serial")
@Component("participanteFormBean")
@Scope("session")
public class ParticipanteFormBean implements Serializable {

	static Logger logger = Logger.getLogger(ParticipanteListBean.class);

	@Autowired
	private ParticipanteService ParticipanteService;

	@Autowired
	private BolaoUtil bolaoUtil;

	private Participante participante = new Participante();
	

	private String password = new String();

	private String confirmarSenha = new String();

	private boolean alterar = false;
	
	private boolean ativacaoUsuario = false;
	
	private int contatoOpcao = 0;

	// dados recuperar senha
	private String recuperarSenhaLogin = new String();
	private String recuperarSehnaEmail = new String();

	public String prepareCadastrar() {
		setParticipante(new Participante());
		return "cadastroForm";
	}

	public String prepareIncluir() {
		this.alterar = false;
		setParticipante(new Participante());
		return "participanteForm?faces-redirect=true";
	}

	public String prepareAlterar() {
		this.alterar = true;
		return "participanteForm";
	}

	private void limpar() {
		this.alterar = false;
		participante = new Participante();
	}

	public String limpaTela() {
		limpar();
		return "participanteForm";
	}

	public String salvar() {

		try {

			// verificar se o password foi alterado
			if (alterar == true && password.length() > 0) {
				// setando o novo password
				participante.setPassword(password);
			}

			// criptografando password e colocando para letra maiuscula
			if (alterar == false || password.length() > 0) {
				try {
					participante.setPassword(bolaoUtil
							.criptografarSenha(participante.getPassword()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// colocando em letra maiuscula para salvar
			participante.setNome(participante.getNome().toUpperCase());
			participante.setUsername(participante.getUsername().toUpperCase());
			
			System.out.println(participante.isAtivo() + " "+ ParticipanteService.findByParticipante(participante).isAtivo());
			if (participante.isAtivo() && !ParticipanteService.findByParticipante(participante).isAtivo())
				ativacaoUsuario = true;
			
			ParticipanteService.salvar(participante);
			
			
			if(ativacaoUsuario){
				StringBuilder msg = new StringBuilder();
				msg.append("Prezado(a) "+participante.getNome()+",\n\n");
				msg.append("Seu pagamento foi efetuado com sucesso!\n");
				msg.append("Voc?? j?? pode cadastrar suas apostas na op????o Minhas Apostas at?? o dia 13/06/2018. \n Preencha seu palpite em todos os jogos e n??o esque??a de cadastrar as sele????es que ficar??o em primeiro e segundo lugar de cada grupo.\n ");
				msg.append("Para maiores informa????es, acesse as Regras do bol??o e a op????o Ajuda no menu.\n");
				msg.append("Caso n??o consiga efetuar seu login. Entre em contato com o Administrador atraves do email <bolaocopadomundofc@gmail.com> ou pelo telefone 987533736\n\n");
				msg.append("Atenciosamente,\n");
				msg.append("Administra????o do Bol??o.\n\n");
			    msg.append("E-mail gerado automaticamente, favor n??o responder.");
				
				
				bolaoUtil.enviaEmailSimples(participante,
						"Confirma????o de pagamento",msg.toString());
			}
			
			limpar();

			FacesUtil.addInfoMessage("Opera????o realizada com sucesso.");
			logger.info("Opera????o realizada com sucesso.");

		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Opera????o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "participanteForm";
	}

	public String limpaTelaCadastro() {
		limpar();
		return "cadastro?faces-redirect=true";
	}

	public String salvarCadastro() {

		try {

			// criptografando password e colocando para letra maiuscula
			try {
				participante.setPassword(bolaoUtil
						.criptografarSenha(participante.getPassword()));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// colocando em letra maiuscula para salvar
			participante.setNome(participante.getNome().toUpperCase());
			participante.setUsername(participante.getUsername().toUpperCase());

			ParticipanteService.salvar(participante);
			
			StringBuilder msg = new StringBuilder();
			msg.append("Prezado Administrador,\n\n");
			msg.append("O "+participante.getNome()+" de contato "+participante.getContato()+" realizou seu cadastro com sucesso.\n");
			msg.append("Agora temos "+(ParticipanteService.findAll().size()-1)+" participantes cadastrados sendo "+(ParticipanteService.count("", 1)-1)+" ativos.\n\n");
			msg.append("Atenciosamente,\n");
			msg.append("Administra????o do Bol??o.\n\n");
			msg.append("E-mail gerado automaticamente, favor n??o responder.");
			bolaoUtil.enviarEmailAdmin("Cadastro Participante Bol??o",msg.toString());
			participante = new Participante();
			
			FacesUtil
					.addInfoMessage("Seu inscri????o foi realizado com sucesso! Voc?? receber??, em breve, um e-mail com mais informa????es para dar continuidade ao seu cadastro");
			logger.info("Opera????o realizada com sucesso.");
			
			RequestContext.getCurrentInstance().execute("dlg.show()");
			

		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Opera????o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "cadastro";
	}

	/*public void enviarEmail() throws EmailException {
		StringBuilder msg = new StringBuilder();
		msg.append("Prezado(a) "+participante.getNome()+",\n\n");
		msg.append("Obrigado pelo seu interesse em participar do nosso Bol??o. Seu cadastro foi efetuado com sucesso!\n");
		msg.append("Apos a confirma????o do pagamento voc?? receber?? um email informando a ativa????o do seu usu??rio.\n");
		msg.append("S?? ent??o voc?? deve cadastrar suas apostas at?? o dia 11/06/2014.\n ");
		msg.append("Para maiores informa????es, acesse as Regras do bol??o e a op????o Ajuda no menu(ap??s est?? logado).\n");
		msg.append("Caso n??o consiga efetuar seu login ap??s ter recebido seu email de ativa????o. Entre em contato com o Administrador atraves do email... ou pelo telefone 87533736\n\n");
		msg.append("Atenciosamente,\n");
		msg.append("Administra????o do Bol??o.\n\n");
		 msg.append("E-mail gerado automaticamente, favor n??o responder.");

		bolaoUtil.enviaEmailSimples(participante,
				"Instru????es para prosseguir seu cadastro",msg.toString());
	}*/

	public String limpaTelaRecuperarSenha() {
		recuperarSehnaEmail = new String();
		recuperarSenhaLogin = new String();

		return "recuperarSenha";
	}

	public String recuperarSenha() {
		try {
			ParticipanteService.recuperarSenha(recuperarSenhaLogin,
					recuperarSehnaEmail);

			limpaTelaRecuperarSenha();
			FacesUtil
					.addInfoMessage("Confirma????o realizada com sucesso. Voc?? receber?? um e-mail com sua nova senha!");
			logger.info("Opera????o realizada com sucesso.");
		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Opera????o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "recuperarSenha";
	}

	public void selecionaContato(){
		if(contatoOpcao==1){
			participante.setContato("TCE");
		}
		else{
			participante.setContato(null);
		}
	}
	
	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante Participante) {
		this.participante = Participante;
	}

	public boolean isAlterar() {
		return alterar;
	}

	public void setAlterar(boolean alterar) {
		this.alterar = alterar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRecuperarSenhaLogin() {
		return recuperarSenhaLogin;
	}

	public void setRecuperarSenhaLogin(String recuperarSenhaLogin) {
		this.recuperarSenhaLogin = recuperarSenhaLogin;
	}

	public String getRecuperarSehnaEmail() {
		return recuperarSehnaEmail;
	}

	public void setRecuperarSehnaEmail(String recuperarSehnaEmail) {
		this.recuperarSehnaEmail = recuperarSehnaEmail;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	public int getContatoOpcao() {
		return contatoOpcao;
	}

	public void setContatoOpcao(int contatoOpcao) {
		this.contatoOpcao = contatoOpcao;
	}
	
	

}
