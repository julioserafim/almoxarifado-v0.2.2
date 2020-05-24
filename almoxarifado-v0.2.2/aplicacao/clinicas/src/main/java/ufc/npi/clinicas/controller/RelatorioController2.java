package ufc.npi.clinicas.controller;


import javax.inject.Inject;
import ufc.npi.clinicas.service.EstoqueSetorService;
import ufc.npi.clinicas.service.SaidaMaterialService;
import ufc.npi.clinicas.service.EntradaService;
import ufc.npi.clinicas.service.SetorService;
import ufc.npi.clinicas.service.ItemSaidaService;
import ufc.npi.clinicas.service.ItemEntradaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import ufc.npi.clinicas.model.Setor;
import org.springframework.web.bind.annotation.PostMapping;
import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.util.alert.AlertSet;
import org.springframework.web.bind.annotation.GetMapping;

public class RelatorioController2 {
	private EstoqueSetorService estoqueSetorService;
	private SaidaMaterialService saidaService;
	private EntradaService entradaService;
	private SetorService setorService;
	private ItemSaidaService itemSaidaService;
	private ItemEntradaService itemEntradaService;

	@RequestMapping(value = "/estoqueEntradaSaidaSetor", method = RequestMethod.POST)
	public ModelAndView estoqueEntradaSaidaSetor(
			@RequestParam(value = "inicio", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date inicio,
			@RequestParam(value = "fim", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date fim,
			@RequestParam(value = "idSetor", required = false) Integer idSetor, ModelAndView mav) {
		if (idSetor == -1) {
			mav.setViewName("redirect:/relatorio/estoqueEntradaSaidaSetor");
			return mav;
		}
		Setor setor = setorService.buscarPorId(idSetor);
		if (setor != null) {
			mav.addObject("entradas", entradaService.buscarPorSetorEData(setor, inicio, fim))
					.addObject("estoques", estoqueSetorService.buscarPorSetor(idSetor))
					.addObject("saidas", saidaService.listarPorOrigemEData(setor, inicio, fim))
					.addObject("inicio", inicio).addObject("fim", fim).addObject("setor", setor)
					.setViewName("relatorio/estoque_entrada_saida_setor");
		}
		mav.addObject("setores", setorService.listar());
		return mav;
	}

	@PostMapping(value = "/saidaPeriodoMaterial")
	public ModelAndView getSaida(@DateTimeFormat(pattern = "dd/MM/yyyy") Date inicio,
			@DateTimeFormat(pattern = "dd/MM/yyyy") Date fim, @RequestParam("destino") Setor setor) {
		ModelAndView modelAndView = new ModelAndView("relatorio/saida_periodo_material")
				.addObject("setores", setorService.listar()).addObject("dataInicio", inicio).addObject("dataFim", fim)
				.addObject("setorSelecionado", setor).addObject("busca", true);
		try {
			modelAndView.addObject("saidas", itemSaidaService.listarPorSetorEData(setor, inicio, fim));
		} catch (ClinicasException e) {
			modelAndView.addObject("alertas", new AlertSet().withLongWarning(e.getMessage()));
		}
		return modelAndView;
	}

	@PostMapping(value = "/mediaHistoricaMateriais")
	public ModelAndView getMediaHistorica(@DateTimeFormat(pattern = "MM/yyyy") Date inicio,
			@DateTimeFormat(pattern = "MM/yyyy") Date fim, @DateTimeFormat(pattern = "yyyy") Date anoInicio,
			@DateTimeFormat(pattern = "yyyy") Date anoFim, @RequestParam("origem") Setor setor,
			@RequestParam("tipo_busca") String tipoBusca, @RequestParam("semestreInicio") Integer semestreInicio,
			@RequestParam("semestreFim") Integer semestreFim) {
		Boolean buscarPeriodo = false;
		if (tipoBusca.equals("busca_periodo"))
			buscarPeriodo = true;
		ModelAndView modelAndView = new ModelAndView("relatorio/media_historica_materiais")
				.addObject("setores", setorService.listar()).addObject("dataInicio", inicio).addObject("dataFim", fim)
				.addObject("anoInicio", anoInicio).addObject("anoFim", anoFim).addObject("setorSelecionado", setor)
				.addObject("buscarPeriodo", buscarPeriodo).addObject("busca", true);
		try {
			if (buscarPeriodo) {
				modelAndView.addObject("saidas", itemSaidaService.getMediaSaidasPorPeriodo(setor, inicio, fim));
			} else {
				modelAndView.addObject("saidas", itemSaidaService.getMediaSaidasPorSemestre(setor, semestreInicio,
						semestreFim, anoInicio, anoFim));
			}
		} catch (ClinicasException e) {
			modelAndView.addObject("alertas", new AlertSet().withLongWarning(e.getMessage()));
		}
		return modelAndView;
	}

	@PostMapping(value = "/entradaPeriodoMaterial")
	public ModelAndView getEntrada(@DateTimeFormat(pattern = "dd/MM/yyyy") Date inicio,
			@DateTimeFormat(pattern = "dd/MM/yyyy") Date fim, @RequestParam("origem") Setor setor) {
		ModelAndView modelAndView = new ModelAndView("relatorio/entrada_periodo_material")
				.addObject("setores", setorService.listar()).addObject("dataInicio", inicio).addObject("dataFim", fim)
				.addObject("setorSelecionado", setor).addObject("busca", true);
		try {
			modelAndView.addObject("entradas", itemEntradaService.listarPorSetorEData(setor, inicio, fim));
		} catch (ClinicasException e) {
			modelAndView.addObject("alertas", new AlertSet().withLongWarning(e.getMessage()));
		}
		return modelAndView;
	}

	@GetMapping(value = "/estoqueEntradaSaidaSetor")
	public ModelAndView estoqueEntradaSaida(ModelAndView mav) {
		mav.setViewName("relatorio/estoque_entrada_saida_setor");
		mav.addObject("setores", setorService.listar());
		mav.addObject("setor", null);
		return mav;
	}

	@GetMapping(value = "/saidaPeriodoMaterial")
	public ModelAndView formSaidaPeriodoMaterial() {
		ModelAndView modelAndView = new ModelAndView("relatorio/saida_periodo_material")
				.addObject("setores", setorService.listar()).addObject("setorSelecionado", new Setor())
				.addObject("busca", false);
		return modelAndView;
	}

	@GetMapping(value = "/entradaPeriodoMaterial")
	public ModelAndView formEntradaPeriodoMaterial() {
		ModelAndView modelAndView = new ModelAndView("relatorio/entrada_periodo_material")
				.addObject("setores", setorService.listar()).addObject("setorSelecionado", new Setor())
				.addObject("busca", false);
		return modelAndView;
	}

	@GetMapping(value = "/mediaHistoricaMateriais")
	public ModelAndView formMediaHistorica() {
		ModelAndView modelAndView = new ModelAndView("relatorio/media_historica_materiais")
				.addObject("setores", setorService.listar()).addObject("setorSelecionado", new Setor())
				.addObject("buscarPeriodo", true).addObject("busca", false);
		return modelAndView;
	}
}