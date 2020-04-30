package ufc.npi.clinicas.service;

import java.util.List;

import ufc.npi.clinicas.model.AlocacaoItemSetor;
import ufc.npi.clinicas.model.ItemEntrada;

public interface AlocacaoItemSetorService {

	AlocacaoItemSetor adicionar(AlocacaoItemSetor alocacaoItemSetor);

	boolean editar(AlocacaoItemSetor alocacaoItemSetor);

	AlocacaoItemSetor buscarPorId(Long idAlocacaoItemSetor);
	
	List<AlocacaoItemSetor> listarPorSetor(Integer idSetor);

	List<AlocacaoItemSetor> buscarPorItemEntrada(Integer idItemEntrada);

	List<AlocacaoItemSetor> listar();

	List<AlocacaoItemSetor> listarPorItemEntrada(ItemEntrada itemEntrada);

	boolean excluir(AlocacaoItemSetor alocacaoItemSetor);

	Integer somaAlocacaoItemSetorPorItemEntrada(ItemEntrada itemEntrada);
	
	void atualizarAlocacaoItemSetor(Integer idMaterial, Integer origem, Integer destino, Integer quantidade);
}
