package ufc.npi.clinicas.service;

import java.util.List;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.Setor;

public interface SetorService {
	
	void adicionar(Setor setor) throws ClinicasException;

	void editar(Setor setor) throws ClinicasException;
	
	Setor buscarPorId(Integer idsetor);
	
	List<Setor> listar();
	
	void excluir(Setor setor) throws ClinicasException;
	
	List<Setor> setoresDestino();
	
	Setor estoqueGeral();

}
