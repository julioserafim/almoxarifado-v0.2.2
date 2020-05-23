package ufc.npi.clinicas.controller;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.EstoqueLote;
import ufc.npi.clinicas.model.EstoqueSetor;
import ufc.npi.clinicas.model.ItemSaida;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.SaidaMaterial;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.Status;
import ufc.npi.clinicas.service.EstoqueLoteService;
import ufc.npi.clinicas.service.EstoqueSetorService;
import ufc.npi.clinicas.service.ItemSaidaService;

import ufc.npi.clinicas.service.SaidaMaterialService;
import ufc.npi.clinicas.service.SetorService;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.alert.AlertSet;
import ufc.npi.clinicas.util.api.Response;

@RestController
@RequestMapping("saida")
public class SaidaController {
	
	@Inject
	private SaidaMaterialService saidaMaterialService;

	@Inject
	private EstoqueSetorService estoqueSetorService;
	
	@Inject
	private EstoqueLoteService estoqueLoteService;


	@Inject
	private SetorService setorService;

	@Inject
	private ItemSaidaService itemSaidaService;
	
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public ModelAndView listar(ModelAndView mav) {
		mav.addObject("saidas", saidaMaterialService.listar());
		mav.addObject("total_em_andamento", saidaMaterialService.buscarTotalStatus(Status.EM_ANDAMENTO));
		mav.addObject("total_finalizada", saidaMaterialService.buscarTotalStatus(Status.FINALIZADA));
		mav.setViewName("saida/listar_saidas");
		return mav;
	}

	@RequestMapping(value = "/adicionar", method = RequestMethod.GET)
	public ModelAndView adicionar() {
		ModelAndView mav = new ModelAndView();

		SaidaMaterial saidaMaterial =  new SaidaMaterial();
		mav.addObject("saidaMaterial",saidaMaterial);
		mav.addObject("setores",setorService.listar());
		mav.setViewName("saida/formulario_saida");
		return mav;
	}

	@PostMapping(value = "/adicionar")
	public ModelAndView adicionarSaida(ModelAndView mav, @Valid SaidaMaterial saidaMaterial, BindingResult result, Authentication auth){

		if(result.hasErrors()){
			mav.addObject("saidaMaterial",saidaMaterial);
			mav.addObject("setores",setorService.listar());
			mav.setViewName("saida/formulario_saida");
			return mav;
		}
		
		saidaMaterialService.adicionarSaida(saidaMaterial,auth.getName() );
		
		return new ModelAndView("redirect:/saida/"+saidaMaterial.getId()+"/incluirItens");

	}
	
	@GetMapping(value = "/{id}/editar")
	public ModelAndView editar(ModelAndView model, @PathVariable("id") Integer idSaida) {
		SaidaMaterial saidaMaterial =  saidaMaterialService.buscarPorId(idSaida);
		model.addObject("saidaMaterial",saidaMaterial);
		model.addObject("setores",setorService.listar());
		model.addObject("action", "editar");
		model.setViewName("saida/formulario_saida");
		return model;
	}
	
	@PostMapping(value = "/editar")
	public ModelAndView editar(SaidaMaterial saida, ModelAndView model, RedirectAttributes redirectAttributes) {
		try {
			saidaMaterialService.editar(saida);
			model.setViewName(String.format("redirect:/saida/%1d/visualizar", saida.getId()));
			redirectAttributes.addFlashAttribute("alertas", new AlertSet().withLongSuccess(Constants.SAIDA_EDITAR_SUCESSO));
		} catch (ClinicasException e) {
			redirectAttributes.addFlashAttribute("alertas", new AlertSet().withLongError(e.getMessage()));
			model.setViewName("redirect:/saida/listar");
		}
		
		return model;
	}
	
	@GetMapping(value = "/api/getEstoqueGeral")
	public Response getEstoqueGeral(){
		Setor setor = setorService.estoqueGeral();
		if(setor!=null){
			return new Response().withObject(setor);
		}
		
		return new Response().withFailStatus().withObject(setor).withErrorMessage(Constants.SETOR_BUSCAR_ESTOQUE_GERAL_ERRO);
	}

