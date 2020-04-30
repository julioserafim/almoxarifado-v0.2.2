package ufc.npi.clinicas.teste.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.service.MaterialService;
import ufc.npi.clinicas.util.api.Response;


@RestController
@RequestMapping("teste/material")
public class MaterialControllerTeste {
	@Inject
	private MaterialService materialService;

		
	/*@GetMapping(value = "api/buscar/cbarras/{cbarras}")
	public List<Material> buscarMaterial(@PathVariable("cbarras") String cBarras){
		// O método deveria ser este abaixo
		// List<Material> materiais = materialService.buscarPorCodigoDeBarras(nome);
		
		// Mas fiz este para teste...
		// Aqui estou filtrando os materiais pelo código de barras passado por parâmetro
		// utilizando uma expressão regular (.*codigo.*)
		// Isso abaixo ficou quebrado quando retirei o código de barras único.
		//return materialService.listar()
		//		.stream()
		//	    .filter(m -> m.getCodigoBarras().matches(".*" + cBarras + ".*"))
		//	    .collect(Collectors.toList());
		return null;
		
	}
<<<<<<< HEAD
	*/

	
	@GetMapping(value = "api/buscar/nome/{nome}")
	public Response buscarMaterialByNome(@PathVariable("nome") String nome){
		// O método deveria ser este abaixo
		// List<Material> materiais = materialService.buscarPorNome(nome);
		
		// Mas fiz este para teste...
		// Aqui estou filtrando os materiais pelo nome passado por parâmetro
		// utilizando uma expressão regular (.*nome.*)
//		return new Response().withObject(materialService.listar()
//				.stream()
//			    .filter(m -> m.getNome().toLowerCase().matches(".*" + nome.toLowerCase() + ".*"))
//			    .collect(Collectors.toList()));
		return null;
		
	}
	

	@GetMapping(value = "api/listar")
	public List<Material> listarMaterial(){
		return materialService.listar();
		
	}
	
	
}
