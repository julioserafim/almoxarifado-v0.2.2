package ufc.npi.clinicas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.CodigoDeBarras;
import ufc.npi.clinicas.model.EstoqueLote;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.service.EstoqueLoteService;
import ufc.npi.clinicas.service.GrupoMaterialService;
import ufc.npi.clinicas.service.MaterialService;
import ufc.npi.clinicas.service.UnidadeDeMedidaService;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.alert.AlertSet;
import ufc.npi.clinicas.util.api.Response;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("material")
public class MaterialController {
	@Autowired
	private MaterialService materialService;

	@Autowired
	private UnidadeDeMedidaService unidadeDeMedidaService;

	@Autowired
	private GrupoMaterialService grupoMaterialService;

	@Autowired
	private EstoqueLoteService estoqueLoteService;

	@GetMapping(value = "/adicionar")
	public ModelAndView formAdicionarMaterial() {
		ModelAndView modelAndView = new ModelAndView("material/formulario_material");
		Material material = new Material();
		modelAndView.addObject("material", material);
		modelAndView.addObject("unidadesMedida", unidadeDeMedidaService.listar());
		modelAndView.addObject("gruposMaterial", grupoMaterialService.listar());
		modelAndView.addObject("acao", "ADICIONAR");

		return modelAndView;
	}

	@PostMapping(value = "api/adicionar")
	public Response adicionarMaterial(@RequestParam(value = "codigos") String codBarras, Model model,
			@Valid Material material) {

		CodigoDeBarras cd = new CodigoDeBarras();
		cd.setCodigo(codBarras);
		cd.setMaterial(material);
		material.addCodigo(cd);

		try {
			Map<String, Object> result = materialService.adicionar(material, cd);
			return new Response().withObject(result.get("material")).withSuccessMessage(result.get("message").toString());
		} catch (ClinicasException e) {
			return new Response().withObject(material).withErrorMessage(e.getMessage());
		}
	}

	@GetMapping(value = "/editar/{idMaterial}")
	public ModelAndView formEditarMaterial(@PathVariable("idMaterial") Integer idMaterial) {
		Material material = materialService.buscarPorId(idMaterial);
		ModelAndView modelAndView = new ModelAndView("material/formulario_material");
		if (material == null) {
			return null;
		}

		modelAndView.addObject("material", material);
		modelAndView.addObject("unidadesMedida", unidadeDeMedidaService.listar());
		modelAndView.addObject("gruposMaterial", grupoMaterialService.listar());
		modelAndView.addObject("acao", "EDITAR");
		return modelAndView;
	}

	@PostMapping(value = "/api/editar")
	public Response editarMaterial(@RequestParam(value = "codigos") String codBarras, @Valid Material material)
			throws ClinicasException {
		// Se houve alteração no codigo de barras, retorna algo apenas se tiver
		// erro
		if (codBarras != null && !codBarras.trim().isEmpty()) {
			CodigoDeBarras cd = new CodigoDeBarras();
			if (materialService.existeCodigoBarras(codBarras)) {
				return new Response().withObject(material).withFailStatus()
						.withInfoMessage(Constants.MATERIAL_CODIGO_BARRAS_EXISTE);
			} else {
				cd.setCodigo(codBarras);
				cd.setMaterial(material);
				material.addCodigo(cd);
				try {
					materialService.adicionarCodigoBarras(cd);
					return new Response().withObject(new Material())
							.withSuccessMessage(Constants.MATERIAL_CODIGO_BARRAS_ADCIONADO);
				} catch (ClinicasException e) {
					return new Response().withFailStatus().withObject(material).withErrorMessage(e.getMessage());
				}
			}
		} else {
			// Atualiza material, caso tenha mudança em outros campos além do
			// codigo de barras
			boolean adicionar = materialService.editar(material);

			if (adicionar) {
				return new Response().withObject(material).withSuccessMessage(Constants.MATERIAL_ADICIONAR_SUCESSO);
			} else {
				return new Response().withObject(material).withFailStatus()
						.withInfoMessage(Constants.MATERIAL_EXISTE_OU_CODIGO_INTERNO_EXISTE);

			}
		}

	}

	@GetMapping(value = "api/remover/{idMaterial}")
	public Response removerMaterialApi(@PathVariable("idMaterial") Integer idMaterial) {

		Material material = materialService.buscarPorId(idMaterial);
		try {
			materialService.excluir(material);
			return new Response().withObject(material).withSuccessMessage(Constants.MATERIAL_REMOVER_SUCESSO);
		} catch (ClinicasException e) {
			return new Response().withFailStatus().withObject(material).withErrorMessage(e.getMessage());
		}

	}

