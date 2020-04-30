package ufc.npi.clinicas.service;

import ufc.npi.clinicas.model.Token;

public interface EmailService {
	
	void emailRecuperacaoSenha(Token token);
}
