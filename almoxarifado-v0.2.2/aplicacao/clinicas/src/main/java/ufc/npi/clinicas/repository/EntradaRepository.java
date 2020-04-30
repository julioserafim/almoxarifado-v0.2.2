package ufc.npi.clinicas.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.npi.clinicas.model.Entrada;
import ufc.npi.clinicas.model.ItemEntrada;

public interface EntradaRepository extends JpaRepository<Entrada, Integer>, JpaSpecificationExecutor<Entrada> {
	List<Entrada> findByItens(List<ItemEntrada> itens);
	
	List<Entrada> getBySetor(Specification<Entrada> specification);

	@Query(value="SELECT DISTINCT e FROM AlocacaoItemSetor alo INNER JOIN alo.itemEntrada item INNER JOIN item.entrada e INNER JOIN alo.setor s WHERE s.id = :idSetor")
	List<Entrada> getBySetor(@Param("idSetor")Integer idSetor);

	List<Entrada> findAllByOrderByChegadaDesc();
}
