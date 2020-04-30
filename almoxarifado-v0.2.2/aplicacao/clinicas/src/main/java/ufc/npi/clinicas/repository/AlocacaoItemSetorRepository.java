package ufc.npi.clinicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.npi.clinicas.model.AlocacaoItemSetor;
import ufc.npi.clinicas.model.ItemEntrada;

public interface AlocacaoItemSetorRepository extends JpaRepository<AlocacaoItemSetor, Long>{
	
	List<AlocacaoItemSetor> findByItemEntrada(ItemEntrada itemEntrada);
	List<AlocacaoItemSetor> getByItemEntradaId(Integer  idItemEntrada);
	List<AlocacaoItemSetor> findBySetorId(Integer  setorId);
	@Query("select ais from AlocacaoItemSetor ais left join ais.itemEntrada ie left join ie.alocacoes a where ie.material.id = :idMaterial AND ais.setor.id= :idSetor")
	List<AlocacaoItemSetor> buscarByMaterialId(@Param("idMaterial") Integer idMaterial, @Param("idSetor") Integer idSetor);
	
}
