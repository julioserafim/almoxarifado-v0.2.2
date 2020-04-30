package ufc.npi.clinicas;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ufc.npi.clinicas.model.Usuario;
import ufc.npi.clinicas.service.UsuarioService;

@ControllerAdvice
public class UsuarioControllerAdvice {
	
	@Inject
	private UsuarioService usuarioService;
	
	@ModelAttribute
	public void addUsuario(Model model, Authentication auth){
		if (auth!=null && auth.isAuthenticated()){
			Usuario usuario = usuarioService.findByEmail(auth.getName());
			model.addAttribute("usuario_", usuario);			
		}
	}
}
