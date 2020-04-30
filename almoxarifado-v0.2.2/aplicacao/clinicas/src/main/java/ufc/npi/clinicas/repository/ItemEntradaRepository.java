package ufc.npi.clinicas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.npi.clinicas.model.AlocacaoItemSetor;
import ufc.npi.clinicas.model.ItemEntrada;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.view.RelatorioEntradaSaidaDTO;

public interface ItemEntradaRepository extends JpaRepository<ItemEntrada, Integer> {
	List<ItemEntrada> findByAlocacoes(List<AlocacaoItemSetor> alocacoes);

	List<ItemEntrada> findByLoteAndMaterial(String lote, Material material);

	@Query("SELECT new ufc.npi.clinicas.model.view.RelatorioEntradaSaidaDTO(al.itemEntrada.material.nome, al.itemEntrada.material.unidadeMedida.nome, SUM(al.quantidade), al.itemEntrada.entrada.chegada) FROM AlocacaoItemSetor al WHERE al.setor = :setor AND al.itemEntrada.entrada.statusEntrada = 'FINALIZADA' AND al.itemEntrada.entrada.chegada BETWEEN :inicio AND :fim GROUP BY al.itemEntrada.material.nome, al.itemEntrada.material.unidadeMedida.nome, al.itemEntrada.entrada.chegada")
	List<RelatorioEntradaSaidaDTO> findByEntradaMaterialSetorEData(@Param("setor") Setor setor, @Param("inicio") Date inicio,
                                                                   @Param("fim") Date fim);

	@Query("SELECT new ufc.npi.clinicas.model.view.RelatorioEntradaSaidaDTO(al.itemEntrada.material.nome, al.itemEntrada.material.unidadeMedida.nome, SUM(al.quantidade), al.itemEntrada.entrada.chegada) FROM AlocacaoItemSetor al WHERE al.itemEntrada.entrada.statusEntrada = 'FINALIZADA' AND al.itemEntrada.entrada.chegada BETWEEN :inicio AND :fim GROUP BY al.itemEntrada.material.nome, al.itemEntrada.material.unidadeMedida.nome, al.itemEntrada.entrada.chegada")
	List<RelatorioEntradaSaidaDTO> findByEntradaMaterialEData(@Param("inicio") Date inicio, @Param("fim") Date fim);

}
