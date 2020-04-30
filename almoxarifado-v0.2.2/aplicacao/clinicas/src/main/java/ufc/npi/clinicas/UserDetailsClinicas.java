package ufc.npi.clinicas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ufc.npi.clinicas.model.Usuario;
import ufc.npi.clinicas.service.UsuarioService;
import static ufc.npi.clinicas.util.Constants.LOGIN_INVALIDO;

@Service
public class UserDetailsClinicas implements UserDetailsService {

	@Autowired
	private UsuarioService usuarioService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.findByEmail(username);

		if (usuario == null) {
			throw new UsernameNotFoundException(LOGIN_INVALIDO);
		} else {
			return usuario;
		}
	}

}
