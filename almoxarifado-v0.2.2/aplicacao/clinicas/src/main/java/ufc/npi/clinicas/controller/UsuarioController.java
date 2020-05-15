package ufc.npi.clinicas.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.clinicas.model.Papel;
import ufc.npi.clinicas.model.Token;
import ufc.npi.clinicas.model.Usuario;
import ufc.npi.clinicas.service.TokenService;
import ufc.npi.clinicas.service.UsuarioService;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.alert.AlertSet;
import ufc.npi.clinicas.util.api.Response;

@RestController
@RequestMapping("usuario")
public class UsuarioController {

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private TokenService tokenService;

	public List<String> todosPapeis() {
		List<String> todos_papeis = new ArrayList<String>();
		for (int i = 0; i < Papel.values().length; i++) {
			todos_papeis.add(Papel.values()[i].name());
		}

		return todos_papeis;
	}

	@GetMapping(value = "/adicionar")
	public ModelAndView formAdicionarUsuario() {
		ModelAndView mav = new ModelAndView("usuario/formulario_usuario");
		Usuario usuario = new Usuario();
		List<String> todosPapeis = todosPapeis();
		{

			mav.addObject("todos_papeis", todosPapeis);
			mav.addObject("usuario", usuario);
			mav.addObject("acao", "ADICIONAR");
			return mav;
		}
	}

	@PostMapping(value = "api/adicionar")
	public Response adicionarCurso(Usuario usuario) {
		if (usuarioService.adicionar(usuario)) {
			return new Response().withObject((new Usuario())).withSuccessMessage(Constants.USUARIO_ADICIONAR_SUCESSO);
		}
		return new Response().withFailStatus().withObject((new Usuario()))
				.withErrorMessage(Constants.USUARIO_EXISTENTE_ERRO);
	}

	@GetMapping(value = "/listar")
	public ModelAndView listarUsuarios() {
		ModelAndView mav = new ModelAndView("usuario/listar_usuarios");
		List<Usuario> usuarios = usuarioService.buscarUsuario();
		mav.addObject("usuarios", usuarios);

		return mav;
	}

	@GetMapping(value = "remover/{idUsuario}")
	public ModelAndView removerusuario(@PathVariable("idUsuario") Integer idUsuario,
			RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView("redirect:/usuario/listar");
		Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
		if (usuarioService.excluir(usuario)) {
			redirectAttributes.addFlashAttribute("alertas",
					new AlertSet().withSuccess(Constants.USUARIO_REMOVER_SUCESSO));
			return modelAndView;
		}
		redirectAttributes.addFlashAttribute("alertas", new AlertSet().withError(Constants.USUARIO_REMOVER_ERRO));
		return modelAndView;
	}

	@GetMapping(value = "/editar/{idUsuario}")
	public ModelAndView formEditarUsuario(@PathVariable("idUsuario") Integer idUsuario) {
		ModelAndView mav = new ModelAndView("usuario/formulario_usuario");
		Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
		Papel[] todos_papeis = Papel.values();
		mav.addObject("todos_papeis", todos_papeis);
		mav.addObject("usuario", usuario);
		mav.addObject("acao", "EDITAR");
		return mav;
	}

	@PostMapping(value = "api/habilitar")
	public Response habilitarUsuario(Integer idUsuario, boolean habilitar) {
		if (usuarioService.habilitarUsuario(idUsuario, habilitar))
			return new Response();
		else
			return new Response().withFailStatus().withErrorMessage(Constants.USUARIO_HABILITAR_ERRO);
	}

	@GetMapping(value = "/alterarSenha")
	public ModelAndView formAlterarSenha(ModelAndView mav) {
		mav.setViewName("usuario/alterar_senha");
		return mav;
	}

