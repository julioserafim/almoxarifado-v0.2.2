package ufc.npi.clinicas.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.UnidadeMedida;
import ufc.npi.clinicas.service.UnidadeDeMedidaService;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.alert.AlertSet;
import ufc.npi.clinicas.util.api.Response;

@RestController
@RequestMapping("unidadeMedida")
public class UnidadeMedidaController {

	@Inject
	private UnidadeDeMedidaService unidadeDeMedidaService;

	@GetMapping(value = "/adicionar")
	public ModelAndView formAdicionarUnidadeMedida(){
		ModelAndView modelAndView = new ModelAndView("unidade_medida/formulario_unidade_medida");
		modelAndView.addObject("unidadeMedida", new UnidadeMedida());
		modelAndView.addObject("acao", "ADICIONAR");

		return modelAndView;
	}
	
	@GetMapping(value = "remover/{idUnidade}")
	public ModelAndView removerUnidade(@PathVariable("idUnidade") Integer idUnidade, RedirectAttributes redirectAttributes){
		ModelAndView modelAndView = new ModelAndView("redirect:/unidadeMedida/listar");
		UnidadeMedida unidade = unidadeDeMedidaService.buscarPorId(idUnidade);
		if (unidadeDeMedidaService.excluir(unidade)){
			redirectAttributes.addFlashAttribute("alertas", new AlertSet().withSuccess("Unidade de Medida excluído!"));			
			return modelAndView;
		}
		redirectAttributes.addFlashAttribute("alertas", new AlertSet().withError("Erro ao excluir unidade de medida!"));
		
		return modelAndView;
	}

	@PostMapping(value = "/api/adicionar")
	public Response adicionarUnidadeMedida(UnidadeMedida unidadeMedida){		
		try{
			unidadeMedida.setNome(unidadeMedida.getNome().toUpperCase());
			unidadeDeMedidaService.adicionar(unidadeMedida);
			return new Response().withObject(unidadeMedida).withSuccessMessage(Constants.UNIDADE_MEDIDA_ADICIONAR_SUCESSO);
		}catch (ClinicasException e) {
			return new Response().withFailStatus().withObject(unidadeMedida).withErrorMessage(e.getMessage());
		}
	
	}

	@GetMapping(value = "/editar/{idUnidadeMedida}")
	public ModelAndView formEditarUnidadeMedida(@PathVariable("idUnidadeMedida") Integer idUnidadeMedida){

		ModelAndView modelAndView = new ModelAndView("unidade_medida/formulario_unidade_medida");
		UnidadeMedida unidadeMedida = this.unidadeDeMedidaService.buscarPorId(idUnidadeMedida);
		modelAndView.addObject("unidadeMedida", unidadeMedida);
		modelAndView.addObject("acao", "EDITAR");
		modelAndView.addObject("unidades", this.unidadeDeMedidaService.listar());
		return modelAndView;
	}

	@PostMapping(value = "/api/editar")
	public Response editarUnidadeMedida(UnidadeMedida unidadeMedida){
		try{
			unidadeDeMedidaService.editar(unidadeMedida);	
			return new Response().withObject(unidadeMedida).withSuccessMessage(Constants.UNIDADE_MEDIDA_EDITAR_SUCESSO);
		}catch (ClinicasException e) {
			return new Response().withFailStatus().withObject(unidadeMedida).withErrorMessage(e.getMessage());
		}
	}

	@GetMapping(value = "/api/remover/{idUnidadeMedida}")
	public Response removerUnidadeMedida(@PathVariable("idUnidadeMedida") Integer idUnidadeMedida){
		UnidadeMedida unidadeMedida = this.unidadeDeMedidaService.buscarPorId(idUnidadeMedida);
		if (this.unidadeDeMedidaService.excluir(unidadeMedida)){		
			return new Response().withObject(unidadeMedida).withSuccessMessage(Constants.UNIDADE_MEDIDA_REMOVER_SUCESSO);
		}
		return new Response().withFailStatus().withObject(unidadeMedida).withErrorMessage(Constants.UNIDADE_MEDIDA_REMOVER_ERRO);
	}

	@GetMapping(value = "/listar")
	public ModelAndView listarUnidadeMedida(){
		ModelAndView modelAndView = new ModelAndView("unidade_medida/unidade_medida_listar");
		modelAndView.addObject("unidades", this.unidadeDeMedidaService.listar());
		return modelAndView;
	}
	
	@GetMapping(value = "/api/listar")
	public List<UnidadeMedida> listarUnidadesMedidaAPI(){
		List<UnidadeMedida> unidades =  this.unidadeDeMedidaService.listar();
		return unidades;
	}


}
