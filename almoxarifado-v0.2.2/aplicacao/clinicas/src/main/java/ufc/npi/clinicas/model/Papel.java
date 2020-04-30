package ufc.npi.clinicas.model;

import org.springframework.security.core.GrantedAuthority;

public enum Papel implements GrantedAuthority {

	ALMOXARIFADO("Almoxarifado"), SETOR("Setor"), ADMIN("Admin");

	private String nome;

	private Papel(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String getAuthority() {
		return this.toString();
	}

}
