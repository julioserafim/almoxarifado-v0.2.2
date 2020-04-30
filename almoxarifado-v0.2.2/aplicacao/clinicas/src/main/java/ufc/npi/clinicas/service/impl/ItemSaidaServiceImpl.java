package ufc.npi.clinicas.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.ItemSaida;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.SaidaMaterial;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.view.RelatorioEntradaSaidaDTO;
import ufc.npi.clinicas.repository.ItemSaidaRepository;
import ufc.npi.clinicas.service.ItemSaidaService;
import ufc.npi.clinicas.util.Constants;

@Named
public class ItemSaidaServiceImpl implements ItemSaidaService {

	@Inject
	@Autowired
	private ItemSaidaRepository itemSaidaRepository;

	@Override
	public boolean existePorSaidaMaterialEMaterialELote(SaidaMaterial saidaMaterial, Material material, String lote) {
		List<ItemSaida> itens = this.itemSaidaRepository.findBySaidaMaterialAndMaterialAndLote(saidaMaterial, material,
				lote);
		return itens != null && !itens.isEmpty();

	}

	@Override
	public ItemSaida adicionar(ItemSaida itemSaida) {
		this.itemSaidaRepository.save(itemSaida);
		return itemSaida;
	}

	@Override
	public boolean excluirItemSaidaMaterial(Long idItemSaidaMaterial) {

		itemSaidaRepository.delete(idItemSaidaMaterial);

		return true;
	}

	@Override
	public ItemSaida buscarPorId(Long idItemSaidaMaterial) {
		return itemSaidaRepository.getById(idItemSaidaMaterial);

	}

	public List<ItemSaida> listarPorSaidaMaterial(SaidaMaterial saidaMaterial) {
		return this.itemSaidaRepository.findBySaidaMaterial(saidaMaterial);
	}

	@Override
	public List<RelatorioEntradaSaidaDTO> listarPorSetorEData(Setor setor, Date inicio, Date fim) throws ClinicasException{
		
		if(inicio.after(fim)){
			throw new ClinicasException(Constants.DATA_INICIO_MAIOR_FIM);
		}

		if (null != setor) {
			return itemSaidaRepository.findBySaidaMaterialSetorEData(setor, inicio, fim);
		} else {
			return itemSaidaRepository.findBySaidaMaterialEData(inicio, fim);
		}
	}
	
	@Override
	public List<RelatorioEntradaSaidaDTO> getMediaSaidasPorPeriodo(Setor setor, Date inicio, Date fim) throws ClinicasException{
		/*List<RelatorioEntradaSaidaDTO> saidas;

		if(inicio.after(fim)){
			throw new ClinicasException(Constants.DATA_INICIO_MAIOR_FIM);
		}

		Calendar calendarFim = Calendar.getInstance();
		Calendar calendarInicio = Calendar.getInstance();
		
		calendarFim.setTime(fim);
		calendarInicio.setTime(inicio);
		
		int diaFim = calendarFim.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendarFim.set(Calendar.DAY_OF_MONTH, diaFim);
		
		fim = calendarFim.getTime();
		
		if (null != setor) {
			saidas = itemSaidaRepository.findBySaidaMaterialSetorEData(setor, inicio, fim);
		} else {
			saidas = itemSaidaRepository.findBySaidaMaterialEData(inicio, fim);
		}
		
		System.out.println(fim.toString());
		System.out.println(diaFim);

		int m1 = calendarInicio.get(calendarInicio.YEAR) * 12 + calendarInicio.get(calendarInicio.MONTH);
		int m2 = calendarFim.get(calendarFim.YEAR) * 12 + calendarFim.get(calendarFim.MONTH);

		float periodo = m2 - m1 + 1;	

		for(RelatorioEntradaSaidaDTO saida : saidas){

			float media = (saida.getSomaQuantidades() / periodo);
			saida.setMediaQuantidadePorPeriodo(media);

		}

		return saidas;*/
		return null;
	}

	public List<RelatorioEntradaSaidaDTO> getMediaSaidasPorSemestre(Setor setor, Integer semestreInicio, Integer semestreFim, Date anoInicio, Date anoFim) throws ClinicasException{

		/*List<ItemSaidaView> saidas;

		Calendar calendarFim = Calendar.getInstance();
		Calendar calendarInicio = Calendar.getInstance();
		
		calendarFim.setTime(anoFim);
		calendarInicio.setTime(anoInicio);
		
		if(anoInicio.after(anoFim)){
			throw new ClinicasException(Constants.DATA_INICIO_MAIOR_FIM);
		}

		if(semestreInicio == 2){
			calendarInicio.set(calendarInicio.MONTH, 7);
		}
		if(semestreFim == 2){
			calendarFim.set(calendarInicio.MONTH, 12);
			calendarFim.set(calendarInicio.DAY_OF_MONTH, 31);
		}else{
			calendarFim.set(calendarFim.MONTH, 6);
			calendarFim.set(calendarFim.DAY_OF_MONTH, 30);
		}
		
		anoInicio = calendarInicio.getTime();
		anoFim = calendarFim.getTime();

		if (null != setor) {
			saidas = itemSaidaRepository.findBySaidaMaterialSetorEData(setor, anoInicio, anoFim);
		} else {
			saidas = itemSaidaRepository.findBySaidaMaterialEData(anoInicio, anoFim);
		}

		int m1 = calendarInicio.get(calendarInicio.YEAR) * 12 + calendarInicio.get(calendarInicio.MONTH);
		int m2 = calendarFim.get(calendarFim.YEAR) * 12 + calendarFim.get(calendarFim.MONTH);

		float periodo = m2 - m1;	

		float periodoSemestral = periodo / 6;

		for(ItemSaidaView saida : saidas){

			float mediaMensal = (saida.getSomaQuantidades() / periodo);
			float mediaSemestre = (saida.getSomaQuantidades() / periodoSemestral);
	
			saida.setMediaQuantidadePorPeriodo(mediaMensal);
			saida.setMediaQuantidadePorSemestre(mediaSemestre);
		}

		return saidas;*/
		return null;
	}

}
