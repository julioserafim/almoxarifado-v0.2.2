package ufc.npi.clinicas.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.Fornecedor;
import ufc.npi.clinicas.service.FornecedorService;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.alert.AlertSet;
import ufc.npi.clinicas.util.api.Response;

@RestController
@RequestMapping("fornecedor")
public class FornecedorController {
	
	@Inject
	private FornecedorService fornecedorService;
	
	@GetMapping("/adicionar")
	public ModelAndView formAdicionarFornecedor(){
		
		ModelAndView modelAndView = new ModelAndView("fornecedor/formulario_fornecedor");
		
		modelAndView.addObject("fornecedor", new Fornecedor());
		modelAndView.addObject("acao", "ADICIONAR");
		
		return modelAndView;
	}

	@GetMapping("/editar/{idFornecedor}")
	public ModelAndView formEditarFornecedor(@PathVariable("idFornecedor") Integer idFornecedor){
		
		ModelAndView modelAndView = new ModelAndView("fornecedor/formulario_fornecedor");
		
		modelAndView.addObject("fornecedor", this.fornecedorService.buscarPorId(idFornecedor));
		modelAndView.addObject("acao", "EDITAR");
		
		return modelAndView;
	}
	
	@GetMapping("/listar")
	public ModelAndView listarFornecedores(){
		
		ModelAndView modelAndView = new ModelAndView("fornecedor/listar_fornecedor");
		
		modelAndView.addObject("fornecedores", this.fornecedorService.listar());
	
		return modelAndView;
	}
	
	@GetMapping(value = "remover/{idFornecedor}")
	public ModelAndView removerFornecedor(@PathVariable("idFornecedor") Integer idFornecedor){
		ModelAndView modelAndView = new ModelAndView("forward:/fornecedor/listar");
		Fornecedor fornecedor = fornecedorService.buscarPorId(idFornecedor);
		try{
			fornecedorService.excluir(fornecedor);		
			modelAndView.addObject("alertas", new AlertSet().withSuccess("Fornecedor Excluido"));
		}catch (ClinicasException e) {
			modelAndView.addObject("alertas", new AlertSet().withError(e.getMessage()));
		}
		return modelAndView;
	}
	
	@PostMapping(value = "/api/adicionar")
	public Response adicionarFornecedor(Fornecedor fornecedor){	
		try{
			fornecedor.setRazaoSocial(fornecedor.getRazaoSocial().toUpperCase());
			this.fornecedorService.adicionar(fornecedor);
			return new Response().withObject(fornecedor).withSuccessMessage(Constants.FORNECEDOR_ADICIONAR_SUCESSO);
		}catch (ClinicasException e) {
			return new Response().withFailStatus().withObject(fornecedor).withErrorMessage(e.getMessage());
		}
	}
		
	@PostMapping(value = "/api/editar")
	public Response editarFornecedor(Fornecedor fornecedor){
		try{
			fornecedor.setRazaoSocial(fornecedor.getRazaoSocial().toUpperCase());
			fornecedorService.editar(fornecedor);
			return new Response().withObject(fornecedor).withInfoMessage(Constants.FORNECEDOR_EDITAR_SUCESSO);
		}catch (ClinicasException e) {
			return new Response().withFailStatus().withObject(fornecedor).withErrorMessage(e.getMessage());
		}
	}
	
	@GetMapping(value = "/api/remover/{idFornecedor}")
	public Response removerFornecedorApi(@PathVariable("idFornecedor") Integer idFornecedor){
		Fornecedor fornecedor = this.fornecedorService.buscarPorId(idFornecedor);		
		try{
			fornecedorService.excluir(fornecedor);
			return new Response().withObject(fornecedor).withInfoMessage(Constants.FORNECEDOR_REMOVER_SUCESSO);
		}catch (ClinicasException e) {
			return new Response().withFailStatus().withObject(fornecedor).withErrorMessage(e.getMessage());
		}				
	}
	
	@GetMapping(value = "/api/listar")
	public List<Fornecedor> listarFornecedorAPI(){
		List<Fornecedor> fornecedores =  this.fornecedorService.listar();
		return fornecedores;
	}
}
