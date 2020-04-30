package ufc.npi.clinicas.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.*;
import ufc.npi.clinicas.service.*;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.alert.AlertSet;
import ufc.npi.clinicas.util.api.Response;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("entrada")
public class EntradaController {

	@Inject
	private EntradaService entradaService;

	@Inject
	private FornecedorService fornecedorService;

	@Inject
	private MaterialService materialService;

	@Inject
	private UnidadeDeMedidaService unidadeDeMedidaService;

	@Inject
	private GrupoMaterialService grupoMaterialService;

	@Inject
	private AlocacaoItemSetorService alocacaoItemSetorService;

	@Inject
	private ItemEntradaService itemEntradaService;

	@Inject
	private SetorService setorService;

	@GetMapping(value = "/adicionar")
	public ModelAndView adicionar(ModelAndView mav) {
		Entrada entrada = new Entrada();
		mav.addObject("fornecedores", fornecedorService.listar());
		mav.addObject("setores", setorService.listar());
		entrada.setChegada(new Date());
		mav.addObject("entrada", entrada);
		mav.addObject("novoFornecedor", new Fornecedor());
		mav.addObject("tipoEntrada", TipoEntrada.values());
		mav.setViewName("entrada/formulario_entrada");
		return mav;
	}

	@PostMapping(value = "/adicionar")
	public ModelAndView adicionarEntrada(ModelAndView mav, RedirectAttributes redirectAttributes, Entrada entrada,
			Authentication auth) {
		try {
			entradaService.adicionar(entrada, auth.getName());
			mav.setViewName(String.format("redirect:/entrada/%1d/incluirMateriais/", entrada.getId()));
		} catch (ClinicasException e) {
			redirectAttributes.addFlashAttribute("alertas", new AlertSet().withLongError(e.getMessage()));
			mav.setViewName("redirect:/entrada/adicionar");
		}
		return mav;
	}

	@GetMapping(value = "/{id}/editar")
	public ModelAndView editar(ModelAndView model, @PathVariable("id") Integer idEntrada) {

		Entrada entrada = entradaService.buscarPorId(idEntrada);
		model.addObject("entrada", entrada);
		model.addObject("fornecedores", fornecedorService.listar());
		model.addObject("setores", setorService.listar());
		model.addObject("novoFornecedor", new Fornecedor());
		model.addObject("tipoEntrada", TipoEntrada.values());
		model.addObject("action", "editar");
		model.setViewName("entrada/formulario_entrada");
		return model;
	}

	@PostMapping(value = "/editar")
	public ModelAndView editar(ModelAndView model, RedirectAttributes redirectAttributes, Entrada entrada) {
		try {
			entradaService.editar(entrada);
			model.setViewName(String.format("redirect:/entrada/%1d/visualizar", entrada.getId()));
			redirectAttributes.addFlashAttribute("alertas", new AlertSet().withLongSuccess(Constants.ENTRADA_EDITAR_SUCESSO));
		} catch (ClinicasException e) {
			redirectAttributes.addFlashAttribute("alertas", new AlertSet().withLongError(e.getMessage()));
			model.setViewName("redirect:/entrada/listar");
		}
		
		return model;
	}

	@RequestMapping(value = "/{idEntrada}/excluir", method = RequestMethod.GET)
	public ModelAndView excluirSaida(ModelAndView mav, RedirectAttributes redirectAttributes,
			@PathVariable("idEntrada") Integer idEntrada) {
		Entrada entrada = entradaService.buscarPorId(idEntrada);
		if (entrada.getItens().isEmpty()) {
			entradaService.excluir(entrada);
			redirectAttributes.addFlashAttribute("alertas",
					new AlertSet().withSuccess(Constants.ENTRADA_EXCLUIDA_SUCESSO));
			mav.setViewName("redirect:/entrada/listar");
		} else {
			mav.setViewName(String.format("redirect:/entrada/%1d/incluirMateriais", entrada.getId()));
			redirectAttributes.addFlashAttribute("alertas",
					new AlertSet().withLongError(Constants.ENTRADA_EXCLUIDA_ERRO));

		}
		return mav;
	}

