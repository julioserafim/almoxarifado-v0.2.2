package ufc.npi.clinicas.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.EstoqueLote;
import ufc.npi.clinicas.model.EstoqueSetor;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.ItemSaida;
import ufc.npi.clinicas.model.SaidaMaterial;
import ufc.npi.clinicas.repository.EstoqueLoteRepository;
import ufc.npi.clinicas.repository.EstoqueSetorRepository;
import ufc.npi.clinicas.repository.MaterialRepository;
import ufc.npi.clinicas.service.EstoqueSetorService;
import ufc.npi.clinicas.util.Constants;

@Named
public class EstoqueSetorServiceImpl implements EstoqueSetorService{
	
	@Inject
	private EstoqueSetorRepository estoqueSetorRepository;

	@Inject
	private MaterialRepository materialRepository;
	
	@Inject
	private EstoqueLoteRepository estoqueLoteRepository;

	
	@Override
	public void adicionar(EstoqueSetor estoqueSetor) {		
		estoqueSetorRepository.save(estoqueSetor);
		
	}
	
	@Override
	public void editar(EstoqueSetor estoqueSetor) {
		estoqueSetorRepository.save(estoqueSetor);
	}
	

	@Override
	public List<EstoqueSetor> buscarPorSetor(Integer idSetor) {
		List<EstoqueSetor> estoqueSetor = estoqueSetorRepository.getBySetorId(idSetor);
		return estoqueSetor;
	}
	
	@Transactional(rollbackOn=ClinicasException.class)
	public void atualizarEstoquesSetor(SaidaMaterial saida) throws ClinicasException{				
		for (ItemSaida item : saida.getItens()){									
			EstoqueSetor estoque = buscarPorSetorEMaterial(saida.getOrigem(), item.getMaterial());					
			
			EstoqueLote estoqueLote = estoqueLoteRepository.findOneByMaterialAndLote(item.getMaterial(), item.getLote());
			
			if(estoque == null){
				throw new ClinicasException(String.format(Constants.ESTOQUE_SETOR_BAIXA_MATERIAL_ERRO, item.getMaterial().getNome(), item.getLote()));
			}else{
				estoque.registrarBaixa(item.getQuantidade());			
				if(estoque.getMaterial().getEstoque() < 0)
					throw new ClinicasException(String.format(Constants.ESTOQUE_SETOR_ESTOQUE_MATERIAL_INSUFICIENTE, estoque.getMaterial().getNome()));
				
				estoqueLote.registrarBaixaLote(item.getQuantidade());
				if(estoqueLote.getQuantidade() < 0)
					throw new ClinicasException(String.format(Constants.ESTOQUE_SETOR_ESTOQUE_MATERIAL_NO_SETOR_DO_LOTE_INSUFICIENTE, estoque.getMaterial().getNome(),item.getLote(),saida.getOrigem().getNome()));
				
				materialRepository.save(estoque.getMaterial());
				estoqueSetorRepository.save(estoque);
				estoqueLoteRepository.save(estoqueLote);
				
			}
		}
	}


	@Override
	public EstoqueSetor buscarPorSetorEMaterial(Setor setor, Material material) {
		return this.estoqueSetorRepository.findByMaterialAndSetor(material, setor);
	}

	@Override
	public EstoqueSetor buscarPorId(Integer id) {
		return this.estoqueSetorRepository.findOne(id);
	}

	
}