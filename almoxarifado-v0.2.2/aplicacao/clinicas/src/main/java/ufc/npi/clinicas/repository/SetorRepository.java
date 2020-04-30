package ufc.npi.clinicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ufc.npi.clinicas.model.Setor;

public interface SetorRepository extends JpaRepository<Setor, Integer>{
	 
	@Query(value = "SELECT * FROM setor s WHERE s.geral = 'false'", nativeQuery=true)
	public List<Setor> setoresDestino();
	
	@Query(value = "SELECT * FROM setor s WHERE s.geral = 'true'", nativeQuery=true)
	public List<Setor> estoqueGeral();
	
}