	@GetMapping(value = "/{idEntrada}/incluirMateriais")
	public ModelAndView incluirMateriais(ModelAndView mav, @PathVariable("idEntrada") Integer idEntrada) {
		Entrada entrada = entradaService.buscarPorId(idEntrada);

		if (entrada.getStatusEntrada().equals(StatusEntrada.FINALIZADA)) {
			mav.setViewName(String.format("redirect:/entrada/%1d/visualizar", entrada.getId()));
			return mav;
		}

		mav.addObject("entrada", entrada);
		mav.addObject("materiais", materialService.listar());
		mav.addObject("material", new Material());
		mav.addObject("unidadesMedida", unidadeDeMedidaService.listar());
		mav.addObject("gruposMaterial", grupoMaterialService.listar());
		mav.addObject("itemEntrada", new ItemEntrada());
		mav.setViewName("entrada/formulario_incluir_itens_entrada");
		return mav;
	}

	@RequestMapping(value = "/{idEntrada}/visualizar", method = RequestMethod.GET)
	public ModelAndView visualizar(@PathVariable("idEntrada") Integer idEntrada) {
		ModelAndView mav = new ModelAndView();
		Entrada entrada = entradaService.buscarPorId(idEntrada);

		if (entrada == null) {
			mav.setViewName("redirect:/entrada/listar");
		} else if (entrada.getStatusEntrada().equals(StatusEntrada.PENDENTE_INCLUSAO)) {
			mav.setViewName(String.format("redirect:/entrada/%1d/incluirMateriais/", entrada.getId()));
		} else if (entrada.getStatusEntrada().equals(StatusEntrada.PENDENTE_ALOCACAO)) {
			mav.setViewName(String.format("redirect:/entrada/%1d/alocarItens/", entrada.getId()));
		} else {
			mav.addObject("entrada", entrada);
			mav.setViewName("entrada/visualizar_entrada");
		}
		return mav;
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public ModelAndView listar(ModelAndView mav) {
		mav.addObject("entradas", entradaService.listar());
		mav.setViewName("entrada/listar_entradas");
		return mav;
	}

	/* REST API */

	@PostMapping(value = "/api/incluirMaterial/{idEntrada}")
	public Response incluirMaterial(ItemEntrada itemEntrada, @PathVariable("idEntrada") Integer idEntrada) {

		Entrada entrada = entradaService.buscarPorId(idEntrada);

		itemEntrada.setEntrada(entrada);
		try {
			entradaService.adicionarItemEntrada(itemEntrada);

			return new Response().withObject(itemEntrada).withInfoMessage("Material adicionado");
		} catch (ClinicasException e) {
			return new Response().withFailStatus().withErrorMessage(e.getMessage());
		}
	}

	@GetMapping("/api/{idEntrada}/removeritem/{idItem}")
	public Response removerMaterial(@PathVariable("idEntrada") Integer idEntrada,
			@PathVariable("idItem") Integer idItem) {
		Entrada entrada = entradaService.buscarPorId(idEntrada);
		ItemEntrada item = entradaService.buscarItemPorId(idItem);
		if (entrada == null || item == null)
			return new Response().withFailStatus()
					.withErrorMessage(Constants.ENTRADA_INCLUIR_MATERIAIS_EXCLUIR_ITEM_ERRO);
		if (entrada.getStatusEntrada().equals(StatusEntrada.PENDENTE_ALOCACAO))
			return new Response().withFailStatus()
					.withErrorMessage(Constants.ENTRADA_INCLUIR_MATERIAIS_EXCLUIR_ITEM_ERRO_NOTA_FINALIZADA);

		entradaService.removerItem(entrada, item);
		return new Response();

	}

	@PostMapping(value = "/api/listarItensMaterial/{idEntrada}")
	public Response apiListarItensMaterial(@PathVariable("idEntrada") Integer idEntrada) {
		Entrada entrada = entradaService.buscarPorId(idEntrada);
		return new Response().withObject(entrada.getItens());
	}

	@PostMapping(value = "/api/finalizarEntrada/{idEntrada}")
	public Response finalizarEntrada(@PathVariable("idEntrada") Integer idEntrada) throws ClinicasException {
		Entrada entrada = entradaService.buscarPorId(idEntrada);

		entrada.setStatusEntrada(StatusEntrada.PENDENTE_ALOCACAO);

		entradaService.editar(entrada);
		return new Response().withSuccessMessage(Constants.ENTRADA_INCLUIR_MATERIAIS_FINALIZADA);
	}

	@PostMapping(value = "listarItensMaterial/{idEntrada}")
	public List<ItemEntrada> listarItensMaterial(@PathVariable("idEntrada") Integer idEntrada) {
		Entrada entrada = entradaService.buscarPorId(idEntrada);
		return entrada.getItens();
	}

	@PostMapping(value = "/api/alocarItens")
	public Response formAlocarItensSetor(AlocacaoItemSetor alocacaoItemSetor) {

		ItemEntrada itemEntrada = this.itemEntradaService.buscarPorId(alocacaoItemSetor.getItemEntrada().getId());

		Integer itensAlocados = this.alocacaoItemSetorService
				.somaAlocacaoItemSetorPorItemEntrada(alocacaoItemSetor.getItemEntrada());
		if (alocacaoItemSetor.getSetor() == null) {
			return new Response().withFailStatus().withErrorMessage(Constants.ENTRADA_ALOCAR_MATERIAIS_ERRO_SETOR);
		} else if (alocacaoItemSetor.getQuantidade() == null) {
			return new Response().withFailStatus()
					.withErrorMessage(Constants.ENTRADA_ALOCAR_MATERIAIS_ERRO_QUANTIDADE_NULA);
		} else if (alocacaoItemSetor.getQuantidade() <= 0) {
			return new Response().withFailStatus()
					.withErrorMessage(Constants.ENTRADA_ALOCAR_MATERIAIS_ERRO_QUANTIDADE_MAIOR_ZERO);
		} else if (itensAlocados == itemEntrada.getQuantidade()) {
			return new Response().withFailStatus()
					.withErrorMessage(Constants.ENTRADA_ALOCAR_MATERIAIS_ERRO_TODOS_ITENS_ALOCADOS);
		} else if ((alocacaoItemSetor.getQuantidade() + itensAlocados) > itemEntrada.getQuantidade()) {
			return new Response().withFailStatus()
					.withErrorMessage(Constants.ENTRADA_ALOCAR_MATERIAIS_ERRO_QUANTIDADE_INDISPONIVEL);
		} else {

			alocacaoItemSetor.setData(new Date());

			return new Response().withDoneStatus()
					.withObject(this.alocacaoItemSetorService.adicionar(alocacaoItemSetor))
					.withSuccessMessage(Constants.ENTRADA_ALOCAR_MATERIAS_SUCESSO_ADICIONADO);
		}
	}

	@GetMapping(value = "{idEntrada}/alocarItens")
	public ModelAndView alocarItensSetor(@PathVariable Integer idEntrada, RedirectAttributes redirectAttributes) {
		Entrada entrada = this.entradaService.buscarPorId(idEntrada);

		if (entrada.getStatusEntrada().equals(StatusEntrada.PENDENTE_INCLUSAO)) {
			redirectAttributes.addFlashAttribute("error", "A nota da entrada ainda não foi finalizada");
			return new ModelAndView("redirect:/entrada/incluirmateriais/" + entrada.getId());
		} else if (entrada.getStatusEntrada().equals(StatusEntrada.FINALIZADA)) {
			redirectAttributes.addFlashAttribute("error", "A alocação dos materias da entrada está finalizada");
			ModelAndView modelAndView = new ModelAndView(
					String.format("redirect:/entrada/%1d/visualizar", entrada.getId()));
			return modelAndView;
		} else {
			ModelAndView mav = new ModelAndView("entrada/formulario_alocar_itens_setores");
			mav.addObject("entrada", entrada);
			mav.addObject("setores", this.setorService.listar());
			return mav;
		}
	}

	@PostMapping(value = "/api/alocarItens/listar")
	public Response listarAlocacaoItemSetorPorItemEntrada(ItemEntrada item) {

		ItemEntrada itemEntrada = this.itemEntradaService.buscarPorId(item.getId());

		return new Response().withObject(this.alocacaoItemSetorService.listarPorItemEntrada(itemEntrada));

	}

	@GetMapping(value = "api/alocarItens/remover/{idItem}")
	@ResponseBody
	public boolean excluiAlocacaoItemDisciplina(@PathVariable Long idItem) {
		AlocacaoItemSetor alocacaoItemSetor = alocacaoItemSetorService.buscarPorId(idItem);
		StatusEntrada statusEntrada = alocacaoItemSetor.getItemEntrada().getEntrada().getStatusEntrada();

		if (statusEntrada.equals(StatusEntrada.PENDENTE_ALOCACAO) && alocacaoItemSetor != null) {
			return alocacaoItemSetorService.excluir(alocacaoItemSetor);
		} else {
			return false;
		}

	}

	@GetMapping(value = "/api/{idEntrada}/verificarFinalizacaoAlocacao")
	public Response verificarFinalizacaoAlocacao(@PathVariable Integer idEntrada) {
		Entrada entrada = this.entradaService.buscarPorId(idEntrada);
		return new Response().withObject(this.itemEntradaService.verificarFinalizacaoAlocacao(entrada.getItens()))
				.withSuccessMessage(Constants.ENTRADA_ALOCAR_MATERIAS_SUCESSO_VERIFICAR_FINALIZAÇAO);

	}

	@GetMapping("/{idEntrada}/finalizar_entrada")
	public ModelAndView finalizarEntradaFinal(@PathVariable Integer idEntrada) throws ClinicasException {
		ModelAndView modelAndView = new ModelAndView();
		Entrada entrada = this.entradaService.buscarPorId(idEntrada);
		if (entrada == null || entrada.getStatusEntrada() != StatusEntrada.PENDENTE_ALOCACAO) {
			modelAndView.setViewName("redirect:/entrada/listar");
			modelAndView.addObject("erro", Constants.ENTRADA_NAO_ENCONTRADA);
			return modelAndView;
		}

		if (!entradaService.finalizarAlocacaoEntrada(entrada)) {
			modelAndView.setViewName("redirect:/entrada/" + idEntrada + "/alocarItens");
			modelAndView.addObject("erro", Constants.ENTRADA_ALOCAR_COM_INCONSISTENCIA);
			return modelAndView;
		}

		entrada.setStatusEntrada(StatusEntrada.FINALIZADA);
		entradaService.editar(entrada);
		modelAndView.setViewName(String.format("redirect:/entrada/%1d/visualizar", entrada.getId()));
		modelAndView.addObject("sucesso", Constants.ENTRADA_ADICIONAR_SUCESSO);
		return modelAndView;
	}

	@PostMapping(value = "/api/buscarMaterialCadastrado/{nome}")
	public Response buscarMaterialCadastrado(@PathVariable("nome") String nome) {
		List<Material> materiais = materialService.buscarPorNomeOuCodigoBarrasOuCodigoInterno(nome);

		if (materiais == null || materiais.isEmpty()) {
			return new Response().withFailStatus().withErrorMessage(Constants.MATERIAL_ERRO_NAO_EXISTE);

		}
		return new Response().withDoneStatus().withObject(materiais);
	}
	
	@GetMapping(value = "/api/buscarAlocacoesPorItemEntrada")
	public Response buscarAlocacoesPorItemEntrada(@RequestParam Integer idItemEntrada) {
		ItemEntrada item = itemEntradaService.buscarPorId(idItemEntrada);
		List<AlocacaoItemSetor> alocacoes = alocacaoItemSetorService.listarPorItemEntrada(item);
		System.out.println(alocacoes.size());
		return new Response().withObject(alocacoes)
				.withSuccessMessage(Constants.ENTRADA_ALOCAR_MATERIAS_SUCESSO_VERIFICAR_FINALIZAÇAO);

	}
}
