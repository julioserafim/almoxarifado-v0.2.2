package ufc.npi.clinicas.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.dao.DataIntegrityViolationException;

import ufc.npi.clinicas.model.AlocacaoItemSetor;
import ufc.npi.clinicas.model.ItemEntrada;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.repository.AlocacaoItemSetorRepository;
import ufc.npi.clinicas.repository.SetorRepository;
import ufc.npi.clinicas.service.AlocacaoItemSetorService;

@Named
public class AlocacaoItemSetorServiceImpl implements AlocacaoItemSetorService{
	
	@Inject
	private AlocacaoItemSetorRepository alocacaoItemSetorRepository;
	
	@Inject 
	private SetorRepository setorRepository;
	
	@Override
	public AlocacaoItemSetor adicionar(AlocacaoItemSetor alocacaoItemSetor) {
		
		this.alocacaoItemSetorRepository.save(alocacaoItemSetor);

		return alocacaoItemSetor;
		
	}

	@Override
	public boolean editar(AlocacaoItemSetor alocacaoItemSetor) {
		
		try{
			this.alocacaoItemSetorRepository.save(alocacaoItemSetor);
		}catch(DataIntegrityViolationException exception){
			return false;
		}
		return true;
		
	}

	@Override
	public AlocacaoItemSetor buscarPorId(Long idAlocacaoItemSetor) {
		return this.alocacaoItemSetorRepository.findOne(idAlocacaoItemSetor);
	}
	
	@Override
	public List<AlocacaoItemSetor> listarPorSetor(Integer idSetor) {
		return this.alocacaoItemSetorRepository.findBySetorId(idSetor);
	}
	
	@Override
	public List<AlocacaoItemSetor> buscarPorItemEntrada(Integer idItemEntrada){
		return this.alocacaoItemSetorRepository.getByItemEntradaId(idItemEntrada);
	}

	@Override
	public List<AlocacaoItemSetor> listar() {
		return this.alocacaoItemSetorRepository.findAll();
	}

	@Override
	public boolean excluir(AlocacaoItemSetor alocacaoItemSetor) {
		
		try{
			this.alocacaoItemSetorRepository.delete(alocacaoItemSetor);
		}catch(Exception exception){
			return false;
		}
		return true;
		
	}

	@Override
	public List<AlocacaoItemSetor> listarPorItemEntrada(ItemEntrada itemEntrada) {
		return this.alocacaoItemSetorRepository.findByItemEntrada(itemEntrada);
	}

	@Override
	public Integer somaAlocacaoItemSetorPorItemEntrada(ItemEntrada itemEntrada) {
		
		List<AlocacaoItemSetor> itens = this.listarPorItemEntrada(itemEntrada);
		
		Integer soma = 0;
		
		for(AlocacaoItemSetor itemSetor:itens){
			soma+= itemSetor.getQuantidade();
		}
		
		return soma;
	}
	
	@Override
	public void atualizarAlocacaoItemSetor(Integer idMaterial,Integer origem, Integer destino, Integer quantidade) {
		Integer idOrigem = origem;
		List<AlocacaoItemSetor> alocacoes = this.alocacaoItemSetorRepository.buscarByMaterialId(idMaterial, idOrigem);
		Setor setorDestino = setorRepository.findOne(destino);
		for(int i = 0; i < alocacoes.size(); i++){
			alocacoes.get(i).setSetor(setorDestino);
			alocacoes.get(i).setQuantidade(quantidade);
			this.alocacaoItemSetorRepository.save(alocacoes.get(i));
		}
	}

}
