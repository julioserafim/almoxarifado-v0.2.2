package ufc.npi.clinicas.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.ItemEntrada;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.view.RelatorioEntradaSaidaDTO;
import ufc.npi.clinicas.repository.ItemEntradaRepository;
import ufc.npi.clinicas.service.ItemEntradaService;
import ufc.npi.clinicas.util.Constants;

@Named
public class ItemEntradaServiceImpl implements ItemEntradaService {

	@Inject
	private ItemEntradaRepository itemEntradaRepository;

	@Override
	public ItemEntrada buscarPorId(Integer idItemEntrada) {
		return this.itemEntradaRepository.findOne(idItemEntrada);
	}

	@Override
	public boolean verificarFinalizacaoAlocacao(List<ItemEntrada> itensEntrada) {

		boolean verificado = true;

		for (ItemEntrada itemEntrada : itensEntrada) {
			if (!(itemEntrada.getQuantidade().equals(itemEntrada.totalItensAlocados()))) {
				verificado = false;
			}
		}
		return verificado;
	}

	@Override
	public List<RelatorioEntradaSaidaDTO> listarPorSetorEData(Setor setor, Date inicio, Date fim) throws ClinicasException {
		if (inicio.after(fim)) {
			throw new ClinicasException(Constants.DATA_INICIO_MAIOR_FIM);
		}

		List<RelatorioEntradaSaidaDTO> resultado = new ArrayList<>();
	
		if (null != setor) {
			resultado =  itemEntradaRepository.findByEntradaMaterialSetorEData(setor, inicio, fim);
		} else {
			resultado =  itemEntradaRepository.findByEntradaMaterialEData(inicio, fim);
		}
		
		return resultado;
	}

}