	@RequestMapping(value = "/{idSaida}/visualizar", method = RequestMethod.GET)
	public ModelAndView visualizar(@PathVariable("idSaida") Integer idSaida, ModelAndView mav) {
		SaidaMaterial saidaMaterial = saidaMaterialService.buscarPorId(idSaida);
		if (saidaMaterial == null) {
			mav.setViewName("redirect:/saida/listar");
		} else if (saidaMaterial.getStatus() == Status.EM_ANDAMENTO) {
			mav.addObject("saida", saidaMaterial);
			mav.setViewName(String.format("redirect:/saida/%1d/incluirItens/",saidaMaterial.getId()));
		}else{
			mav.addObject("saida", saidaMaterial);
			mav.setViewName("/saida/visualizar_saida");
		}
		return mav;

	}

	@GetMapping(value = "{idSaidaMaterial}/incluirItens")
	public ModelAndView incluirItensSaida(@PathVariable("idSaidaMaterial") Integer idSaidaMaterial){
		
		SaidaMaterial saidaMaterial = this.saidaMaterialService.buscarPorId(idSaidaMaterial);
		
		
		ModelAndView modelAndView = new ModelAndView();

		if(saidaMaterial == null){
			modelAndView.setViewName("redirect:/saida/adicionar/");
			return modelAndView;
		}else if(saidaMaterial.getStatus().equals(Status.EM_ANDAMENTO)){
			modelAndView.addObject("saidaMaterial", saidaMaterial);
			modelAndView.setViewName("/saida/formulario_incluir_itens_saida");
			return modelAndView;
		}else{
			modelAndView.setViewName("redirect:/saida/"+saidaMaterial.getId()+"/visualizar/");
			return modelAndView;
		}
		
	}
	
	@RequestMapping(value="/{idSaida}/excluir", method = RequestMethod.GET)
	public ModelAndView excluirSaida(ModelAndView mav, RedirectAttributes redirectAttributes, @PathVariable("idSaida") Integer idSaida){
		SaidaMaterial saida = saidaMaterialService.buscarPorId(idSaida);
		if(saida.getItens().isEmpty()){
			saidaMaterialService.excluir(saida);
			redirectAttributes.addFlashAttribute("alertas", new AlertSet().withSuccess(Constants.SAIDA_EXCLUIDA_SUCESSO));
			mav.setViewName("redirect:/saida/listar");
		}
		else{
			mav.setViewName(String.format("redirect:/saida/%1d/incluirItens",saida.getId()));
			redirectAttributes.addFlashAttribute("alertas", new AlertSet().withLongError(Constants.SAIDA_EXCLUIDA_ERRO));
			
		}
		return mav;
	}
	
	@RequestMapping(value="/{idSaida}/finalizar", method = RequestMethod.GET)
	public ModelAndView finalizarSaida(ModelAndView mav, RedirectAttributes redirectAttributes, @PathVariable("idSaida") Integer idSaida){		
		SaidaMaterial saida = saidaMaterialService.buscarPorId(idSaida);
		if(saida == null){
			mav.setViewName("redirect:/saida/listar");
			mav.addObject("erro", Constants.SAIDA_NAO_ENCONTRADA);
		}else if (saida.getStatus().equals(Status.FINALIZADA)){
			mav.setViewName(String.format("redirect:/saida/%1d/visualizar",saida.getId()));
			mav.addObject("sucesso", Constants.SAIDA_FINALIZAR_SUCESSO);
		}else{			
			try {
				if(saida.getItens().isEmpty()){
					mav.setViewName(String.format("redirect:/saida/%1d/incluirItens",saida.getId()));
					redirectAttributes.addFlashAttribute("alertas", new AlertSet().withLongError(Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE));
				}
				else{
					estoqueSetorService.atualizarEstoquesSetor(saida);							
					saida.setStatus(Status.FINALIZADA);
					saidaMaterialService.editar(saida);				
					
					mav.setViewName(String.format("redirect:/saida/%1d/visualizar",saida.getId()));
					redirectAttributes.addFlashAttribute("alertas", new AlertSet().withSuccess(Constants.SAIDA_FINALIZAR_SUCESSO));
				}
			} catch (ClinicasException e) {
				mav.setViewName(String.format("redirect:/saida/%1d/incluirItens",saida.getId()));
				redirectAttributes.addFlashAttribute("alertas", new AlertSet().withLongError(e.getMessage()));
			}
		}	
		return mav;

	}
	
