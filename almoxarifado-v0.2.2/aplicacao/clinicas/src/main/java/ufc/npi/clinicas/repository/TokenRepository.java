package ufc.npi.clinicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.clinicas.model.Token;
import ufc.npi.clinicas.model.Usuario;

public interface TokenRepository extends JpaRepository<Token, String>{
	Token findByUsuario(Usuario usuario);
}
