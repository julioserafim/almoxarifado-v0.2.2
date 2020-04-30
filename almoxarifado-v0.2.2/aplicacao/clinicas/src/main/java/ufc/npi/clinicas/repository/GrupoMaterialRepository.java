package ufc.npi.clinicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.clinicas.model.GrupoMaterial;

public interface GrupoMaterialRepository extends JpaRepository<GrupoMaterial, Integer>{
		
	List<GrupoMaterial> findAllByOrderByNome();
}

