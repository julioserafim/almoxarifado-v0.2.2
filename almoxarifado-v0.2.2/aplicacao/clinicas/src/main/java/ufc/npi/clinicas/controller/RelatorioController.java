package ufc.npi.clinicas.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.EstoqueLote;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.service.EntradaService;
import ufc.npi.clinicas.service.EstoqueLoteService;
import ufc.npi.clinicas.service.EstoqueSetorService;
import ufc.npi.clinicas.service.ItemEntradaService;
import ufc.npi.clinicas.service.ItemSaidaService;
import ufc.npi.clinicas.service.MaterialService;
import ufc.npi.clinicas.service.SaidaMaterialService;
import ufc.npi.clinicas.service.SetorService;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.alert.AlertSet;
import ufc.npi.clinicas.util.api.Response;

@RestController
@RequestMapping("relatorio")
public class RelatorioController {

	@Inject
	private EstoqueSetorService estoqueSetorService;
	@Inject
	private MaterialService materialService;
	@Inject
	private EstoqueLoteService estoqueLoteService;
	@Inject
	private SetorService setorService;
	@Inject
	private SaidaMaterialService saidaService;
	@Inject
	private EntradaService entradaService;
	
	@Inject
	private ItemSaidaService itemSaidaService;
	
	@Inject
	private ItemEntradaService itemEntradaService;

	
	@GetMapping(value="/estoqueEntradaSaidaSetor")
	public ModelAndView estoqueEntradaSaida(ModelAndView mav){
		mav.setViewName("relatorio/estoque_entrada_saida_setor");
		mav.addObject("setores", setorService.listar());
		mav.addObject("setor", null);
		return mav;
	}
	
