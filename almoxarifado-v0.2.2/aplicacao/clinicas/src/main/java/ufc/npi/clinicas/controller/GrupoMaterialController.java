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
import ufc.npi.clinicas.model.GrupoMaterial;
import ufc.npi.clinicas.service.GrupoMaterialService;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.alert.AlertSet;
import ufc.npi.clinicas.util.api.Response;

@RestController
@RequestMapping("grupoMaterial")
public class GrupoMaterialController {

	@Inject
	private GrupoMaterialService grupoMaterialService;

	@GetMapping(value = "/adicionar")
	public ModelAndView formAdicionarGrupoMaterial(){
		ModelAndView modelAndView = new ModelAndView("grupo_material/formulario_grupo_material");
		
		modelAndView.addObject("grupoMaterial", new GrupoMaterial());
		modelAndView.addObject("acao", "ADICIONAR");

		return modelAndView;
	}

	@PostMapping(value = "/api/adicionar")
	public Response adicionarGrupoMaterial(GrupoMaterial grupoMaterial ){
		try{
			grupoMaterial.setNome(grupoMaterial.getNome().toUpperCase());
			this.grupoMaterialService.adicionar(grupoMaterial);
			return new Response().withObject(grupoMaterial).withSuccessMessage(Constants.GRUPO_MATERIAL_ADICIONAR_SUCESSO);
		}catch (ClinicasException e) {
			return new Response().withFailStatus().withObject(grupoMaterial).withErrorMessage(e.getMessage());
		}
			
	}

	@GetMapping(value = "/editar/{idGrupoMaterial}")
	public ModelAndView formEditarGrupoMaterial(@PathVariable("idGrupoMaterial") Integer idGrupoMaterial){

		ModelAndView modelAndView= new ModelAndView("grupo_material/formulario_grupo_material");
		GrupoMaterial grupoMaterial = this.grupoMaterialService.buscarPorId(idGrupoMaterial);
		modelAndView.addObject("grupoMaterial", grupoMaterial);
		modelAndView.addObject("acao", "EDITAR");

		return modelAndView;
	}
	
	@GetMapping(value = "remover/{idGrupoMaterial}")
	public ModelAndView removerGrupoMaterial(@PathVariable("idGrupoMaterial") Integer idGrupoMaterial){
		ModelAndView modelAndView = new ModelAndView("forward:/grupoMaterial/listar");
		GrupoMaterial grupoMaterial = grupoMaterialService.buscarPorId(idGrupoMaterial);
		try {
			grupoMaterialService.excluir(grupoMaterial);		
			modelAndView.addObject("alertas", new AlertSet().withSuccess("Grupo excluído!"));
		}catch (ClinicasException e){
			modelAndView.addObject("alertas", new AlertSet().withError(e.getMessage()));
		}
		return modelAndView;
	}

	@PostMapping(value = "/api/editar")
	public Response editarGrupoMaterial(GrupoMaterial grupoMaterial){
		try {
			grupoMaterialService.editar(grupoMaterial);
			return new Response().withObject(grupoMaterial).withInfoMessage(Constants.GRUPO_MATERIAL_EDITAR_SUCESSO);			
		} catch (ClinicasException e) {
			return new Response().withFailStatus().withObject(grupoMaterial).withErrorMessage(e.getMessage());
		}	
		
	}
	
	@GetMapping(value = "/api/remover/{idGrupoMaterial}")
	public Response removerGrupoMaterialApi(GrupoMaterial grupoMaterial){
		try{
			grupoMaterialService.excluir(grupoMaterial);
			return new Response().withObject(grupoMaterial).withInfoMessage(Constants.GRUPO_MATERIAL_REMOVER_SUCESSO);
		}catch (ClinicasException e){
			return new Response().withFailStatus().withObject(grupoMaterial).withErrorMessage(e.getMessage());
		}
	}
	
	@GetMapping(value = "/listar")
	public ModelAndView listarGrupoMaterial(){
		ModelAndView modelAndView = new ModelAndView("grupo_material/grupo_material_listar");
		modelAndView.addObject("grupos", this.grupoMaterialService.listar());
		return modelAndView;
	}
	
	@GetMapping(value = "/api/listar")
	public List<GrupoMaterial> listarGrupoMaterialAPI(){
		List<GrupoMaterial> grupos =  this.grupoMaterialService.listar();
		return grupos;
	}


}