	@PostMapping(value = "/alterarSenha")
	public ModelAndView alterarSenha(ModelAndView mav, Authentication auth,
			@RequestParam(name = "senhaAtual") String senhaAtual, @RequestParam(name = "novaSenha") String novaSenha,
			@RequestParam(name = "confirmNovaSenha") String confirmNovaSenha) {
		if (novaSenha == null || novaSenha.isEmpty()) {
			mav.addObject("alertas", new AlertSet().withLongError("Erro. Informe a nova senha!"));
		} else if (novaSenha.equals(confirmNovaSenha)) {
			Usuario usuario = usuarioService.findByEmail(auth.getName());
			if (usuario != null && new BCryptPasswordEncoder().matches(senhaAtual, usuario.getSenha())) {
				usuario.setSenha(novaSenha);
				usuarioService.adicionar(usuario);
				mav.addObject("alertas", new AlertSet().withLongSuccess("Senha Alterada com sucesso!"));
			} else {
				mav.addObject("alertas", new AlertSet().withLongError("Erro. Senha Incorreta!"));
			}
		} else {
			mav.addObject("alertas", new AlertSet().withLongError("Erro. Nova senha é diferente da confiramação!"));
		}

		mav.setViewName("usuario/alterar_senha");
		return mav;
	}

	// begin RECUPERAR SENHA

	@GetMapping(value = "/recuperarSenha")
	public ModelAndView formRecuperarSenha(ModelAndView mav) {
		mav.setViewName("usuario/recuperar_senha");
		return mav;
	}

	@PostMapping(value = "/recuperarSenha")
	public ModelAndView recuperarSenha(ModelAndView mav, @RequestParam(name = "email") String email,
			RedirectAttributes redirectAttributes) {
		usuarioService.recuperarSenha(email);
		mav.addObject("alertas", new AlertSet().withLongSuccess("Email enviado! Verifique sua caixa de Entrada"));
		mav.setViewName("login");
		return mav;
	}

	@GetMapping("/senha")
	public ModelAndView alteracaoSenhaForm(ModelAndView mav, @RequestParam("token") Token token) {
		if (token == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		mav.addObject("token", token);
		mav.setViewName("usuario/alteracao_senha");
		return mav;
	}

	@PostMapping("/senha")
	public ModelAndView alteracaoSenha(@RequestParam(name = "token") String token,
			@RequestParam(name = "novaSenha") String novaSenha,
			@RequestParam(name = "confirmNovaSenha") String confirmNovaSenha, RedirectAttributes redirectAttributes) {
		if (novaSenha.equals(confirmNovaSenha)) {
			Token tok = tokenService.buscar(token);
			usuarioService.novaSenha(tok, novaSenha);
			redirectAttributes.addFlashAttribute("alertas",
					new AlertSet().withLongSuccess("Senha alterada com Sucesso!"));
		} else {
			redirectAttributes.addFlashAttribute("alertas",
					new AlertSet().withLongError("Ocorreu um erro ao alterar sua senha, or favor tente novamente."));
		}
		return new ModelAndView("redirect:/login");
	}

	// end RECUPERAR SENHA

	@PostMapping(value = "/api/editar")
	public Response editarUsuario(Usuario usuario) {
		if (usuarioService.editar(usuario)) {
			return new Response().withObject(new Usuario()).withSuccessMessage(Constants.USUARIO_EDITAR_SUCESSO);
		}
		return new Response().withFailStatus().withObject(new Usuario())
				.withErrorMessage(Constants.USUARIO_EDITAR_ERRO);
	}

	@GetMapping(value = "/api/remover/{idUsuario}")
	public Response removerUsuario(@PathVariable("idUsuario") Integer idUsuario) {
		Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
		if (usuarioService.excluir(usuario)) {
			return new Response().withObject(usuario).withSuccessMessage(Constants.USUARIO_REMOVER_SUCESSO);
		}
		return new Response().withFailStatus().withObject(usuario).withErrorMessage(Constants.USUARIO_REMOVER_ERRO);

	}

}
