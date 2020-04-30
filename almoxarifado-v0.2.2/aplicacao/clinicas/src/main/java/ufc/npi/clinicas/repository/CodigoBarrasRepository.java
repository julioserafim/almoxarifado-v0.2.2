package ufc.npi.clinicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.clinicas.model.CodigoDeBarras;

public interface CodigoBarrasRepository extends JpaRepository<CodigoDeBarras, String>{
	
	List<CodigoDeBarras> findByCodigo(String codigoBarras);

	List<CodigoDeBarras> findByMaterialId(Integer codigoMaterial);

}
