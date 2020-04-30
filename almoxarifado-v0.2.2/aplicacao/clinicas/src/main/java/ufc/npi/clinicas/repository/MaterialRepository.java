package ufc.npi.clinicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.UnidadeMedida;

public interface MaterialRepository extends JpaRepository<Material, Integer>{
	
	
	@Query(
            "SELECT DISTINCT(t) FROM Material t JOIN FETCH t.unidadeMedida JOIN FETCH t.codigos c WHERE LOWER(t.nome) LIKE LOWER(CONCAT('%', :nomeMaterial, '%')) OR LOWER(c.codigo) LIKE LOWER(CONCAT(:nomeMaterial, '%')) OR LOWER(t.codigoInterno) LIKE LOWER(CONCAT(:nomeMaterial, '%'))"
    )public List<Material> search(@Param("nomeMaterial") String nomeMaterial);
	
	Material CodigoInterno(String codigoInterno);
	
	@Query(value = "FROM Material m WHERE m.estoque = 0 ORDER BY m.nome")
    List<Material> getByMateriaisSemEstoque();
	@Query(value = "FROM Material m WHERE m.estoque > 0 ORDER BY m.nome")
    List<Material> getByMateriaisEmEstoque();
	
	@Query(value="SELECT DISTINCT(el.material) FROM EstoqueLote el WHERE ( SELECT COUNT(*) FROM EstoqueLote el2 WHERE el.material.id = el2.material.id and el2.quantidade > 0 and el2.validade >= CURRENT_DATE ) = 0 AND el.validade is not NULL ORDER BY el.material.nome")
	List<Material> getMateriaisEstaoTodosVencidos();
	
	List<Material> getDistinctByNomeStartingWithIgnoreCaseOrCodigoInternoOrCodigos_Codigo(String nome, String codigoInterno ,String codigo);
	
	Material getByNomeAndUnidadeMedida(String nome, UnidadeMedida unidadeMedida);


}
