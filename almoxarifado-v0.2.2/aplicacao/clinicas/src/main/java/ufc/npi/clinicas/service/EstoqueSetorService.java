package ufc.npi.clinicas.service;


import java.util.List;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.EstoqueSetor;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.SaidaMaterial;


public interface EstoqueSetorService {
		
	public void adicionar(EstoqueSetor estoqueSetor);
	
	public void editar(EstoqueSetor estoqueSetor);
	
	public List<EstoqueSetor> buscarPorSetor(Integer idSetor);

	public void atualizarEstoquesSetor(SaidaMaterial saida ) throws ClinicasException;
		
	public EstoqueSetor buscarPorSetorEMaterial(Setor setor, Material material);
		
	public EstoqueSetor buscarPorId(Integer id);
}
