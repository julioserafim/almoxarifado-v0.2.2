package ufc.npi.clinicas.service;

import java.util.Date;
import java.util.List;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.ItemSaida;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.SaidaMaterial;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.view.RelatorioEntradaSaidaDTO;

public interface ItemSaidaService {

	boolean existePorSaidaMaterialEMaterialELote(SaidaMaterial saidaMaterial, Material material, String lote);
	ItemSaida adicionar(ItemSaida itemSaida);

	void excluirItemSaidaMaterial(Long idItemSaidaMaterial) throws ClinicasException;
	ItemSaida buscarPorId(Long idItemSaidaMaterial);

	List<ItemSaida> listarPorSaidaMaterial(SaidaMaterial saidaMaterial);

	List<RelatorioEntradaSaidaDTO> listarPorSetorEData(Setor setor, Date inicio, Date fim) throws ClinicasException;

	List<RelatorioEntradaSaidaDTO> getMediaSaidasPorPeriodo (Setor setor, Date inicio, Date fim) throws ClinicasException;

	List<RelatorioEntradaSaidaDTO> getMediaSaidasPorSemestre(Setor setor, Integer semestreInicio, Integer semestreFim, Date anoInicio, Date anoFim) throws ClinicasException;

}
