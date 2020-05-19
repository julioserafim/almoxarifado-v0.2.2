package ufc.npi.clinicas.service.impl;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.dao.DataIntegrityViolationException;

import ufc.npi.clinicas.model.Token;
import ufc.npi.clinicas.model.Usuario;
import ufc.npi.clinicas.repository.UsuarioRepository;
import ufc.npi.clinicas.service.EmailService;
import ufc.npi.clinicas.service.TokenService;
import ufc.npi.clinicas.service.UsuarioService;

@Named
public class UsuarioServiceImpl implements UsuarioService{
	
	@Inject
	private UsuarioRepository usuarioRepository;
	
	@Inject
	private TokenService tokenService;

	@Inject
	private EmailService emailService;
	
	@Override
	public boolean adicionar(Usuario usuario) {
		try {
			if (usuario.getSenha() == null ){
				//senha padrão é o número do SIAPE
				usuario.setSenha(usuario.getSiape());				
			}
			usuario.setHashSenha(usuario.getSenha());
			usuario.setHabilitado(true);
			usuarioRepository.save(usuario);
		}
		catch (DataIntegrityViolationException e){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean editar(Usuario usuario) {
		Usuario usuarioAntigo = this.buscarUsuarioPorId(usuario.getId());
		if(usuarioAntigo == null){
			return false;
		}

		usuario.setHabilitado(usuarioAntigo.isHabilitado());
		usuario.setSenha(usuarioAntigo.getSenha());

		return this.salvarUsuario(usuario);
	}

	private boolean salvarUsuario(Usuario usuario) {
		try {
			usuarioRepository.save(usuario);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	@Override
	public Usuario buscarUsuarioPorId(Integer idUsuario) {
		return usuarioRepository.findOne(idUsuario);
	}

	@Override
	public List<Usuario> buscarUsuario() {
		return usuarioRepository.findAllByOrderByNomeAsc();
	}

	@Override
	public boolean excluir(Usuario usuario){
		if(usuario!=null){
			try {
				usuarioRepository.delete(usuario);	
			} catch (DataIntegrityViolationException e) {
				return false;
			}
		}else{
			return false;
		} 
		
		return true;
		
		
	}
	
	@Override
	public Usuario findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	
	@Override
	public void recuperarSenha(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email);

		if (usuario != null) {
			Token token = null;
			token = tokenService.buscarPorUsuario(usuario);

			if (token == null) {
				token = new Token();

				token.setUsuario(usuario);

				do {
					token.setToken(UUID.randomUUID().toString());
				} while (tokenService.existe(token.getToken()));

				tokenService.salvar(token);
			}

			emailService.emailRecuperacaoSenha(token);
		}

	}

	@Override
	public void novaSenha(Token token, String senha) {
		if (token != null) {
			Usuario usuario = token.getUsuario();
			usuario.setHashSenha(senha);
	
			usuarioRepository.save(usuario);
	
			tokenService.deletar(token);
		}
	}

	@Override
	public boolean habilitarUsuario(Integer idUsuario,boolean habilitar) {
		Usuario usuario = this.buscarUsuarioPorId(idUsuario);
		usuario.setHabilitado(habilitar);
		return editar(usuario);
	}
}
