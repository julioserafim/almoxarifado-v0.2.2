package ufc.npi.clinicas.service;


import java.util.List;

import ufc.npi.clinicas.model.Token;
import ufc.npi.clinicas.model.Usuario;

public interface UsuarioService {
	
	boolean adicionar(Usuario usuario);

	boolean editar(Usuario usuario);
	
	Usuario buscarUsuarioPorId(Integer idUsuario);
	
	List<Usuario> buscarUsuario();
	
	boolean excluir(Usuario usuario);
	
	Usuario findByEmail(String email);
	
	void recuperarSenha(String email);
	
	void novaSenha(Token token, String senha);
	
	boolean habilitarUsuario(Integer idUsuario, boolean habilitar);

}
