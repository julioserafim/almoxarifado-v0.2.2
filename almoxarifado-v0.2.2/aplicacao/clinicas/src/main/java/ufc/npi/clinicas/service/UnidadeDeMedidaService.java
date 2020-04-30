package ufc.npi.clinicas.service;

import java.util.List;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.UnidadeMedida;

public interface UnidadeDeMedidaService {

	void adicionar(UnidadeMedida unidadeMedida) throws ClinicasException;

	void editar(UnidadeMedida unidadeMedida) throws ClinicasException;
	
	UnidadeMedida buscarPorId(Integer idUnidadeMedida);
	
	List<UnidadeMedida> listar();
	
	boolean excluir(UnidadeMedida unidadeMedida);
	
}