	@RequestMapping(value = "/{idSaida}/incluirMateriais", method = RequestMethod.GET)
	public ModelAndView incluirMateriais(@PathVariable("idSaida") Integer idSaida) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/saida/formulario_incluir_itens_saida");
		return mav;
	}
	
	@PostMapping(value = "/api/buscarMaterialEstoqueSetor/cbarras/{cbarras}")
	public Response buscarMaterialEstoqueSetor(Setor setor, @PathVariable("cbarras") String codigoBarras){
		
		List<EstoqueLote> estoqueSetores = 
				this.estoqueLoteService.buscarPorNomeMaterialCodigoInternoCodigoDeBarrasESetor(codigoBarras, codigoBarras, codigoBarras);
		if(estoqueSetores == null){
			return new Response().
					withFailStatus().
					withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_NAO_EXISTE_ESTOQUE_MATERIAL_SETOR);
		}else if(estoqueSetores.isEmpty()){
			return new Response().
					withFailStatus().
					withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_NAO_EXISTE_ESTOQUE_MATERIAL_SETOR);
		}
		return new Response().
				withObject(estoqueSetores);

	}

	@GetMapping(value = "saidaMaterial/remover/{idItemSaidaMaterial}")
	public Response excluiAlocacaoItemMaterial(@PathVariable("idItemSaidaMaterial") Long idItemSaidaMaterial) {
		ItemSaida itemSaida = itemSaidaService.buscarPorId(idItemSaidaMaterial);

		if (itemSaida != null) {
			if (itemSaida.getSaidaMaterial().getStatus().equals(Status.EM_ANDAMENTO)) {
				itemSaidaService.excluirItemSaidaMaterial(idItemSaidaMaterial);
				return new Response().withSuccessMessage(Constants.SAIDA_INCLUIR_MATERIAIS_SUCESSO_REMOVER);
			}
		}
		else{
			return new Response().withFailStatus().withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_REMOVER_NULL);
		}
		return new Response().withFailStatus().withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_REMOVER_ERRO);
	}

	@PostMapping("/api/itemSaida/adicionar")
	public Response adicionarItemSaida(Integer quantidade, Integer idSaida, Integer idMaterial, Integer idEstoqueLote, Integer idSetor){
		
		EstoqueLote estoqueLote = estoqueLoteService.buscarPorId(idEstoqueLote);
		
		Material material = estoqueLote.getMaterial();
		
		SaidaMaterial saidaMaterial = this.saidaMaterialService.buscarPorId(idSaida);
		
		ItemSaida itemSaida = new ItemSaida();
		itemSaida.setSaidaMaterial(saidaMaterial);
		itemSaida.setMaterial(material);
		itemSaida.setQuantidade(quantidade);
		itemSaida.setLote(estoqueLote.getLote());
		
		Setor setor = setorService.buscarPorId(idSetor);
		EstoqueSetor estoqueSetor = estoqueSetorService.buscarPorSetorEMaterial(setor, material);

		
		if(this.itemSaidaService.existePorSaidaMaterialEMaterialELote(saidaMaterial, material, estoqueLote.getLote())){
			
			return new Response().
					withFailStatus().
					withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_ITEM_SAIDA_EXISTENTE);
				
		}else{
			if (estoqueSetor == null || itemSaida.getQuantidade() > estoqueSetor.getQuantidade()){
				return new Response().withFailStatus().withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE_MAIOR_ESTOQUE);
			}else if(itemSaida.getQuantidade() > estoqueLote.getQuantidade()){
				return new Response().withFailStatus().withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE_MAIOR_ESTOQUE_LOTE);
			}else if(itemSaida.getQuantidade() <= 0){	
				return new Response().withFailStatus().withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE_INVALIDA);
				
			}else{
				itemSaida.setSaidaMaterial(saidaMaterial);
				itemSaida.setMaterial(material);
				return new Response().withObject(this.itemSaidaService.adicionar(itemSaida))
						.withSuccessMessage(Constants.SAIDA_INCLUIR_MATERIAIS_SUCESSO_ADICIONADO);
			}
		}
	}
	
	@PostMapping("/api/itemSaida/listar")
	public Response listarItensSaidaPorSaida(SaidaMaterial saidaMaterial){
				
		return new Response().
				withObject(this.itemSaidaService.listarPorSaidaMaterial(saidaMaterial));
		
	}

}
