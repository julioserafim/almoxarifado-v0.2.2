package ufc.npi.clinicas.service;

import java.util.Date;
import java.util.List;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.SaidaMaterial;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.Status;

public interface SaidaMaterialService {

	List<SaidaMaterial> listar();
	
	Integer buscarTotalStatus(Status status);
	
	List<SaidaMaterial> listarPorOrigemEData(Setor setor, Date dataInicio, Date dataFim);
	
	public void adicionarSaida(SaidaMaterial saidaMaterial, String emailResponsavel);

	SaidaMaterial buscarPorId(Integer idSaida);
	
	void editar(SaidaMaterial saida) throws ClinicasException;
	
	List<SaidaMaterial> buscarPorDataEMaterial(Date dataInicio, Date dataFim , Material material);

	List<SaidaMaterial> buscarPorOrigem(Integer idOrigem);

	void excluir(SaidaMaterial saida);
}
