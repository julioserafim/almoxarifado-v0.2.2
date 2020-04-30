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
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.service.EntradaService;
import ufc.npi.clinicas.service.SaidaMaterialService;
import ufc.npi.clinicas.service.SetorService;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.alert.AlertSet;
import ufc.npi.clinicas.util.api.Response;

@RestController
@RequestMapping("setor")
public class SetorController {
	
	@Inject
	private SetorService setorService;
	
	@Inject
	private EntradaService entradaService;
	
	@Inject
	private SaidaMaterialService saidaMaterialService;
	
	@GetMapping(value = "/adicionar")
	public ModelAndView formAdicionarsetor(){
		ModelAndView mav = new ModelAndView("setor/formulario_setor");
		Setor setor = new Setor();
		mav.addObject("setor",setor);
		mav.addObject("acao","ADICIONAR");
		return mav;
		
	}
	
	@GetMapping(value = "/editar/{idsetor}")
	public ModelAndView formEditarsetor(@PathVariable("idsetor") Integer idsetor, ModelAndView mav, RedirectAttributes redirectAttributes){
		Setor setor = setorService.buscarPorId(idsetor);
		if(setor == null){
			return null;
		}
		if(setor.isGeral()){
			mav.setViewName("redirect:/setor/listar");
			redirectAttributes.addFlashAttribute("alertas", new AlertSet().withLongError("Não é possível alterar o Estoque Geral"));
			return mav;
		}
		
		mav.setViewName("setor/formulario_setor");
		mav.addObject("setor",setor);
		mav.addObject("acao","EDITAR");
		return mav;
	}
	
	@PostMapping(value = "/api/editar")
	public Response editarsetor(Setor setor){
		try{
			setor.setNome(setor.getNome().toUpperCase());
			setorService.editar(setor);		
			return new Response().withObject(setor).withSuccessMessage(Constants.SETOR_EDITAR_SUCESSO);
		}catch (ClinicasException e){			
			return new Response().withFailStatus().withObject(setor).withErrorMessage(e.getMessage());
		}
	}
	
	
	@GetMapping(value = "/listar")
	public ModelAndView listarsetors(){
		ModelAndView mav = new ModelAndView("setor/listar_setor");
		List<Setor> setors = setorService.listar();
		mav.addObject("setors", setors);
		return mav;
		
	}
	
	@GetMapping(value = "/{idSetor}/visualizar")
	public ModelAndView visualizarMaterial(@PathVariable("idSetor") Integer idsetor,  ModelAndView mav){
		Setor setor = setorService.buscarPorId(idsetor);
		if (setor != null){
			mav.setViewName("setor/visualizar_setor");
			mav.addObject("setor", setor);
			mav.addObject("entradas", entradaService.buscarPorSetor(idsetor));
			mav.addObject("saidas", saidaMaterialService.buscarPorOrigem(idsetor));
		}
		else {
			mav.setViewName("redirect:/setor/listar");
		}
			
		return mav;
	}
	
	@PostMapping(value = "api/adicionar")
	public Response adicionarsetor(Setor setor ){
		try {
			setor.setNome(setor.getNome().toUpperCase());
			setorService.adicionar(setor);		
			return new Response().withObject(setor).withSuccessMessage(Constants.SETOR_ADICIONAR_SUCESSO);
		}catch (ClinicasException e) {
			return new Response().withFailStatus().withObject(setor).withErrorMessage(e.getMessage());
		}
		
	}
	
	@GetMapping(value = "/api/remover/{idsetor}")
	public Response removerSetorApi(@PathVariable("idsetor") Integer idsetor){
		Setor setor = setorService.buscarPorId(idsetor);
		try{
			setorService.excluir(setor);	
			return new Response().withObject(setor).withSuccessMessage(Constants.SETOR_REMOVER_SUCESSO);
		}catch (ClinicasException e) {
			return new Response().withFailStatus().withObject(setor).withErrorMessage(e.getMessage());
		}
	}
	
	@GetMapping(value = "/remover/{idsetor}")
	public ModelAndView removerSetor(@PathVariable("idsetor") Integer idSetor, RedirectAttributes redirectAttributes){
		ModelAndView modelAndView = new ModelAndView("redirect:/setor/listar");
		Setor setor = setorService.buscarPorId(idSetor);
		try{
			setorService.excluir(setor);
			redirectAttributes.addFlashAttribute("alertas", new AlertSet().withSuccess(Constants.SETOR_REMOVER_SUCESSO));
			return modelAndView;
		}catch (ClinicasException e) {
			redirectAttributes.addFlashAttribute("alertas", new AlertSet().withError(e.getMessage()));
		}
		return modelAndView;
	}
	
	@GetMapping(value = "/api/listar")
	public List<Setor> listarsetorsAPI(){
		List<Setor> setors =  setorService.listar();
		return setors;
	}
}
