package ufc.npi.clinicas.service;

import java.util.List;
import java.util.Map;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.CodigoDeBarras;
import ufc.npi.clinicas.model.EstoqueSetor;
import ufc.npi.clinicas.model.Material;

public interface MaterialService {

	boolean verificarESalvarMaterial(Material material);

	boolean editar(Material material);

	Material buscarPorId(Integer idMaterial);

	List<Material> listar();

	void excluir(Material material) throws ClinicasException;

	Map<String, Object> buscarDetalhesPorId(Integer idMaterial);

	List<EstoqueSetor> buscarEstoquesPorSetores(Integer idMaterial);

	List<Material> buscarPorContemNome(String nome);

	List<Material> buscarMateriaisSemEstoque();
	
	List<Material> buscarMateriaisEmEstoque();
	
	List<Material> buscarMateriaisEmFalta(Boolean incluirVencidos);

	List<Material> buscarPorNomeOuCodigoBarrasOuCodigoInterno(String nome);


	Map<String, Object> adicionar(Material material, CodigoDeBarras codigoDeBarras) throws ClinicasException;

	void adicionarCodigoBarras(CodigoDeBarras codigoBarras) throws ClinicasException;

	
	boolean existeCodigoBarras(String codigoBarras);

	List<CodigoDeBarras> buscarCodigoBarras(Integer codigoBarras);

	public boolean temValidade(Integer idMaterial);

	void removerCodigoDeBarras(Material material, CodigoDeBarras codigoDeBarras);
}
