package ufc.npi.clinicas.service;


import java.util.Date;
import java.util.List;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.EstoqueLote;
import ufc.npi.clinicas.model.Material;

public interface EstoqueLoteService {
	EstoqueLote buscarPorMaterialLote(Material material, String lote);
	
	void salvar(EstoqueLote estoqueLote);
	
	public List<EstoqueLote> buscarPorNomeMaterialCodigoInternoCodigoDeBarrasESetor(String codigoBarras,
			String nomeMaterial, String codigoInterno) ;
	
	public EstoqueLote buscarPorId(Integer id);
	
	public List<EstoqueLote> buscarMateriaisVencidos();
	
	public List<EstoqueLote> getByDataVencimentoMaterialEmEstoque(Date date);
	
	public List<EstoqueLote> getByMaterial(Material material);
	
	public List<EstoqueLote> getByMaterialAndValidadeDesc(Material material);
	
	public void editarEstoqueLote(EstoqueLote estoque, String lote, Date validade) throws ClinicasException;

	public void excluirEstoqueLote(EstoqueLote estoque);
}
