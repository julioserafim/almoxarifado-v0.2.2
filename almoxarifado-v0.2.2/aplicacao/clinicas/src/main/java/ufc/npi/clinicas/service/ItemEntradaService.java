package ufc.npi.clinicas.service;

import java.util.Date;
import java.util.List;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.ItemEntrada;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.view.RelatorioEntradaSaidaDTO;

public interface ItemEntradaService {
	
	ItemEntrada buscarPorId(Integer idItemEntrada);
	boolean verificarFinalizacaoAlocacao(List<ItemEntrada> itensEntrada);
	List<RelatorioEntradaSaidaDTO> listarPorSetorEData(Setor setor, Date inicio, Date fim) throws ClinicasException;
	
}
