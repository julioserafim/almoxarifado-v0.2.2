package ufc.npi.clinicas.service;

import java.util.List;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.Fornecedor;

public interface FornecedorService {

	void adicionar(Fornecedor fornecedor) throws ClinicasException;

	void editar(Fornecedor fornecedor) throws ClinicasException;
	
	Fornecedor buscarPorId(Integer idFornecedor);
	
	List<Fornecedor> listar();
	
	void excluir(Fornecedor fornecedor) throws ClinicasException;
	
}
