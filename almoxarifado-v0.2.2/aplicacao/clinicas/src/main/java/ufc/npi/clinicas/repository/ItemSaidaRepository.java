package ufc.npi.clinicas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.npi.clinicas.model.ItemSaida;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.SaidaMaterial;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.view.RelatorioEntradaSaidaDTO;

public interface ItemSaidaRepository extends JpaRepository<ItemSaida, Long> {

	ItemSaida getById(Long idItemSaidaMaterial);

	List<ItemSaida> findBySaidaMaterialAndMaterialAndLote(SaidaMaterial saidaMaterial, Material material, String lote);

	List<ItemSaida> findBySaidaMaterial(SaidaMaterial saidaMaterial);

	@Query("SELECT new ufc.npi.clinicas.model.view.RelatorioEntradaSaidaDTO(item.material.nome, item.material.unidadeMedida.nome, SUM(item.quantidade), item.saidaMaterial.data) FROM ItemSaida item WHERE item.saidaMaterial.destino = :destino AND item.saidaMaterial.data BETWEEN :inicio AND :fim GROUP BY item.material.nome, item.material.unidadeMedida.nome, item.saidaMaterial.data")
	List<RelatorioEntradaSaidaDTO> findBySaidaMaterialSetorEData(@Param("destino") Setor origem, @Param("inicio") Date inicio,
																 @Param("fim") Date fim);

	@Query("SELECT new ufc.npi.clinicas.model.view.RelatorioEntradaSaidaDTO(item.material.nome, item.material.unidadeMedida.nome, SUM(item.quantidade), item.saidaMaterial.data) FROM ItemSaida item WHERE item.saidaMaterial.data BETWEEN :inicio AND :fim GROUP BY item.material.nome, item.material.unidadeMedida.nome, item.saidaMaterial.data")
	List<RelatorioEntradaSaidaDTO> findBySaidaMaterialEData(@Param("inicio") Date inicio, @Param("fim") Date fim);
}
