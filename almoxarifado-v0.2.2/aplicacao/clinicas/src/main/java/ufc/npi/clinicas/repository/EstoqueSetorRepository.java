package ufc.npi.clinicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.clinicas.model.EstoqueSetor;

import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.Setor;

public interface EstoqueSetorRepository extends JpaRepository<EstoqueSetor, Integer>{

	EstoqueSetor findByMaterialAndSetor(Material material, Setor setor);
	
	List<EstoqueSetor> getByMaterialId(Integer  idMaterial);
	
	List<EstoqueSetor> getBySetorId(Integer  idSetor);

}