	@GetMapping(value = "remover/{idMaterial}")
	public ModelAndView removerMaterial(@PathVariable("idMaterial") Integer idMaterial) {
		ModelAndView modelAndView = new ModelAndView("forward:/material/listar");
		Material material = materialService.buscarPorId(idMaterial);
		try {
			materialService.excluir(material);
			modelAndView.addObject("alertas", new AlertSet().withSuccess("Material excluído!"));
		} catch (ClinicasException e) {
			modelAndView.addObject("alertas", new AlertSet().withError("Erro ao excluir material!"));
		}
		return modelAndView;
	}
	
	@GetMapping(value = "listar")
	public ModelAndView listarMateriais() {
		ModelAndView modelAndView = new ModelAndView("material/listar_materiais");
		
		modelAndView.addObject("materiais", materialService.listar());
		return modelAndView;
	}

	@GetMapping(value = "/{idMaterial}/detalhes/ajax")
	public Response detalhesMaterial(@PathVariable("idMaterial") Integer idMaterial) {
		return new Response().withSuccessMessage("").withObject(materialService.buscarDetalhesPorId(idMaterial));
	}

	@GetMapping(value = "/api/{idMaterial}/buscarCodigoBarras")
	private Response buscarCodigoBarras(@PathVariable("idMaterial") Integer idMaterial) {

		List<CodigoDeBarras> cod = materialService.buscarCodigoBarras(idMaterial);

		if (cod == null) {
			return new Response().withFailStatus()
					.withErrorMessage("NÃO EXISTEM CÓDIGOS DE BARRAS CADASTRADOS PARA ESSE MATERIAL");
		}
		return new Response().withObject(cod).withDoneStatus();

	}

	@PostMapping(value = "api/buscarPorNome/material/{nomeMaterial}")
	private Response buscarPorNome(@PathVariable("nomeMaterial") String nomeMaterial) {
		List<Material> mw = materialService.buscarPorNomeOuCodigoBarrasOuCodigoInterno(nomeMaterial);
		return new Response().withObject(mw).withDoneStatus();
	}

	@GetMapping(value = "{idMaterial}/visualizar")
	public ModelAndView visualizarMaterial(@PathVariable("idMaterial") Material material, ModelAndView mav) {
		if (material != null) {
			mav.setViewName("material/visualizar_material");
			mav.addObject("material", material);
			mav.addObject("estoques", materialService.buscarEstoquesPorSetores(material.getId()));			
			mav.addObject("lotes", estoqueLoteService.getByMaterialAndValidadeDesc(material));
		} else {
			mav.setViewName("redirect:/material/listar");
		}

		return mav;
	}

	@GetMapping(value = "{idMaterial}/removerCodigoBarras/{codigo}")
	public Response removerCodigoBarras(@PathVariable("idMaterial") Material material,
			@PathVariable("codigo") CodigoDeBarras codigoDeBarras) {
		if (material == null || codigoDeBarras == null) {
			return new Response().withFailStatus().withErrorMessage("Não foi possível remover o código de barras.");
		}
		materialService.removerCodigoDeBarras(material, codigoDeBarras);
		return new Response().withDoneStatus().withSuccessMessage("Código de barras removido.");
	}

	@PostMapping(value = "{idMaterial}/editarEstoqueLote/{idEstoqueLote}")
	public @ResponseBody ModelAndView editarEstoqueLoteMaterial(@PathVariable("idMaterial") Material material,
			@PathVariable("idEstoqueLote") EstoqueLote estoque, @RequestParam("lote") String lote,
			@RequestParam("validade") @DateTimeFormat(pattern = "dd/MM/yyyy") Date validade) {

		ModelAndView modelAndView = new ModelAndView(
				Constants.REDIRECT_VISUALIZAR_MATERIAL.replaceAll("id", String.valueOf(material.getId())));

		try {
			estoqueLoteService.editarEstoqueLote(estoque, lote, validade);
		} catch (ClinicasException e) {
			modelAndView.addObject("alertas", new AlertSet().withError(e.getMessage()));
		}

		return modelAndView;
	}
	
	@GetMapping(value = "/{idMaterial}/excluirEstoqueLote/{idEstoqueLote}")
	public ModelAndView excluirEstoqueLoteMaterial(@PathVariable("idMaterial") String idMaterial,
			@PathVariable("idEstoqueLote") EstoqueLote estoque) {

		ModelAndView modelAndView = new ModelAndView(
				Constants.REDIRECT_VISUALIZAR_MATERIAL.replaceAll("id", idMaterial));

		estoqueLoteService.excluirEstoqueLote(estoque);

		return modelAndView;
	}
}