	@RequestMapping(value = "/estoqueEntradaSaidaSetor", method = RequestMethod.POST)
	public ModelAndView estoqueEntradaSaidaSetor(@RequestParam(value="inicio",required=false) @DateTimeFormat(pattern="dd/MM/yyyy")  Date inicio, 
			@RequestParam(value="fim",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date fim, 
			@RequestParam(value="idSetor",required=false) Integer idSetor, ModelAndView mav) {
		if (idSetor == -1){
			mav.setViewName("redirect:/relatorio/estoqueEntradaSaidaSetor");
			return mav;
		}
		Setor setor = setorService.buscarPorId(idSetor);
		
		if(setor != null){
			mav.addObject("entradas", entradaService.buscarPorSetorEData(setor, inicio, fim))
				.addObject("estoques", estoqueSetorService.buscarPorSetor(idSetor))
				.addObject("saidas", saidaService.listarPorOrigemEData(setor,inicio,fim))
				.addObject("inicio",inicio)
				.addObject("fim",fim)
				.addObject("setor", setor)
				.setViewName("relatorio/estoque_entrada_saida_setor");
		}
		
		mav.addObject("setores", setorService.listar());
		return mav;		
	}
	
	@GetMapping(value = "/listar")
	public ModelAndView relatorios(){
		ModelAndView modelAndView = new ModelAndView("relatorio/relatorios");
		return modelAndView;
	}
	
	@GetMapping(value = "/materiaisValidade")
	public ModelAndView formMaterialVencer(){
		ModelAndView modelAndView = new ModelAndView("relatorio/materiais_validade")
				.addObject("estoqueSetores", estoqueLoteService.getByDataVencimentoMaterialEmEstoque(new Date()))
				.addObject("data",new Date() );
		return modelAndView;
	}
	
	@PostMapping(value = "/materiaisValidade")
	public ModelAndView getEstoqueGeral(@DateTimeFormat(pattern="dd/MM/yyyy") Date data){
		ModelAndView modelAndView = new ModelAndView("relatorio/materiais_validade");
		modelAndView.addObject("data",data);
		
	    //Se a data for maior ou igual a data atual
		Date atual = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyy");
		if( !fmt.format(data).equals(fmt.format(atual)) && data.before(atual)  ){
			modelAndView.addObject("alertas", new AlertSet().withInfo(Constants.SAIDA_NAO_ENCONTRADA));
			return modelAndView;
		}	
		
		modelAndView.addObject("estoqueSetores", estoqueLoteService.getByDataVencimentoMaterialEmEstoque(data));
		return modelAndView;	
	}
	
	@GetMapping(value = "/materiais_vencidos")
	public ModelAndView materiaisVencidos(){
		ModelAndView modelAndView = new ModelAndView("relatorio/materiais_vencidos");
		modelAndView.addObject("estoqueSetores", estoqueLoteService.buscarMateriaisVencidos());
		return modelAndView;
	}

	@GetMapping(value = "/saidaPeriodoMaterial")
	public ModelAndView formSaidaPeriodoMaterial(){
		ModelAndView modelAndView = new ModelAndView("relatorio/saida_periodo_material")
				.addObject("setores", setorService.listar())
				.addObject("setorSelecionado" , new Setor())
				.addObject("busca", false);
		return modelAndView;
	}
	
	
	@PostMapping(value = "/saidaPeriodoMaterial")
	public ModelAndView getSaida(@DateTimeFormat(pattern="dd/MM/yyyy") Date inicio, @DateTimeFormat(pattern="dd/MM/yyyy") Date fim, @RequestParam("destino") Setor setor ){	
		ModelAndView modelAndView = new ModelAndView("relatorio/saida_periodo_material")
				.addObject("setores", setorService.listar())
				.addObject("dataInicio", inicio)
				.addObject("dataFim", fim)
				.addObject("setorSelecionado" , setor)
				.addObject("busca", true);
		try {
			modelAndView.addObject("saidas", itemSaidaService.listarPorSetorEData(setor, inicio, fim));
		} catch (ClinicasException e) {
			modelAndView.addObject("alertas", new AlertSet().withLongWarning(e.getMessage()));
		}
		return modelAndView;
		
	}

	
	@GetMapping(value = {"/materiais_sem_estoque", "/materiais_sem_estoque/incluir_vencidos={incluirVencidos}"})
	public ModelAndView materiaisSemEstoque(ModelAndView mav, @PathVariable Optional<Boolean> incluirVencidos){
		mav.setViewName("relatorio/materiais_sem_estoque");
		mav.addObject("incluir_vencidos", adicionarVencidos(incluirVencidos));
		mav.addObject("materiais", materialService.buscarMateriaisEmFalta(adicionarVencidos(incluirVencidos)));
		return mav;
	}
	
	@GetMapping(value = "/entradaPeriodoMaterial")
	public ModelAndView formEntradaPeriodoMaterial(){
		ModelAndView modelAndView = new ModelAndView("relatorio/entrada_periodo_material")
				.addObject("setores", setorService.listar())
				.addObject("setorSelecionado" , new Setor())
				.addObject("busca", false);
		return modelAndView;
	}
	
	
	@PostMapping(value = "/entradaPeriodoMaterial")
	public ModelAndView getEntrada(@DateTimeFormat(pattern="dd/MM/yyyy") Date inicio, @DateTimeFormat(pattern="dd/MM/yyyy") Date fim, @RequestParam("origem") Setor setor ){
		ModelAndView modelAndView = new ModelAndView("relatorio/entrada_periodo_material")
				.addObject("setores", setorService.listar())
				.addObject("dataInicio", inicio)
				.addObject("dataFim", fim)
				.addObject("setorSelecionado" , setor)
				.addObject("busca", true);
		try {
			modelAndView.addObject("entradas", itemEntradaService.listarPorSetorEData(setor, inicio, fim));
		} catch (ClinicasException e) {
			modelAndView.addObject("alertas", new AlertSet().withLongWarning(e.getMessage()));
		}
		
		return modelAndView;
	}
	
	@GetMapping(value = "/api/getLotes/{idMaterial}")
	public Response getEstoqueGeral(@PathVariable("idMaterial") Integer idMaterial){
		Material material = materialService.buscarPorId(idMaterial);
		if (material == null) {
			return new Response().
					withFailStatus().
					withErrorMessage(Constants.MATERIAL_ERRO_NAO_EXISTE);
		}
		List<EstoqueLote> estoqueLote = estoqueLoteService.getByMaterial(material);
		return new Response().withObject(estoqueLote);
	}
	
	@GetMapping(value = {"/relatorioGeralMateriais",
							"/relatorioGeralMateriais/incluir_sem_estoque={incluirSemEstoque}"})
	public ModelAndView getTodosMateriais(ModelAndView mav, @PathVariable Optional<Boolean> incluirSemEstoque){
		
		mav.setViewName("relatorio/relatorio_geral_materiais");
		List<Material> materiais;
		if(incluirSemEstoque.isPresent() && incluirSemEstoque.get()){
			materiais = materialService.listar();
			mav.addObject("incluir_sem_estoque", true);
			mav.addObject("materiais", materiais);
		}else{
			materiais = materialService.buscarMateriaisEmEstoque();
			mav.addObject("incluir_sem_estoque", false);
			mav.addObject("materiais", materiais);
		}
		return mav;
	}
	
	@GetMapping(value = "/mediaHistoricaMateriais")
	public ModelAndView formMediaHistorica(){
		ModelAndView modelAndView = new ModelAndView("relatorio/media_historica_materiais")
				.addObject("setores", setorService.listar())
				.addObject("setorSelecionado" , new Setor())
				.addObject("buscarPeriodo", true)
				.addObject("busca", false);
		return modelAndView;
	}

	@PostMapping(value = "/mediaHistoricaMateriais")
	public ModelAndView getMediaHistorica(
			@DateTimeFormat(pattern="MM/yyyy") Date inicio, 
			@DateTimeFormat(pattern="MM/yyyy") Date fim,
			@DateTimeFormat(pattern="yyyy") Date anoInicio,
			@DateTimeFormat(pattern="yyyy") Date anoFim,
			@RequestParam("origem") Setor setor,
			@RequestParam("tipo_busca") String tipoBusca,
			@RequestParam("semestreInicio") Integer semestreInicio,
			@RequestParam("semestreFim") Integer semestreFim
			){

		Boolean buscarPeriodo = false;
		if (tipoBusca.equals("busca_periodo"))
			buscarPeriodo = true;

		ModelAndView modelAndView = new ModelAndView("relatorio/media_historica_materiais")
				.addObject("setores", setorService.listar())
				.addObject("dataInicio", inicio)
				.addObject("dataFim", fim)
				.addObject("anoInicio", anoInicio)
				.addObject("anoFim", anoFim)
				.addObject("setorSelecionado", setor)
				.addObject("buscarPeriodo", buscarPeriodo)
				.addObject("busca", true);

		try {
			if(buscarPeriodo){
				modelAndView.addObject("saidas", itemSaidaService.getMediaSaidasPorPeriodo(setor, inicio, fim));
			}
			else{
				modelAndView.addObject("saidas", itemSaidaService.getMediaSaidasPorSemestre(setor, semestreInicio, semestreFim, anoInicio, anoFim));
			}
		
		} catch (ClinicasException e) {
			modelAndView.addObject("alertas", new AlertSet().withLongWarning(e.getMessage()));
		}

		return modelAndView;
	}
	
	@SuppressWarnings("unused")
	private boolean adicionarVencidos(Optional<Boolean> incluirVencidos) {
		return incluirVencidos.isPresent() && incluirVencidos.get();
	}
}
