package ufc.npi.clinicas.service;

import ufc.npi.clinicas.model.Token;
import ufc.npi.clinicas.model.Usuario;

public interface TokenService {

	Token buscarPorUsuario(Usuario usuario);

	Token buscar(String token);

	boolean existe(String token);

	void salvar(Token token);

	void deletar(Token token);
}
