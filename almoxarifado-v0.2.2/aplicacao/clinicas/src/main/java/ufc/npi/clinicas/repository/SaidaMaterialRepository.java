package ufc.npi.clinicas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import ufc.npi.clinicas.model.SaidaMaterial;
import ufc.npi.clinicas.model.Status;


public interface SaidaMaterialRepository extends JpaRepository<SaidaMaterial, Integer>, JpaSpecificationExecutor<SaidaMaterial> {
	@Query(value="SELECT s FROM SaidaMaterial s INNER JOIN s.origem o WHERE o.id = :idOrigem")
	List<SaidaMaterial> getByOrigem(@Param("idOrigem") Integer idOrigem);
	
	@Query(value="SELECT COUNT(*) FROM SaidaMaterial s WHERE s.status = :status")
	Integer getTotalStatus(@Param("status") Status status);

	List<SaidaMaterial> findAllByOrderByDataDesc();
}
