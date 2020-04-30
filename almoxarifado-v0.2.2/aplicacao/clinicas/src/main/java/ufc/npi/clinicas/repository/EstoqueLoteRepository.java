package ufc.npi.clinicas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.npi.clinicas.model.*;

public interface EstoqueLoteRepository extends JpaRepository<EstoqueLote, Integer> {

	EstoqueLote findOneByMaterialAndLote(Material material, String lote);

	@Query(value = "SELECT el FROM EstoqueLote as el LEFT OUTER JOIN el.material as mat LEFT OUTER JOIN el.material.codigos as codigos WHERE (codigos.codigo = :codigoBarras or mat.nome like :nome or mat.codigoInterno = :codigoInterno) and el.quantidade > 0")
	List<EstoqueLote> findDistinctByMaterial_Codigos_CodigoOrMaterialNomeStartingWithOrMaterialCodigoInterno(
			@Param("codigoBarras") String codigoBarras, @Param("nome") String nomeMaterial,
			@Param("codigoInterno") String codigoInterno);

	EstoqueLote findOneById(Integer id);

	@Query(value = "SELECT DISTINCT(el) FROM EstoqueLote el WHERE el.validade < CURRENT_DATE")
	List<EstoqueLote> getMateriaisVencidos();

	@Query(value = "SELECT SUM(e.quantidade) FROM EstoqueLote e WHERE e.material.id = :idMaterial AND e.validade < CURRENT_DATE")
	Integer getLotesVencidosMaterial(@Param("idMaterial") Integer id);

	@Query(value = "SELECT COUNT(e.quantidade) FROM EstoqueLote e WHERE e.material.id = :idMaterial AND e.validade is NULL")
	Integer getQuantidadeSemValidade(@Param("idMaterial") Integer id);

	@Query(value = "SELECT DISTINCT(el) FROM EstoqueLote el WHERE el.validade < :data ORDER BY el.validade")
	List<EstoqueLote> getByDataVencimento(@Param("data") Date data);

	@Query(value = "SELECT DISTINCT(el) FROM EstoqueLote el WHERE el.material = :data ORDER BY el.validade DESC")
	List<EstoqueLote> findByMaterialAndValidadeDesc(@Param("data") Material material);
	
	@Query(value = "SELECT DISTINCT(el) FROM EstoqueLote el WHERE el.validade < :data and el.quantidade > 0 "
			+ "ORDER BY el.validade ASC")
	List<EstoqueLote> getByDataVencimentoEmEstoque(@Param("data") Date data);
	
	List<EstoqueLote> findByMaterial(Material material);
}
