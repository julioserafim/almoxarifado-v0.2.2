package ufc.npi.clinicas.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.EstoqueLote;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.repository.EstoqueLoteRepository;
import ufc.npi.clinicas.service.EstoqueLoteService;

@Named
public class EstoqueLoteServiceImpl implements EstoqueLoteService {

	@Inject
	private EstoqueLoteRepository estoqueLoteRepository;

	@Override
	public EstoqueLote buscarPorMaterialLote(Material material, String lote) {
		return estoqueLoteRepository.findOneByMaterialAndLote(material, lote);
	}

	@Override
	public void salvar(EstoqueLote estoqueLote) {
		estoqueLoteRepository.save(estoqueLote);
	}

	@Override
	public List<EstoqueLote> buscarPorNomeMaterialCodigoInternoCodigoDeBarrasESetor(String codigoBarras,
			String nomeMaterial, String codigoInterno) {

		nomeMaterial = "%" + nomeMaterial + "%"; //formata o nome do material para entrada da Query
		List<EstoqueLote> materiais = estoqueLoteRepository.findDistinctByMaterial_Codigos_CodigoOrMaterialNomeStartingWithOrMaterialCodigoInterno(codigoBarras, nomeMaterial, codigoInterno);
		
		return materiais;
	}

	@Override
	public EstoqueLote buscarPorId(Integer id) {
		return estoqueLoteRepository.findOne(id);
	}

	@Override
	public List<EstoqueLote> buscarMateriaisVencidos() {
		List<EstoqueLote> estoqueLotes = estoqueLoteRepository.getMateriaisVencidos();
		if (estoqueLotes.size() > 0) {
			Collections.sort(estoqueLotes, new Comparator<EstoqueLote>() {
				@Override
				public int compare(final EstoqueLote object1, final EstoqueLote object2) {
					return object1.getMaterial().getNome().compareTo(object2.getMaterial().getNome());
				}
			});
		}
		return estoqueLotes;
	}

	@Override
	public List<EstoqueLote> getByDataVencimentoMaterialEmEstoque(Date data) {
		return estoqueLoteRepository.getByDataVencimentoEmEstoque(data);
	}

	@Override
	public List<EstoqueLote> getByMaterial(Material material) {
		return estoqueLoteRepository.findByMaterial(material);
	}
	@Override
	public List<EstoqueLote> getByMaterialAndValidadeDesc(Material material) {
		return estoqueLoteRepository.findByMaterialAndValidadeDesc(material);
	}

	@Override
	public void editarEstoqueLote(EstoqueLote estoque, String lote, Date validade) throws ClinicasException {
		if (null != lote) {
			estoque.setLote(lote);
			estoque.setValidade(validade);
			estoqueLoteRepository.save(estoque);
		} else {
			throw new ClinicasException("O lote não pode ser vazio");
		}

	}

	@Override
	public void excluirEstoqueLote(EstoqueLote estoque) {
		if (estoque.getQuantidade() == 0)
			estoqueLoteRepository.delete(estoque);
	}
}
