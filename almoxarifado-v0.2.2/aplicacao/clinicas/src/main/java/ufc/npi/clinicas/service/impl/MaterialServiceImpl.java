package ufc.npi.clinicas.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.CodigoDeBarras;
import ufc.npi.clinicas.model.EstoqueSetor;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.repository.CodigoBarrasRepository;
import ufc.npi.clinicas.repository.EstoqueLoteRepository;
import ufc.npi.clinicas.repository.EstoqueSetorRepository;
import ufc.npi.clinicas.repository.MaterialRepository;
import ufc.npi.clinicas.service.MaterialService;
import ufc.npi.clinicas.util.Constants;

@Named
public class MaterialServiceImpl implements MaterialService {
	@Inject
	private MaterialRepository materialRepository;

	@Inject
	private EstoqueSetorRepository estoqueSetorRepository;

	@Inject
	private CodigoBarrasRepository codigoDeBarrasRepository;

	@Inject
	private EstoqueLoteRepository estoqueLoteRepository;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private Material material;

	public boolean materialIsNotNull(Material material) {
		this.material = material;
		if (material != null) {

		}

		return true;
	}

	@Override
	public boolean adicionar(Material material) {

		Material materialExistente = materialRepository.getByNomeAndUnidadeMedida(material.getNome(),
				material.getUnidadeMedida());
		materialIsNotNull(materialExistente);
		try {
			material(material);
			materialRepository.save(material);
		} catch (DataIntegrityViolationException e) {
			return false;
		}
		return true;
	}

	private void material(Material material) {
		if (material.getEstoque() == null) {
			material.setEstoque(0);
		}
	}

	@Override

	public void adicionarCodigoBarras(CodigoDeBarras codigoBarras) throws ClinicasException {

		try {
			CodigoDeBarras codigo = new CodigoDeBarras(codigoBarras, material);
			
			material.addCodigo(codigo);
			codigoDeBarrasRepository.save(codigo);

		} catch (IllegalArgumentException e) {
			throw new ClinicasException(Constants.MATERIAL_CODIGO_BARRAS_ADICIONAR_ERRO);
		}

	}

	@Override
	public boolean editar(Material material) {
		Material materialExistenteEditar;

		try {
			if (material.getEstoque() == null) {
				material.setEstoque(0);
			}

			material.setCodigoInterno(material.getCodigoInterno().trim());
			material.setNome(material.getNome().trim());
			if (material.getCodigoInterno() == null || material.getNome() == null || material.getNome().isEmpty()) {
				return false;
			} else if (!material.getCodigoInterno().isEmpty()) {
				materialExistenteEditar = materialRepository.CodigoInterno(material.getCodigoInterno());
				if (materialExistenteEditar != null && !materialExistenteEditar.getId().equals(material.getId()))
					return false;
			}
			materialRepository.save(material);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public Material buscarPorId(Integer idMaterial) {
		return materialRepository.findOne(idMaterial);
	}

	@Override
	public List<Material> listar() {
		return materialRepository.findAll();
	}

	@Override
	public void excluir(Material material) throws ClinicasException {
		if (material != null) {
			try {
				materialRepository.delete(material);
			} catch (DataIntegrityViolationException e) {
				throw new ClinicasException(Constants.MATERIAL_REMOVER_ERRO);
			}
		}
	}

	@Override
	public Map<String, Object> buscarDetalhesPorId(Integer idMaterial) {
		Material material = this.buscarPorId(idMaterial);
		if (material == null) {
			return null;
		}
		List<EstoqueSetor> estoqueSetores = this.buscarEstoquesPorSetores(idMaterial);

		Map<String, Object> detalhesMaterial = new HashMap<String, Object>();
		detalhesMaterial.put("material", material);
		detalhesMaterial.put("estoque_por_setores", estoqueSetores);
		return detalhesMaterial;
	}

	@Override
	public List<EstoqueSetor> buscarEstoquesPorSetores(Integer idMaterial) {
		return estoqueSetorRepository.getByMaterialId(idMaterial);
	}

	@Override

	public List<Material> buscarPorContemNome(String nome) {
		return materialRepository.search(nome);
	}

	@Override
	public List<Material> buscarPorNomeOuCodigoBarrasOuCodigoInterno(String nome) {

		// retorna todos os materiais digitados
		List<Material> listaMaterial = materialRepository.search(nome);
		return listaMaterial;
	}

	@Override
	public List<CodigoDeBarras> buscarCodigoBarras(Integer codigoBarras) {

		return codigoDeBarrasRepository.findByMaterialId(codigoBarras);

	}

	@Override
	public boolean existeCodigoBarras(String codigoBarras) {
		return codigoBarras != null && !codigoBarras.isEmpty()
				&& !codigoDeBarrasRepository.findByCodigo(codigoBarras).isEmpty();
	}

	public List<Material> buscarMateriaisSemEstoque() {
		return materialRepository.getByMateriaisSemEstoque();
	}

	public List<Material> buscarMateriaisEmEstoque() {
		return materialRepository.getByMateriaisEmEstoque();
	}

	public void removerCodigoDeBarras(Material material, CodigoDeBarras codigoDeBarras) {
		material.removeCodigo(codigoDeBarras);
		codigoDeBarrasRepository.delete(codigoDeBarras);
	}

	@Override
	public List<Material> buscarMateriaisEmFalta(Boolean incluirVencidos) {
		List<Material> materiais = materialRepository.getByMateriaisSemEstoque();
		if (incluirVencidos) {
			materiais.addAll(materialRepository.getMateriaisEstaoTodosVencidos());
		}
		return materiais;
	}

	@Override
	public boolean temValidade(Integer idMaterial) {
		if (estoqueLoteRepository.getQuantidadeSemValidade(idMaterial) == 0)
			return false;
		return true;
	}

}
