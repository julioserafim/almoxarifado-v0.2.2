package ufc.npi.clinicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.clinicas.model.UnidadeMedida;

public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Integer>{
	
	List<UnidadeMedida> findAllByOrderByNomeAsc();

}
